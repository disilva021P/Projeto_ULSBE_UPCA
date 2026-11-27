package pt.ipca.cuidas.ui.medicamentos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipca.cuidas.Contentor
import pt.ipca.cuidas.models.Medicamento

data class TodosMedicamentosViewState(
    val todos: List<Medicamento> = emptyList(),
) {
    val terminados: List<Medicamento> get() = todos.filter { it.terminado() }.sortedByDescending { it.dataFim }
    val emCurso: List<Medicamento> get() = todos.filterNot { it.terminado() }
}

class TodosMedicamentosViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TodosMedicamentosViewState())
    val uiState: StateFlow<TodosMedicamentosViewState> = _uiState.asStateFlow()

    // Carrega todos os medicamentos.
    fun fetchMedicamentos() {
        viewModelScope.launch {
            val todos = withContext(Dispatchers.IO) { Contentor.medicamentos.listarTodos() }
            _uiState.value = TodosMedicamentosViewState(todos = todos)
        }
    }

    // Lê em voz alta o nome, a quantidade e as horas do medicamento.
    fun ouvir(medicamento: Medicamento) {
        Contentor.voz.falar(descricaoParaOuvir(medicamento))
    }
}
