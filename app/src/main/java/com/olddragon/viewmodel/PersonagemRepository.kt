package com.olddragon.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.olddragon.model.Personagem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object PersonagemRepository {
    private val _personagens = mutableStateListOf<Personagem>()
    val personagens: SnapshotStateList<Personagem> = _personagens

    fun adicionarPersonagem(personagem: Personagem) {
        _personagens.add(personagem.copy(id = System.currentTimeMillis()))
    }

    fun removerPersonagem(personagem: Personagem) {
        _personagens.remove(personagem)
    }

    fun getPersonagemById(id: Long): Personagem? {
        return _personagens.find { it.id == id }
    }
}