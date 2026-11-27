// Utilitários de acesso ao SQLite usados pelos repositórios.
package pt.ipca.cuidas.data.local

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

// Corre o bloco numa transação: ou grava tudo ou nada.
inline fun <T> SQLiteDatabase.transacao(bloco: SQLiteDatabase.() -> T): T {
    beginTransaction()
    try {
        val r = bloco()
        setTransactionSuccessful()
        return r
    } finally {
        endTransaction()
    }
}

// Texto de uma coluna, ou null se estiver vazia.
fun Cursor.stringOuNulo(indice: Int): String? = if (isNull(indice)) null else getString(indice)

// Corre um SELECT COUNT(*) e devolve o número.
fun SQLiteDatabase.contar(sql: String, vararg argumentos: String): Int =
    rawQuery(sql, argumentos).use { c -> if (c.moveToFirst()) c.getInt(0) else 0 }
