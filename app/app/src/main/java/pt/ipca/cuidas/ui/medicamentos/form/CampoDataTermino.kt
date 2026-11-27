package pt.ipca.cuidas.ui.medicamentos.form

import android.app.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.time.ZoneId
import pt.ipca.cuidas.toDateWithWeekday
import pt.ipca.cuidas.ui.components.BotaoSecundario
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.components.ParDeBotoes
import pt.ipca.cuidas.ui.theme.Cores

// Data de término da toma: opcional, para tratamentos com duração definida.
@Composable
fun CampoDataTermino(dataFim: LocalDate?, minimo: LocalDate, aoMudar: (LocalDate?) -> Unit) {
    val context = LocalContext.current
    Text(
        if (dataFim == null) "Sem data de término (toma contínua)." else "Toma até ${dataFim.toDateWithWeekday()}.",
        style = MaterialTheme.typography.bodyLarge,
        color = if (dataFim == null) Cores.TextoSuave else Cores.Texto,
    )
    val escolher = {
        val inicial = dataFim ?: LocalDate.now().plusDays(7)
        DatePickerDialog(
            context,
            { _, ano, mes, dia -> aoMudar(LocalDate.of(ano, mes + 1, dia)) },
            inicial.year, inicial.monthValue - 1, inicial.dayOfMonth,
        ).apply {
            datePicker.minDate = minimo.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }.show()
    }
    if (dataFim == null) {
        BotaoSecundario("Escolher data", icone = Icones.Calendario, aoCarregar = escolher)
    } else {
        ParDeBotoes(
            { m -> BotaoSecundario("Mudar data", m, icone = Icones.Calendario, aoCarregar = escolher) },
            { m -> BotaoSecundario("Tirar data", m) { aoMudar(null) } },
        )
    }
}
