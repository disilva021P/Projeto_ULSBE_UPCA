package pt.ipca.cuidas.ui.definicoes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.ipca.cuidas.contentor

data class DefinicoesViewState(
    val escala: Float = 1f,
    val leituraAutomatica: Boolean = false,
)

class DefinicoesViewModel(application: Application) : AndroidViewModel(application) {

    private val c = application.contentor()

    private val _uiState = MutableStateFlow(DefinicoesViewState())
    val uiState: StateFlow<DefinicoesViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            c.preferencias.escala.collect { _uiState.value = _uiState.value.copy(escala = it) }
        }
        viewModelScope.launch {
            c.preferencias.leituraAutomatica.collect { _uiState.value = _uiState.value.copy(leituraAutomatica = it) }
        }
    }

    fun definirEscala(escala: Float) = c.preferencias.definirEscala(escala)

    fun definirLeituraAutomatica(ligada: Boolean) = c.preferencias.definirLeituraAutomatica(ligada)
}
