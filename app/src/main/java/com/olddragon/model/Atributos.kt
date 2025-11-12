package com.olddragon.model

import java.io.Serializable

data class Atributos(
    val FOR: Int = 0,
    val DES: Int = 0,
    val CON: Int = 0,
    val INT: Int = 0,
    val SAB: Int = 0,
    val CAR: Int = 0
) : Serializable {
    fun calcularModificador(valor: Int): Int = when {
        valor <= 3 -> -3
        valor <= 5 -> -2
        valor <= 8 -> -1
        valor <= 12 -> 0
        valor <= 14 -> 1
        valor <= 16 -> 2
        valor <= 18 -> 3
        else -> 4
    }
}