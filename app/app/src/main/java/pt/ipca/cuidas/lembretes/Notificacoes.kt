// Notificações dos lembretes de toma, com som, vibração e luz.
package pt.ipca.cuidas.lembretes

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import pt.ipca.cuidas.MainActivity
import pt.ipca.cuidas.R
import pt.ipca.cuidas.models.Horario
import pt.ipca.cuidas.models.Medicamento
import pt.ipca.cuidas.models.ParaQueServe

object Notificacoes {

    const val CANAL = "lembretes_tomas"

    // Cria o canal dos lembretes (som, vibração e luz); o Android exige-o a partir da versão 8.
    fun criarCanal(context: Context) {
        val canal = NotificationChannel(CANAL, "Lembretes das tomas", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Avisa à hora de tomar cada medicamento"
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 600, 300, 600, 300, 600)
            enableLights(true)
            lightColor = Color.CYAN
            setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build(),
            )
        }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
    }

    // Verdadeiro se a app tem permissão para mostrar notificações.
    fun podeNotificar(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

    // Mostra o lembrete de uma toma, com o botão "Já tomei".
    fun mostrarLembrete(context: Context, medicamento: Medicamento, horario: Horario) {
        if (!podeNotificar(context)) return
        val id = horario.id.toInt()
        val abrir = PendingIntent.getActivity(
            context, id,
            Intent(context, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra(MainActivity.EXTRA_ABRIR, MainActivity.ABRIR_MEDICAMENTOS),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val jaTomei = PendingIntent.getBroadcast(
            context, id,
            Intent(context, TomaReceiver::class.java).putExtra(TomaReceiver.EXTRA_HORARIO, horario.id),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val texto = listOf(medicamento.dose, ParaQueServe.de(medicamento.paraQueServe).nome)
            .filter { it.isNotBlank() }.joinToString(" · ")
        val notificacao = NotificationCompat.Builder(context, CANAL)
            .setSmallIcon(R.drawable.ic_notificacao)
            .setContentTitle("Hora de tomar: ${medicamento.nome}")
            .setContentText(texto)
            .setStyle(NotificationCompat.BigTextStyle().bigText("$texto\nÀs ${horario.texto()}"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(abrir)
            .setAutoCancel(true)
            .addAction(0, "Já tomei", jaTomei)
            .build()
        try {
            NotificationManagerCompat.from(context).notify(id, notificacao)
        } catch (e: SecurityException) {
            // A autorização pode ter sido retirada entre a verificação e o envio.
        }
    }

    // Retira a notificação de um horário (depois de confirmar a toma).
    fun fechar(context: Context, horarioId: Long) {
        NotificationManagerCompat.from(context).cancel(horarioId.toInt())
    }
}
