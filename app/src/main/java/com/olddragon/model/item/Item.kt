// model/item/Item.kt
package com.olddragon.model.item

sealed class Item(
    open val nome: String,
    open val descricao: String,
    open val custo: String,
    open val peso: String
)