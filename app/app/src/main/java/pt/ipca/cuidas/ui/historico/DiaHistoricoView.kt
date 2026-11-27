// Um dia do histórico: que medicamentos foram tomados e a que horas.
package pt.ipca.cuidas.ui.historico

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.time.LocalDate
import pt.ipca.cuidas.models.TomaDoDia
import pt.ipca.cuidas.toDateWithWeekday
import pt.ipca.cuidas.toShortTime
import pt.ipca.cuidas.ui.components.Aviso
import pt.ipca.cuidas.ui.components.Cartao
import pt.ipca.cuidas.ui.components.Ecra
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.theme.Cores

// Ecrã com as tomas de um dia do histórico.
@Composable
fun DiaHistoricoView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    dia: LocalDate = LocalDate.now(),
) {
    val viewModel: DiaHistoricoViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(dia) {
        viewModel.fetchTomas(dia)
    }

    val tomas = uiState.tomas
    val hoje = dia == LocalDate.now()
    val titulo = if (hoje) "Hoje" else dia.toDateWithWeekday()
    val falar = "$titulo. Tomou ${uiState.feitas} de ${tomas.size} tomas. " +
        tomas.joinToString(". ") { "${it.medicamento.nome} às ${it.horario.texto()}, " + if (it.confirmada) "tomado" else "não tomado" }

    Ecra(
        titulo = titulo,
        textoParaOuvir = falar,
        modifier = modifier,
        navController = navController,
    ) {
        Text("${uiState.feitas} de ${tomas.size} tomas feitas", style = MaterialTheme.typography.headlineMedium, color = Cores.DestaqueEscuro)
        if (tomas.isEmpty()) Aviso("Neste dia não havia tomas marcadas.")
        tomas.forEach { LinhaTomaDoDia(it, hoje) }
    }
}

// Uma toma do dia, com a hora, o nome e se foi feita.
@Composable
private fun LinhaTomaDoDia(toma: TomaDoDia, hoje: Boolean) {
    val confirmadaEm = toma.confirmadaEm
    Cartao(contorno = if (confirmadaEm != null) Cores.Destaque else Cores.Linha) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(toma.horario.texto(), style = MaterialTheme.typography.titleLarge, color = Cores.DestaqueEscuro)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(toma.medicamento.nome, style = MaterialTheme.typography.titleMedium)
                if (toma.medicamento.dose.isNotBlank()) {
                    Text(toma.medicamento.dose, style = MaterialTheme.typography.bodyMedium, color = Cores.TextoSuave)
                }
                Text(
                    when {
                        confirmadaEm != null -> "Tomado às ${confirmadaEm.toShortTime()}"
                        hoje -> "Por tomar"
                        else -> "Não tomado"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            if (confirmadaEm != null) {
                Icon(Icones.Certo, contentDescription = null, tint = Cores.DestaqueEscuro, modifier = Modifier.size(28.dp))
            }
        }
    }
}
