// Pergunta da primeira utilização: ler os ecrãs em voz alta ou não.
package pt.ipca.cuidas.ui.boasvindas

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import pt.ipca.cuidas.ui.components.BotaoEscolha
import pt.ipca.cuidas.ui.components.TituloSecao
import pt.ipca.cuidas.ui.theme.Cores

@Composable
fun EscolhaLeitura(escolha: Boolean?, aoEscolher: (Boolean) -> Unit) {
    TituloSecao("Quer que a aplicação leia cada ecrã em voz alta?")
    BotaoEscolha("Sim, ler em voz alta", escolhido = escolha == true) { aoEscolher(true) }
    BotaoEscolha("Não, obrigado", escolhido = escolha == false) { aoEscolher(false) }
    Text(
        "Pode mudar isto mais tarde nas Definições. O botão Ouvir está sempre disponível no topo de cada ecrã.",
        style = MaterialTheme.typography.bodyMedium,
        color = Cores.TextoSuave,
    )
}
