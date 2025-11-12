// view/home/TelaPrincipal.kt
package com.olddragon.view.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olddragon.model.Personagem
import com.olddragon.view.viewmodel.CriacaoPersonagemViewModel

@Composable
fun TelaPrincipal(
    personagem: Personagem,
    viewModel: CriacaoPersonagemViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Old Dragon", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))
        
        // Aqui vai a imagem do personagem no futuro
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Imagem do ${personagem.nome}", style = MaterialTheme.typography.bodyLarge)
                Text("Nível ${personagem.nivel} ${personagem.classe.nome}")
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        val opcoes = listOf("Personagem", "Inventário", "Loja", "Aventura")
        
        opcoes.forEach { opcao ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { /* Navegar para tela específica */ },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(opcao, style = MaterialTheme.typography.headlineSmall)
                    Text("→", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(onClick = { viewModel.reiniciarCriacao() }) {
            Text("Novo Personagem")
        }
    }
}