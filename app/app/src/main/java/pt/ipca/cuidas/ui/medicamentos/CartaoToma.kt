package pt.ipca.cuidas.ui.medicamentos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.ipca.cuidas.models.TomaDoDia
import pt.ipca.cuidas.toShortTime
import pt.ipca.cuidas.ui.components.BotaoPrincipal
import pt.ipca.cuidas.ui.components.BotaoSecundario
import pt.ipca.cuidas.ui.components.Cartao
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.theme.Cores

// Uma toma de hoje, com o botão para confirmar ou desfazer.
@Composable
fun CartaoToma(toma: TomaDoDia, aoMudar: (confirmar: Boolean) -> Unit) {
    Cartao(
        contorno = if (toma.confirmada) Cores.Sucesso else Cores.Atencao,
        fundo = if (toma.confirmada) Cores.SucessoClaro else Cores.Campo,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(toma.horario.texto(), style = MaterialTheme.typography.titleLarge, color = Cores.DestaqueEscuro)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(toma.medicamento.nome, style = MaterialTheme.typography.titleMedium)
                if (toma.medicamento.dose.isNotBlank()) {
                    Text(toma.medicamento.dose, style = MaterialTheme.typography.bodyMedium, color = Cores.TextoSuave)
                }
            }
        }
        val confirmadaEm = toma.confirmadaEm
        if (confirmadaEm != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icones.Certo, contentDescription = null, tint = Cores.Sucesso, modifier = Modifier.size(26.dp))
                Spacer(Modifier.width(8.dp))
                Text("Tomado às ${confirmadaEm.toShortTime()}", style = MaterialTheme.typography.titleMedium, color = Cores.Sucesso)
            }
            BotaoSecundario("Enganei-me, ainda não tomei") { aoMudar(false) }
        } else {
            BotaoPrincipal("Já tomei", icone = Icones.Confirmar) { aoMudar(true) }
        }
    }
}
