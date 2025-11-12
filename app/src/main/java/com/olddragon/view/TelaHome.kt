package com.olddragon.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.olddragon.model.Personagem
import com.olddragon.R

 @Composable
fun TelaHome(
    personagem: Personagem?,
    onAventura: () -> Unit,
    onPersonagens: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (personagem == null) {
            Text("Nenhum personagem selecionado")
            Button(onClick = onPersonagens) {
                Text("Selecionar Personagem")
            }
        } else {
            // Imagem do personagem
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = getCharacterImage(personagem)),
                        contentDescription = "Personagem",
                        modifier = Modifier.fillMaxSize()
                    )
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            personagem.nome, 
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            "Nível ${personagem.nivel} ${personagem.classe.nome}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Status do personagem
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Status", style = MaterialTheme.typography.headlineSmall)
                    Text("PV: ${personagem.pvAtuais}/${personagem.pvMaximos}")
                    Text("XP: ${personagem.experiencia}/${personagem.xpParaProximoNivel()}")
                    Text("CA: ${personagem.calcularCA()}")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botões de ação
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onAventura,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("AVENTURA")
                }

                Button(
                    onClick = onPersonagens,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("PERSONAGENS")
                }
            }
        }
    }
}

// Função auxiliar para obter imagem baseada na classe
private fun getCharacterImage(personagem: Personagem): Int {
    return when (personagem.classe.nome) {
        "Guerreiro" -> R.drawable.ic_launcher_foreground
        "Clérigo" -> R.drawable.ic_launcher_foreground
        "Ladrão" -> R.drawable.ic_launcher_foreground
        else -> R.drawable.ic_launcher_foreground
    }
}