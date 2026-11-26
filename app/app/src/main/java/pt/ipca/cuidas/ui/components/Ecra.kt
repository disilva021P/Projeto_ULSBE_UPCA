// Estrutura comum dos ecrãs: barra de topo com voltar e ouvir, e conteúdo com scroll.
package pt.ipca.cuidas.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pt.ipca.cuidas.contentor
import pt.ipca.cuidas.ui.theme.Cores

// Ícone com a palavra por baixo, em tamanho fixo, para caber na barra mesmo com letra muito grande.
@Composable
private fun BotaoOuvir(aoCarregar: () -> Unit) {
    val pequeno = with(LocalDensity.current) { 12.dp.toSp() }
    Column(
        modifier = Modifier
            .padding(end = 4.dp)
            .size(width = 64.dp, height = 56.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(role = Role.Button, onClickLabel = "Ouvir este ecrã", onClick = aoCarregar),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icones.Ouvir, contentDescription = null, tint = Cores.DestaqueEscuro)
        Text("Ouvir", fontSize = pequeno, color = Cores.DestaqueEscuro, fontWeight = FontWeight.Bold, maxLines = 1)
    }
}

@Composable
fun Ecra(
    titulo: String,
    textoParaOuvir: String,
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    isBaseScreen: Boolean = false,
    conteudo: @Composable ColumnScope.() -> Unit,
) {
    val c = LocalContext.current.contentor()
    val leituraAutomatica by c.preferencias.leituraAutomatica.collectAsState()
    LaunchedEffect(textoParaOuvir) {
        if (leituraAutomatica) c.voz.falar(textoParaOuvir)
    }
    var tituloCortado by remember(titulo) { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Cores.Fundo,
        topBar = {
            MyTopBar(
                title = titulo,
                navController = navController,
                isBaseScreen = isBaseScreen,
                onTitleOverflow = { tituloCortado = it },
                actions = { BotaoOuvir { c.voz.falar(textoParaOuvir) } },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // Se o título não coube na barra, aparece inteiro no início do ecrã.
            if (tituloCortado) Text(titulo, style = MaterialTheme.typography.headlineMedium)
            conteudo()
        }
    }
}
