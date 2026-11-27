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
import pt.ipca.cuidas.utils.Indicativos
import pt.ipca.cuidas.utils.Ligacoes
import pt.ipca.cuidas.utils.Pais

data class ContactoFormViewState(
    val id: Long = 0,
    val nome: String = "",
    val pais: Pais = Indicativos.PORTUGAL,
    val numero: String = "",
    val tipo: TipoContacto = TipoContacto.MEDICO,
    val tentouGuardar: Boolean = false,
) {
    val numeroValido: Boolean get() = Ligacoes.valido(numero)
    val erroNome: String? get() = if (tentouGuardar && nome.isBlank()) "Escreva um nome." else null
    val erroNumero: String? get() = if (tentouGuardar && !numeroValido) "Escreva um número válido." else null
}

class ContactoFormViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ContactoFormViewState())
    val uiState: StateFlow<ContactoFormViewState> = _uiState.asStateFlow()

    private var iniciado = false

    // Carrega o contacto a alterar.
    fun fetchContacto(id: Long) {
        if (iniciado) return
        iniciado = true
        _uiState.value = _uiState.value.copy(id = id)
        if (id == 0L) return
        viewModelScope.launch {
            val contacto = withContext(Dispatchers.IO) { Contentor.contactos.obter(id) } ?: return@launch
            val (pais, numero) = Indicativos.separar(contacto.numero)
            _uiState.value = _uiState.value.copy(
                nome = contacto.nome,
                pais = pais ?: Indicativos.PORTUGAL,
                numero = numero,
                tipo = contacto.tipo,
            )
        }
    }

    // Atualizam o estado à medida que a pessoa preenche o formulário.
    fun editarNome(nome: String) {
        _uiState.value = _uiState.value.copy(nome = nome)
    }

    fun editarNumero(numero: String) {
        _uiState.value = _uiState.value.copy(numero = numero)
    }

    fun escolherPais(pais: Pais) {
        _uiState.value = _uiState.value.copy(pais = pais)
    }

    fun escolherTipo(tipo: TipoContacto) {
        _uiState.value = _uiState.value.copy(tipo = tipo)
    }

    // Guarda o contacto, com o indicativo do país antes do número.
    fun guardar(onSuccess: () -> Unit) {
        _uiState.value = _uiState.value.copy(tentouGuardar = true)
        val e = _uiState.value
        if (e.nome.isBlank() || !e.numeroValido) return
        viewModelScope.launch {
            withContext(Dispatchers.IO) { Contentor.contactos.guardar(Contacto(e.id, e.nome, Indicativos.juntar(e.pais, e.numero), e.tipo)) }
            onSuccess()
        }
    }

    // Apaga o contacto.
    fun apagar(onSuccess: () -> Unit) {
        val id = _uiState.value.id
        viewModelScope.launch {
            withContext(Dispatchers.IO) { Contentor.contactos.apagar(id) }
            onSuccess()
        }
    }
}
