// Chega à hora de uma toma: mostra a notificação e agenda o dia seguinte.
package pt.ipca.cuidas.lembretes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.time.LocalDate
import pt.ipca.cuidas.Contentor

class LembreteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val horarioId = intent.getLongExtra(EXTRA_HORARIO, -1L)
        if (horarioId < 0) return
        emSegundoPlano {
            val c = Contentor
            val (medicamento, horario) = c.medicamentos.obterPorHorario(horarioId) ?: return@emSegundoPlano
            val hoje = LocalDate.now()
            if (medicamento.ativoEm(hoje)) Notificacoes.mostrarLembrete(context, medicamento, horario)
            // Depois da data de término o alarme não volta a ser agendado.
            if (medicamento.ativoEm(hoje.plusDays(1))) c.agendador.agendar(horario, aPartirDeAmanha = true)
        }
    }

    companion object {
        const val EXTRA_HORARIO = "horario_id"
    }
}
