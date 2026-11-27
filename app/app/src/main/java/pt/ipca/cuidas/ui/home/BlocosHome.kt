package pt.ipca.cuidas.ui.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import pt.ipca.cuidas.Screen
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.theme.Cores

data class BlocoHome(val texto: String, val icone: ImageVector, val rota: String, val cor: Color, val fundo: Color)

// Secções do ecrã inicial, pela ordem em que aparecem, cada uma com a sua cor.
fun blocosHome(): List<BlocoHome> = buildList {
    add(BlocoHome("A minha medicação", Icones.Medicamento, Screen.Medicamentos.route, Cores.Destaque, Cores.DestaqueClaro))
    add(BlocoHome("Histórico", Icones.Calendario, Screen.Historico.route, Cores.Sucesso, Cores.SucessoClaro))
    add(BlocoHome("Contactos importantes", Icones.Telefone, Screen.Contactos.route, Cores.Atencao, Cores.AtencaoClaro))
    add(BlocoHome("Definições", Icones.Definicoes, Screen.Definicoes.route, Cores.TextoSuave, Cores.Campo))
}
