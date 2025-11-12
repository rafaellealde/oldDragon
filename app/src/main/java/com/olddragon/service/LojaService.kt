// service/LojaService.kt
package com.olddragon.service

import com.olddragon.model.inventario.Dinheiro
import com.olddragon.model.inventario.Inventario
import com.olddragon.model.item.*
import kotlin.random.Random

class LojaService {
    
    fun comprarItem(inventario: Inventario, item: Item, preco: Dinheiro): Boolean {
        if (inventario.dinheiro.removerDinheiro(preco.po, preco.pp, preco.pc)) {
            return inventario.adicionarItem(item)
        }
        return false
    }
    
    fun venderItem(inventario: Inventario, item: Item): Boolean {
        if (inventario.removerItem(item)) {
            val precoVenda = calcularPrecoVenda(item)
            inventario.dinheiro.adicionarDinheiro(precoVenda.po, precoVenda.pp, precoVenda.pc)
            return true
        }
        return false
    }
    
    private fun calcularPrecoVenda(item: Item): Dinheiro {
        // Preço de venda é 50% do preço de compra
        return when (item) {
            is Arma -> parsePreco(item.custo, 0.5)
            is Armadura -> parsePreco(item.custo, 0.5)
            is ItemGeral -> parsePreco(item.custo, 0.5)
            else -> Dinheiro()
        }
    }
    
    fun parsePreco(precoString: String, multiplicador: Double = 1.0): Dinheiro {
        var po = 0
        var pp = 0
        var pc = 0
        
        val partes = precoString.split(" ")
        partes.forEach { parte ->
            when {
                parte.endsWith("po") -> po = (parte.replace("po", "").trim().toInt() * multiplicador).toInt()
                parte.endsWith("pp") -> pp = (parte.replace("pp", "").trim().toInt() * multiplicador).toInt()
                parte.endsWith("pc") -> pc = (parte.replace("pc", "").trim().toInt() * multiplicador).toInt()
            }
        }
        
        return Dinheiro(po, pp, pc)
    }
    
    fun gerarLootAleatorio(nivel: Int): Item? {
        val chance = Random.nextDouble(100.0)
        
        return when {
            chance < 40 -> null // Sem loot
            chance < 70 -> gerarItemGeralAleatorio()
            chance < 90 -> gerarArmaAleatoria(nivel)
            else -> gerarArmaduraAleatoria(nivel)
        }
    }
    
    private fun gerarArmaAleatoria(nivel: Int): Arma {
        val armasBasicas = listOf(
            Arma("Adaga", "Pequena, Perfurante", "2 po", "1kg", "1d4", 1, "Pequena", listOf("Arremesso")),
            Arma("Espada Curta", "Cortante", "6 po", "1kg", "1d6", 1, "Pequena", listOf("Cortante")),
            Arma("Maça", "Impactante", "6 po", "2kg", "1d8", 2, "Média", listOf("Impactante"))
        )
        
        return armasBasicas.random()
    }
    
    private fun gerarArmaduraAleatoria(nivel: Int): Armadura {
        val armadurasBasicas = listOf(
            Armadura("Armadura de Couro", "Leve, Couro", "20 po", "5kg", "+2", 1, "Leve"),
            Armadura("Cota de Malha", "Média, Metal", "60 po", "10kg", "+4", 2, "Média")
        )
        
        return armadurasBasicas.random()
    }
    
    private fun gerarItemGeralAleatorio(): ItemGeral {
        val itens = listOf(
            ItemGeral("Poção de Cura", "Restaura 2d4+2 PV", "25 po", "0.5kg", "Consumível"),
            ItemGeral("Tocha", "Ilumina 6m", "1 po", "0.5kg", "Utilitário"),
            ItemGeral("Corda", "15m de comprimento", "1 po", "2kg", "Utilitário")
        )
        
        return itens.random()
    }
}