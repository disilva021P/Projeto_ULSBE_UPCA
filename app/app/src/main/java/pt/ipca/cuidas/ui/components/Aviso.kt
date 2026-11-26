// Mensagem curta destacada, com cor segundo o tipo (informação, sucesso, atenção ou perigo).
package pt.ipca.cuidas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.ipca.cuidas.ui.theme.Cores

enum class TipoAviso { INFO, SUCESSO, ATENCAO, PERIGO }

@Composable
fun Aviso(texto: String, modifier: Modifier = Modifier, perigo: Boolean = false, tipo: TipoAviso = if (perigo) TipoAviso.PERIGO else TipoAviso.INFO) {
    val forma = RoundedCornerShape(12.dp)
    val (cor, fundo, icone) = when (tipo) {
        TipoAviso.INFO -> Triple(Cores.DestaqueEscuro, Cores.DestaqueClaro, Icones.Informacao)
        TipoAviso.SUCESSO -> Triple(Cores.Sucesso, Cores.SucessoClaro, Icones.Certo)
        TipoAviso.ATENCAO -> Triple(Cores.Atencao, Cores.AtencaoClaro, Icones.Alerta)
        TipoAviso.PERIGO -> Triple(Cores.Perigo, Cores.PerigoClaro, Icones.Alerta)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(fundo, forma)
            .border(2.dp, cor, forma)
            .padding(14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(icone, contentDescription = null, tint = cor, modifier = Modifier.size(26.dp))
        Spacer(Modifier.width(12.dp))
        Texto(texto, cor = if (tipo == TipoAviso.PERIGO) Cores.Perigo else Cores.Texto)
    }
}

@Composable
fun AvisoNaoSubstitui() {
    Texto(
        "Esta aplicação não substitui um profissional de saúde. Em caso de dúvida, ligue para o SNS 24.",
        estilo = MaterialTheme.typography.bodyMedium,
        cor = Cores.TextoSuave,
    )
}
