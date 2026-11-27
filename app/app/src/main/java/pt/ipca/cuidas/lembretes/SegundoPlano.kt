// Corre trabalho fora da thread principal dentro de um BroadcastReceiver.
package pt.ipca.cuidas.lembretes

import android.content.BroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal fun BroadcastReceiver.emSegundoPlano(bloco: suspend () -> Unit) {
    val pendente = goAsync()
    CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
        try {
            bloco()
        } finally {
            pendente.finish()
        }
    }
}
