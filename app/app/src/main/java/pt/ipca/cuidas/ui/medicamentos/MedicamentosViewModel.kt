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
import pt.ipca.cuidas.models.TomaDoDia

data class MedicamentosViewState(
    // null enquanto as tomas de hoje ainda não foram lidas.
    val tomas: List<TomaDoDia>? = null,
    val semHorario: List<Medicamento> = emptyList(),
) {
    val lista: List<TomaDoDia> get() = tomas.orEmpty()
    val feitas: List<TomaDoDia> get() = lista.filter { it.confirmada }
    val porTomar: List<TomaDoDia> get() = lista.filterNot { it.confirmada }
}

class MedicamentosViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MedicamentosViewState())
    val uiState: StateFlow<MedicamentosViewState> = _uiState.asStateFlow()

    // Carrega as tomas de hoje.
    fun fetchTomas() {
        viewModelScope.launch {
            val tomas = withContext(Dispatchers.IO) { Contentor.tomas.tomasDoDia() }
            val semHorario = withContext(Dispatchers.IO) { Contentor.medicamentos.ativos().filter { it.horarios.isEmpty() } }
            _uiState.value = MedicamentosViewState(tomas = tomas, semHorario = semHorario)
        }
    }

    // Confirma ou desfaz uma toma e atualiza a lista.
    fun mudarToma(toma: TomaDoDia, confirmar: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (confirmar) Contentor.tomas.confirmar(toma.horario.id) else Contentor.tomas.desfazer(toma.horario.id)
            }
            fetchTomas()
        }
    }
}
