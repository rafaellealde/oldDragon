// view/criacao/TelaClasse.kt
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
import com.olddragon.model.Classe
import com.olddragon.view.viewmodel.CriacaoPersonagemViewModel

@Composable
fun TelaClasse(
    personagem: Personagem,
    viewModel: CriacaoPersonagemViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Escolha sua Classe", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))
        
        val classes = viewModel.classes
        
        classes.forEach { classe ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { viewModel.selecionarClasse(classe) },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (personagem.classe.nome == classe.nome) 8.dp else 2.dp
                ),
                border = if (personagem.classe.nome == classe.nome) BorderStroke(2.dp, Color.Blue) else null
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(classe.nome, style = MaterialTheme.typography.headlineSmall)
                    Text("Dado de Vida: d${classe.dadoVida}")
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
                enabled = personagem.classe.nome.isNotBlank()
            ) {
                Text("Próximo")
            }
        }
    }
}