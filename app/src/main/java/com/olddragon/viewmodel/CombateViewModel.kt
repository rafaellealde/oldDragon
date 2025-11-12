// viewmodel/CombateViewModel.kt
package com.olddragon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olddragon.model.Personagem
import com.olddragon.service.AventuraService
import com.olddragon.service.CombateService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CombateViewModel : ViewModel() {
    private val combateService = CombateService()
    
    private val _estadoCombate = MutableStateFlow<EstadoCombate>(EstadoCombate.Inativo)
    val estadoCombate: StateFlow<EstadoCombate> = _estadoCombate
    
    private val _logCombate = MutableStateFlow<List<String>>(emptyList())
    val logCombate: StateFlow<List<String>> = _logCombate
    
    fun iniciarAventura(personagem: Personagem) {
        viewModelScope.launch {
            _estadoCombate.value = EstadoCombate.EmAventura(personagem.copy())
            adicionarLog("${personagem.nome} iniciou uma aventura!")
            
            // Em um app real, você iniciaria o Service aqui
            // val intent = Intent(context, AventuraService::class.java).apply {
            //     putExtra("personagem", personagem)
            // }
            // context.startService(intent)
        }
    }
    
    fun pararAventura() {
        viewModelScope.launch {
            _estadoCombate.value = EstadoCombate.Inativo
            adicionarLog("Aventura interrompida.")
            
            // Em um app real, você pararia o Service aqui
            // context.stopService(Intent(context, AventuraService::class.java))
        }
    }
    
    fun simularCombateManual(personagem: Personagem) {
        viewModelScope.launch {
            val personagemAtual = personagem.copy() // Work with a mutable copy
            _estadoCombate.value = EstadoCombate.EmCombate(personagemAtual)
            
            val inimigo = combateService.gerarInimigoAleatorio(personagemAtual.nivel)
            adicionarLog("${personagemAtual.nome} encontrou um ${inimigo.nome}!")
            
            val resultado = combateService.simularCombate(personagemAtual, inimigo)
            
            if (resultado.vitoria) {
                adicionarLog("${personagemAtual.nome} venceu! Ganhou ${resultado.xpGanha} XP")
                personagemAtual.experiencia += resultado.xpGanha
                personagemAtual.pvAtuais -= resultado.danoSofrido
                
                if (personagemAtual.pvAtuais <= 0) {
                    adicionarLog("${personagemAtual.nome} morreu após a vitória!")
                    _estadoCombate.value = EstadoCombate.Morto(personagemAtual)
                } else {
                    _estadoCombate.value = EstadoCombate.EmAventura(personagemAtual)
                }
            } else {
                adicionarLog("${personagemAtual.nome} foi derrotado!")
                personagemAtual.pvAtuais = 0
                _estadoCombate.value = EstadoCombate.Morto(personagemAtual)
            }
        }
    }
    
    private fun adicionarLog(mensagem: String) {
        _logCombate.value = _logCombate.value + mensagem
    }
}

sealed class EstadoCombate {
    object Inativo : EstadoCombate()
    data class EmAventura(val personagem: Personagem) : EstadoCombate()
    data class EmCombate(val personagem: Personagem) : EstadoCombate()
    data class Morto(val personagem: Personagem) : EstadoCombate()
}