package pt.ipca.cuidas.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipca.cuidas.Contentor
import pt.ipca.cuidas.models.RegistoDia
import pt.ipca.cuidas.models.TomaDoDia

data class HomeViewState(
    val proximaToma: TomaDoDia? = null,
    val semana: List<RegistoDia> = emptyList(),
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeViewState())
    val uiState: StateFlow<HomeViewState> = _uiState.asStateFlow()

    // Carrega a próxima toma e a última semana.
    fun fetchResumo() {
        viewModelScope.launch {
            val proxima = withContext(Dispatchers.IO) { Contentor.tomas.tomasDoDia().firstOrNull { !it.confirmada } }
            val semana = withContext(Dispatchers.IO) { Contentor.registoDias.ultimosDias(7) }
            _uiState.value = _uiState.value.copy(proximaToma = proxima, semana = semana)
        }
    }

    // Confirma a toma e atualiza o resumo.
    fun confirmarToma(toma: TomaDoDia) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { Contentor.tomas.confirmar(toma.horario.id) }
            fetchResumo()
        }
    }
}
