// Botões grandes e com aspeto claro de botão (RNF01: alvos bem acima dos 48 dp).
package pt.ipca.cuidas.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pt.ipca.cuidas.ui.theme.Cores

val ALTURA_BOTAO = 60.dp
private val FORMA = RoundedCornerShape(12.dp)
private val MARGENS = PaddingValues(horizontal = 16.dp, vertical = 10.dp)

// Botão principal do ecrã, grande e cheio.
@Composable
fun BotaoPrincipal(
    texto: String,
    modifier: Modifier = Modifier,
    icone: ImageVector? = null,
    ativo: Boolean = true,
    aoCarregar: () -> Unit,
) {
    Button(
        onClick = aoCarregar,
        enabled = ativo,
        modifier = modifier.fillMaxWidth().heightIn(min = ALTURA_BOTAO),
        shape = FORMA,
        contentPadding = MARGENS,
        colors = ButtonDefaults.buttonColors(
            containerColor = Cores.Destaque,
            contentColor = Cores.SobreDestaque,
            disabledContainerColor = Cores.Linha,
            disabledContentColor = Cores.TextoSuave,
        ),
    ) { ConteudoBotao(texto, icone) }
}

// Botão com contorno, para ações secundárias.
@Composable
fun BotaoSecundario(
    texto: String,
    modifier: Modifier = Modifier,
    icone: ImageVector? = null,
    preencher: Boolean = true,
    aoCarregar: () -> Unit,
) {
    OutlinedButton(
        onClick = aoCarregar,
        modifier = (if (preencher) modifier.fillMaxWidth() else modifier).heightIn(min = ALTURA_BOTAO),
        shape = FORMA,
        contentPadding = MARGENS,
        border = BorderStroke(2.dp, Cores.Destaque),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Cores.Campo, contentColor = Cores.DestaqueEscuro),
    ) { ConteudoBotao(texto, icone) }
}

// Botão vermelho para ações perigosas (apagar).
@Composable
fun BotaoPerigo(
    texto: String,
    modifier: Modifier = Modifier,
    icone: ImageVector? = null,
    aoCarregar: () -> Unit,
) {
    Button(
        onClick = aoCarregar,
        modifier = modifier.fillMaxWidth().heightIn(min = ALTURA_BOTAO),
        shape = FORMA,
        contentPadding = MARGENS,
        colors = ButtonDefaults.buttonColors(containerColor = Cores.Perigo, contentColor = Color.White),
    ) { ConteudoBotao(texto, icone) }
}

// Botão que fica destacado quando está escolhido (sim/não, opções).
@Composable
fun BotaoEscolha(texto: String, escolhido: Boolean, modifier: Modifier = Modifier, aoCarregar: () -> Unit) {
    if (escolhido) {
        BotaoPrincipal(texto, modifier, icone = Icones.Confirmar, aoCarregar = aoCarregar)
    } else {
        BotaoSecundario(texto, modifier, aoCarregar = aoCarregar)
    }
}

@Composable
private fun RowScope.ConteudoBotao(texto: String, icone: ImageVector?) {
    if (icone != null) {
        Icon(icone, contentDescription = null, modifier = Modifier.size(26.dp))
        Spacer(Modifier.width(10.dp))
    }
    Text(texto, style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center)
}
