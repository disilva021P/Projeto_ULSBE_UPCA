package pt.ipca.cuidas.ui.definicoes

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import pt.ipca.cuidas.ui.components.Aviso
import pt.ipca.cuidas.ui.components.BotaoSecundario
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.components.TipoAviso
import pt.ipca.cuidas.ui.components.TituloSecao

// Definições: mostra se as notificações dos lembretes estão ligadas e deixa voltar a permiti-las.
@Composable
fun SecaoNotificacoes(permitidas: Boolean, aoVerificar: () -> Unit) {
    val context = LocalContext.current

    // Abre as definições de notificações desta app no Android; ao voltar, volta a verificar.
    val abrirDefinicoes = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        aoVerificar()
    }

    fun irParaDefinicoes() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        abrirDefinicoes.launch(intent)
    }

    // Android 13+: pede a permissão. Se a pessoa já recusou de vez, o pedido já não aparece,
    // por isso abre-se logo as definições do Android.
    val pedir = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { aceitou ->
        if (!aceitou) {
            val podeVoltarAPedir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                (context as? Activity)?.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) == true
            if (!podeVoltarAPedir) irParaDefinicoes()
        }
        aoVerificar()
    }

    TituloSecao("Notificações dos lembretes")
    if (permitidas) {
        Aviso("As notificações dos lembretes estão ligadas.", tipo = TipoAviso.SUCESSO)
    } else {
        Aviso(
            "As notificações estão desligadas. Sem elas não recebe os lembretes das tomas.",
            tipo = TipoAviso.ATENCAO,
        )
        BotaoSecundario("Permitir notificações", icone = Icones.Definicoes) {
            val semPermissao = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            if (semPermissao) pedir.launch(Manifest.permission.POST_NOTIFICATIONS) else irParaDefinicoes()
        }
    }
}
