package pt.ipca.cuidas.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.ipca.cuidas.ui.theme.Cores

// Opção de ligar ou desligar, com o texto à esquerda.
@Composable
fun LinhaInterruptor(texto: String, ligado: Boolean, aoMudar: (Boolean) -> Unit) {
    Cartao {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(texto, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(12.dp))
            Switch(
                checked = ligado,
                onCheckedChange = aoMudar,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Cores.Destaque,
                    checkedThumbColor = Cores.SobreDestaque,
                    uncheckedTrackColor = Cores.Campo,
                ),
            )
        }
    }
}
