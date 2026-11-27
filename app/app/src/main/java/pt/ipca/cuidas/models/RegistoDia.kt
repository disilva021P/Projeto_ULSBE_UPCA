// Resultado de um dia e do período: quantas tomas estavam previstas e quantas foram feitas.
package pt.ipca.cuidas.models

import java.time.LocalDate

enum class EstadoDia { COMPLETO, INCOMPLETO, SEM_TOMAS }

data class RegistoDia(val dia: LocalDate, val previstas: Int, val confirmadas: Int) {
    val completo: Boolean get() = previstas > 0 && confirmadas >= previstas
    val estado: EstadoDia
        get() = when {
            previstas == 0 -> EstadoDia.SEM_TOMAS
            completo -> EstadoDia.COMPLETO
            else -> EstadoDia.INCOMPLETO
        }
    val fracao: Float get() = if (previstas == 0) 0f else confirmadas.coerceAtMost(previstas).toFloat() / previstas
}

data class ResumoRegisto(val dias: List<RegistoDia>, val inicio: LocalDate?) {
    val comTomas: List<RegistoDia> get() = dias.filter { it.previstas > 0 }
    val completos: Int get() = comTomas.count { it.completo }
    val total: Int get() = comTomas.size
    val fracao: Float get() = if (total == 0) 0f else completos.toFloat() / total
}

// Dias completos seguidos até hoje; hoje só conta depois de completo, para a sequência não "cair" de manhã.
fun sequenciaCompleta(dias: List<RegistoDia>, hoje: LocalDate = LocalDate.now()): Int {
    var n = 0
    for (d in dias.sortedByDescending { it.dia }) {
        when {
            d.completo -> n++
            d.dia == hoje -> continue
            else -> break
        }
    }
    return n
}
