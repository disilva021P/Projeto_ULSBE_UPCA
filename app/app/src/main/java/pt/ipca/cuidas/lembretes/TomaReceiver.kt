// Botão "Já tomei" da notificação (RF11).
package pt.ipca.cuidas.lembretes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import pt.ipca.cuidas.Contentor

class TomaReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val horarioId = intent.getLongExtra(EXTRA_HORARIO, -1L)
        if (horarioId < 0) return
        emSegundoPlano {
            val c = Contentor
            c.tomas.confirmar(horarioId)
            Notificacoes.fechar(context, horarioId)
        }
    }

    companion object {
        const val EXTRA_HORARIO = "horario_id"
    }
}
