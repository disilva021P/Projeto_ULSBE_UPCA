// Aplicação CUIDAS: sem conta nem início de sessão; os dados do utilizador ficam no telemóvel.
package pt.ipca.cuidas

import android.app.Application
import android.content.Context

class CuidasApp : Application() {

    lateinit var contentor: Contentor
        private set

    override fun onCreate() {
        super.onCreate()
        contentor = Contentor(this)
    }
}

fun Context.contentor(): Contentor = (applicationContext as CuidasApp).contentor
