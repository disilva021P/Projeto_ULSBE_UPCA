package pt.ipca.cuidas.ui.medicamentos.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import pt.ipca.cuidas.models.ParaQueServe
import pt.ipca.cuidas.ui.components.BotaoEscolha
import pt.ipca.cuidas.ui.components.GrelhaResponsiva

// Escolha de para que serve o medicamento, em botões de duas colunas.
@Composable
fun EscolhaParaQueServe(atual: String, aoEscolher: (String) -> Unit) {
    val opcoes = ParaQueServe.OPCOES
    GrelhaResponsiva(opcoes.size, larguraMinima = 150.dp) { i, m ->
        val o = opcoes[i]
        BotaoEscolha(o.nome, escolhido = o.chave == atual, modifier = m) { aoEscolher(o.chave) }
    }
}
