// Chamadas telefónicas num toque (RF15): abre o marcador e é a pessoa que liga.
package pt.ipca.cuidas.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object Ligacoes {

    // Abre o marcador do telefone com o número; sem telefone mostra o número.
    fun ligar(context: Context, numero: String) {
        val limpo = numero.filter { it.isDigit() || it == '+' }
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$limpo")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Ligue para o ${formatar(numero)}", Toast.LENGTH_LONG).show()
        }
    }

    // Agrupa os algarismos de três em três, como se dizem em voz alta (808 242 424, +351 912 345 678).
    fun formatar(numero: String): String {
        val (pais, local) = Indicativos.separar(numero)
        return if (pais != null) "${pais.indicativo} ${agrupar(local)}" else agrupar(local)
    }

    private fun agrupar(numero: String): String {
        if (numero.length <= 4) return numero
        return numero.reversed().chunked(3).joinToString(" ").reversed()
    }

    // Verdadeiro se o número tem algarismos suficientes.
    fun valido(numero: String): Boolean = numero.count { it.isDigit() } >= 3 &&
        numero.all { it.isDigit() || it == '+' || it == ' ' }
}
