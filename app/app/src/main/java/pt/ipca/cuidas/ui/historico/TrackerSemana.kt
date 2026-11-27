// Registo dos últimos sete dias no ecrã inicial, um círculo por dia, que se ajusta à largura e à letra.
package pt.ipca.cuidas.ui.historico

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import pt.ipca.cuidas.models.EstadoDia
import pt.ipca.cuidas.models.RegistoDia
import pt.ipca.cuidas.models.sequenciaCompleta
import pt.ipca.cuidas.ui.components.Cartao
import pt.ipca.cuidas.ui.theme.Cores

private val ABREVIATURAS = mapOf(
    DayOfWeek.MONDAY to "SEG", DayOfWeek.TUESDAY to "TER", DayOfWeek.WEDNESDAY to "QUA", DayOfWeek.THURSDAY to "QUI",
    DayOfWeek.FRIDAY to "SEX", DayOfWeek.SATURDAY to "SÁB", DayOfWeek.SUNDAY to "DOM",
)

private val NOMES = mapOf(
    DayOfWeek.MONDAY to "segunda", DayOfWeek.TUESDAY to "terça", DayOfWeek.WEDNESDAY to "quarta", DayOfWeek.THURSDAY to "quinta",
    DayOfWeek.FRIDAY to "sexta", DayOfWeek.SATURDAY to "sábado", DayOfWeek.SUNDAY to "domingo",
)

// Resumo da semana com um círculo por dia e a legenda.
@Composable
fun TrackerSemana(dias: List<RegistoDia>, aoAbrir: () -> Unit) {
    val hoje = LocalDate.now()
    val seguidos = sequenciaCompleta(dias, hoje)
    Cartao(
        modifier = Modifier.clickable(role = Role.Button, onClickLabel = "Abrir o histórico", onClick = aoAbrir),
        fundo = Cores.Campo,
    ) {
        Text("Os seus últimos 7 dias", style = MaterialTheme.typography.titleMedium)
        // A largura de cada dia é fixa (um sétimo da linha); o círculo e a letra adaptam-se a ela
        // em vez de crescerem com o tamanho de letra escolhido, para os sete caberem sempre.
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val coluna = maxWidth / 7
            val circulo = minOf(40.dp, coluna - 8.dp)
            Row(Modifier.fillMaxWidth()) {
                dias.forEach { d -> Dia(d, d.dia == hoje, coluna, circulo, Modifier.weight(1f)) }
            }
        }
        Text(
            when (seguidos) {
                0 -> "Confirme todas as tomas de hoje para começar uma sequência."
                1 -> "1 dia completo seguido."
                else -> "$seguidos dias completos seguidos."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = if (seguidos > 0) Cores.Sucesso else Cores.TextoSuave,
            fontWeight = if (seguidos > 0) FontWeight.Bold else FontWeight.Normal,
        )
        Legenda()
    }
}

@Composable
private fun Dia(d: RegistoDia, hoje: Boolean, coluna: Dp, circulo: Dp, modifier: Modifier) {
    val letra = with(LocalDensity.current) { minOf(14.dp, coluna / 3.4f).toSp() }
    val descricao = when (d.estado) {
        EstadoDia.COMPLETO -> "completo"
        EstadoDia.INCOMPLETO -> "${d.confirmadas} de ${d.previstas} tomas"
        EstadoDia.SEM_TOMAS -> "sem tomas"
    }
    Column(
        modifier = modifier
            .background(if (hoje) Cores.DestaqueClaro else Cores.Campo, RoundedCornerShape(10.dp))
            .padding(vertical = 6.dp)
            .clearAndSetSemantics { contentDescription = (if (hoje) "hoje, " else "") + "${NOMES[d.dia.dayOfWeek]}: $descricao" },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        CirculoDia(d.estado, d.fracao, circulo)
        Text(
            ABREVIATURAS.getValue(d.dia.dayOfWeek),
            fontSize = letra,
            maxLines = 1,
            fontWeight = if (hoje) FontWeight.Bold else FontWeight.Normal,
            color = if (hoje) Cores.DestaqueEscuro else Cores.Texto,
        )
    }
}

// Legenda das cores dos círculos.
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Legenda() {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        ItemLegenda(EstadoDia.COMPLETO, 1f, "completo")
        ItemLegenda(EstadoDia.INCOMPLETO, 0.5f, "incompleto")
        ItemLegenda(EstadoDia.SEM_TOMAS, 0f, "sem tomas")
    }
}

@Composable
private fun ItemLegenda(estado: EstadoDia, fracao: Float, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CirculoDia(estado, fracao, tamanho = 18.dp)
        Spacer(Modifier.width(6.dp))
        Text(texto, style = MaterialTheme.typography.bodyMedium, color = Cores.TextoSuave)
    }
}
