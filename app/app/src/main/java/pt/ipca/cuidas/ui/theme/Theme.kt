// Tema Material da aplicação com a escala de letra escolhida pelo utilizador.
package pt.ipca.cuidas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

private val esquema = lightColorScheme(
    primary = Cores.Destaque,
    onPrimary = Cores.SobreDestaque,
    secondary = Cores.DestaqueEscuro,
    onSecondary = Color.White,
    background = Cores.Fundo,
    onBackground = Cores.Texto,
    surface = Cores.Superficie,
    onSurface = Cores.Texto,
    surfaceVariant = Cores.Superficie,
    onSurfaceVariant = Cores.TextoSuave,
    surfaceContainerLowest = Cores.Superficie,
    surfaceContainerLow = Cores.Superficie,
    surfaceContainer = Cores.Superficie,
    surfaceContainerHigh = Cores.Superficie,
    surfaceContainerHighest = Cores.Superficie,
    outline = Cores.Linha,
    error = Cores.Perigo,
)

@Composable
fun CuidasTheme(escala: Float = 1f, conteudo: @Composable () -> Unit) {
    val densidade = LocalDensity.current
    // A escala multiplica a letra do sistema, para quem precisa de letra ainda maior.
    CompositionLocalProvider(LocalDensity provides Density(densidade.density, densidade.fontScale * escala)) {
        MaterialTheme(colorScheme = esquema, typography = Typography, content = conteudo)
    }
}
