// Um medicamento da lista: dados à esquerda e a quantidade por toma à direita (por baixo se a letra for grande).
package pt.ipca.cuidas.ui.medicamentos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pt.ipca.cuidas.models.Medicamento
import pt.ipca.cuidas.models.ParaQueServe
import pt.ipca.cuidas.toShortDate
import pt.ipca.cuidas.ui.components.BotaoSecundario
import pt.ipca.cuidas.ui.components.Cartao
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.components.ParDeBotoes
import pt.ipca.cuidas.ui.components.Texto
import pt.ipca.cuidas.ui.components.colunasPara
import pt.ipca.cuidas.ui.theme.Cores

// Linha de um medicamento com fotografia, horas e botões de ouvir e alterar.
@Composable
fun LinhaMedicamento(medicamento: Medicamento, aoOuvir: () -> Unit, aoAlterar: () -> Unit) {
    val terminado = medicamento.terminado()
    Cartao(fundo = Cores.Campo, contorno = if (terminado) Cores.Linha else Cores.Destaque) {
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val estreito = colunasPara(maxWidth, larguraMinima = 150.dp, maximo = 2) == 1
            Row(verticalAlignment = Alignment.Top) {
                MiniaturaMedicamento(medicamento.foto, medicamento.nome)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(medicamento.nome, style = MaterialTheme.typography.titleMedium)
                    if (estreito) Quantidade(medicamento.dose, Modifier)
                    Texto("Para: ${ParaQueServe.de(medicamento.paraQueServe).nome}", estilo = MaterialTheme.typography.bodyMedium,
                        cor = Cores.TextoSuave)
                    Text(
                        if (medicamento.horarios.isEmpty()) "Sem horário fixo" else medicamento.horarios.joinToString(", ") { it.texto() },
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    textoPeriodo(medicamento)?.let {
                        Text(it, style = MaterialTheme.typography.bodyMedium, color = if (terminado) Cores.TextoSuave else Cores.Atencao)
                    }
                }
                if (!estreito) {
                    Spacer(Modifier.width(8.dp))
                    Quantidade(medicamento.dose, Modifier.widthIn(max = 120.dp))
                }
            }
        }
        ParDeBotoes(
            { m -> BotaoSecundario("Ouvir", m, icone = Icones.Ouvir, aoCarregar = aoOuvir) },
            { m -> BotaoSecundario("Alterar", m, icone = Icones.Editar, aoCarregar = aoAlterar) },
        )
    }
}

@Composable
private fun Quantidade(dose: String, modifier: Modifier) {
    Text(
        dose.ifBlank { "sem dose" },
        style = MaterialTheme.typography.titleMedium,
        color = Cores.DestaqueEscuro,
        textAlign = TextAlign.End,
        modifier = modifier
            .background(Cores.DestaqueClaro, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
    )
}

// Texto com a data de término, se houver.
private fun textoPeriodo(m: Medicamento): String? {
    val fim = m.dataFim ?: return null
    return if (m.terminado()) "Terminado em ${fim.toShortDate()}" else "Até ${fim.toShortDate()}"
}

// Frase lida em voz alta para um medicamento.
fun descricaoParaOuvir(m: Medicamento): String {
    val horas = if (m.horarios.isEmpty()) "sem horário fixo" else "às " + m.horarios.joinToString(" e às ") { it.texto() }
    val fim = m.dataFim?.let { ". Até ${it.toShortDate()}" } ?: ""
    return "${m.nome}. ${m.dose}. Serve para: ${ParaQueServe.de(m.paraQueServe).nome}. Tomar $horas$fim."
}
