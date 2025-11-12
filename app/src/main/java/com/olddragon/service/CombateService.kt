package com.olddragon.service

import com.olddragon.model.Personagem
import com.olddragon.model.combate.*
import kotlin.random.Random
import kotlinx.coroutines.delay

class CombateService {
    
    suspend fun simularCombateAutomatico(
        personagem: Personagem, 
        onProgress: (String) -> Unit,
        onCombateTerminado: (ResultadoCombate) -> Unit
    ) {
        val inimigo = gerarInimigoAleatorio(personagem.nivel)
        var personagemPV = personagem.pvAtuais
        var inimigoPV = inimigo.pvAtuais
        var round = 0
        
        onProgress("${personagem.nome} encontrou um ${inimigo.nome}!")
        
        while (personagemPV > 0 && inimigoPV > 0) {
            round++
            delay(1000) // 1 segundo entre rounds
            
            // Turno do personagem (sempre ataca em background)
            val danoPersonagem = calcularAtaquePersonagem(personagem, inimigo)
            if (danoPersonagem > 0) {
                inimigoPV -= danoPersonagem
                onProgress("${personagem.nome} ataca! Causa $danoPersonagem de dano.")
            } else {
                onProgress("${personagem.nome} erra o ataque!")
            }
            
            if (inimigoPV <= 0) break
            
            // Turno do inimigo
            val danoInimigo = calcularAtaqueInimigo(inimigo, personagem)
            if (danoInimigo > 0) {
                personagemPV -= danoInimigo
                onProgress("${inimigo.nome} ataca! Causa $danoInimigo de dano.")
            } else {
                onProgress("${inimigo.nome} erra o ataque!")
            }
        }
        
        val vitoria = inimigoPV <= 0
        val resultado = ResultadoCombate(
            vitoria = vitoria,
            xpGanha = if (vitoria) inimigo.xpRecompensa else 0,
            danoSofrido = personagem.pvAtuais - personagemPV,
            rounds = round
        )
        
        onCombateTerminado(resultado)
    }
    
    private fun calcularAtaquePersonagem(personagem: Personagem, inimigo: Inimigo): Int {
        val rolagemAtaque = Random.nextInt(1, 21)
        val modificador = when (personagem.classe.nome) {
            "Guerreiro" -> personagem.atributos.calcularModificador(personagem.atributos.FOR)
            "Clérigo" -> personagem.atributos.calcularModificador(personagem.atributos.SAB)
            "Ladrão" -> personagem.atributos.calcularModificador(personagem.atributos.DES)
            else -> 0
        }
        
        val totalAtaque = rolagemAtaque + modificador
        
        if (totalAtaque >= inimigo.ca) {
            // Dano base da classe
            val danoBase = when (personagem.classe.nome) {
                "Guerreiro" -> Random.nextInt(1, 9) // 1d8
                "Clérigo" -> Random.nextInt(1, 7)   // 1d6  
                "Ladrão" -> Random.nextInt(1, 7)    // 1d6
                else -> Random.nextInt(1, 7)
            }
            return danoBase + modificador
        }
        
        return 0
    }
    
    private fun calcularAtaqueInimigo(inimigo: Inimigo, personagem: Personagem): Int {
        val rolagemAtaque = Random.nextInt(1, 21)
        val caPersonagem = personagem.calcularCA()
        
        if (rolagemAtaque >= caPersonagem) {
            return when (inimigo.danoBase) {
                "1d8" -> Random.nextInt(1, 9)
                "2d4" -> Random.nextInt(1, 5) + Random.nextInt(1, 5)
                else -> Random.nextInt(1, 7) // 1d6 padrão
            }
        }
        
        return 0
    }
    
    fun gerarInimigoAleatorio(nivelPersonagem: Int): Inimigo {
        val nomes = listOf("Goblin", "Orc", "Esqueleto", "Lobisomem", "Aranha Gigante")
        val nivel = maxOf(1, nivelPersonagem)
        val pvBase = 10 + (nivel * 5)
        val ca = 10 + nivel
        val xp = nivel * 25
        
        return Inimigo(
            nome = nomes.random(),
            nivel = nivel,
            pvAtuais = pvBase,
            pvMaximos = pvBase,
            ca = ca,
            danoBase = "1d6",
            xpRecompensa = xp
        )
    }
}