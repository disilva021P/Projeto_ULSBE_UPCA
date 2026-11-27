package pt.ipca.cuidas.ui.medicamentos.form

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.io.File
import pt.ipca.cuidas.ui.components.BotaoSecundario
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.medicamentos.MiniaturaMedicamento
import pt.ipca.cuidas.utils.Fotos

// Fotografia da embalagem, para distinguir caixas parecidas.
@Composable
fun CampoFotografia(foto: String?, nome: String, aoNovaFoto: (String) -> Unit) {
    val context = LocalContext.current
    var pendente by remember { mutableStateOf<File?>(null) }

    val tirarFoto = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
        val f = pendente
        if (ok && f != null) aoNovaFoto(f.absolutePath) else f?.delete()
        pendente = null
    }
    val abrirCamara: () -> Unit = {
        val f = Fotos.novoFicheiro(context)
        pendente = f
        tirarFoto.launch(Fotos.uriPara(context, f))
    }
    val pedirCamara = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedida ->
        if (concedida) abrirCamara()
        else Toast.makeText(context, "Sem acesso à câmara não é possível tirar a fotografia.", Toast.LENGTH_LONG).show()
    }

    if (foto != null) MiniaturaMedicamento(foto, nome, tamanho = 160.dp)
    BotaoSecundario(if (foto == null) "Tirar fotografia" else "Tirar outra fotografia", icone = Icones.Camara) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            abrirCamara()
        } else {
            pedirCamara.launch(Manifest.permission.CAMERA)
        }
    }
}
