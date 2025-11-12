// view/aventura/TelaAventura.kt
package com.olddragon.view.aventura

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.olddragon.model.Personagem
import com.olddragon.viewmodel.AventuraViewModel
import com.olddragon.viewmodel.EstadoAventura

 @Composable
fun TelaAventura(
    personagem: Personagem,
    onVoltar: () -> Unit,
    viewModel: AventuraViewModel = viewModel()
) {
    val estadoAventura by viewModel.estadoAventura.collectAsState()
    val logAventura by viewModel.logAventura.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Cabeçalho
        Text("Modo Aventura", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        
        // Status do Personagem
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("${personagem.nome} - Nível ${personagem.nivel}", 
                     style = MaterialTheme.typography.headlineSmall)
                Text("PV: ${personagem.pvAtuais}/${personagem.pvMaximos}")
                Text("XP: ${personagem.experiencia}/${personagem.classe.xpParaProximoNivel(personagem.nivel)}")
                Text("Estado: ${viewModel.verificarEstadoPersonagem(personagem)}")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Controles de Aventura
        when (estadoAventura) {
            is EstadoAventura.Inativo -> {
                Button(
                    onClick = { viewModel.iniciarAventura(personagem) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🎮 Iniciar Aventura")
                }
            }
            is EstadoAventura.Ativa -> {
                Button(
                    onClick = { viewModel.pararAventura() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("⏹️ Parar Aventura")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Log de Aventura
        Text("Log da Aventura:", style = MaterialTheme.typography.headlineSmall)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = logAventura.joinToString(""),
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Botão Voltar
        Button(
            onClick = onVoltar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("← Voltar")
        }
    }
}