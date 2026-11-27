package pt.ipca.cuidas.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import pt.ipca.cuidas.ui.theme.Cores

// Texto da aplicação, com o estilo e a cor por omissão do tema.
@Composable
fun Texto(
    texto: String,
    modifier: Modifier = Modifier,
    estilo: TextStyle = MaterialTheme.typography.bodyLarge,
    cor: Color = Cores.Texto,
    peso: FontWeight? = null,
) {
    Text(texto, modifier = modifier, style = estilo.copy(color = cor, fontWeight = peso ?: estilo.fontWeight))
}
