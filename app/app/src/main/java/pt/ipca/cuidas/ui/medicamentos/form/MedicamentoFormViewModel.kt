package pt.ipca.cuidas.ui.medicamentos.form

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
import pt.ipca.cuidas.models.Horario
import pt.ipca.cuidas.models.Medicamento
import pt.ipca.cuidas.utils.LeituraQr.QrLido
import pt.ipca.cuidas.utils.Fotos
import pt.ipca.cuidas.utils.LeituraQr

data class MedicamentoFormViewState(
    val id: Long = 0,
    val nome: String = "",
    val dose: String = "",
    val paraQueServe: String = "outro",
    val foto: String? = null,
    val fotoOriginal: String? = null,
    val dataInicio: LocalDate = LocalDate.now(),
    val dataFim: LocalDate? = null,
    val horarios: List<Horario> = emptyList(),
    val carregado: Boolean = true,
    val isLoading: Boolean = false,
) {
    val novo: Boolean get() = id == 0L
}

class MedicamentoFormViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MedicamentoFormViewState())
    val uiState: StateFlow<MedicamentoFormViewState> = _uiState.asStateFlow()

    // Ao rodar o ecrã a View volta a pedir os dados: só se carregam uma vez para não perder o que foi escrito.
    private var iniciado = false

    // Prepara o formulário: carrega o medicamento a alterar ou aplica o QR recebido.
    fun iniciar(id: Long, qr: String?, aoAvisar: (String) -> Unit, aoTerminar: () -> Unit) {
        if (iniciado) return
        iniciado = true
        _uiState.value = _uiState.value.copy(id = id)
        if (id != 0L) {
            _uiState.value = _uiState.value.copy(carregado = false)
            viewModelScope.launch {
                val m = withContext(Dispatchers.IO) { Contentor.medicamentos.obter(id) }
                if (m != null) {
                    preencher(m)
                    _uiState.value = _uiState.value.copy(foto = m.foto, fotoOriginal = m.foto, dataInicio = m.dataInicio)
                }
                _uiState.value = _uiState.value.copy(carregado = true)
            }
        } else if (qr != null) {
            aplicarQr(qr, aoAvisar, aoTerminar)
        }
    }

    // Atualizam o estado à medida que a pessoa preenche o formulário.
    fun editarNome(nome: String) {
        _uiState.value = _uiState.value.copy(nome = nome)
    }

    fun editarDose(dose: String) {
        _uiState.value = _uiState.value.copy(dose = dose)
    }

    fun escolherParaQueServe(chave: String) {
        _uiState.value = _uiState.value.copy(paraQueServe = chave)
    }

    fun mudarDataFim(data: LocalDate?) {
        _uiState.value = _uiState.value.copy(dataFim = data)
    }

    // Junta uma hora, sem repetir, e mantém as horas por ordem.
    fun juntarHorario(h: Horario) {
        val atuais = _uiState.value.horarios
        if (atuais.none { it.minutosDoDia() == h.minutosDoDia() }) {
            _uiState.value = _uiState.value.copy(horarios = (atuais + h).sortedBy { it.minutosDoDia() })
        }
    }

    fun tirarHorario(h: Horario) {
        _uiState.value = _uiState.value.copy(horarios = _uiState.value.horarios - h)
    }

    // Troca a fotografia e apaga a anterior se ainda não estava guardada.
    fun novaFoto(caminho: String) {
        val estado = _uiState.value
        if (estado.foto != estado.fotoOriginal) Fotos.apagar(estado.foto)
        _uiState.value = estado.copy(foto = caminho)
    }

    // Lê o QR da receita: com um medicamento preenche o formulário, com vários guarda-os todos.
    fun aplicarQr(texto: String, aoAvisar: (String) -> Unit, aoTerminar: () -> Unit) {
        when (val lido = LeituraQr.ler(texto)) {
            is QrLido.Receita -> if (lido.medicamentos.size == 1) {
                preencher(lido.medicamentos.first())
                aoAvisar("Receita lida. Confirme os dados e guarde.")
            } else {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        lido.medicamentos.forEach { Contentor.medicamentos.guardar(it) }
                        Contentor.agendador.reagendarTodos()
                    }
                    aoAvisar("Foram adicionados ${lido.medicamentos.size} medicamentos da receita.")
                    aoTerminar()
                }
            }
            QrLido.Abrir -> aoAvisar("Este código não é uma receita.")
            QrLido.Desconhecido -> aoAvisar("Não foi possível ler a receita. Pode escrever à mão.")
        }
    }

    // Guarda o medicamento, apaga a fotografia antiga e reagenda os lembretes.
    fun guardar(onSuccess: (mensagem: String) -> Unit) {
        val e = _uiState.value
        val medicamento = Medicamento(e.id, e.nome, e.dose, e.paraQueServe, e.foto, e.horarios, e.dataInicio, e.dataFim)
        _uiState.value = e.copy(isLoading = true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Contentor.medicamentos.guardar(medicamento)
                if (e.fotoOriginal != null && e.fotoOriginal != e.foto) Fotos.apagar(e.fotoOriginal)
                Contentor.agendador.reagendarTodos()
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
            onSuccess(if (medicamento.terminado()) "Guardado no histórico de medicamentos." else "Medicamento guardado.")
        }
    }

    // Apaga o medicamento, a fotografia e os lembretes.
    fun apagar(onSuccess: () -> Unit) {
        val e = _uiState.value
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Contentor.medicamentos.apagar(e.id)
                Fotos.apagar(e.fotoOriginal)
                Contentor.agendador.reagendarTodos()
            }
            onSuccess()
        }
    }

    // Copia os dados de um medicamento para o formulário.
    private fun preencher(m: Medicamento) {
        _uiState.value = _uiState.value.copy(
            nome = m.nome,
            dose = m.dose,
            paraQueServe = m.paraQueServe,
            dataFim = m.dataFim,
        )
        m.horarios.forEach { juntarHorario(it) }
    }
}
