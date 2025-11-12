// viewmodel/CriacaoPersonagemViewModel.kt
package com.olddragon.view.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.olddragon.model.*
import kotlin.random.Random

class CriacaoPersonagemViewModel : ViewModel() {
    private var _estadoPersonagem = MutableStateFlow(Personagem())
    val estadoPersonagem: StateFlow<Personagem> = _estadoPersonagem.asStateFlow()
    
    private var _etapaAtual = MutableStateFlow(0)
    val etapaAtual: StateFlow<Int> = _etapaAtual.asStateFlow()

    private var _atributosDisponiveis = MutableStateFlow<List<Int>>(emptyList())
    val atributosDisponiveis: StateFlow<List<Int>> = _atributosDisponiveis.asStateFlow()

    fun selecionarEstiloAtributos(estilo: String, nome: String = "") {
        val atributosRolados = when (estilo) {
            "Classico" -> rolarAtributosClassico()
            "Aventureiro" -> rolarAtributosAventureiro()
            "Heroico" -> rolarAtributosHeroico()
            else -> Atributos()
        }
        
        _atributosDisponiveis.value = listOf(
            atributosRolados.FOR,
            atributosRolados.DES, 
            atributosRolados.CON,
            atributosRolados.INT,
            atributosRolados.SAB,
            atributosRolados.CAR
        )
        
        _estadoPersonagem.value = _estadoPersonagem.value.copy(
            nome = nome,
            atributos = atributosRolados
        )
        avancarEtapa()
    }

    private fun rolarAtributosClassico(): Atributos {
        val dados = List(6) { rolar3d6() }
        return Atributos(
            FOR = dados[0],
            DES = dados[1],
            CON = dados[2],
            INT = dados[3],
            SAB = dados[4],
            CAR = dados[5]
        )
    }

    private fun rolarAtributosAventureiro(): Atributos {
        val dados = List(6) { rolar3d6() }.sortedDescending()
        return Atributos().copy(
            FOR = dados[0],
            DES = dados[1],
            CON = dados[2],
            INT = dados[3],
            SAB = dados[4],
            CAR = dados[5]
        )
    }

    private fun rolarAtributosHeroico(): Atributos {
        val dados = List(6) { rolar4d6DescartarMenor() }.sortedDescending()
        return Atributos().copy(
            FOR = dados[0],
            DES = dados[1],
            CON = dados[2],
            INT = dados[3],
            SAB = dados[4],
            CAR = dados[5]
        )
    }

    private fun rolar3d6(): Int = List(3) { Random.nextInt(1, 7) }.sum()
    
    private fun rolar4d6DescartarMenor(): Int {
        val dados = List(4) { Random.nextInt(1, 7) }.sorted()
        return dados.drop(1).sum()
    }

    fun redistribuirAtributos(novoAtributos: Atributos) {
        val personagem = _estadoPersonagem.value
        val pvMax = calcularPVParaClasse(personagem.classe, novoAtributos.CON)
        _estadoPersonagem.value = personagem.copy(
            atributos = novoAtributos,
            pvMaximos = pvMax,
            pvAtuais = pvMax
        )
    }

    fun atualizarNome(nome: String) {
        _estadoPersonagem.value = _estadoPersonagem.value.copy(nome = nome)
    }
    
    fun selecionarRaca(raca: Raca) {
        _estadoPersonagem.value = _estadoPersonagem.value.copy(raca = raca)
    }
    
    fun selecionarClasse(classe: Classe) {
        val personagem = _estadoPersonagem.value
        val pvMax = calcularPVParaClasse(classe, personagem.atributos.CON)
        _estadoPersonagem.value = personagem.copy(
            classe = classe,
            pvMaximos = pvMax,
            pvAtuais = pvMax
        )
    }
    
    private fun calcularPVParaClasse(classe: Classe, con: Int): Int {
        val pvBase = when (classe) {
            is Classe.Guerreiro -> 10
            is Classe.Clerigo -> 8
            is Classe.Ladrao -> 6
        }
        val modificador = when {
            con <= 3 -> -3
            con <= 5 -> -2
            con <= 8 -> -1
            con <= 12 -> 0
            con <= 14 -> 1
            con <= 16 -> 2
            con <= 18 -> 3
            else -> 4
        }
        return maxOf(1, pvBase + modificador)
    }
    
    fun atualizarAlinhamento(alinhamento: String) {
        _estadoPersonagem.value = _estadoPersonagem.value.copy(alinhamento = alinhamento)
    }
    
    fun avancarEtapa() {
        _etapaAtual.value++
    }
    
    fun voltarEtapa() {
        _etapaAtual.value--
    }
    
    fun finalizarCriacao() {
        _etapaAtual.value = 6
    }
    
    fun reiniciarCriacao() {
        _estadoPersonagem.value = Personagem()
        _etapaAtual.value = 0
        _atributosDisponiveis.value = emptyList()
    }
}
