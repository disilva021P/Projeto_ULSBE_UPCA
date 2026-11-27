// Tomas confirmadas (RF11): uma por horário e por dia.
package pt.ipca.cuidas.data.repository

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import java.time.LocalDate
import pt.ipca.cuidas.data.local.CuidasBd
import pt.ipca.cuidas.models.TomaDoDia

class TomaRepositorio(private val bd: CuidasBd, private val medicamentos: MedicamentoRepositorio) {

    // Tomas previstas para um dia, com a hora em que foram confirmadas.
    fun tomasDoDia(dia: LocalDate = LocalDate.now()): List<TomaDoDia> {
        val confirmadas = mutableMapOf<Long, Long>()
        bd.readableDatabase.rawQuery("SELECT horario_id, confirmada_em FROM toma WHERE dia = ?", arrayOf(dia.toString())).use { c ->
            while (c.moveToNext()) confirmadas[c.getLong(0)] = c.getLong(1)
        }
        return medicamentos.ativos(dia)
            .flatMap { m -> m.horarios.map { h -> TomaDoDia(m, h, confirmadas[h.id]) } }
            .sortedBy { it.horario.minutosDoDia() }
    }

    // Confirma a toma; devolve false se já estava confirmada.
    fun confirmar(horarioId: Long, dia: LocalDate = LocalDate.now()): Boolean {
        val valores = ContentValues().apply {
            put("horario_id", horarioId)
            put("dia", dia.toString())
            put("confirmada_em", System.currentTimeMillis())
        }
        return bd.writableDatabase.insertWithOnConflict("toma", null, valores, SQLiteDatabase.CONFLICT_IGNORE) != -1L
    }

    // Desfaz a confirmação de uma toma.
    fun desfazer(horarioId: Long, dia: LocalDate = LocalDate.now()) {
        bd.writableDatabase.delete("toma", "horario_id = ? AND dia = ?", arrayOf(horarioId.toString(), dia.toString()))
    }

    // Horários confirmados em cada dia de um intervalo.
    fun confirmadasEntre(inicio: LocalDate, fim: LocalDate): Map<LocalDate, Set<Long>> {
        val mapa = mutableMapOf<LocalDate, MutableSet<Long>>()
        bd.readableDatabase.rawQuery(
            "SELECT dia, horario_id FROM toma WHERE dia BETWEEN ? AND ?",
            arrayOf(inicio.toString(), fim.toString()),
        ).use { c ->
            while (c.moveToNext()) mapa.getOrPut(LocalDate.parse(c.getString(0))) { mutableSetOf() } += c.getLong(1)
        }
        return mapa
    }
}
