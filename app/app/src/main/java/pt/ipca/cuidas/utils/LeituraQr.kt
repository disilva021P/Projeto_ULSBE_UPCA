// Interpreta o texto dos códigos QR: receita (RF06) e abertura da aplicação (RF04).
package pt.ipca.cuidas.utils

import android.net.Uri
import org.json.JSONObject
import pt.ipca.cuidas.models.Horario
import pt.ipca.cuidas.models.Medicamento
import pt.ipca.cuidas.models.ParaQueServe
import pt.ipca.cuidas.objetos
import pt.ipca.cuidas.toLocalDateOrNull
import java.time.LocalDate
import java.util.Base64
import java.util.zip.GZIPInputStream

// Aceita três formatos de receita: a receita eletrónica do SNS (Base64 + gzip), a ligação curta
// cuidas://receita gerada pelo backoffice e o JSON. Só este ficheiro precisa de mudar se o formato mudar.
object LeituraQr {

    // Resultado da leitura de um código QR ou de uma ligação cuidas:// (só usado com a LeituraQr).
    sealed class QrLido {
        class Receita(val medicamentos: List<Medicamento>) : QrLido()
        object Abrir : QrLido()
        object Desconhecido : QrLido()
    }

    // Identifica o tipo de QR (ligação cuidas://, JSON ou receita do SNS) e lê-o.
    fun ler(texto: String?): QrLido {
        if (texto.isNullOrBlank()) return QrLido.Desconhecido
        val t = texto.trim().removePrefix("﻿")
        return when {
            t.startsWith("cuidas:", ignoreCase = true) -> deLigacao(Uri.parse(t))
            t.startsWith("{") -> runCatching { receitaJson(JSONObject(t)) }.getOrNull() ?: QrLido.Desconhecido
            else -> receitaSns(t) ?: QrLido.Desconhecido
        }
    }

    // Lê uma ligação cuidas:// (receita ou abrir a app).
    fun deLigacao(uri: Uri?): QrLido {
        if (uri == null || !uri.scheme.equals("cuidas", ignoreCase = true)) return QrLido.Desconhecido
        return when (uri.host?.lowercase()) {
            "receita" -> receitaLigacao(uri) ?: QrLido.Desconhecido
            "abrir" -> QrLido.Abrir
            else -> QrLido.Desconhecido
        }
    }

    // Formato curto gerado pelo backoffice: cuidas://receita?n=Nome&d=Dose&h=08:00,20:00&p=tensao&f=2026-12-31
    private fun receitaLigacao(uri: Uri): QrLido? {
        val nome = uri.getQueryParameter("n")?.trim().orEmpty()
        if (nome.isEmpty()) return null
        val medicamento = Medicamento(
            nome = nome,
            dose = uri.getQueryParameter("d")?.trim().orEmpty(),
            paraQueServe = ParaQueServe.de(uri.getQueryParameter("p")).chave,
            horarios = horarios(uri.getQueryParameter("h")?.split(",").orEmpty()),
            dataFim = uri.getQueryParameter("f").toLocalDateOrNull(),
        )
        return QrLido.Receita(listOf(medicamento))
    }

    // Formato JSON, também aceite: permite vários medicamentos no mesmo código.
    private fun receitaJson(o: JSONObject): QrLido? {
        if (o.optString("tipo") != "receita") return null
        val lista = o.optJSONArray("medicamentos").objetos().mapNotNull { m ->
            val nome = m.optString("nome").trim()
            if (nome.isEmpty()) return@mapNotNull null
            val horas = m.optJSONArray("horarios")?.let { a -> (0 until a.length()).map { a.optString(it) } }.orEmpty()
            Medicamento(
                nome = nome,
                dose = m.optString("dose").trim(),
                paraQueServe = ParaQueServe.de(m.optString("paraQueServe")).chave,
                horarios = horarios(horas),
                dataFim = m.optString("fim").toLocalDateOrNull(),
            )
        }
        return if (lista.isEmpty()) null else QrLido.Receita(lista)
    }

    // Receita eletrónica do SNS. O texto do QR é Base64 de um ficheiro gzip. Descomprimido, tem um
    // segmento por linha, com os campos separados por "|":
    //   VER|versão|
    //   CRC|nº da receita|...|data de emissão|utente^^telefone|...   -> só se usa a data de emissão
    //   EFR / PRO / LPR                                               -> entidade, médico e local (ignorados)
    //   LRC|linha|...|validade|embalagens|^^código|posologia||         -> uma linha por medicamento
    //   DIP|
    // Os dados do utente e do médico nunca são lidos nem guardados.
    // A receita não traz o nome do medicamento, só o código (CNPEM). Para já o nome fica
    // "Medicamento <código>" e o utilizador corrige-o no formulário.
    private fun receitaSns(texto: String): QrLido? {
        val bytes = runCatching { Base64.getMimeDecoder().decode(texto) }.getOrNull() ?: return null
        // Um ficheiro gzip começa sempre pelos bytes 1F 8B.
        if (bytes.size < 2 || bytes[0] != 0x1f.toByte() || bytes[1] != 0x8b.toByte()) return null
        val conteudo = runCatching {
            GZIPInputStream(bytes.inputStream()).bufferedReader(Charsets.UTF_8).use { it.readText() }
        }.getOrNull() ?: return null

        val segmentos = conteudo.lines().map { it.trim().split("|") }.filter { it.first().isNotEmpty() }
        if (segmentos.none { it.first() == "VER" }) return null
        val emissao = segmentos.firstOrNull { it.first() == "CRC" }?.getOrNull(4)?.take(10).toLocalDateOrNull()

        val lista = segmentos.filter { it.first() == "LRC" }.map { campos ->
            val codigo = campos.getOrNull(6).orEmpty().substringAfterLast("^").trim()
            val posologia = campos.getOrNull(7).orEmpty().trim()
            Medicamento(
                nome = if (codigo.isEmpty()) "Medicamento da receita" else "Medicamento $codigo",
                dose = doseDaPosologia(posologia),
                paraQueServe = ParaQueServe.de(null).chave,
                horarios = horariosDaPosologia(posologia),
                dataFim = fimDaPosologia(posologia, emissao ?: LocalDate.now()),
            )
        }
        return if (lista.isEmpty()) null else QrLido.Receita(lista)
    }

