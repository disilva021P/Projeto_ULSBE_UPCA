// Para que serve cada medicamento, em palavras simples.
package pt.ipca.cuidas.models

object ParaQueServe {

    data class Opcao(val chave: String, val nome: String)

    val OPCOES = listOf(
        Opcao("tensao", "Tensão arterial"),
        Opcao("diabetes", "Diabetes (açúcar)"),
        Opcao("coracao", "Coração"),
        Opcao("colesterol", "Colesterol"),
        Opcao("dor", "Dores"),
        Opcao("estomago", "Estômago"),
        Opcao("sono", "Dormir"),
        Opcao("outro", "Outro"),
    )

    // Opção a partir da chave guardada; se não existir, "Outro".
    fun de(chave: String?): Opcao = OPCOES.firstOrNull { it.chave == chave } ?: OPCOES.last()
}
