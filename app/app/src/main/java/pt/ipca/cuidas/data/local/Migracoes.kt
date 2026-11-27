// Alterações à base de dados entre versões publicadas da aplicação.
// Ainda não há nenhuma versão publicada, por isso ainda não há nada para migrar.
package pt.ipca.cuidas.data.local

import android.database.sqlite.SQLiteDatabase

object Migracoes {

    fun paraVersao(version: Int, db: SQLiteDatabase) {
        if (version == 2) {
            // algo aqui, ainda não temos versões
        }
    }
}
