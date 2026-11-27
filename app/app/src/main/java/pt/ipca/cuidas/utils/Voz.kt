// Leitura em voz alta em português com o motor de voz do Android (RF05, RF18).
package pt.ipca.cuidas.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class Voz(context: Context) : TextToSpeech.OnInitListener {

    private val tts = TextToSpeech(context.applicationContext, this)
    private var pronta = false
    private var pendente: String? = null

    // O motor de voz ficou pronto: escolhe o português de Portugal e uma velocidade mais lenta.
    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) return
        val resultado = tts.setLanguage(Locale("pt", "PT"))
        if (resultado == TextToSpeech.LANG_MISSING_DATA || resultado == TextToSpeech.LANG_NOT_SUPPORTED) {
            tts.setLanguage(Locale("pt"))
        }
        // Um pouco mais devagar do que o normal, para ser mais fácil de perceber.
        tts.setSpeechRate(0.85f)
        pronta = true
        pendente?.let { falar(it) }
        pendente = null
    }

    // Lê o texto em voz alta; se o motor ainda não estiver pronto, lê quando estiver.
    fun falar(texto: String) {
        if (texto.isBlank()) return
        if (!pronta) {
            pendente = texto
            return
        }
        tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "cuidas")
    }

    // Para a leitura.
    fun parar() {
        pendente = null
        if (pronta) tts.stop()
    }
}
