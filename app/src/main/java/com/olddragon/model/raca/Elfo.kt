// model/raca/Elfo.kt
package com.olddragon.model.raca

import com.olddragon.model.Raca

class Elfo : Raca(
    nome = "Elfo",
    movimentoBase = 9,
    infravisao = 18,
    alinhamentoTendencia = "Neutro",
    habilidades = listOf("Percepção Natural", "Graciosos", "Arma Racial", "Imunidades")
) {
    override fun podeUsarArmaEspecial(arma: String): Boolean {
        return when (arma) {
            "arco" -> true
            else -> false
        }
    }
    
    override fun podeUsarArmaduraEspecial(armadura: String): Boolean = true
    
    override fun calcularBonusJP(tipoJP: String): Int {
        return when (tipoJP) {
            "JPD" -> 1 // +1 em JPD
            else -> 0
        }
    }
    
    fun calcularBonusDanoArco(): Int = 1 // +1 de dano com arcos
    
    fun detectarPortaSecreta(procurandoAtivamente: Boolean = false): Boolean {
        // Lógica para detectar portas secretas
        return true // Implementar lógica real depois
    }
    
    companion object {
        val IMUNIDADES = listOf("Sono", "Paralisia de Ghoul")
    }
}