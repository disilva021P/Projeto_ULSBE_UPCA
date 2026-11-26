// Funções de extensão usadas em toda a aplicação: datas em português de Portugal e leitura tolerante de JSON.
package pt.ipca.cuidas

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import org.json.JSONArray
import org.json.JSONObject

private val PT = Locale("pt", "PT")
private val DATA_CURTA = DateTimeFormatter.ofPattern("dd/MM/yyyy", PT)
private val HORA = DateTimeFormatter.ofPattern("HH:mm")

// 2026-12-31 -> LocalDate; texto vazio ou inválido -> null
fun String?.toLocalDateOrNull(): LocalDate? =
    if (this.isNullOrBlank()) null else runCatching { LocalDate.parse(this) }.getOrNull()

// 31/12/2026
fun LocalDate.toShortDate(): String = format(DATA_CURTA)

// Quinta-feira, 31/12/2026
fun LocalDate.toDateWithWeekday(): String {
    val semana = dayOfWeek.getDisplayName(TextStyle.FULL, PT).replaceFirstChar { it.uppercase() }
    return "$semana, ${toShortDate()}"
}

// Milissegundos guardados no SQLite -> dia e hora locais
fun Long.toLocalDate(): LocalDate = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

fun Long.toShortTime(): String = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).format(HORA)

// Campos em falta não fazem falhar a aplicação.
fun JSONArray?.objetos(): List<JSONObject> =
    if (this == null) emptyList() else (0 until length()).mapNotNull { optJSONObject(it) }

fun JSONObject.textoOuNulo(campo: String): String? =
    if (isNull(campo)) null else optString(campo, "").ifBlank { null }
