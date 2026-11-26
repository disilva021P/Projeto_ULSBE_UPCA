// Escolha do tamanho da letra em três botões.
package pt.ipca.cuidas.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun EscolhaEscala(atual: Float, aoEscolher: (Float) -> Unit) {
    val opcoes = listOf(1.0f to "Normal", 1.15f to "Grande", 1.35f to "Muito grande")
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        opcoes.forEach { (valor, nome) ->
            BotaoEscolha(nome, escolhido = abs(atual - valor) < 0.01f) { aoEscolher(valor) }
        }
    }
}
