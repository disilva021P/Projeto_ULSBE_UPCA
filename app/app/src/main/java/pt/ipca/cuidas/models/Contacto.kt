// Contacto importante guardado no telemóvel: médico, farmácia, familiar ou serviço de saúde.
package pt.ipca.cuidas.models

enum class TipoContacto(val nome: String) {
    MEDICO("Médico"),
    FARMACIA("Farmácia"),
    FAMILIAR("Familiares"),
    SERVICO_SAUDE("Serviços de saúde");

    companion object {
        // Tipo a partir do texto guardado; por omissão, familiar.
        fun de(texto: String?): TipoContacto = entries.firstOrNull { it.name == texto } ?: FAMILIAR
    }
}

data class Contacto(
    val id: Long = 0,
    val nome: String,
    val numero: String,
    val tipo: TipoContacto,
    val chave: String? = null,
) {
    // Os números oficiais (SNS 24 e 112) vêm com a aplicação e não podem ser alterados.
    val oficial: Boolean get() = chave != null

    companion object {
        const val SNS24 = "sns24"
        const val EMERGENCIA = "112"
    }
}
