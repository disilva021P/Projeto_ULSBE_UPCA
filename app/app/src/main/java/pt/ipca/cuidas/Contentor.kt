// Objetos partilhados pela aplicação, criados uma vez (injeção de dependências manual).
// É iniciado uma vez no CuidasApp; os ViewModels usam Contentor.medicamentos, Contentor.preferencias, ...
// (como o Firebase.firestore nos exemplos das aulas). Cada objeto só é criado na primeira vez que é usado.
package pt.ipca.cuidas

import android.app.Application
import pt.ipca.cuidas.data.local.CuidasBd
import pt.ipca.cuidas.data.local.Preferencias
import pt.ipca.cuidas.data.repository.ContactoRepositorio
import pt.ipca.cuidas.data.repository.MedicamentoRepositorio
import pt.ipca.cuidas.data.repository.RegistoDiasRepositorio
import pt.ipca.cuidas.data.repository.TomaRepositorio
import pt.ipca.cuidas.lembretes.AgendadorLembretes
import pt.ipca.cuidas.utils.Voz

object Contentor {
    lateinit var app: Application
        private set

    // Guarda a Application para os objetos poderem ser criados mais tarde (chamado no CuidasApp).
    fun iniciar(application: Application) {
        app = application
    }

    // Definições do utilizador guardadas no telemóvel (tamanho da letra, leitura em voz alta, boas vindas feitas, ...).
    val preferencias by lazy { Preferencias(app) }

    // Base de dados SQLite da app (tabelas medicamento, horario, toma e contacto).
    val bd by lazy { CuidasBd(app) }

    // Acesso aos medicamentos e aos seus horários na base de dados (listar, guardar, apagar).
    val medicamentos by lazy { MedicamentoRepositorio(bd) }

    // Registo das tomas (tomada, falhada, ...); usa os medicamentos para saber o que estava previsto.
    val tomas by lazy { TomaRepositorio(bd, medicamentos) }

    // Resumo de cada dia (tomas feitas e previstas) para o histórico e o início.
    val registoDias by lazy { RegistoDiasRepositorio(medicamentos, tomas) }

    // Contactos importantes guardados no telemóvel (SNS 24, 112, médico, farmácia, ...).
    val contactos by lazy { ContactoRepositorio(bd) }

    // Leitura em voz alta dos textos dos ecrãs (botão "Ouvir").
    val voz by lazy { Voz(app) }

    // Marca e desmarca os alarmes dos lembretes de cada toma, segundo os horários dos medicamentos e as preferências.
    val agendador by lazy { AgendadorLembretes(app, medicamentos, preferencias) }
}
