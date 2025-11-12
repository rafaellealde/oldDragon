// view/criacao/TelaAlinhamento.kt
package com.olddragon.view.criacao

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.olddragon.model.Personagem
import com.olddragon.view.viewmodel.CriacaoPersonagemViewModel

@Composable
fun TelaAlinhamento(
    personagem: Personagem,
    viewModel: CriacaoPersonagemViewModel
) {
    val alinhamentos = listOf("Ordeiro", "Neutro", "Caótico")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Escolha o Alinhamento", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))
        
        alinhamentos.forEach { alinhamento ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { viewModel.atualizarAlinhamento(alinhamento) },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (personagem.alinhamento == alinhamento) 8.dp else 2.dp
                ),
                border = if (personagem.alinhamento == alinhamento) BorderStroke(2.dp, Color.Blue) else null
            ) {
                Text(
                    text = alinhamento,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { viewModel.voltarEtapa() }) {
                Text("Voltar")
            }
            Button(onClick = { viewModel.avancarEtapa() }) {
                Text("Próximo")
            }
        }
    }
}