package pt.ipca.cuidas.ui.contactos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pt.ipca.cuidas.Screen
import pt.ipca.cuidas.models.TipoContacto
import pt.ipca.cuidas.ui.components.Aviso
import pt.ipca.cuidas.ui.components.BotaoPrincipal
import pt.ipca.cuidas.ui.components.Ecra
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.components.TituloSecao

// Contactos importantes agrupados por tipo, com chamada direta.
@Composable
fun ContactosView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
) {
    val viewModel: ContactosViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchContactos()
    }

    val falar = "Contactos importantes. " + uiState.contactos.joinToString(". ") { it.nome }

    Ecra(titulo = "Contactos importantes", textoParaOuvir = falar, modifier = modifier, navController = navController) {
        BotaoPrincipal("Adicionar contacto", icone = Icones.Adicionar) {
            navController.navigate(Screen.ContactoForm.route.replace("{id}", "0"))
        }
        TipoContacto.entries.forEach { tipo ->
            val lista = uiState.porTipo[tipo].orEmpty()
            TituloSecao(tipo.nome)
            if (lista.isEmpty()) {
                Aviso("Ainda não tem nenhum contacto em ${tipo.nome.lowercase()}.")
            }
            lista.forEach { contacto ->
                CartaoContacto(contacto) {
                    navController.navigate(Screen.ContactoForm.route.replace("{id}", contacto.id.toString()))
                }
            }
        }
    }
}
