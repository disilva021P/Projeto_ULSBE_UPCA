package pt.ipca.cuidas.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Quantas colunas cabem: a largura mínima de cada uma cresce com a letra escolhida.
@Composable
fun colunasPara(larguraDisponivel: Dp, larguraMinima: Dp, maximo: Int, espaco: Dp = 10.dp): Int {
    val escala = LocalDensity.current.fontScale
    val minima = larguraMinima * escala
    return ((larguraDisponivel + espaco) / (minima + espaco)).toInt().coerceIn(1, maximo)
}

// Grelhas que mudam o número de colunas conforme a largura e o tamanho da letra, para nada ficar cortado.
@Composable
fun GrelhaResponsiva(
    quantidade: Int,
    larguraMinima: Dp,
    maximoColunas: Int = 2,
    espaco: Dp = 10.dp,
    item: @Composable (indice: Int, modifier: Modifier) -> Unit,
) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val colunas = colunasPara(maxWidth, larguraMinima, maximoColunas, espaco)
        Column(verticalArrangement = Arrangement.spacedBy(espaco)) {
            (0 until quantidade).chunked(colunas).forEach { linha ->
                Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(espaco)) {
                    linha.forEach { i -> item(i, Modifier.weight(1f).fillMaxHeight()) }
                    repeat(colunas - linha.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
}

// Dois botões lado a lado quando cabem; um por baixo do outro quando a letra é grande.
@Composable
fun ParDeBotoes(primeiro: @Composable (Modifier) -> Unit, segundo: @Composable (Modifier) -> Unit) {
    GrelhaResponsiva(quantidade = 2, larguraMinima = 160.dp) { i, m -> if (i == 0) primeiro(m) else segundo(m) }
}
