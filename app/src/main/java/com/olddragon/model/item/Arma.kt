// model/item/Arma.kt
package com.olddragon.model.item

data class Arma(
    override val nome: String,
    override val descricao: String,
    override val custo: String,
    override val peso: String,
    val dano: String,
    val carga: Int,
    val tipo: String,
    val propriedades: List<String>
) : Item(nome, descricao, custo, peso)