// Instruções SQL que criam as tabelas locais.
// A aplicação ainda não foi publicada: a base de dados está na versão 1 e o onCreate cria todas as tabelas.
package pt.ipca.cuidas.data.local

import android.database.sqlite.SQLiteDatabase

object Esquema {

    fun criar(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE medicamento (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                dose TEXT NOT NULL DEFAULT '',
                para_que_serve TEXT NOT NULL DEFAULT 'outro',
                foto TEXT,
                criado_em INTEGER NOT NULL,
                data_inicio TEXT NOT NULL,
                data_fim TEXT
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE horario (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                medicamento_id INTEGER NOT NULL REFERENCES medicamento(id) ON DELETE CASCADE,
                hora INTEGER NOT NULL,
                minuto INTEGER NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE toma (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                horario_id INTEGER NOT NULL REFERENCES horario(id) ON DELETE CASCADE,
                dia TEXT NOT NULL,
                confirmada_em INTEGER NOT NULL,
                UNIQUE (horario_id, dia)
            )
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX idx_horario_medicamento ON horario(medicamento_id)")
        db.execSQL("CREATE INDEX idx_toma_dia ON toma(dia)")
    }
}
