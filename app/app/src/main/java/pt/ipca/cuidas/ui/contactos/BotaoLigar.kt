package pt.ipca.cuidas.ui.contactos

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import pt.ipca.cuidas.models.Contacto
import pt.ipca.cuidas.ui.components.ALTURA_BOTAO
import pt.ipca.cuidas.ui.components.BotaoPerigo
import pt.ipca.cuidas.ui.components.BotaoPrincipal
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.utils.Ligacoes

// Botão de chamada direta para um contacto guardado.
@Composable
fun BotaoLigar(
    contacto: Contacto,
    modifier: Modifier = Modifier,
    texto: String = "Ligar ${contacto.nome}",
    mostrarNumero: Boolean = true,
    altura: Dp = ALTURA_BOTAO,
) {
    val context = LocalContext.current
    val rotulo = if (mostrarNumero) "$texto (${Ligacoes.formatar(contacto.numero)})" else texto
    if (contacto.chave == Contacto.EMERGENCIA) {
        BotaoPerigo(rotulo, modifier, icone = Icones.Telefone, altura = altura) { Ligacoes.ligar(context, contacto.numero) }
    } else {
        BotaoPrincipal(rotulo, modifier, icone = Icones.Telefone, altura = altura) { Ligacoes.ligar(context, contacto.numero) }
    }
}
