// Ecrã a abrir quando a aplicação é aberta por uma ligação, um código QR ou um lembrete.
package pt.ipca.cuidas

import android.content.Intent
import pt.ipca.cuidas.models.QrLido
import pt.ipca.cuidas.utils.LeituraQr

object DestinoExterno {

    /**
     * Determina a rota de navegação inicial com base de onde veio.
     *
     * Verifica primeiro se existe um extra explícito para abrir diretamente o ecrã de medicamentos.
     * Caso contrário, tenta ler e interpretar o URI (deep link/QR code) associado ao intent:
     * - Se for uma receita válida, redireciona para o formulário de criação de medicamento com os dados do QR.
     * - Se for nulo, desconhecido ou apenas um comando genérico de abertura, retorna `null` (mantendo o fluxo padrão).
     *
     * @param intent O [Intent] recebido pela Activity (ex: via notificação, atalho ou link externo).
     * @return A rota de destino ([String]) correspondente ou `null` caso não haja redirecionamento específico.
     */
    fun de(intent: Intent?): String? {
        if (intent == null) return null
        if (intent.getStringExtra(MainActivity.EXTRA_ABRIR) == MainActivity.ABRIR_MEDICAMENTOS) return Screen.Medicamentos.route
        val uri = intent.data ?: return null
        return when (LeituraQr.deLigacao(uri)) {
            is QrLido.Receita -> Screen.MedicamentoForm.criar(0, qr = uri.toString())
            QrLido.Abrir, QrLido.Desconhecido -> null
        }
    }
}
