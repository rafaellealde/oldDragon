// model/raca/Humano.kt
package com.olddragon.model.raca

import com.olddragon.model.Raca

class Humano : Raca(
    nome = "Humano",
    movimentoBase = 9,
    infravisao = 0,
    alinhamentoTendencia = "Qualquer",
    habilidades = listOf("Aprendizado", "Adaptabilidade")
) {
    override fun podeUsarArmaEspecial(arma: String): Boolean = true
    
    override fun podeUsarArmaduraEspecial(armadura: String): Boolean = true
    
    override fun calcularBonusJP(tipoJP: String): Int {
        return when (tipoJP) {
            "JPD", "JPM", "JPV", "JPS", "JPC" -> 1 // Humanos escolhem uma JP para receber +1
            else -> 0
        }
    }
    
    fun calcularBonusXP(xpBase: Int): Int {
        return (xpBase * 0.10).toInt() // +10% de XP
    }
    
    companion object {
        val BONUS_ADAPTABILIDADE = "+1 em uma JP à escolha"
        val BONUS_APRENDIZADO = "+10% de XP"
    }
}