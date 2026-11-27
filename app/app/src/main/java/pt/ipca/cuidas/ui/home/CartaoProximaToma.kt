package pt.ipca.cuidas.ui.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import pt.ipca.cuidas.models.TomaDoDia
import pt.ipca.cuidas.ui.components.BotaoPrincipal
import pt.ipca.cuidas.ui.components.Cartao
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.theme.Cores

// Próxima toma do dia com o botão "Já tomei", para confirmar sem abrir a medicação (RNF02).
@Composable
fun CartaoProximaToma(toma: TomaDoDia, aoConfirmar: () -> Unit) {
    Cartao(contorno = Cores.Destaque, fundo = Cores.DestaqueClaro) {
        Text("Próxima toma", style = MaterialTheme.typography.titleMedium, color = Cores.DestaqueEscuro)
        Text(toma.medicamento.nome, style = MaterialTheme.typography.titleLarge)
        Text(
            listOf(toma.medicamento.dose, "às ${toma.horario.texto()}").filter { it.isNotBlank() }.joinToString(" · "),
            style = MaterialTheme.typography.bodyLarge,
        )
        BotaoPrincipal("Já tomei", icone = Icones.Confirmar, aoCarregar = aoConfirmar)
    }
}
