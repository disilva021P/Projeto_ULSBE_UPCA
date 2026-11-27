// Medicamento inserido pelo utilizador ou pelo cuidador, com o período em que é tomado.
package pt.ipca.cuidas.models

import java.time.LocalDate

data class Medicamento(
    val id: Long = 0,
    val nome: String,
    val dose: String,
    val paraQueServe: String,
    val foto: String? = null,
    val horarios: List<Horario> = emptyList(),
    val dataInicio: LocalDate = LocalDate.now(),
    val dataFim: LocalDate? = null,
) {
    // Verdadeiro se o medicamento está em toma nesse dia.
    fun ativoEm(dia: LocalDate): Boolean = !dia.isBefore(dataInicio) && (dataFim == null || !dia.isAfter(dataFim))

    // Depois da data de término sai da lista ativa e deixa de ter tomas, mas fica no histórico.
    fun terminado(hoje: LocalDate = LocalDate.now()): Boolean = dataFim != null && hoje.isAfter(dataFim)
}

data class Horario(val id: Long = 0, val hora: Int, val minuto: Int) {
    fun texto(): String = "%02d:%02d".format(hora, minuto)
    fun minutosDoDia(): Int = hora * 60 + minuto
}

data class TomaDoDia(val medicamento: Medicamento, val horario: Horario, val confirmadaEm: Long?) {
    val confirmada: Boolean get() = confirmadaEm != null
}
