// view/criacao/TelaRaca.kt
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
import com.olddragon.model.Raca
import com.olddragon.view.viewmodel.CriacaoPersonagemViewModel

@Composable
fun TelaRaca(
    personagem: Personagem,
    viewModel: CriacaoPersonagemViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Escolha sua Raça", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))
        
        val racas = listOf(Raca.Humano, Raca.Elfo, Raca.Anao, Raca.Halfling)
        
        racas.forEach { raca ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { viewModel.selecionarRaca(raca) },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (personagem.raca == raca) 8.dp else 2.dp
                ),
                border = if (personagem.raca == raca) BorderStroke(2.dp, Color.Blue) else null
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(raca.nome, style = MaterialTheme.typography.headlineSmall)
                    Text("Movimento: ${raca.movimentoBase}m")
                    Text("Infravisão: ${raca.infravisao}m")
                }
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
            Button(
                onClick = { viewModel.avancarEtapa() },
                enabled = personagem.raca.nome.isNotBlank()
            ) {
                Text("Próximo")
            }
        }
    }
}