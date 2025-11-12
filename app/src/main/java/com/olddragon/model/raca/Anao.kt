// model/raca/Anao.kt
package com.olddragon.model.raca

import com.olddragon.model.Raca

class Anao : Raca(
    nome = "Anão",
    movimentoBase = 6,
    infravisao = 18,
    alinhamentoTendencia = "Ordeiro",
    habilidades = listOf("Mineradores", "Vigoroso", "Armas Anãs", "Inimigos Naturais")
) {
    override fun podeUsarArmaEspecial(arma: String): Boolean {
        return when (arma) {
            "anã" -> true // Armas grandes anãs são consideradas médias
            else -> false
        }
    }
    
    override fun podeUsarArmaduraEspecial(armadura: String): Boolean = true
    
    override fun calcularBonusJP(tipoJP: String): Int {
        return when (tipoJP) {
            "JPC" -> 1 // +1 em JPC
            else -> 0
        }
    }
    
    fun calcularBonusAtaqueInimigo(inimigo: String): Boolean {
        val inimigosNaturais = listOf("orc", "ogro", "hobgoblin")
        return inimigosNaturais.contains(inimigo.lowercase())
    }
    
    fun detectarAnomaliasPedra(procurandoAtivamente: Boolean = false): Boolean {
        // Lógica para detectar anomalias em pedras
        return true // Implementar lógica real depois
    }
    
    companion object {
        val INIMIGOS_NATURAIS = listOf("Orcs", "Ogro", "Hobgoblin")
    }
}