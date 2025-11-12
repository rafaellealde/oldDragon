// model/classe/Guerreiro.kt
package com.olddragon.model.classe

import com.olddragon.model.Classe
import com.olddragon.model.HabilidadeData
import com.olddragon.service.HabilidadeLoader

class Guerreiro : Classe(
    nome = "Guerreiro",
    dadoVida = 10,
    tabelaXP = mapOf(
        1 to 0,
        2 to 2000,
        3 to 4000,
        4 to 7000,
        5 to 10000,
        6 to 20000,
        7 to 30000,
        8 to 40000,
        9 to 50000,
        10 to 100000
    ),
    habilidades = listOf("Golpes Corporais")
) {
    override fun calcularPVBase(): Int = 10
    
    override fun podeUsarArma(arma: String): Boolean = true
    
    override fun podeUsarArmadura(armadura: String): Boolean = true
    
    fun getHabilidadesDisponiveis(nivel: Int, loader: HabilidadeLoader): List<HabilidadeData> {
        return loader.getHabilidadesPorNivel("Guerreiro", nivel)
    }
    
    companion object {
        val HABILIDADES_ESPECIAIS = listOf(
            "Ataque Extra (Nível 6)",
            "Maestria em Arma Avançada (Nível 10)"
        )
    }
}