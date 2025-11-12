// view/criacao/TelaEstiloAtributosENome.kt
package com.olddragon.view.criacao

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.olddragon.model.Personagem
import com.olddragon.view.viewmodel.CriacaoPersonagemViewModel

@Composable
fun TelaEstiloAtributosENome(
    personagem: Personagem,
    viewModel: CriacaoPersonagemViewModel
) {
    var nome by remember { mutableStateOf(personagem.nome) }
    var estiloSelecionado by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Criação de Personagem", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Nome do Personagem", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = nome,
            onValueChange = { 
                nome = it
                viewModel.atualizarNome(it)
            },
            label = { Text("Digite o nome do seu personagem") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("Escolha o Estilo de Atributos", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        
        val estilos = listOf(
            "Classico" to "3d6 em ordem fixa (Hard)",
            "Aventureiro" to "3d6 distribuídos livremente (Normal)", 
            "Heroico" to "4d6 descartando menor (Fácil)"
        )
        
        estilos.forEach { (estilo, descricao) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { estiloSelecionado = estilo },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (estiloSelecionado == estilo) 8.dp else 4.dp
                ),
                border = if (estiloSelecionado == estilo) BorderStroke(2.dp, Color.Blue) else null
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(estilo, style = MaterialTheme.typography.headlineSmall)
                    Text(descricao, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = { 
                if (estiloSelecionado.isNotBlank()) {
                    viewModel.selecionarEstiloAtributos(estiloSelecionado, nome)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = nome.isNotBlank() && estiloSelecionado.isNotBlank()
        ) {
            Text("Próximo")
        }
    }
}