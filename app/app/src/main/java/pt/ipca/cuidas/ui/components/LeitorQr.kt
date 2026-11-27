package pt.ipca.cuidas.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

// Abre a câmara para ler um código QR e devolve o texto lido.
@Composable
fun rememberLeitorQr(aviso: String, aoLer: (String) -> Unit): () -> Unit {
    val leitor = rememberLauncherForActivityResult(ScanContract()) { resultado ->
        resultado.contents?.let(aoLer)
    }
    return remember(leitor, aviso) { { leitor.launch(opcoes(aviso)) } }
}

private fun opcoes(aviso: String): ScanOptions = ScanOptions()
    .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
    .setPrompt(aviso)
    .setBeepEnabled(false)
    .setBarcodeImageEnabled(false)
    .setOrientationLocked(false)
    // Lê também códigos claros sobre fundo escuro, comuns quando o QR é mostrado num ecrã.
    .addExtra(Intents.Scan.SCAN_TYPE, Intents.Scan.MIXED_SCAN)
