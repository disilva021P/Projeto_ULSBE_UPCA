package pt.ipca.cuidas.ui.historico

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.ipca.cuidas.models.EstadoDia
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.theme.Cores

// Círculo de um dia no registo: verde com visto se completo, arco laranja se incompleto, cinzento sem tomas.
@Composable
fun CirculoDia(estado: EstadoDia, fracao: Float, tamanho: Dp = 40.dp) {
    Box(Modifier.size(tamanho), contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(tamanho)) {
            val traco = size.minDimension * 0.13f
            val lado = size.minDimension - traco
            val canto = Offset(traco / 2, traco / 2)
            when (estado) {
                EstadoDia.COMPLETO -> drawCircle(Cores.Sucesso)
                EstadoDia.INCOMPLETO -> {
                    drawCircle(Cores.AtencaoClaro, radius = lado / 2)
                    drawArc(Cores.Linha, 0f, 360f, false, canto, Size(lado, lado), style = Stroke(traco))
                    if (fracao > 0f) {
                        drawArc(Cores.Atencao, -90f, 360f * fracao, false, canto, Size(lado, lado),
                            style = Stroke(traco, cap = StrokeCap.Round))
                    }
                }
                EstadoDia.SEM_TOMAS -> drawCircle(Cores.Linha, radius = lado / 2, style = Stroke(traco / 2))
            }
        }
        if (estado == EstadoDia.COMPLETO) {
            Icon(Icones.Confirmar, contentDescription = null, tint = Color.White, modifier = Modifier.size(tamanho * 0.6f))
        }
    }
}
