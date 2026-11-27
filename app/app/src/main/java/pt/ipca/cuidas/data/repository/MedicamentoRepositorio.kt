// Medicamentos e horários guardados no SQLite do telemóvel (RF05).
package pt.ipca.cuidas.data.repository

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import java.time.LocalDate
import pt.ipca.cuidas.data.local.CuidasBd
import pt.ipca.cuidas.data.local.stringOuNulo
import pt.ipca.cuidas.data.local.transacao
import pt.ipca.cuidas.models.Horario
import pt.ipca.cuidas.models.Medicamento
import pt.ipca.cuidas.toLocalDateOrNull

class MedicamentoRepositorio(private val bd: CuidasBd) {

    // Todos os medicamentos com os seus horários, por ordem alfabética.
    fun listarTodos(): List<Medicamento> {
        val horarios = horariosPorMedicamento()
        val lista = mutableListOf<Medicamento>()
        bd.readableDatabase.rawQuery(
            "SELECT id, nome, dose, para_que_serve, foto, data_inicio, data_fim FROM medicamento ORDER BY nome COLLATE NOCASE",
            null,
        ).use { c ->
            while (c.moveToNext()) {
                val id = c.getLong(0)
                lista += Medicamento(
                    id = id,
                    nome = c.getString(1),
                    dose = c.getString(2),
                    paraQueServe = c.getString(3),
                    foto = c.stringOuNulo(4),
                    horarios = horarios[id].orEmpty(),
                    dataInicio = c.getString(5).toLocalDateOrNull() ?: LocalDate.now(),
                    dataFim = c.stringOuNulo(6).toLocalDateOrNull(),
                )
            }
        }
        return lista
    }

    // Medicamentos em toma num dia.
    fun ativos(dia: LocalDate = LocalDate.now()): List<Medicamento> = listarTodos().filter { it.ativoEm(dia) }

    fun obter(id: Long): Medicamento? = listarTodos().firstOrNull { it.id == id }

    // Medicamento a que pertence um horário (usado pelos lembretes).
    fun obterPorHorario(horarioId: Long): Pair<Medicamento, Horario>? {
        for (m in listarTodos()) {
            val h = m.horarios.firstOrNull { it.id == horarioId }
            if (h != null) return m to h
        }
        return null
    }

    // Horários que ainda podem tocar: os de medicamentos sem data de término ou que ainda não terminaram.
    fun horariosPorTerminar(hoje: LocalDate = LocalDate.now()): List<Horario> =
        listarTodos().filter { !it.terminado(hoje) }.flatMap { it.horarios }

    // Insere ou atualiza o medicamento e os horários numa só transação.
    fun guardar(m: Medicamento): Long = bd.writableDatabase.transacao {
        val valores = ContentValues().apply {
            put("nome", m.nome.trim())
            put("dose", m.dose.trim())
            put("para_que_serve", m.paraQueServe)
            put("foto", m.foto)
            put("data_inicio", m.dataInicio.toString())
            put("data_fim", m.dataFim?.toString())
        }
        val id = if (m.id == 0L) {
            valores.put("criado_em", System.currentTimeMillis())
            insertOrThrow("medicamento", null, valores)
        } else {
            update("medicamento", valores, "id = ?", arrayOf(m.id.toString()))
            m.id
        }
        atualizarHorarios(this, id, m.horarios)
        id
    }

    // Apaga o medicamento; os horários e as tomas vão em cascata.
    fun apagar(id: Long) {
        bd.writableDatabase.delete("medicamento", "id = ?", arrayOf(id.toString()))
    }

    // Mantém os horários que não mudaram, para não perder as tomas já confirmadas.
    private fun atualizarHorarios(db: SQLiteDatabase, medicamentoId: Long, horarios: List<Horario>) {
        val existentes = mutableMapOf<Int, Long>()
        db.rawQuery("SELECT id, hora, minuto FROM horario WHERE medicamento_id = ?", arrayOf(medicamentoId.toString())).use { c ->
            while (c.moveToNext()) existentes[c.getInt(1) * 60 + c.getInt(2)] = c.getLong(0)
        }
        val novos = horarios.map { it.minutosDoDia() }.toSet()
        for ((minutos, horarioId) in existentes) {
            if (minutos !in novos) db.delete("horario", "id = ?", arrayOf(horarioId.toString()))
        }
        for (minutos in novos - existentes.keys) {
            db.insertOrThrow("horario", null, ContentValues().apply {
                put("medicamento_id", medicamentoId)
                put("hora", minutos / 60)
                put("minuto", minutos % 60)
            })
        }
    }

    private fun horariosPorMedicamento(): Map<Long, List<Horario>> {
        val mapa = mutableMapOf<Long, MutableList<Horario>>()
        bd.readableDatabase.rawQuery("SELECT id, medicamento_id, hora, minuto FROM horario ORDER BY hora, minuto", null).use { c ->
            while (c.moveToNext()) {
                mapa.getOrPut(c.getLong(1)) { mutableListOf() } += Horario(c.getLong(0), c.getInt(2), c.getInt(3))
            }
        }
        return mapa
    }
}
