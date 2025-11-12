package com.olddragon.model

sealed class Raca(
    val nome: String,
    val movimentoBase: Int,
    val infravisao: Int,
    val alinhamentoTendencia: String,
    val habilidades: List<String>
) {
    object Humano : Raca("Humano", 9, 0, "Qualquer", listOf("Aprendizado", "Adaptabilidade"))
    object Elfo : Raca("Elfo", 9, 18, "Neutro", listOf("Percepção Natural", "Graciosos"))
    object Anao : Raca("Anão", 6, 18, "Ordeiro", listOf("Mineradores", "Vigoroso"))
    object Halfling : Raca("Halfling", 6, 0, "Neutro", listOf("Furtivos", "Destemidos"))
}