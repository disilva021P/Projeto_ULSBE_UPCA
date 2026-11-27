package pt.ipca.cuidas.ui.boasvindas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.ipca.cuidas.Contentor

data class BoasVindasViewState(
    val leitura: Boolean? = null,
    val escala: Float = 1f,
)

class BoasVindasViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BoasVindasViewState())
    val uiState: StateFlow<BoasVindasViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            Contentor.preferencias.escala.collect { escala ->
                _uiState.value = _uiState.value.copy(escala = escala)
            }
        }
    }

    // Guarda a escolha da leitura em voz alta e confirma-a em voz.
    fun escolherLeitura(ligada: Boolean) {
        _uiState.value = _uiState.value.copy(leitura = ligada)
        Contentor.preferencias.definirLeituraAutomatica(ligada)
        if (ligada) Contentor.voz.falar("A leitura em voz alta está ligada.") else Contentor.voz.parar()
    }

    // Guarda o tamanho da letra (aplica-se logo à app toda).
    fun escolherEscala(escala: Float) {
        Contentor.preferencias.definirEscala(escala)
    }

    // Marca as boas-vindas como vistas, para não voltarem a aparecer.
    fun concluir() {
        Contentor.preferencias.boasVindasConcluidas = true
    }
}
