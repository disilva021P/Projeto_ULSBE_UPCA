package pt.ipca.cuidas.ui.historico

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import pt.ipca.cuidas.models.RegistoDia
import pt.ipca.cuidas.toDateWithWeekday
import pt.ipca.cuidas.ui.components.Cartao
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.theme.Cores

// Um dia do histórico: estado, tomas feitas e toque para ver os medicamentos.
@Composable
fun LinhaRegistoDia(registo: RegistoDia, hoje: LocalDate, aoAbrir: (() -> Unit)? = null) {
    val estado = when {
        registo.previstas == 0 -> "Sem tomas marcadas"
        registo.completo -> "Completo"
        registo.dia == hoje -> "Em curso"
        else -> "Incompleto"
    }
    val modificador = if (aoAbrir != null) Modifier.clickable(role = Role.Button, onClick = aoAbrir) else Modifier
    Cartao(
        modifier = modificador,
        contorno = if (registo.completo) Cores.Sucesso else Cores.Linha,
        fundo = if (registo.completo) Cores.SucessoClaro else Cores.Campo,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CirculoDia(registo.estado, registo.fracao, tamanho = 32.dp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(if (registo.dia == hoje) "Hoje" else registo.dia.toDateWithWeekday(), style = MaterialTheme.typography.titleMedium)
                val tomas = if (registo.previstas > 0) " · ${registo.confirmadas} de ${registo.previstas} tomas" else ""
                Text(estado + tomas, style = MaterialTheme.typography.bodyMedium, color = Cores.TextoSuave)
            }
            if (aoAbrir != null) {
                Spacer(Modifier.width(6.dp))
                Icon(Icones.Seguinte, contentDescription = null, tint = Cores.DestaqueEscuro)
            }
        }
    }
}
