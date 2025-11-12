// service/HabilidadeLoader.kt
package com.olddragon.service

import android.content.Context
import com.google.gson.Gson
import com.olddragon.model.HabilidadeData

class HabilidadeLoader(private val context: Context) {
    private val gson = Gson()
    
    fun carregarHabilidadesClerigo(): List<HabilidadeData> {
        return carregarDoJson("clerigo_magias.json")
    }
    
    fun carregarHabilidadesGuerreiro(): List<HabilidadeData> {
        return carregarDoJson("guerreiro_golpes.json")
    }
    
    fun carregarHabilidadesLadrao(): List<HabilidadeData> {
        return carregarDoJson("ladrao_habilidades.json")
    }
    
    private fun carregarDoJson(nomeArquivo: String): List<HabilidadeData> {
        return try {
            val jsonString = context.assets.open(nomeArquivo).bufferedReader().use { it.readText() }
            val data = gson.fromJson(jsonString, Map::class.java)
            val habilidadesList = data["magias"] ?: data["golpes"] ?: data["habilidades"]
            gson.fromJson(gson.toJson(habilidadesList), Array<HabilidadeData>::class.java).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun getHabilidadesPorNivel(classe: String, nivelPersonagem: Int): List<HabilidadeData> {
        val todasHabilidades = when (classe) {
            "Clérigo" -> carregarHabilidadesClerigo()
            "Guerreiro" -> carregarHabilidadesGuerreiro()
            "Ladrão" -> carregarHabilidadesLadrao()
            else -> emptyList()
        }
        
        return todasHabilidades.filter { it.nivel <= nivelPersonagem }
    }
}