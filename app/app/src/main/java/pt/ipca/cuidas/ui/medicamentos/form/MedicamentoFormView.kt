package pt.ipca.cuidas.ui.medicamentos.form

import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pt.ipca.cuidas.ui.components.BotaoPerigo
import pt.ipca.cuidas.ui.components.BotaoPrincipal
import pt.ipca.cuidas.ui.components.BotaoSecundario
import pt.ipca.cuidas.ui.components.CampoTexto
import pt.ipca.cuidas.ui.components.DialogoConfirmar
import pt.ipca.cuidas.ui.components.Divisoria
import pt.ipca.cuidas.ui.components.Ecra
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.components.TituloSecao
import pt.ipca.cuidas.ui.components.rememberLeitorQr
import pt.ipca.cuidas.ui.theme.Cores

// Adicionar ou alterar um medicamento, à mão ou pela leitura do código QR da receita (RF06).
@Composable
fun MedicamentoFormView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    id: Long = 0L,
    qr: String? = null,
) {
    val context = LocalContext.current
    val viewModel: MedicamentoFormViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    var confirmarApagar by remember { mutableStateOf(false) }

    val avisar: (String) -> Unit = { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
    val voltar: () -> Unit = { navController.popBackStack() }

    LaunchedEffect(id, qr) {
        viewModel.iniciar(id, qr, avisar, voltar)
    }

    val lerQr = rememberLeitorQr("Aponte para o código QR da receita") { viewModel.aplicarQr(it, avisar, voltar) }

    val falar = if (id == 0L) {
        "Adicionar medicamento. Pode ler o código QR da receita ou escrever o nome, a quantidade, para que serve e as horas."
    } else {
        "Alterar o medicamento ${uiState.nome}."
    }

    Ecra(
        titulo = if (id == 0L) "Adicionar medicamento" else "Alterar medicamento",
        textoParaOuvir = falar,
        modifier = modifier,
        navController = navController,
    ) {
        if (!uiState.carregado) {
            CircularProgressIndicator(color = Cores.Destaque)
            return@Ecra
        }
        if (id == 0L) {
            BotaoPrincipal("Ler o código QR da receita", icone = Icones.CodigoQr, aoCarregar = lerQr)
            Text("Ou escreva à mão:", style = MaterialTheme.typography.titleMedium)
        }

        CampoTexto(uiState.nome, { viewModel.editarNome(it) }, "Nome do medicamento")
        CampoTexto(uiState.dose, { viewModel.editarDose(it) }, "Quantidade por toma (ex.: 1 comprimido)")

        TituloSecao("Para que serve?")
        EscolhaParaQueServe(uiState.paraQueServe) { viewModel.escolherParaQueServe(it) }

        TituloSecao("A que horas toma?")
        CampoHoras(uiState.horarios, aoJuntar = { viewModel.juntarHorario(it) }, aoTirar = { viewModel.tirarHorario(it) })

        TituloSecao("Data de término da toma")
        CampoDataTermino(uiState.dataFim, minimo = uiState.dataInicio) { viewModel.mudarDataFim(it) }

        TituloSecao("Fotografia da embalagem")
        CampoFotografia(uiState.foto, uiState.nome) { viewModel.novaFoto(it) }

        Divisoria()
        BotaoPrincipal("Guardar", icone = Icones.Confirmar, ativo = uiState.nome.isNotBlank() && !uiState.isLoading) {
            viewModel.guardar { mensagem ->
                avisar(mensagem)
                voltar()
            }
        }
        if (id != 0L) {
            BotaoPerigo("Apagar este medicamento", icone = Icones.Apagar) { confirmarApagar = true }
        } else {
            BotaoSecundario("Cancelar", aoCarregar = voltar)
        }
    }

    if (confirmarApagar) {
        DialogoConfirmar(
            titulo = "Apagar ${uiState.nome}?",
            texto = "Os lembretes e o registo das tomas deste medicamento também são apagados. " +
                "Se o tratamento só terminou, é melhor escolher a data de término.",
            confirmar = "Apagar",
            aoCancelar = { confirmarApagar = false },
            aoConfirmar = {
                confirmarApagar = false
                viewModel.apagar(onSuccess = voltar)
            },
        )
    }
}
