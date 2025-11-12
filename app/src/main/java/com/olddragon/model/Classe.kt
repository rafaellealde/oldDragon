// model/Classe.kt
package com.olddragon.model

abstract class Classe(
    val nome: String,
    val dadoVida: Int,
    val tabelaXP: Map<Int, Int>,
    val habilidades: List<String>
) {
    abstract fun calcularPVBase(): Int
    abstract fun podeUsarArma(arma: String): Boolean
    abstract fun podeUsarArmadura(armadura: String): Boolean
    
    fun xpParaProximoNivel(nivelAtual: Int): Int {
        return tabelaXP[nivelAtual + 1] ?: 0
    }
    
    fun xpParaNivel(nivel: Int): Int {
        return tabelaXP[nivel] ?: 0
    }
    
    companion object {
        val TODAS: List<Classe> by lazy {
            listOf(
                com.olddragon.model.classe.Guerreiro(),
                com.olddragon.model.classe.Clerigo(),
                com.olddragon.model.classe.Ladrao()
            )
        }
        
        fun porNome(nome: String): Classe? {
            return TODAS.find { it.nome == nome }
        }
    }
}