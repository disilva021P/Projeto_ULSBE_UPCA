package pt.ipca.cuidas.ui.contactos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pt.ipca.cuidas.models.TipoContacto
import pt.ipca.cuidas.ui.components.GrelhaResponsiva
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.theme.Cores

private val FORMA = RoundedCornerShape(12.dp)

// Escolha do tipo de contacto em quadrados grandes com ícone.
// O escolhido fica com fundo azul claro, contorno grosso e "Escolhido" escrito, para não se confundir com os botões.
@Composable
fun EscolhaTipoContacto(atual: TipoContacto, aoEscolher: (TipoContacto) -> Unit) {
    val tipos = TipoContacto.entries
    Column(Modifier.selectableGroup()) {
        GrelhaResponsiva(tipos.size, larguraMinima = 150.dp) { i, m ->
            val tipo = tipos[i]
            OpcaoTipo(tipo, escolhido = tipo == atual, modifier = m) { aoEscolher(tipo) }
        }
    }
}

@Composable
private fun OpcaoTipo(tipo: TipoContacto, escolhido: Boolean, modifier: Modifier, aoCarregar: () -> Unit) {
    Column(
        modifier = modifier
            .heightIn(min = 104.dp)
            .clip(FORMA)
            .background(if (escolhido) Cores.DestaqueClaro else Cores.Campo)
            .border(if (escolhido) 4.dp else 2.dp, if (escolhido) Cores.DestaqueEscuro else Cores.Linha, FORMA)
            .selectable(selected = escolhido, role = Role.RadioButton, onClick = aoCarregar)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            iconeDe(tipo),
            contentDescription = null,
            tint = if (escolhido) Cores.DestaqueEscuro else Cores.TextoSuave,
            modifier = Modifier.size(34.dp),
        )
        Spacer(Modifier.height(6.dp))
        Text(
            tipo.nome,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = if (escolhido) FontWeight.Bold else FontWeight.Normal,
            color = Cores.Texto,
            textAlign = TextAlign.Center,
        )
        if (escolhido) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                Icon(Icones.Confirmar, contentDescription = null, tint = Cores.DestaqueEscuro, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(4.dp))
                Text("Escolhido", style = MaterialTheme.typography.bodyMedium, color = Cores.DestaqueEscuro)
            }
        }
    }
}

private fun iconeDe(tipo: TipoContacto): ImageVector = when (tipo) {
    TipoContacto.MEDICO -> Icones.Pessoa
    TipoContacto.FARMACIA -> Icones.Medicamento
    TipoContacto.FAMILIAR -> Icones.Casa
    TipoContacto.SERVICO_SAUDE -> Icones.Telefone
}
