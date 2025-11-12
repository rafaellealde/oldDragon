// model/item/Material.kt
package com.olddragon.model.item

data class Material(
    val nome: String,
    val efeito: String,
    val beneficio: String,
    val precoMultiplicador: Double,
    val raridade: String = "Comum"
)