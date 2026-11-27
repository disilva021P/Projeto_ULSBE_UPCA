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
import pt.ipca.cuidas.ui.components.BotaoPrincipal
import pt.ipca.cuidas.ui.components.Divisoria
import pt.ipca.cuidas.ui.components.Ecra
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.components.TituloSecao

// Todos os medicamentos, incluindo os tratamentos que já terminaram; é aqui que se adiciona um novo.
@Composable
fun TodosMedicamentosView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
) {
    val viewModel: TodosMedicamentosViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchMedicamentos()
    }

    val emCurso = uiState.emCurso
    val terminados = uiState.terminados
    val falar = "Todos os medicamentos. ${emCurso.size} em curso e ${terminados.size} terminados."
    val abrir: (Long) -> Unit = { id -> navController.navigate(Screen.MedicamentoForm.criar(id)) }

    Ecra(
        titulo = "Todos os medicamentos",
        textoParaOuvir = falar,
        modifier = modifier,
        navController = navController,
    ) {
        BotaoPrincipal("Adicionar medicamento", icone = Icones.Adicionar) { abrir(0L) }
        if (uiState.todos.isEmpty()) Aviso("Ainda não registou nenhum medicamento. Pode adicionar à mão ou ler o código QR da receita.")

        if (emCurso.isNotEmpty()) TituloSecao("Em curso (${emCurso.size})")
        emCurso.forEach { m ->
            LinhaMedicamento(m, aoOuvir = { viewModel.ouvir(m) }, aoAlterar = { abrir(m.id) })
        }

        if (terminados.isNotEmpty()) {
            if (emCurso.isNotEmpty()) Divisoria()
            TituloSecao("Terminados (${terminados.size})")
        }
        terminados.forEach { m ->
            LinhaMedicamento(m, aoOuvir = { viewModel.ouvir(m) }, aoAlterar = { abrir(m.id) })
        }
    }
}
