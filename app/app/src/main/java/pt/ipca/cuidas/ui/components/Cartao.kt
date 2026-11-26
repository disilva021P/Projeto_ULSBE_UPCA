// Cartão com contorno, para agrupar informação relacionada.
package pt.ipca.cuidas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.ipca.cuidas.ui.theme.Cores

@Composable
fun Cartao(
    modifier: Modifier = Modifier,
    contorno: Color = Cores.Linha,
    fundo: Color = Cores.Superficie,
    conteudo: @Composable ColumnScope.() -> Unit,
) {
    val forma = RoundedCornerShape(12.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(fundo, forma)
            .border(1.5.dp, contorno, forma)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        content = conteudo,
    )
}
