package pt.ipca.cuidas.ui.medicamentos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.ipca.cuidas.models.Medicamento
import pt.ipca.cuidas.models.ParaQueServe
import pt.ipca.cuidas.ui.components.Cartao
import pt.ipca.cuidas.ui.components.Texto
import pt.ipca.cuidas.ui.theme.Cores

// Medicamento sem horário fixo (por exemplo, para as dores): aparece hoje para não ser esquecido.
@Composable
fun MedicamentoSemHorario(m: Medicamento) {
    Cartao(fundo = Cores.Campo, contorno = Cores.Turquesa) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MiniaturaMedicamento(m.foto, m.nome, tamanho = 52.dp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(m.nome, style = MaterialTheme.typography.titleMedium)
                if (m.dose.isNotBlank()) Text(m.dose, style = MaterialTheme.typography.bodyMedium, color = Cores.DestaqueEscuro)
                Texto("Para: ${ParaQueServe.de(m.paraQueServe).nome}", estilo = MaterialTheme.typography.bodyMedium, cor = Cores.TextoSuave)
            }
        }
        Text("Tome só quando precisar, como o médico indicou.", style = MaterialTheme.typography.bodyMedium, color = Cores.Turquesa)
    }
}
