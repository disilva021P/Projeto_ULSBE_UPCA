package pt.ipca.cuidas.ui.historico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.time.LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipca.cuidas.Contentor
import pt.ipca.cuidas.models.TomaDoDia

data class DiaHistoricoViewState(
    val tomas: List<TomaDoDia> = emptyList(),
) {
    val feitas: Int get() = tomas.count { it.confirmada }
}

class DiaHistoricoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DiaHistoricoViewState())
    val uiState: StateFlow<DiaHistoricoViewState> = _uiState.asStateFlow()

    // Carrega as tomas de um dia.
    fun fetchTomas(dia: LocalDate) {
        viewModelScope.launch {
            val tomas = withContext(Dispatchers.IO) { Contentor.tomas.tomasDoDia(dia) }
            _uiState.value = DiaHistoricoViewState(tomas = tomas)
        }
    }
}
