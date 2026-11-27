// Preferências guardadas no telemóvel; as que mudam o ecrã são StateFlow para atualizar logo.
package pt.ipca.cuidas.data.local

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class Preferencias(context: Context) {

    private val prefs = context.getSharedPreferences("cuidas_preferencias", Context.MODE_PRIVATE)

    private val _escala = MutableStateFlow(prefs.getFloat(ESCALA, ESCALA_PADRAO))
    val escala: StateFlow<Float> = _escala.asStateFlow()

    // Desligada até a pessoa escolher: a leitura automática só liga se ela disser que sim.
    private val _leituraAutomatica = MutableStateFlow(prefs.getBoolean(LEITURA, false))
    val leituraAutomatica: StateFlow<Boolean> = _leituraAutomatica.asStateFlow()

    // Guardam a escolha e atualizam o StateFlow, para os ecrãs mudarem logo.
    fun definirEscala(escala: Float) {
        prefs.edit().putFloat(ESCALA, escala).apply()
        _escala.value = escala
    }

    fun definirLeituraAutomatica(ligada: Boolean) {
        prefs.edit().putBoolean(LEITURA, ligada).apply()
        _leituraAutomatica.value = ligada
    }

    var boasVindasConcluidas: Boolean
        get() = prefs.getBoolean(BOAS_VINDAS, false)
        set(v) = prefs.edit().putBoolean(BOAS_VINDAS, v).apply()

    var lembretesAgendados: Set<String>
        get() = prefs.getStringSet(LEMBRETES, emptySet()) ?: emptySet()
        set(v) = prefs.edit().putStringSet(LEMBRETES, v).apply()

    // Apaga todas as preferências e volta aos valores iniciais.
    fun apagarTudo() {
        prefs.edit().clear().apply()
        _escala.value = ESCALA_PADRAO
        _leituraAutomatica.value = false
    }

    private companion object {
        const val ESCALA_PADRAO = 1.15f
        const val ESCALA = "escala"
        const val LEITURA = "leitura_automatica"
        const val BOAS_VINDAS = "boas_vindas"
        const val LEMBRETES = "lembretes_agendados"
    }
}
