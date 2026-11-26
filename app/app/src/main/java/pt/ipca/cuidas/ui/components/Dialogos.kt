// Diálogo de confirmação com botões grandes, usado antes de apagar dados.
package pt.ipca.cuidas.ui.components

import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.ipca.cuidas.ui.theme.Cores

@Composable
fun DialogoConfirmar(
    titulo: String,
    texto: String,
    confirmar: String,
    aoConfirmar: () -> Unit,
    aoCancelar: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = aoCancelar,
        title = { Text(titulo) },
        text = { Text(texto, style = MaterialTheme.typography.bodyLarge) },
        confirmButton = {
            TextButton(onClick = aoConfirmar, modifier = Modifier.heightIn(min = 56.dp)) {
                Text(confirmar, color = Cores.Perigo, style = MaterialTheme.typography.labelLarge)
            }
        },
        dismissButton = {
            TextButton(onClick = aoCancelar, modifier = Modifier.heightIn(min = 56.dp)) {
                Text("Cancelar", style = MaterialTheme.typography.labelLarge)
            }
        },
    )
}
