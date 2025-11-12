package com.olddragon.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.olddragon.model.Personagem
import com.olddragon.model.Raca
import com.olddragon.model.Classe
import com.olddragon.view.viewmodel.CriacaoPersonagemViewModel

@Composable
fun TelaCriacaoPersonagem(
    viewModel: CriacaoPersonagemViewModel = viewModel()
) {
    val estado by viewModel.estadoPersonagem.collectAsState()
    val etapa by viewModel.etapaAtual.collectAsState()
    
    when (etapa) {
        0 -> TelaEstiloAtributos(estado, viewModel)
        1 -> TelaNome(estado, viewModel)
        2 -> TelaRaca(estado, viewModel)
        3 -> TelaClasse(estado, viewModel)
        4 -> TelaAtributos(estado, viewModel)
        5 -> TelaAlinhamento(estado, viewModel)
        6 -> TelaResumo(estado, viewModel)
        7 -> TelaPrincipal(estado, viewModel)
    }
}

@Composable
fun TelaEstiloAtributos(
    personagem: Personagem,
    viewModel: CriacaoPersonagemViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Escolha o Estilo de Atributos", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))
        
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
                    .clickable { viewModel.selecionarEstiloAtributos(estilo) },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(estilo, style = MaterialTheme.typography.headlineSmall)
                    Text(descricao, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun TelaNome(
    personagem: Personagem,
    viewModel: CriacaoPersonagemViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Nome do Personagem", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = personagem.nome,
            onValueChange = { viewModel.atualizarNome(it) },
            label = { Text("Digite o nome") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { viewModel.avancarEtapa() },
            enabled = personagem.nome.isNotBlank()
        ) {
            Text("Próximo")
        }
    }
}

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
                elevation = CardDefaults.cardElevation(defaultElevation = if (personagem.raca == raca) 8.dp else 2.dp),
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
            Button(onClick = { viewModel.avancarEtapa() }) {
                Text("Próximo")
            }
        }
    }
}

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
        
        val classes = listOf(Classe.Guerreiro, Classe.Clerigo, Classe.Ladrao)
        
        classes.forEach { classe ->
            val pvClasse = calcularPVClasse(classe, personagem.atributos.CON)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { viewModel.selecionarClasse(classe) },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (personagem.classe == classe) 8.dp else 2.dp
                ),
                border = if (personagem.classe == classe) BorderStroke(2.dp, Color.Blue) else null
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(classe.nome, style = MaterialTheme.typography.headlineSmall)
                    Text("Dado de Vida: d${classe.dadoVida}")
                    Text("PV: $pvClasse")
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

@Composable
fun TelaAtributos(
    personagem: Personagem,
    viewModel: CriacaoPersonagemViewModel
) {
    var atributos by remember { mutableStateOf(personagem.atributos) }
    val estiloAtributos by viewModel.estiloAtributos.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Seus Atributos", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Estilo: ${estiloAtributos}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        
        val atributosList = listOf(
            "FOR" to atributos.FOR,
            "DES" to atributos.DES, 
            "CON" to atributos.CON,
            "INT" to atributos.INT,
            "SAB" to atributos.SAB,
            "CAR" to atributos.CAR
        )
        
        atributosList.forEach { (nome, valor) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("$nome: $valor", style = MaterialTheme.typography.bodyLarge)
                    Text("Mod: ${personagem.atributos.calcularModificador(valor)}", 
                         style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text("PV Atuais: ${personagem.pvAtuais}", style = MaterialTheme.typography.bodyLarge)
        
        Spacer(modifier = Modifier.weight(1f))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { viewModel.voltarEtapa() }) {
                Text("Voltar")
            }
            Button(onClick = { 
                viewModel.atualizarAtributos(atributos)
                viewModel.avancarEtapa() 
            }) {
                Text("Próximo")
            }
        }
    }
}

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
                elevation = CardDefaults.cardElevation(defaultElevation = if (personagem.alinhamento == alinhamento) 8.dp else 2.dp),
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

// Função auxiliar para calcular PV de uma classe específica
private fun calcularPVClasse(classe: Classe, constituicao: Int): Int {
    val pvBase = when (classe) {
        is Classe.Guerreiro -> 10
        is Classe.Clerigo -> 8
        is Classe.Ladrao -> 6
    }
    val modificador = when {
        constituicao <= 3 -> -3
        constituicao <= 5 -> -2
        constituicao <= 8 -> -1
        constituicao <= 12 -> 0
        constituicao <= 14 -> 1
        constituicao <= 16 -> 2
        constituicao <= 18 -> 3
        else -> 4
    }
    return maxOf(1, pvBase + modificador)
}