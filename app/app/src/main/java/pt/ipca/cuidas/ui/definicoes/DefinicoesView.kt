package pt.ipca.cuidas.ui.definicoes

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pt.ipca.cuidas.Screen
import pt.ipca.cuidas.ui.components.Aviso
import pt.ipca.cuidas.ui.components.Ecra
import pt.ipca.cuidas.ui.components.EscolhaEscala
import pt.ipca.cuidas.ui.components.LinhaInterruptor
import pt.ipca.cuidas.ui.components.TituloSecao

// Definições: letra, leitura automática e privacidade.
@Composable
fun DefinicoesView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
) {
    val context = LocalContext.current
    val viewModel: DefinicoesViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchDefinicoes()
    }

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

        SecaoNotificacoes(uiState.notificacoesPermitidas) { viewModel.verificarNotificacoes() }

        TituloSecao("Privacidade")
        Aviso(
            "Não tem conta e a aplicação não recolhe dados pessoais nem de saúde. A medicação, as fotografias, " +
                "as tomas e os contactos ficam só neste telemóvel.",
        )
        ApagarDados {
            viewModel.apagarTudo {
                Toast.makeText(context, "Os seus dados foram apagados.", Toast.LENGTH_LONG).show()
                navController.navigate(Screen.BoasVindas.route) { popUpTo(0) { inclusive = true } }
            }
        }
    }
}
