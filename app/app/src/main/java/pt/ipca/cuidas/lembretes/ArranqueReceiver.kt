// Depois de reiniciar o telemóvel ou atualizar a aplicação, os alarmes têm de ser agendados de novo.
package pt.ipca.cuidas.lembretes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import pt.ipca.cuidas.Contentor

class ArranqueReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED && intent.action != Intent.ACTION_MY_PACKAGE_REPLACED) return
        emSegundoPlano { Contentor.agendador.reagendarTodos() }
    }
}
