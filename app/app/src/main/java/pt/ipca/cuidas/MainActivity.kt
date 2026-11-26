// Atividade única: arranca o tema e a navegação entre ecrãs.
package pt.ipca.cuidas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.ipca.cuidas.ui.boasvindas.BoasVindasView
import pt.ipca.cuidas.ui.definicoes.DefinicoesView
import pt.ipca.cuidas.ui.home.HomeView
import pt.ipca.cuidas.ui.theme.CuidasTheme

const val TAG = "CUIDAS"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val c = contentor()

        setContent {
            val navController = rememberNavController()
            val escala by c.preferencias.escala.collectAsState()
            val inicial = if (c.preferencias.boasVindasConcluidas) Screen.Home.route else Screen.BoasVindas.route

            CuidasTheme(escala) {
                NavHost(navController = navController, startDestination = inicial) {
                    composable(Screen.BoasVindas.route) {
                        BoasVindasView(navController = navController)
                    }
                    composable(Screen.Home.route) {
                        HomeView(navController = navController)
                    }
                    composable(Screen.Definicoes.route) {
                        DefinicoesView(navController = navController)
                    }
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object BoasVindas : Screen("boas_vindas")
    object Home : Screen("home")
    object Definicoes : Screen("definicoes")
}
