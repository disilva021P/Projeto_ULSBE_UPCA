// Aplicação CUIDAS: sem conta nem início de sessão; os dados do utilizador ficam no telemóvel.
package pt.ipca.cuidas

import android.app.Application
import pt.ipca.cuidas.lembretes.Notificacoes

class CuidasApp : Application() {

    // Inicia o Contentor e cria o canal das notificações quando a app arranca.
    override fun onCreate() {
        super.onCreate()
        Contentor.iniciar(this)
        Notificacoes.criarCanal(this)
    }
}
