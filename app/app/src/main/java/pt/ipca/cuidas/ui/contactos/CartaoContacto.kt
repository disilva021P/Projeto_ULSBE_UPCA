package pt.ipca.cuidas.ui.contactos

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import pt.ipca.cuidas.models.Contacto
import pt.ipca.cuidas.ui.components.BotaoSecundario
import pt.ipca.cuidas.ui.components.Cartao
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.components.ParDeBotoes
import pt.ipca.cuidas.ui.theme.Cores
import pt.ipca.cuidas.utils.Indicativos
import pt.ipca.cuidas.utils.Ligacoes

private val ALTURA_CARTAO = 52.dp

// Um contacto da lista: nome em letra grande, número e, por baixo, os botões para ligar e, se for pessoal, alterar.
@Composable
fun CartaoContacto(contacto: Contacto, aoAlterar: () -> Unit) {
    Cartao {
        Text(contacto.nome, style = MaterialTheme.typography.headlineMedium, color = Cores.Texto)
        Text(
            listOfNotNull(
                Indicativos.separar(contacto.numero).first?.bandeira,
                Ligacoes.formatar(contacto.numero),
            ).joinToString(" ") + if (contacto.oficial) " · número oficial" else "",
            style = MaterialTheme.typography.bodyLarge,
            color = Cores.TextoSuave,
        )
        // Contacto oficial (SNS 24, 112): só tem o "Ligar", a toda a largura.
        // Contacto pessoal: "Ligar" e "Alterar" lado a lado, mais baixos, para o nome ser o que mais se vê.
        if (contacto.oficial) {
            BotaoLigar(contacto, texto = "Ligar", mostrarNumero = false, altura = ALTURA_CARTAO)
        } else {
            ParDeBotoes(
                { m -> BotaoLigar(contacto, m, texto = "Ligar", mostrarNumero = false, altura = ALTURA_CARTAO) },
                { m -> BotaoSecundario("Alterar", m, icone = Icones.Editar, altura = ALTURA_CARTAO, aoCarregar = aoAlterar) },
            )
        }
    }
}
