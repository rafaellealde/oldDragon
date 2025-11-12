package com.olddragon.model

sealed class Classe(
    val nome: String,
    val dadoVida: Int,
    val tabelaXP: Map<Int, Int>,
    val habilidades: List<String>
) {
    object Guerreiro : Classe("Guerreiro", 10, mapOf(
        1 to 0, 2 to 2000, 3 to 4000, 4 to 7000, 5 to 10000,
        6 to 20000, 7 to 30000, 8 to 40000, 9 to 50000, 10 to 100000
    ), listOf("Aparar", "Maestria em Arma"))
    
    object Clerigo : Classe("Clérigo", 8, mapOf(
        1 to 0, 2 to 1500, 3 to 3000, 4 to 5500, 5 to 8500,
        6 to 17000, 7 to 27000, 8 to 37000, 9 to 47000, 10 to 94000
    ), listOf("Magias Divinas", "Afastar Mortos-Vivos"))
    
    object Ladrao : Classe("Ladrão", 6, mapOf(
        1 to 0, 2 to 1000, 3 to 2000, 4 to 4000, 5 to 7000,
        6 to 14000, 7 to 24000, 8 to 34000, 9 to 44000, 10 to 88000
    ), listOf("Ataque Furtivo", "Ouvir Ruídos"))
}