    // "1.0 comprimidos, pequeno-almoço, ..." -> "1 comprimido". Se não perceber, fica o primeiro pedaço.
    private fun doseDaPosologia(posologia: String): String {
        val r = Regex("""^(\d+(?:[.,]\d+)?)\s*(\p{L}+)?""").find(posologia.trim())
            ?: return posologia.substringBefore(",").trim()
        val numero = r.groupValues[1].replace(',', '.').toDoubleOrNull()
            ?: return posologia.substringBefore(",").trim()
        val quantidade = if (numero % 1.0 == 0.0) numero.toInt().toString() else numero.toString().replace('.', ',')
        var unidade = r.groupValues[2].lowercase()
        if (numero == 1.0 && unidade.endsWith("s")) unidade = unidade.dropLast(1)
        return "$quantidade $unidade".trim()
    }

    // Horas por omissão para os momentos do dia que aparecem na posologia.
    private val momentos = listOf(
        Regex("""pequeno[- ]almo[cç]o|em jejum|manh[aã]""") to 8,
        Regex("""almo[cç]o""") to 13,
        Regex("""lanche""") to 17,
        Regex("""jantar""") to 20,
        Regex("""deitar|noite""") to 22,
    )

    // Horas a partir da posologia: momentos do dia, "de N em N horas" ou "N vezes por dia".
    private fun horariosDaPosologia(posologia: String): List<Horario> {
        var t = posologia.lowercase()
        val horas = mutableListOf<Int>()
        // "pequeno-almoço" também contém "almoço": cada momento encontrado é retirado do texto.
        for ((regex, hora) in momentos) {
            if (regex.containsMatchIn(t)) {
                horas += hora
                t = regex.replace(t, " ")
            }
        }
        if (horas.isEmpty()) {
            // "de 8 em 8 horas" ou "8/8h": a partir das 08:00.
            Regex("""(\d+)\s*(?:em|/)\s*(\d+)\s*h""").find(t)?.groupValues?.get(2)?.toIntOrNull()
                ?.takeIf { it in 1..24 }
                ?.let { n -> horas += (0 until 24 / n).map { (8 + it * n) % 24 } }
        }
        if (horas.isEmpty()) {
            // "2 vezes por dia", "1 vez ao dia", "3x dia".
            val vezes = Regex("""(\d+)\s*(?:vez(?:es)?|x)\s*(?:por|ao|/)?\s*dia""").find(t)?.groupValues?.get(1)?.toIntOrNull()
            horas += when (vezes) {
                1 -> listOf(8)
                2 -> listOf(8, 20)
                3 -> listOf(8, 14, 20)
                4 -> listOf(8, 12, 16, 20)
                else -> emptyList()
            }
        }
        return horas.distinct().sorted().map { Horario(hora = it, minuto = 0) }
    }

    // "durante 12 meses" -> data de fim contada a partir da emissão. Sem duração, fica sem data de fim.
    private fun fimDaPosologia(posologia: String, inicio: LocalDate): LocalDate? {
        val r = Regex("""durante\s+(\d+)\s+(dias?|semanas?|m[eê]s|meses|anos?)""").find(posologia.lowercase()) ?: return null
        val n = r.groupValues[1].toLongOrNull() ?: return null
        val fim = when {
            r.groupValues[2].startsWith("dia") -> inicio.plusDays(n)
            r.groupValues[2].startsWith("semana") -> inicio.plusWeeks(n)
            r.groupValues[2].startsWith("ano") -> inicio.plusYears(n)
            else -> inicio.plusMonths(n)
        }
        return fim.minusDays(1)
    }

    // Converte textos "08:00" em horários, sem repetir.
    private fun horarios(textos: List<String>): List<Horario> = textos.mapNotNull { horario(it) }.distinctBy { it.minutosDoDia() }

    // Converte "08:00" num horário; null se não for uma hora válida.
    private fun horario(texto: String): Horario? {
        val partes = texto.trim().split(":")
        if (partes.size != 2) return null
        val h = partes[0].toIntOrNull() ?: return null
        val m = partes[1].toIntOrNull() ?: return null
        return if (h in 0..23 && m in 0..59) Horario(hora = h, minuto = m) else null
    }
}
