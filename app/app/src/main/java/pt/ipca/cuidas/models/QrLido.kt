// Resultado da leitura de um código QR ou de uma ligação cuidas://.
package pt.ipca.cuidas.models

sealed interface QrLido {
    data class Receita(val medicamentos: List<Medicamento>) : QrLido
    data object Abrir : QrLido
    data object Desconhecido : QrLido
}
