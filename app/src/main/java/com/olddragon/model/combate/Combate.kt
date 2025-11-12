// model/combate/Combate.kt
package com.olddragon.model.combate

import com.olddragon.model.Personagem
import kotlin.random.Random

data class Inimigo(
    val nome: String,
    val nivel: Int,
    var pvAtuais: Int,
    val pvMaximos: Int,
    val ca: Int,
    val danoBase: String,
    val xpRecompensa: Int
) {
    fun estaVivo(): Boolean = pvAtuais > 0
}

data class ResultadoCombate(
    val vitoria: Boolean,
    val xpGanha: Int,
    val danoSofrido: Int,
    val rounds: Int
)

// Habilidades das classes
data class Habilidade(
    val nome: String,
    val tipo: String, // "dano" ou "defesa"
    val dano: String? = null,
    val cura: String? = null,
    val defesa: Int? = null
)