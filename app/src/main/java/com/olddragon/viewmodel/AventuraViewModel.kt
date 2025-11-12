package com.olddragon.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.olddragon.model.Personagem
import com.olddragon.service.AventuraService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AventuraViewModel(application: Application) : AndroidViewModel(application) {
    
    private val _estadoAventura = MutableStateFlow<EstadoAventura>(EstadoAventura.Inativo)
    val estadoAventura: StateFlow<EstadoAventura> = _estadoAventura
    
    private val _logAventura = MutableStateFlow<List<String>>(emptyList())
    val logAventura: StateFlow<List<String>> = _logAventura
    
    fun iniciarAventura(personagem: Personagem) {
        viewModelScope.launch {
            _estadoAventura.value = EstadoAventura.Ativa(personagem)
            adicionarLog("🎮 ${personagem.nome} iniciou uma aventura!")
            
            // Iniciar o service em background
            AventuraService.startService(getApplication(), personagem)
        }
    }
    
    fun pararAventura() {
        viewModelScope.launch {
            _estadoAventura.value = EstadoAventura.Inativo
            adicionarLog("⏹️ Aventura interrompida")
            
            AventuraService.stopService(getApplication())
        }
    }
    
    fun verificarEstadoPersonagem(personagem: Personagem): String {
        return when {
            personagem.pvAtuais <= 0 -> "💀 Morto (Recuperando...)"
            personagem.pvAtuais < personagem.pvMaximos / 2 -> "😰 Ferido"
            else -> "😊 Saudável"
        }
    }
    
    private fun adicionarLog(mensagem: String) {
        _logAventura.value = _logAventura.value + "$mensagem\n"
        
        // Manter apenas os últimos 20 logs
        if (_logAventura.value.size > 20) {
            _logAventura.value = _logAventura.value.drop(1)
        }
    }
}

sealed class EstadoAventura {
    object Inativo : EstadoAventura()
    data class Ativa(val personagem: Personagem) : EstadoAventura()
}
