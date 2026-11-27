// Contactos importantes em SQLite, incluindo os números oficiais do SNS 24 e do 112.
package pt.ipca.cuidas.data.repository

import android.content.ContentValues
import android.database.Cursor
import pt.ipca.cuidas.data.local.CuidasBd
import pt.ipca.cuidas.data.local.stringOuNulo
import pt.ipca.cuidas.models.Contacto
import pt.ipca.cuidas.models.TipoContacto

class ContactoRepositorio(private val bd: CuidasBd) {

    // Consultas dos contactos.
    fun listar(): List<Contacto> = consultar("SELECT id, nome, numero, tipo, chave FROM contacto ORDER BY ordem, nome COLLATE NOCASE")

    fun obter(id: Long): Contacto? =
        consultar("SELECT id, nome, numero, tipo, chave FROM contacto WHERE id = ?", id.toString()).firstOrNull()

    // Insere ou atualiza um contacto.
    fun guardar(contacto: Contacto) {
        val existente = if (contacto.id == 0L) null else obter(contacto.id)
        if (existente?.oficial == true) return
        val valores = ContentValues().apply {
            put("nome", contacto.nome.trim())
            put("numero", contacto.numero.trim())
            put("tipo", contacto.tipo.name)
            put("ordem", ORDEM_PESSOAIS)
        }
        if (existente == null) {
            bd.writableDatabase.insertOrThrow("contacto", null, valores)
        } else {
            bd.writableDatabase.update("contacto", valores, "id = ?", arrayOf(contacto.id.toString()))
        }
    }

    // Apaga um contacto (o SNS 24 e o 112 não se apagam).
    fun apagar(id: Long) {
        bd.writableDatabase.delete("contacto", "id = ? AND chave IS NULL", arrayOf(id.toString()))
    }

    private fun consultar(sql: String, vararg argumentos: String): List<Contacto> {
        val lista = mutableListOf<Contacto>()
        bd.readableDatabase.rawQuery(sql, argumentos).use { c -> while (c.moveToNext()) lista += ler(c) }
        return lista
    }

    private fun ler(c: Cursor) = Contacto(
        id = c.getLong(0),
        nome = c.getString(1),
        numero = c.getString(2),
        tipo = TipoContacto.de(c.getString(3)),
        chave = c.stringOuNulo(4),
    )

    private companion object {
        // Os contactos da pessoa aparecem depois dos números oficiais dentro de cada grupo.
        const val ORDEM_PESSOAIS = 10
    }
}
