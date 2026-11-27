// Base de dados SQLite onde ficam todos os dados do utilizador, que nunca saem do telemóvel.
package pt.ipca.cuidas.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CuidasBd(context: Context) : SQLiteOpenHelper(context, NOME, null, VERSAO) {

    // Liga as chaves estrangeiras, para o ON DELETE CASCADE funcionar.
    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) = Esquema.criar(db)

    // Corre as migrações quando a versão da base de dados sobe.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) Migracoes.paraVersao(newVersion, db)
    }

    // RF03. Os números oficiais (SNS 24 e 112) ficam, porque não são pessoais.
    fun apagarDadosDoUtilizador() {
        writableDatabase.transacao {
            delete("toma", null, null)
            delete("horario", null, null)
            delete("medicamento", null, null)
            delete("contacto", "chave IS NULL", null)
        }
    }

    companion object {
        const val NOME = "cuidas.db"
        // Ainda não há versões publicadas: fica na 1 até haver uma atualização que mude as tabelas.
        const val VERSAO = 1
    }
}
