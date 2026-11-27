// Indicativos de país para os números de telefone dos contactos; Portugal (+351) é o país por omissão.
package pt.ipca.cuidas.utils

data class Pais(val bandeira: String, val nome: String, val indicativo: String)

object Indicativos {

    val PORTUGAL = Pais("🇵🇹", "Portugal", "+351")

    val PAISES = listOf(
        PORTUGAL,
        Pais("🇪🇸", "Espanha", "+34"),
        Pais("🇫🇷", "França", "+33"),
        Pais("🇨🇭", "Suíça", "+41"),
        Pais("🇱🇺", "Luxemburgo", "+352"),
        Pais("🇩🇪", "Alemanha", "+49"),
        Pais("🇬🇧", "Reino Unido", "+44"),
        Pais("🇧🇷", "Brasil", "+55"),
        Pais("🇦🇴", "Angola", "+244"),
        Pais("🇨🇻", "Cabo Verde", "+238"),
        Pais("🇲🇿", "Moçambique", "+258"),
    )

    // Separa um número guardado no país e no número sem indicativo.
    // Sem indicativo (ex.: 808 242 424 do SNS 24) ou com um indicativo que não está na lista, o país fica a null.
    fun separar(numero: String): Pair<Pais?, String> {
        var limpo = numero.filter { it.isDigit() || it == '+' }
        if (limpo.startsWith("00")) limpo = "+" + limpo.drop(2)
        if (!limpo.startsWith("+")) return null to limpo
        val pais = PAISES.sortedByDescending { it.indicativo.length }.firstOrNull { limpo.startsWith(it.indicativo) }
        return if (pais != null) pais to limpo.removePrefix(pais.indicativo) else null to limpo
    }

    // Junta o indicativo do país ao número escrito, para guardar (ex.: +351912345678).
    // Se a pessoa já escreveu o número com + à frente, fica como escreveu.
    fun juntar(pais: Pais, numero: String): String {
        val digitos = numero.filter { it.isDigit() }
        return if (numero.trim().startsWith("+")) "+$digitos" else pais.indicativo + digitos
    }
}
