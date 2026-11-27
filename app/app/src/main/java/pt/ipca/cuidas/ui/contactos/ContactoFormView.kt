package pt.ipca.cuidas.ui.contactos

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pt.ipca.cuidas.ui.components.BotaoCancelar
import pt.ipca.cuidas.ui.components.BotaoPerigo
import pt.ipca.cuidas.ui.components.BotaoPrincipal
import pt.ipca.cuidas.ui.components.CampoTexto
import pt.ipca.cuidas.ui.components.DialogoConfirmar
import pt.ipca.cuidas.ui.components.Ecra
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.components.TituloSecao

// Adicionar ou alterar um contacto importante.
@Composable
fun ContactoFormView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    id: Long = 0L,
) {
    val context = LocalContext.current
    val viewModel: ContactoFormViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    var confirmarApagar by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        viewModel.fetchContacto(id)
    }

    val titulo = if (id == 0L) "Adicionar contacto" else "Alterar contacto"

    Ecra(
        titulo = titulo,
        textoParaOuvir = "$titulo. Escolha o tipo de contacto e escreva o nome e o número de telefone.",
        modifier = modifier,
        navController = navController,
    ) {
        TituloSecao("Tipo de contacto")
        EscolhaTipoContacto(uiState.tipo) { viewModel.escolherTipo(it) }

        TituloSecao("Nome e número")
        CampoTexto(uiState.nome, { viewModel.editarNome(it) }, "Nome (ex.: Dr.ª Ana, Farmácia Central)", erro = uiState.erroNome)
        CampoTexto(
            uiState.numero,
            { viewModel.editarNumero(it) },
            "Número de telefone",
            teclado = KeyboardType.Phone,
            erro = uiState.erroNumero,
            inicio = { EscolhaIndicativo(uiState.pais) { viewModel.escolherPais(it) } },
        )

        Spacer(Modifier.height(12.dp))
        BotaoPrincipal("Guardar contacto", icone = Icones.Confirmar) {
            viewModel.guardar {
                Toast.makeText(context, "Contacto guardado.", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        }
        BotaoCancelar { navController.popBackStack() }

        if (id != 0L) {
            Spacer(Modifier.height(24.dp))
            BotaoPerigo("Apagar este contacto", icone = Icones.Apagar) { confirmarApagar = true }
        }
    }

    if (confirmarApagar) {
        DialogoConfirmar(
            titulo = "Apagar ${uiState.nome}?",
            texto = "O contacto deixa de aparecer na lista.",
            confirmar = "Apagar",
            aoCancelar = { confirmarApagar = false },
            aoConfirmar = {
                confirmarApagar = false
                viewModel.apagar { navController.popBackStack() }
            },
        )
    }
}
