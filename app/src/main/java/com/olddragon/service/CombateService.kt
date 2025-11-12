// service/CombateService.kt
package com.olddragon.service

import com.olddragon.model.Personagem
import com.olddragon.model.combate.*
import kotlin.random.Random

class CombateService {
    
    // Habilidades por classe
    private val habilidadesGuerreiro = listOf(
        Habilidade("Golpe Poderoso", "dano", "2d6 + FOR"),
        Habilidade("Postura Defensiva", "defesa", defesa = 2)
    )
    
    private val habilidadesClerigo = listOf(
        Habilidade("Chama Sagrada", "dano", "2d6 + SAB"),
        Habilidade("Proteção Divina", "defesa", cura = "1d4 + SAB", defesa = 1)
    )
    
    private val habilidadesLadrao = listOf(
        Habilidade("Ataque Furtivo", "dano", "2d6 + DES"),
        Habilidade("Esquiva", "defesa", defesa = 3)
    )
    
    fun simularCombate(personagem: Personagem, inimigo: Inimigo): ResultadoCombate {
        var personagemPV = personagem.pvAtuais
        var inimigoPV = inimigo.pvAtuais
        var rounds = 0
        
        while (personagemPV > 0 && inimigoPV > 0) {
            rounds++
            
            // Turno do personagem
            val danoPersonagem = calcularAtaquePersonagem(personagem, inimigo)
            if (danoPersonagem > 0) {
                inimigoPV -= danoPersonagem
            }
            
            if (inimigoPV <= 0) break
            
            // Turno do inimigo
            val danoInimigo = calcularAtaqueInimigo(inimigo, personagem)
            if (danoInimigo > 0) {
                personagemPV -= danoInimigo
            }
        }
        
        val vitoria = inimigoPV <= 0
        val danoSofrido = personagem.pvAtuais - personagemPV
        
        return ResultadoCombate(
            vitoria = vitoria,
            xpGanha = if (vitoria) inimigo.xpRecompensa else 0,
            danoSofrido = danoSofrido,
            rounds = rounds
        )
    }
    
    private fun calcularAtaquePersonagem(personagem: Personagem, inimigo: Inimigo): Int {
        // Escolher habilidade (50% chance de cada)
        val habilidades = when (personagem.classe.nome) {
            "Guerreiro" -> habilidadesGuerreiro
            "Clérigo" -> habilidadesClerigo
            "Ladrão" -> habilidadesLadrao
            else -> habilidadesGuerreiro
        }
        
        val habilidade = if (Random.nextBoolean()) habilidades[0] else habilidades[1]
        
        return when (habilidade.tipo) {
            "dano" -> {
                val acerto = rolarD20() + getModificadorAtaque(personagem)
                if (acerto > inimigo.ca) {
                    calcularDano(habilidade.dano!!, personagem)
                } else {
                    0
                }
            }
            "defesa" -> {
                // Habilidades defensivas não causam dano
                0
            }
            else -> 0
        }
    }
    
    private fun calcularAtaqueInimigo(inimigo: Inimigo, personagem: Personagem): Int {
        val acerto = rolarD20() + inimigo.nivel
        val caPersonagem = personagem.calcularCA()
        
        if (acerto > caPersonagem) {
            return calcularDanoInimigo(inimigo.danoBase)
        }
        return 0
    }
    
    private fun getModificadorAtaque(personagem: Personagem): Int {
        return when (personagem.classe.nome) {
            "Guerreiro" -> personagem.atributos.calcularModificador(personagem.atributos.FOR)
            "Clérigo" -> personagem.atributos.calcularModificador(personagem.atributos.SAB)
            "Ladrão" -> personagem.atributos.calcularModificador(personagem.atributos.DES)
            else -> 0
        }
    }
    
    private fun calcularDano(danoString: String, personagem: Personagem): Int {
        return when {
            danoString.contains("2d6") -> rolarDados(2, 6) + getModificadorAtaque(personagem)
            danoString.contains("1d8") -> rolarDados(1, 8) + getModificadorAtaque(personagem)
            danoString.contains("1d6") -> rolarDados(1, 6) + getModificadorAtaque(personagem)
            else -> rolarDados(1, 6) + getModificadorAtaque(personagem)
        }
    }
    
    private fun calcularDanoInimigo(danoString: String): Int {
        return when {
            danoString.contains("1d8") -> rolarDados(1, 8)
            danoString.contains("1d6") -> rolarDados(1, 6)
            danoString.contains("2d4") -> rolarDados(2, 4)
            else -> rolarDados(1, 4)
        }
    }
    
    private fun rolarD20(): Int = Random.nextInt(1, 21)
    private fun rolarDados(quantidade: Int, faces: Int): Int = 
        List(quantidade) { Random.nextInt(1, faces + 1) }.sum()
    
    fun gerarInimigoAleatorio(nivelPersonagem: Int): Inimigo {
        val nomes = listOf("Goblin", "Orc", "Esqueleto", "Lobisomem", "Aranha Gigante")
        val nivel = maxOf(1, nivelPersonagem + Random.nextInt(-1, 2))
        val pvBase = 10 + (nivel * 5)
        val ca = 10 + nivel
        val danos = listOf("1d6", "1d8", "2d4")
        val xp = nivel * 25
        
        return Inimigo(
            nome = nomes.random(),
            nivel = nivel,
            pvAtuais = pvBase,
            pvMaximos = pvBase,
            ca = ca,
            danoBase = danos.random(),
            xpRecompensa = xp
        )
    }
}