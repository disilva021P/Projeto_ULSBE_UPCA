// Primeira utilização: explica a privacidade e pergunta a leitura em voz alta e o tamanho da letra.
package pt.ipca.cuidas.ui.boasvindas

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pt.ipca.cuidas.Screen
import pt.ipca.cuidas.ui.components.Aviso
import pt.ipca.cuidas.ui.components.BotaoPrincipal
import pt.ipca.cuidas.ui.components.Ecra
import pt.ipca.cuidas.ui.components.EscolhaEscala
import pt.ipca.cuidas.ui.components.TituloSecao
import pt.ipca.cuidas.ui.theme.Cores

@Composable
fun BoasVindasView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
) {
    val viewModel: BoasVindasViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    fun concluir() {
        viewModel.concluir()
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.BoasVindas.route) { inclusive = true }
        }
    }

    val pedirNotificacoes = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concluir() }

    val falar = "Bem-vindo à CUIDAS. Esta aplicação ajuda a perceber a sua medicação e diz o que fazer no dia a dia. " +
        "Não precisa de criar conta. Quer que a aplicação leia cada ecrã em voz alta? Escolha sim ou não."

    Ecra(
        titulo = "Bem-vindo",
        textoParaOuvir = falar,
        modifier = modifier,
        navController = navController,
        isBaseScreen = true,
    ) {
        Text("A CUIDAS ajuda a perceber a sua medicação e diz o que fazer no dia a dia.", style = MaterialTheme.typography.titleLarge)
        Aviso(
            "Não precisa de criar conta. A sua medicação e as suas respostas ficam só neste telemóvel. " +
                "Para a ULSBE só vão números anónimos, sem o seu nome.",
        )

        EscolhaLeitura(uiState.leitura) { viewModel.escolherLeitura(it) }

        TituloSecao("Tamanho da letra")
        EscolhaEscala(uiState.escala) { viewModel.escolherEscala(it) }

        BotaoPrincipal("Começar", ativo = uiState.leitura != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pedirNotificacoes.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                concluir()
            }
        }
        if (uiState.leitura == null) {
            Text("Responda à pergunta da leitura em voz alta para continuar.", style = MaterialTheme.typography.bodyMedium, color = Cores.TextoSuave)
        }
    }
}
