package pt.ipca.cuidas.ui.medicamentos

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
import pt.ipca.cuidas.ui.components.BotaoSecundario
import pt.ipca.cuidas.ui.components.Divisoria
import pt.ipca.cuidas.ui.components.Ecra
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.components.TipoAviso
import pt.ipca.cuidas.ui.components.TituloSecao

// A minha medicação: só o que há para tomar hoje e o que já foi tomado hoje.
@Composable
fun MedicamentosView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
) {
    val viewModel: MedicamentosViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchTomas()
    }

    val lista = uiState.lista
    val feitas = uiState.feitas
    val porTomar = uiState.porTomar
    val semHorario = uiState.semHorario

    val falar = when {
        lista.isEmpty() && semHorario.isEmpty() -> "Hoje não há tomas marcadas."
        lista.isEmpty() -> "Hoje não há tomas com hora marcada. Tem ${semHorario.size} medicamentos para tomar quando precisar."
        porTomar.isEmpty() -> "Já fez todas as tomas de hoje."
        else -> "Hoje fez ${feitas.size} de ${lista.size} tomas. " +
            porTomar.joinToString(". ") { "Falta ${it.medicamento.nome} às ${it.horario.texto()}" }
    }

    Ecra(
        titulo = "A minha medicação",
        textoParaOuvir = falar,
        modifier = modifier,
        navController = navController,
    ) {
        if (uiState.tomas == null) return@Ecra
        if (lista.isEmpty() && semHorario.isEmpty()) {
            Aviso("Hoje não há tomas marcadas. Pode adicionar medicamentos em «Todos os medicamentos».")
            BotaoSecundario("Todos os medicamentos", icone = Icones.Lista) {
                navController.navigate(Screen.TodosMedicamentos.route)
            }
            return@Ecra
        }
        if (lista.isNotEmpty()) {
            ResumoHoje(feitas.size, lista.size)
            TituloSecao("Por tomar")
            if (porTomar.isEmpty()) Aviso("Já fez todas as tomas de hoje.", tipo = TipoAviso.SUCESSO)
            porTomar.forEach { toma -> CartaoToma(toma) { viewModel.mudarToma(toma, it) } }
        }
        if (feitas.isNotEmpty()) {
            Divisoria()
            TituloSecao("Já tomei")
            feitas.forEach { toma -> CartaoToma(toma) { viewModel.mudarToma(toma, it) } }
        }
        if (semHorario.isNotEmpty()) {
            Divisoria()
            TituloSecao("Sem horário fixo")
            semHorario.forEach { MedicamentoSemHorario(it) }
        }
        Divisoria()
        BotaoSecundario("Ver todos os medicamentos", icone = Icones.Lista) {
            navController.navigate(Screen.TodosMedicamentos.route)
        }
    }
}
