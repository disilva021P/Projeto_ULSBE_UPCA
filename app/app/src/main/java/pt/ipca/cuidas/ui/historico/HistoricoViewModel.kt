package pt.ipca.cuidas.ui.historico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipca.cuidas.Contentor
import pt.ipca.cuidas.models.ResumoRegisto

data class HistoricoViewState(
    // null enquanto o histórico ainda não foi lido.
    val resumo: ResumoRegisto? = null,
)

class HistoricoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HistoricoViewState())
    val uiState: StateFlow<HistoricoViewState> = _uiState.asStateFlow()

    // Carrega o registo das tomas dos últimos dias.
    fun fetchResumo() {
        viewModelScope.launch {
            val resumo = withContext(Dispatchers.IO) { Contentor.registoDias.resumo() }
            _uiState.value = HistoricoViewState(resumo = resumo)
        }
    }
}
