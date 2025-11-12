// model/item/ItemGeral.kt
package com.olddragon.model.item

data class ItemGeral(
    override val nome: String,
    override val descricao: String,
    override val custo: String,
    override val peso: String,
    val categoria: String
) : Item(nome, descricao, custo, peso)