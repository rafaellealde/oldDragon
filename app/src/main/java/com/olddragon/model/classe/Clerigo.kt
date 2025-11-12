// model/classe/Clerigo.kt
package com.olddragon.model.classe

import com.olddragon.model.Classe
import com.olddragon.model.HabilidadeData
import com.olddragon.service.HabilidadeLoader

class Clerigo : Classe(
    nome = "Clérigo",
    dadoVida = 8,
    tabelaXP = mapOf(
        1 to 0,
        2 to 1500,
        3 to 3000,
        4 to 5500,
        5 to 8500,
        6 to 17000,
        7 to 27000,
        8 to 37000,
        9 to 47000,
        10 to 94000
    ),
    habilidades = listOf("Magias Divinas")
) {
    override fun calcularPVBase(): Int = 8
    
    override fun podeUsarArma(arma: String): Boolean {
        return when (arma) {
            "impactante" -> true
            else -> false
        }
    }
    
    override fun podeUsarArmadura(armadura: String): Boolean = true
    
    fun getHabilidadesDisponiveis(nivel: Int, loader: HabilidadeLoader): List<HabilidadeData> {
        return loader.getHabilidadesPorNivel("Clérigo", nivel)
    }
    
    companion object {
        val MAGIAS_DIVINAS = listOf(
            "Curar Ferimentos",
            "Proteção contra o Mal",
            "Bênção"
        )
    }
}