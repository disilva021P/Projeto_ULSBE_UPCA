// Título de secção e separador, para dividir visualmente os blocos de um ecrã.
package pt.ipca.cuidas.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pt.ipca.cuidas.ui.theme.Cores

@Composable
fun TituloSecao(texto: String, modifier: Modifier = Modifier) {
    Text(
        texto,
        style = MaterialTheme.typography.titleLarge,
        color = Cores.Texto,
        modifier = modifier.padding(top = 6.dp).semantics { heading() },
    )
}

@Composable
fun Divisoria() {
    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 2.dp, color = Cores.Linha)
}
