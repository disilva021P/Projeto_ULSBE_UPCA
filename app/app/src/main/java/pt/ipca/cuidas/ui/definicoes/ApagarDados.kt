package pt.ipca.cuidas.ui.definicoes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import pt.ipca.cuidas.ui.components.BotaoPerigo
import pt.ipca.cuidas.ui.components.DialogoConfirmar
import pt.ipca.cuidas.ui.components.Icones

// Apagar todos os dados do utilizador guardados neste telemóvel (RF03), sempre com confirmação.
@Composable
fun ApagarDados(aoConfirmar: () -> Unit) {
    var confirmar by remember { mutableStateOf(false) }

    BotaoPerigo("Apagar todos os meus dados", icone = Icones.Apagar) { confirmar = true }

    if (confirmar) {
        DialogoConfirmar(
            titulo = "Apagar todos os dados?",
            texto = "Apaga a medicação, as fotografias e as tomas guardadas neste telemóvel. Não é possível desfazer.",
            confirmar = "Apagar tudo",
            aoCancelar = { confirmar = false },
            aoConfirmar = {
                confirmar = false
                aoConfirmar()
            },
        )
    }
}
