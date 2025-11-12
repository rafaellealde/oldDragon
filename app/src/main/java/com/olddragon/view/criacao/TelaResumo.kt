// view/criacao/TelaResumo.kt
package com.olddragon.view.criacao

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olddragon.model.Personagem
import com.olddragon.view.viewmodel.CriacaoPersonagemViewModel

@Composable
fun TelaResumo(
    personagem: Personagem,
    viewModel: CriacaoPersonagemViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Resumo do Personagem", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Nome: ${personagem.nome}", style = MaterialTheme.typography.bodyLarge)
                Text("Raça: ${personagem.raca.nome}")
                Text("Classe: ${personagem.classe.nome}")
                Text("Nível: ${personagem.nivel}")
                Text("Alinhamento: ${personagem.alinhamento}")
                Text("PV: ${personagem.pvAtuais}/${personagem.pvMaximos}")
                Text("CA: ${personagem.calcularCA()}")
                Text("BAC: ${personagem.calcularBAC()}")
                Text("BAD: ${personagem.calcularBAD()}")
                
                Spacer(modifier = Modifier.height(16.dp))
                Text("Atributos:", style = MaterialTheme.typography.bodyLarge)
                Text("FOR: ${personagem.atributos.FOR} (Mod: ${personagem.atributos.calcularModificador(personagem.atributos.FOR)})")
                Text("DES: ${personagem.atributos.DES} (Mod: ${personagem.atributos.calcularModificador(personagem.atributos.DES)})")
                Text("CON: ${personagem.atributos.CON} (Mod: ${personagem.atributos.calcularModificador(personagem.atributos.CON)})")
                Text("INT: ${personagem.atributos.INT} (Mod: ${personagem.atributos.calcularModificador(personagem.atributos.INT)})")
                Text("SAB: ${personagem.atributos.SAB} (Mod: ${personagem.atributos.calcularModificador(personagem.atributos.SAB)})")
                Text("CAR: ${personagem.atributos.CAR} (Mod: ${personagem.atributos.calcularModificador(personagem.atributos.CAR)})")
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
            Button(onClick = { viewModel.finalizarCriacao() }) {
                Text("Ir para Home")
            }
        }
    }
}