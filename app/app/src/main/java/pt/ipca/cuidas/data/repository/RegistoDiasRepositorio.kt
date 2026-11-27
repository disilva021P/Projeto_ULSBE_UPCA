// Calcula, a partir das tomas confirmadas, como correu cada dia.
package pt.ipca.cuidas.data.repository

import java.time.LocalDate
import pt.ipca.cuidas.models.RegistoDia
import pt.ipca.cuidas.models.ResumoRegisto

class RegistoDiasRepositorio(
    private val medicamentos: MedicamentoRepositorio,
    private val tomas: TomaRepositorio,
) {

    // Do mais antigo para o mais recente, incluindo os dias sem tomas marcadas.
    fun ultimosDias(n: Int = 7, hoje: LocalDate = LocalDate.now()): List<RegistoDia> =
        dias(hoje.minusDays(n - 1L), hoje)

    // O histórico começa no primeiro medicamento, para não contar dias anteriores ao uso da aplicação.
    fun resumo(dias: Int = PERIODO, hoje: LocalDate = LocalDate.now()): ResumoRegisto {
        val primeiro = medicamentos.listarTodos().minOfOrNull { it.dataInicio } ?: return ResumoRegisto(emptyList(), null)
        val inicio = maxOf(primeiro, hoje.minusDays(dias - 1L))
        if (inicio.isAfter(hoje)) return ResumoRegisto(emptyList(), inicio)
        return ResumoRegisto(dias(inicio, hoje).reversed(), inicio)
    }

    // Um dia está completo quando todas as tomas dos medicamentos ativos nesse dia foram confirmadas.
    // Usa os horários atuais: a aplicação não guarda o histórico de mudanças de horário.
    private fun dias(inicio: LocalDate, fim: LocalDate): List<RegistoDia> {
        val lista = medicamentos.listarTodos()
        val confirmadas = tomas.confirmadasEntre(inicio, fim)
        return generateSequence(inicio) { it.plusDays(1) }
            .takeWhile { !it.isAfter(fim) }
            .map { dia ->
                val previstos = lista.filter { it.ativoEm(dia) }.flatMap { m -> m.horarios.map { it.id } }.toSet()
                RegistoDia(dia, previstos.size, confirmadas[dia].orEmpty().count { it in previstos })
            }
            .toList()
    }

    companion object {
        const val PERIODO = 30
    }
}
