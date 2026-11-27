// Agenda um alarme por horário de toma; funciona sem internet (RF10).
package pt.ipca.cuidas.lembretes

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import pt.ipca.cuidas.data.local.Preferencias
import pt.ipca.cuidas.data.repository.MedicamentoRepositorio
import pt.ipca.cuidas.models.Horario

class AgendadorLembretes(
    private val context: Context,
    private val medicamentos: MedicamentoRepositorio,
    private val preferencias: Preferencias,
) {

    private val alarmes = context.getSystemService(AlarmManager::class.java)

    // Volta a agendar os alarmes de todos os horários em vigor e cancela os que deixaram de existir.
    fun reagendarTodos() {
        val horarios = medicamentos.horariosPorTerminar()
        val novos = horarios.map { it.id.toString() }.toSet()
        for (antigo in preferencias.lembretesAgendados - novos) {
            antigo.toLongOrNull()?.let { cancelar(it) }
        }
        horarios.forEach { agendar(it) }
        preferencias.lembretesAgendados = novos
    }

    // Agenda o alarme da próxima ocorrência de um horário (exato quando o Android deixa).
    fun agendar(horario: Horario, aPartirDeAmanha: Boolean = false) {
        val quando = proximaOcorrencia(horario, aPartirDeAmanha)
        val pi = intentAlarme(horario.id)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmes.canScheduleExactAlarms()) {
            alarmes.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, quando, pi)
        } else {
            // Sem autorização para alarmes exatos o Android pode atrasar uns minutos.
            alarmes.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, quando, pi)
        }
    }

    // Cancela o alarme de um horário.
    fun cancelar(horarioId: Long) {
        alarmes.cancel(intentAlarme(horarioId))
    }

    // Cancela todos os alarmes agendados (usado ao apagar os dados).
    fun cancelarTodos() {
        preferencias.lembretesAgendados.forEach { id -> id.toLongOrNull()?.let { cancelar(it) } }
        preferencias.lembretesAgendados = emptySet()
    }

    private fun intentAlarme(horarioId: Long): PendingIntent = PendingIntent.getBroadcast(
        context,
        horarioId.toInt(),
        Intent(context, LembreteReceiver::class.java).putExtra(LembreteReceiver.EXTRA_HORARIO, horarioId),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )

    // Próxima data e hora em que o horário deve tocar, hoje ou amanhã.
    private fun proximaOcorrencia(horario: Horario, aPartirDeAmanha: Boolean): Long {
        val hora = LocalTime.of(horario.hora, horario.minuto)
        var quando = LocalDateTime.of(if (aPartirDeAmanha) LocalDate.now().plusDays(1) else LocalDate.now(), hora)
        if (!quando.isAfter(LocalDateTime.now())) quando = quando.plusDays(1)
        return quando.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}
