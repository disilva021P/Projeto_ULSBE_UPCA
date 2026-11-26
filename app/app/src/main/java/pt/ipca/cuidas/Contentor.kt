// Objetos partilhados pela aplicação, criados uma vez (injeção de dependências manual).
// Os ViewModels vão buscar aqui os repositórios com application.contentor().
package pt.ipca.cuidas

import android.app.Application
import pt.ipca.cuidas.data.local.Preferencias
import pt.ipca.cuidas.utils.Voz

class Contentor(app: Application) {
    val preferencias = Preferencias(app)
    val voz = Voz(app)
}
