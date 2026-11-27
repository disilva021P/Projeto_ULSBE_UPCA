package pt.ipca.cuidas.ui.historico

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import pt.ipca.cuidas.models.ResumoRegisto
import pt.ipca.cuidas.ui.components.Cartao
import pt.ipca.cuidas.ui.theme.Cores

// Barra de progresso dos dias completos, com o texto "X de Y dias completos".
@Composable
fun BarraProgressoDias(resumo: ResumoRegisto) {
    Cartao {
        Text(
            "${resumo.completos} de ${resumo.total} dias completos",
            style = MaterialTheme.typography.headlineMedium,
            color = Cores.DestaqueEscuro,
        )
        LinearProgressIndicator(
            progress = { resumo.fracao },
            modifier = Modifier.fillMaxWidth().height(18.dp).clip(RoundedCornerShape(9.dp)),
            color = Cores.Destaque,
            trackColor = Cores.Linha,
            strokeCap = StrokeCap.Round,
            drawStopIndicator = {},
        )
        Text(
            "Um dia fica completo quando todas as tomas desse dia são confirmadas.",
            style = MaterialTheme.typography.bodyMedium,
            color = Cores.TextoSuave,
        )
    }
}
