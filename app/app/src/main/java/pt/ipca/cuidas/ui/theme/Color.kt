// Paleta da aplicação: cinzento claro e azul como cores principais, e cores de apoio com significado fixo.
package pt.ipca.cuidas.ui.theme

import androidx.compose.ui.graphics.Color
import pt.ipca.cuidas.ui.components.Texto

object Cores {
    val Fundo = Color(0xFFEDEDED)
    val Superficie = Color(0xFFEDEDED)
    val Campo = Color(0xFFFFFFFF)
    val Destaque = Color(0xFF00A0EA)
    val DestaqueClaro = Color(0xFFE1F4FD)

    // Texto escuro sobre o azul: o branco não atinge o contraste mínimo de 4,5:1.
    val SobreDestaque = Color(0xFF0A1F2E)

    // Versão escura do azul para texto e contornos sobre o fundo claro.
    val DestaqueEscuro = Color(0xFF006E9F)
    val Texto = Color(0xFF1C1C1C)
    val TextoSuave = Color(0xFF4A4A4A)
    val Linha = Color(0xFFC4C4C4)

    // Cores de apoio: verde é feito ou completo, laranja é por fazer ou atenção, vermelho é urgente ou apagar.
    // Todas têm contraste suficiente com texto branco.
    val Sucesso = Color(0xFF2E7D32)
    val SucessoClaro = Color(0xFFE6F4EA)
    val Atencao = Color(0xFFB45309)
    val AtencaoClaro = Color(0xFFFFF4E0)
    val Perigo = Color(0xFFB3261E)
    val PerigoClaro = Color(0xFFFCE8E6)
    val Roxo = Color(0xFF6B4FA0)
    val RoxoClaro = Color(0xFFEFEAF8)
    val Turquesa = Color(0xFF00796B)
    val TurquesaClaro = Color(0xFFE0F2F1)
}
