// model/Raca.kt
package com.olddragon.model

abstract class Raca(
    val nome: String,
    val movimentoBase: Int,
    val infravisao: Int,
    val alinhamentoTendencia: String,
    val habilidades: List<String>
) {
    abstract fun podeUsarArmaEspecial(arma: String): Boolean
    abstract fun podeUsarArmaduraEspecial(armadura: String): Boolean
    abstract fun calcularBonusJP(tipoJP: String): Int
    
    fun temInfravisao(): Boolean = infravisao > 0
    
    companion object {
        val TODAS: List<Raca> by lazy {
            listOf(
                com.olddragon.model.raca.Humano(),
                com.olddragon.model.raca.Elfo(),
                com.olddragon.model.raca.Anao(),
                com.olddragon.model.raca.Halfling()
            )
        }
        
        fun porNome(nome: String): Raca? {
            return TODAS.find { it.nome == nome }
        }
    }
}