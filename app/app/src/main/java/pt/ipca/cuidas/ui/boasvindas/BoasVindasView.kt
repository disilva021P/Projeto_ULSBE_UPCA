package pt.ipca.cuidas.ui.boasvindas

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

// Primeira utilização: explica a privacidade e pergunta a leitura em voz alta e o tamanho da letra.
@Composable
fun BoasVindasView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
) {
    // ViewModel do ecrã e o seu estado (leitura em voz alta e tamanho da letra escolhidos).
    val viewModel: BoasVindasViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchPreferencias()
    }

    // Marca as boas-vindas como vistas (as escolhas já ficaram guardadas ao tocar) e vai para o início.
    // O popUpTo tira este ecrã da pilha, para o botão "voltar" não regressar aqui.
    fun concluir() {
        viewModel.concluir()
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.BoasVindas.route) { inclusive = true }
        }
    }

    // Pede a permissão das notificações (Android 13+). Aceite ou recusada, segue para o início:
    // se recusar, pode voltar a permitir mais tarde nas Definições.
    val pedirNotificacoes = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concluir() }

    // Texto lido em voz alta neste ecrã: ao abrir, se a leitura automática estiver ligada, ou no botão de ouvir.
    val falar = "Bem-vindo à CUIDAS. Esta aplicação ajuda a perceber a sua medicação e diz o que fazer no dia a dia. " +
        "Não precisa de criar conta. Quer que a aplicação leia cada ecrã em voz alta? Escolha sim ou não."

    // Ecra é o esqueleto comum: barra de topo com título, leitura em voz alta e conteúdo com scroll.
    // isBaseScreen = true porque é o primeiro ecrã e não tem para onde voltar.
    Ecra(
        titulo = "Bem-vindo",
        textoParaOuvir = falar,
        modifier = modifier,
        navController = navController,
        isBaseScreen = true,
    ) {
        // Apresentação curta e aviso de privacidade (sem conta, dados só no telemóvel).
        Text("A CUIDAS ajuda a perceber a sua medicação e diz o que fazer no dia a dia.", style = MaterialTheme.typography.titleLarge)
        Aviso(
            "Não precisa de criar conta. A sua medicação e os seus contactos ficam só neste telemóvel.",
        )

        // Pergunta se quer a leitura automática de cada ecrã (sim/não). Fica null até responder;
        // a escolha é guardada logo nas preferências.
        EscolhaLeitura(uiState.leitura) { viewModel.escolherLeitura(it) }

        // Tamanho da letra; a mudança é guardada e aplica-se logo à app toda.
        TituloSecao("Tamanho da letra")
        EscolhaEscala(uiState.escala) { viewModel.escolherEscala(it) }

        // Só fica ativo depois de responder à leitura. No Android 13+ pede primeiro a permissão
        // das notificações (o concluir corre no retorno do pedido); nas versões anteriores a
        // permissão já vem dada na instalação, por isso conclui logo.
        BotaoPrincipal("Começar", ativo = uiState.leitura != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pedirNotificacoes.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                concluir()
            }
        }
        // Enquanto não responder, explica porque é que o botão está desativado.
        if (uiState.leitura == null) {
            Text("Responda à pergunta da leitura em voz alta para continuar.", style = MaterialTheme.typography.bodyMedium, color = Cores.TextoSuave)
        }
    }
}
