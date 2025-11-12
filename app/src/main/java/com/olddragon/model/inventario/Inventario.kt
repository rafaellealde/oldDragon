// model/inventario/Inventario.kt
package com.olddragon.model.inventario

import com.olddragon.model.item.Item

data class Dinheiro(
    var po: Int = 0,
    var pp: Int = 0,
    var pc: Int = 0
) {
    fun totalEmPo(): Double {
        return po + (pp * 0.1) + (pc * 0.01)
    }
    
    fun adicionarDinheiro(po: Int = 0, pp: Int = 0, pc: Int = 0) {
        this.po += po
        this.pp += pp
        this.pc += pc
        normalizar()
    }
    
    fun removerDinheiro(po: Int = 0, pp: Int = 0, pc: Int = 0): Boolean {
        val totalNecessario = po + (pp * 0.1) + (pc * 0.01)
        if (totalEmPo() >= totalNecessario) {
            this.po -= po
            this.pp -= pp
            this.pc -= pc
            normalizar()
            return true
        }
        return false
    }
    
    private fun normalizar() {
        while (pc >= 100) {
            pc -= 100
            pp += 1
        }
        while (pp >= 10) {
            pp -= 10
            po += 1
        }
    }
}

data class Inventario(
    val dinheiro: Dinheiro = Dinheiro(),
    val itens: MutableList<Item> = mutableListOf(),
    val capacidadeMaxima: Int = 100,
    var cargaAtual: Int = 0
) {
    fun adicionarItem(item: Item, peso: Int = 1): Boolean {
        if (cargaAtual + peso <= capacidadeMaxima) {
            itens.add(item)
            cargaAtual += peso
            return true
        }
        return false
    }
    
    fun removerItem(item: Item): Boolean {
        return itens.remove(item)
    }
    
    fun encontrarItemPorNome(nome: String): Item? {
        return itens.find { it.nome == nome }
    }
    
    fun getArmas(): List<Item> {
        return itens.filter { it is com.olddragon.model.item.Arma }
    }
    
    fun getArmaduras(): List<Item> {
        return itens.filter { it is com.olddragon.model.item.Armadura }
    }
    
    fun getItensGerais(): List<Item> {
        return itens.filter { it is com.olddragon.model.item.ItemGeral }
    }
}