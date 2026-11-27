package pt.ipca.cuidas.ui.medicamentos.form

import android.app.TimePickerDialog
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import pt.ipca.cuidas.models.Horario
import pt.ipca.cuidas.ui.components.BotaoSecundario
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.theme.Cores

// Lista das horas de toma, com botões para juntar e tirar.
@Composable
fun CampoHoras(horarios: List<Horario>, aoJuntar: (Horario) -> Unit, aoTirar: (Horario) -> Unit) {
    val context = LocalContext.current
    if (horarios.isEmpty()) {
        Text("Sem horas, o medicamento aparece em «Sem horário fixo», para tomar quando precisar.",
            style = MaterialTheme.typography.bodyMedium, color = Cores.TextoSuave)
    }
    horarios.forEach { h ->
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icones.Relogio, contentDescription = null, tint = Cores.DestaqueEscuro, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(10.dp))
            Text(h.texto(), style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            BotaoSecundario("Tirar", icone = Icones.Apagar, preencher = false) { aoTirar(h) }
        }
    }
    BotaoSecundario("Juntar hora", icone = Icones.Adicionar) {
        TimePickerDialog(context, { _, hora, minuto -> aoJuntar(Horario(hora = hora, minuto = minuto)) }, 8, 0, true).show()
    }
}
