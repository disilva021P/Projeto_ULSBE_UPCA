package pt.ipca.cuidas.ui.contactos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipca.cuidas.Contentor
import pt.ipca.cuidas.models.Contacto
import pt.ipca.cuidas.models.TipoContacto

data class ContactosViewState(
    val contactos: List<Contacto> = emptyList(),
    val isLoading: Boolean = false,
) {
    val porTipo: Map<TipoContacto, List<Contacto>> get() = contactos.groupBy { it.tipo }
}

class ContactosViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ContactosViewState())
    val uiState: StateFlow<ContactosViewState> = _uiState.asStateFlow()

    // Carrega os contactos.
    fun fetchContactos() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            val contactos = withContext(Dispatchers.IO) { Contentor.contactos.listar() }
            _uiState.value = _uiState.value.copy(contactos = contactos, isLoading = false)
        }
    }
}
