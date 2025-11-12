package com.olddragon.model

data class Personagem(
    val id: Long = 0,
    val nome: String = "",
    val nivel: Int = 1,
    val raca: Raca = Raca.Humano,
    val classe: Classe = Classe.Guerreiro,
    val alinhamento: String = "Neutro",
    val atributos: Atributos = Atributos(),
    val experiencia: Int = 0,
    val pvAtuais: Int = 0,
    val pvMaximos: Int = 0
) {
    fun calcularPV(): Int {
        val pvBase = when (classe) {
            is Classe.Guerreiro -> 10
            is Classe.Clerigo -> 8
            is Classe.Ladrao -> 6
        }
        return maxOf(1, pvBase + atributos.calcularModificador(atributos.CON))
    }
    
    fun calcularCA(): Int {
        return 10 + atributos.calcularModificador(atributos.DES)
    }
    
    fun calcularBAC(): Int {
        return nivel + atributos.calcularModificador(atributos.FOR)
    }
    
    fun calcularBAD(): Int {
        return nivel + atributos.calcularModificador(atributos.DES)
    }
}