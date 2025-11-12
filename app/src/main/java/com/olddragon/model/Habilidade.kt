package com.olddragon.model

data class HabilidadeData(
    val nome: String,
    val tipo: String,
    val dano: String?,
    val cura: String?,
    val nivel: Int,
    val descricao: String
)