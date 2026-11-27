package pt.ipca.cuidas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import pt.ipca.cuidas.ui.theme.Cores

// Bloco do ecrã inicial: quadrado na grelha de duas colunas, ou largo numa coluna quando a letra é grande.
@Composable
fun BlocoQuadrado(
    texto: String,
    icone: ImageVector,
    cor: Color,
    fundo: Color,
    largo: Boolean,
    modifier: Modifier = Modifier,
    contador: Int? = null,
    aoCarregar: () -> Unit,
) {
    val forma = RoundedCornerShape(14.dp)
    val base = modifier
        .clip(forma)
        .background(fundo)
        .border(1.5.dp, cor.copy(alpha = 0.45f), forma)
        .clickable(role = Role.Button, onClick = aoCarregar)
        .padding(12.dp)
    if (largo) {
        Row(base.heightIn(min = 84.dp), verticalAlignment = Alignment.CenterVertically) {
            Circulo(icone, cor)
            Spacer(Modifier.width(14.dp))
            Text(texto, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
            Contador(contador, cor)
        }
    } else {
        Column(base.aspectRatio(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Circulo(icone, cor)
            Spacer(Modifier.height(10.dp))
            Text(texto, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center, maxLines = 3,
                overflow = TextOverflow.Ellipsis)
            Contador(contador, cor)
        }
    }
}

@Composable
private fun Circulo(icone: ImageVector, cor: Color) {
    // O azul principal leva ícone escuro por contraste; as cores de apoio aguentam ícone branco.
    val tinta = if (cor == Cores.Destaque) Cores.SobreDestaque else Color.White
    Box(Modifier.size(52.dp).background(cor, CircleShape), contentAlignment = Alignment.Center) {
        Icon(icone, contentDescription = null, tint = tinta, modifier = Modifier.size(28.dp))
    }
}

@Composable
private fun Contador(contador: Int?, cor: Color) {
    if (contador == null || contador <= 0) return
    Text(
        "$contador",
        style = MaterialTheme.typography.labelLarge,
        color = Color.White,
        modifier = Modifier
            .padding(top = 4.dp, start = 6.dp)
            .background(if (cor == Cores.Destaque) Cores.DestaqueEscuro else cor, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 1.dp),
    )
}
