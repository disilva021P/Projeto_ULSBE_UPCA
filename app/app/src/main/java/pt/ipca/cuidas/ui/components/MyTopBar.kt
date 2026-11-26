// Barra de topo comum: voltar (fora dos ecrãs base), título numa linha e ações, com a linha azul por baixo.
package pt.ipca.cuidas.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pt.ipca.cuidas.ui.theme.Cores
import pt.ipca.cuidas.ui.theme.CuidasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    title: String = "CUIDAS",
    navController: NavController = rememberNavController(),
    isBaseScreen: Boolean = true,
    onTitleOverflow: (Boolean) -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    Column {
        TopAppBar(
            // Uma linha só: a barra tem altura fixa e duas linhas de letra grande ficavam cortadas.
            title = {
                Text(
                    title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge,
                    onTextLayout = { onTitleOverflow(it.hasVisualOverflow) },
                )
            },
            navigationIcon = {
                if (!isBaseScreen) {
                    IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.size(56.dp)) {
                        Icon(Icones.Voltar, contentDescription = "Voltar")
                    }
                }
            },
            actions = actions,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Cores.Superficie,
                titleContentColor = Cores.Texto,
                navigationIconContentColor = Cores.Texto,
            ),
        )
        HorizontalDivider(thickness = 3.dp, color = Cores.Destaque)
    }
}

@Preview(showBackground = true)
@Composable
fun MyTopBarPreview() {
    CuidasTheme {
        MyTopBar(title = "A minha medicação", isBaseScreen = false)
    }
}
