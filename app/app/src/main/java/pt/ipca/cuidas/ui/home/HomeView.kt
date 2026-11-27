package pt.ipca.cuidas.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pt.ipca.cuidas.Screen
import pt.ipca.cuidas.ui.components.BotaoSecundario
import pt.ipca.cuidas.ui.components.Ecra
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.components.TituloSecao

// Ecrã inicial: próxima toma e as secções em blocos quadrados iguais.
@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
) {
    val viewModel: HomeViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchResumo()
    }

    val blocos = blocosHome()

    val falar = buildString {
        append("Olá. ")
        uiState.proximaToma?.let { append("A próxima toma é ${it.medicamento.nome}, às ${it.horario.texto()}. ") }
        append("Escolha: ")
        append(blocos.joinToString(", ") { it.texto })
        append(".")
    }

    Ecra(
        titulo = "CUIDAS",
        textoParaOuvir = falar,
        modifier = modifier,
        navController = navController,
        isBaseScreen = true,
    ) {
        uiState.proximaToma?.let { toma ->
            CartaoProximaToma(toma) { viewModel.confirmarToma(toma) }
        }
        BotaoSecundario("Mostrar todos os medicamentos", icone = Icones.Lista) {
            navController.navigate(Screen.TodosMedicamentos.route)
        }
        TituloSecao("O que quer fazer?")
        GrelhaBlocos(blocos, emptyMap()) { bloco ->
            navController.navigate(bloco.rota) { launchSingleTop = true }
        }
    }
}
