// Definições: letra, leitura automática e privacidade.
package pt.ipca.cuidas.ui.definicoes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pt.ipca.cuidas.ui.components.Aviso
import pt.ipca.cuidas.ui.components.Ecra
import pt.ipca.cuidas.ui.components.EscolhaEscala
import pt.ipca.cuidas.ui.components.LinhaInterruptor
import pt.ipca.cuidas.ui.components.TituloSecao

@Composable
fun DefinicoesView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
) {
    val viewModel: DefinicoesViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Ecra(
        titulo = "Definições",
        textoParaOuvir = "Definições. Pode mudar o tamanho da letra e a leitura em voz alta.",
        modifier = modifier,
        navController = navController,
    ) {
        TituloSecao("Tamanho da letra")
        EscolhaEscala(uiState.escala) { viewModel.definirEscala(it) }

        TituloSecao("Leitura em voz alta")
        LinhaInterruptor("Ler cada ecrã automaticamente ao abrir", uiState.leituraAutomatica) {
            viewModel.definirLeituraAutomatica(it)
        }

        TituloSecao("Privacidade")
        Aviso(
            "Não tem conta e a aplicação não recolhe dados pessoais nem de saúde. " +
                "Tudo o que guardar fica só neste telemóvel.",
        )
    }
}
