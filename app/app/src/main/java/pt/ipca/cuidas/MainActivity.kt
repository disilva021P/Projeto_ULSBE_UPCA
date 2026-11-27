// Atividade única: arranca o tema e a navegação entre ecrãs.
package pt.ipca.cuidas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import java.time.LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ipca.cuidas.ui.boasvindas.BoasVindasView
import pt.ipca.cuidas.ui.contactos.ContactoFormView
import pt.ipca.cuidas.ui.contactos.ContactosView
import pt.ipca.cuidas.ui.definicoes.DefinicoesView
import pt.ipca.cuidas.ui.historico.DiaHistoricoView
import pt.ipca.cuidas.ui.historico.HistoricoView
import pt.ipca.cuidas.ui.home.HomeView
import pt.ipca.cuidas.ui.medicamentos.MedicamentosView
import pt.ipca.cuidas.ui.medicamentos.TodosMedicamentosView
import pt.ipca.cuidas.ui.medicamentos.form.MedicamentoFormView
import pt.ipca.cuidas.ui.theme.CuidasTheme

const val TAG = "CUIDAS"

class MainActivity : ComponentActivity() {

    private var destinoExterno by mutableStateOf<String?>(null)

    // Arranque do ecrã: tarefas de fundo e navegação entre ecrãs com o tamanho de letra escolhido.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val c = Contentor
        destinoExterno = DestinoExterno.de(intent)

        lifecycleScope.launch(Dispatchers.IO) { c.agendador.reagendarTodos() }

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
                    composable(Screen.Medicamentos.route) {
                        MedicamentosView(navController = navController)
                    }
                    composable(
                        Screen.MedicamentoForm.route,
                        arguments = listOf(navArgument("qr") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        }),
                    ) { backStackEntry ->
                        MedicamentoFormView(
                            navController = navController,
                            id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: 0L,
                            qr = backStackEntry.arguments?.getString("qr"),
                        )
                    }
                    composable(Screen.TodosMedicamentos.route) {
                        TodosMedicamentosView(navController = navController)
                    }
                    composable(Screen.Historico.route) {
                        HistoricoView(navController = navController)
                    }
                    composable(Screen.Dia.route) { backStackEntry ->
                        DiaHistoricoView(
                            navController = navController,
                            dia = backStackEntry.arguments?.getString("dia").toLocalDateOrNull() ?: LocalDate.now(),
                        )
                    }
                    composable(Screen.Contactos.route) {
                        ContactosView(navController = navController)
                    }
                    composable(Screen.ContactoForm.route) { backStackEntry ->
                        ContactoFormView(
                            navController = navController,
                            id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: 0L,
                        )
                    }
                    composable(Screen.Definicoes.route) {
                        DefinicoesView(navController = navController)
                    }
                }

                // Só depois das boas vindas: sem a escolha da leitura e da letra não se salta para outro ecrã.
                LaunchedEffect(destinoExterno) {
                    val destino = destinoExterno
                    if (destino != null && c.preferencias.boasVindasConcluidas) {
                        navController.navigate(destino) { launchSingleTop = true }
                        destinoExterno = null
                    }
                }
            }
        }
    }
    /**
     * Chamada quando a app já estava aberta e voltou a ser aberta de fora
     * (notificação ou QR). Guarda o novo pedido e atualiza o destinoExterno,
     * para o LaunchedEffect saltar para o ecrã certo.
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        destinoExterno = DestinoExterno.de(intent)
    }

    companion object {
        const val EXTRA_ABRIR = "abrir"
        const val ABRIR_MEDICAMENTOS = "medicamentos"
    }
}

/**
 * Define as rotas
 */
sealed class Screen(val route: String) {
    object BoasVindas : Screen("boas_vindas")
    object Home : Screen("home")
    object Medicamentos : Screen("medicamentos")
    object MedicamentoForm : Screen("medicamento/{id}?qr={qr}") {
        // O código QR da receita é opcional, por isso a rota é montada aqui.
        fun criar(id: Long, qr: String? = null) = "medicamento/$id" + (qr?.let { "?qr=" + Uri.encode(it) } ?: "")
    }
    object TodosMedicamentos : Screen("medicamentos/todos")
    object Historico : Screen("historico")
    object Dia : Screen("historico/{dia}") {
        fun criar(dia: LocalDate) = "historico/$dia"
    }
    object Contactos : Screen("contactos")
    object ContactoForm : Screen("contacto/{id}")
    object Definicoes : Screen("definicoes")
}
