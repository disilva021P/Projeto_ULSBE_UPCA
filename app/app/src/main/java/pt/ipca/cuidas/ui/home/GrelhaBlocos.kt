package pt.ipca.cuidas.ui.home

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.ipca.cuidas.ui.components.BlocoQuadrado
import pt.ipca.cuidas.ui.components.GrelhaResponsiva
import pt.ipca.cuidas.ui.components.colunasPara

// Grelha uniforme dos blocos: duas colunas de quadrados, ou uma coluna de blocos largos se a letra não couber.
@Composable
fun GrelhaBlocos(blocos: List<BlocoHome>, contadores: Map<String, Int>, aoEscolher: (BlocoHome) -> Unit) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val largo = colunasPara(maxWidth, larguraMinima = 150.dp, maximo = 2) == 1
        GrelhaResponsiva(blocos.size, larguraMinima = 150.dp, espaco = 12.dp) { i, m ->
            val b = blocos[i]
            BlocoQuadrado(b.texto, b.icone, b.cor, b.fundo, largo, m, contadores[b.rota]) { aoEscolher(b) }
        }
    }
}
