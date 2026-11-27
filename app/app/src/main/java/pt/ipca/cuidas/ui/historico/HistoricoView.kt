package pt.ipca.cuidas.ui.historico

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.time.LocalDate
import pt.ipca.cuidas.Screen
import pt.ipca.cuidas.toShortDate
import pt.ipca.cuidas.ui.components.Aviso
import pt.ipca.cuidas.ui.components.Ecra
import pt.ipca.cuidas.ui.components.TituloSecao

// Histórico: progresso dos últimos 30 dias e lista dos dias, cada um com os medicamentos tomados.
@Composable
fun HistoricoView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
) {
    val viewModel: HistoricoViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchResumo()
    }

    val hoje = LocalDate.now()
    val atual = uiState.resumo
    val falar = if (atual == null || atual.total == 0) "Histórico. Ainda não há dias com tomas marcadas."
    else "Histórico. ${atual.completos} de ${atual.total} dias completos. Toque num dia para ver os medicamentos."

    Ecra(
        titulo = "Histórico",
        textoParaOuvir = falar,
        modifier = modifier,
        navController = navController,
    ) {
        if (atual == null) return@Ecra
        if (atual.total == 0) {
            Aviso("Ainda não há dias com tomas marcadas. Adicione medicamentos com horário para começar o histórico.")
            return@Ecra
        }
        BarraProgressoDias(atual)
        TituloSecao("Desde ${atual.inicio?.toShortDate().orEmpty()}")
        atual.dias.forEach { d ->
            LinhaRegistoDia(
                d,
                hoje,
                aoAbrir = if (d.previstas > 0) ({ navController.navigate(Screen.Dia.criar(d.dia)) }) else null,
            )
        }
    }
}
