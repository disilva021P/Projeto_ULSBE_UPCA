package pt.ipca.cuidas.ui.medicamentos

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
import java.time.LocalDate
import pt.ipca.cuidas.toDateWithWeekday
import pt.ipca.cuidas.ui.components.Cartao
import pt.ipca.cuidas.ui.theme.Cores

// Quantas tomas de hoje já foram feitas, com uma barra de progresso.
@Composable
fun ResumoHoje(feitas: Int, previstas: Int) {
    val completo = previstas > 0 && feitas >= previstas
    Cartao(fundo = if (completo) Cores.SucessoClaro else Cores.Campo, contorno = if (completo) Cores.Sucesso else Cores.Linha) {
        Text(LocalDate.now().toDateWithWeekday(), style = MaterialTheme.typography.bodyMedium, color = Cores.TextoSuave)
        Text("$feitas de $previstas tomas feitas hoje", style = MaterialTheme.typography.headlineMedium,
            color = if (completo) Cores.Sucesso else Cores.DestaqueEscuro)
        LinearProgressIndicator(
            progress = { if (previstas == 0) 0f else feitas.toFloat() / previstas },
            modifier = Modifier.fillMaxWidth().height(14.dp).clip(RoundedCornerShape(7.dp)),
            color = if (completo) Cores.Sucesso else Cores.Destaque,
            trackColor = Cores.Linha,
            strokeCap = StrokeCap.Round,
            drawStopIndicator = {},
        )
    }
}
