package pt.ipca.cuidas.ui.boasvindas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.ipca.cuidas.contentor

data class BoasVindasViewState(
    val leitura: Boolean? = null,
    val escala: Float = 1f,
)

class BoasVindasViewModel(application: Application) : AndroidViewModel(application) {

    private val c = application.contentor()

    private val _uiState = MutableStateFlow(BoasVindasViewState())
    val uiState: StateFlow<BoasVindasViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            c.preferencias.escala.collect { escala ->
                _uiState.value = _uiState.value.copy(escala = escala)
            }
        }
    }

    fun escolherLeitura(ligada: Boolean) {
        _uiState.value = _uiState.value.copy(leitura = ligada)
        c.preferencias.definirLeituraAutomatica(ligada)
        if (ligada) c.voz.falar("A leitura em voz alta está ligada.") else c.voz.parar()
    }

    fun escolherEscala(escala: Float) {
        c.preferencias.definirEscala(escala)
    }

    fun concluir() {
        c.preferencias.boasVindasConcluidas = true
    }
}
