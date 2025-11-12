package com.olddragon.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olddragon.model.Personagem
import com.olddragon.viewmodel.PersonagemRepository

 @Composable
fun TelaPersonagens(
    onSelectCharacter: (Personagem) -> Unit,
    onCreateNew: () -> Unit,
    onBack: () -> Unit
) {
    val personagens = PersonagemRepository.personagens

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Personagens", style = MaterialTheme.typography.headlineLarge)
            Button(onClick = onBack) {
                Text("Voltar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de Personagens
        if (personagens.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Nenhum personagem criado")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onCreateNew) {
                    Text("Criar Primeiro Personagem")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(personagens) { personagem ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { onSelectCharacter(personagem) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                personagem.nome, 
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text("Nível ${personagem.nivel} ${personagem.classe.nome}")
                            Text("PV: ${personagem.pvAtuais}/${personagem.pvMaximos}")
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Button(
                                onClick = { 
                                    PersonagemRepository.removerPersonagem(personagem) 
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text("Excluir")
                            }
                        }
                    }
                }
            }
        }

        // Botão Criar Novo
        Button(
            onClick = onCreateNew,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Criar Novo Personagem")
        }
    }
}