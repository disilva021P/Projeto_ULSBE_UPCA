package pt.ipca.cuidas.ui.definicoes

import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipca.cuidas.Contentor
import pt.ipca.cuidas.utils.Fotos

data class DefinicoesViewState(
    val escala: Float = 1f,
    val leituraAutomatica: Boolean = false,
    val notificacoesPermitidas: Boolean = true,
)

class DefinicoesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DefinicoesViewState())
    val uiState: StateFlow<DefinicoesViewState> = _uiState.asStateFlow()

    // Lê as escolhas guardadas e o estado das notificações.
    fun fetchDefinicoes() {
        verificarNotificacoes()
        _uiState.value = _uiState.value.copy(
            escala = Contentor.preferencias.escala.value,
            leituraAutomatica = Contentor.preferencias.leituraAutomatica.value,
        )
    }

    // O utilizador pode desligar as notificações a qualquer momento nas definições do Android.
    fun verificarNotificacoes() {
        _uiState.value = _uiState.value.copy(
            notificacoesPermitidas = NotificationManagerCompat.from(Contentor.app).areNotificationsEnabled(),
        )
    }

    // Guardam as escolhas nas preferências e mostram-nas logo no ecrã.
    fun definirEscala(escala: Float) {
        Contentor.preferencias.definirEscala(escala)
        _uiState.value = _uiState.value.copy(escala = escala)
    }

    fun definirLeituraAutomatica(ligada: Boolean) {
        Contentor.preferencias.definirLeituraAutomatica(ligada)
        _uiState.value = _uiState.value.copy(leituraAutomatica = ligada)
    }

    // RF03: apaga a medicação, as fotografias, as tomas e os contactos pessoais.
    fun apagarTudo(onSuccess: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Contentor.agendador.cancelarTodos()
                Contentor.bd.apagarDadosDoUtilizador()
                Fotos.apagarTodas(Contentor.app)
                Contentor.preferencias.apagarTudo()
            }
            onSuccess()
        }
    }
}
