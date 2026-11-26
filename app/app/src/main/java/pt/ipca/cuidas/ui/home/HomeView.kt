// Ecrã inicial: as secções em blocos quadrados iguais.
package pt.ipca.cuidas.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pt.ipca.cuidas.ui.components.Ecra
import pt.ipca.cuidas.ui.components.TituloSecao

@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
) {
    val blocos = blocosHome()

    val falar = buildString {
        append("Olá. ")
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
        TituloSecao("O que quer fazer?")
        GrelhaBlocos(blocos, emptyMap()) { bloco ->
            navController.navigate(bloco.rota) { launchSingleTop = true }
        }
    }
}
