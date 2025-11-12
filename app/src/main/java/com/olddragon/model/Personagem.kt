package com.olddragon.model

import com.olddragon.model.classe.Guerreiro
import com.olddragon.model.classe.Clerigo
import com.olddragon.model.classe.Ladrao
import com.olddragon.model.raca.Humano
import com.olddragon.model.raca.Elfo
import com.olddragon.model.raca.Anao
import com.olddragon.model.raca.Halfling

data class Personagem(
    val id: Long = 0,
    val nome: String = "",
    val nivel: Int = 1,
    val raca: Raca = Humano(),
    val classe: Classe = Guerreiro(),
    val alinhamento: String = "Neutro",
    val atributos: Atributos = Atributos(),
    val experiencia: Int = 0,
    val pvAtuais: Int = 0,
    val pvMaximos: Int = 0
) {
    fun calcularPV(): Int {
        val pvBase = classe.calcularPVBase()
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
    
    fun xpParaProximoNivel(): Int {
        return classe.xpParaProximoNivel(nivel)
    }
    
    fun podeSubirDeNivel(): Boolean {
        return experiencia >= xpParaProximoNivel()
    }
}