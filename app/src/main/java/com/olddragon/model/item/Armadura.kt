// model/item/Armadura.kt
package com.olddragon.model.item

data class Armadura(
    override val nome: String,
    override val descricao: String,
    override val custo: String,
    override val peso: String,
    val ca: String,
    val carga: Int,
    val tipo: String
) : Item(nome, descricao, custo, peso)