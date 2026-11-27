package pt.ipca.cuidas.ui.contactos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.theme.Cores
import pt.ipca.cuidas.utils.Indicativos
import pt.ipca.cuidas.utils.Pais

// Bandeira e indicativo antes do número de telefone (🇵🇹 +351); ao tocar abre a lista dos países.
@Composable
fun EscolhaIndicativo(atual: Pais, aoEscolher: (Pais) -> Unit) {
    var aberto by remember { mutableStateOf(false) }
    Box {
        Row(
            modifier = Modifier
                .heightIn(min = 48.dp)
                .clip(RoundedCornerShape(8.dp))
                .semantics { contentDescription = "País ${atual.nome}, ${atual.indicativo}. Toque para mudar." }
                .clickable { aberto = true }
                .padding(start = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("${atual.bandeira} ${atual.indicativo}", style = MaterialTheme.typography.bodyLarge, color = Cores.Texto)
            Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = Cores.Texto)
        }
        DropdownMenu(
            expanded = aberto,
            onDismissRequest = { aberto = false },
            modifier = Modifier.background(Cores.Campo),
        ) {
            Indicativos.PAISES.forEach { pais ->
                val escolhido = pais == atual
                DropdownMenuItem(
                    text = {
                        Text(
                            "${pais.bandeira}  ${pais.nome}  ${pais.indicativo}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (escolhido) FontWeight.Bold else FontWeight.Normal,
                        )
                    },
                    leadingIcon = if (escolhido) {
                        { Icon(Icones.Confirmar, contentDescription = "Escolhido", tint = Cores.DestaqueEscuro) }
                    } else null,
                    onClick = {
                        aberto = false
                        aoEscolher(pais)
                    },
                    modifier = Modifier.heightIn(min = 56.dp),
                )
            }
        }
    }
}
