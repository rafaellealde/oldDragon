// model/classe/Ladrao.kt
package com.olddragon.model.classe

import com.olddragon.model.Classe
import com.olddragon.model.HabilidadeData
import com.olddragon.service.HabilidadeLoader

class Ladrao : Classe(
    nome = "Ladrão",
    dadoVida = 6,
    tabelaXP = mapOf(
        1 to 0,
        2 to 1000,
        3 to 2000,
        4 to 4000,
        5 to 7000,
        6 to 14000,
        7 to 24000,
        8 to 34000,
        9 to 44000,
        10 to 88000
    ),
    habilidades = listOf("Habilidades de Ladrão")
) {
    override fun calcularPVBase(): Int = 6
    
    override fun podeUsarArma(arma: String): Boolean {
        return when (arma) {
            "pequena", "media" -> true
            else -> false
        }
    }
    
    override fun podeUsarArmadura(armadura: String): Boolean {
        return when (armadura) {
            "leve" -> true
            else -> false
        }
    }
    
    fun getHabilidadesDisponiveis(nivel: Int, loader: HabilidadeLoader): List<HabilidadeData> {
        return loader.getHabilidadesPorNivel("Ladrão", nivel)
    }
    
    companion object {
        val TALENTOS = listOf(
            "Armadilha",
            "Arrombar",
            "Escalar",
            "Furtividade",
            "Punga"
        )
    }
}