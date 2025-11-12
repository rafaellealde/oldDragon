// model/raca/Halfling.kt
package com.olddragon.model.raca

import com.olddragon.model.Raca

class Halfling : Raca(
    nome = "Halfling",
    movimentoBase = 6,
    infravisao = 0,
    alinhamentoTendencia = "Neutro",
    habilidades = listOf("Furtivos", "Destemidos", "Bons de Mira", "Pequenos")
) {
    override fun podeUsarArmaEspecial(arma: String): Boolean {
        return when (arma) {
            "arremesso" -> true
            else -> false
        }
    }
    
    override fun podeUsarArmaduraEspecial(armadura: String): Boolean {
        return when (armadura) {
            "couro" -> true
            else -> false
        }
    }
    
    override fun calcularBonusJP(tipoJP: String): Int {
        return when (tipoJP) {
            "JPS" -> 1 // +1 em JPS
            else -> 0
        }
    }
    
    fun calcularBonusFurtividade(): Int = 1 // +1 em furtividade
    
    fun ataqueFacilArremesso(): Boolean = true
    
    fun ataqueDificilContraCriaturaGrande(): Boolean = true
    
    companion object {
        val RESTRICOES_ARMAS = listOf("grande")
        val RESTRICOES_ARMADURAS = listOf("media", "pesada")
    }
}