// view/criacao/TelaAtributos.kt
package com.olddragon.view.criacao

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.olddragon.model.Personagem
import com.olddragon.model.Atributos
import com.olddragon.view.viewmodel.CriacaoPersonagemViewModel

@Composable
fun TelaAtributos(
    personagem: Personagem,
    viewModel: CriacaoPersonagemViewModel
) {
    var forValue by remember { mutableStateOf(personagem.atributos.FOR) }
    var desValue by remember { mutableStateOf(personagem.atributos.DES) }
    var conValue by remember { mutableStateOf(personagem.atributos.CON) }
    var intValue by remember { mutableStateOf(personagem.atributos.INT) }
    var sabValue by remember { mutableStateOf(personagem.atributos.SAB) }
    var carValue by remember { mutableStateOf(personagem.atributos.CAR) }
    
    val atributosDisponiveis by viewModel.atributosDisponiveis.collectAsState()
    
    val valoresAtuais = listOf(forValue, desValue, conValue, intValue, sabValue, carValue)
    val valoresOrdenadosDisponiveis = atributosDisponiveis.sortedDescending()
    val valoresOrdenadosAtuais = valoresAtuais.sortedDescending()
    
    val atributosValidos = valoresOrdenadosAtuais == valoresOrdenadosDisponiveis
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Distribua seus Atributos", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Valores disponíveis: ${atributosDisponiveis.sortedDescending().joinToString(", ")}")
        Spacer(modifier = Modifier.height(16.dp))
        
        if (!atributosValidos) {
            Text(
                "⚠️ Os valores devem corresponder aos disponíveis!",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        // Componente para cada atributo
        AtributoItem("FOR", forValue, { if (it > 3) forValue-- }, { if (it < 18) forValue++ })
        AtributoItem("DES", desValue, { if (it > 3) desValue-- }, { if (it < 18) desValue++ })
        AtributoItem("CON", conValue, { if (it > 3) conValue-- }, { if (it < 18) conValue++ })
        AtributoItem("INT", intValue, { if (it > 3) intValue-- }, { if (it < 18) intValue++ })
        AtributoItem("SAB", sabValue, { if (it > 3) sabValue-- }, { if (it < 18) sabValue++ })
        AtributoItem("CAR", carValue, { if (it > 3) carValue-- }, { if (it < 18) carValue++ })
        
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
            Button(
                onClick = { 
                    val novosAtributos = Atributos(
                        FOR = forValue,
                        DES = desValue,
                        CON = conValue,
                        INT = intValue,
                        SAB = sabValue,
                        CAR = carValue
                    )
                    viewModel.redistribuirAtributos(novosAtributos)
                    viewModel.avancarEtapa() 
                },
                enabled = atributosValidos
            ) {
                Text("Próximo")
            }
        }
    }
}

@Composable
private fun AtributoItem(
    nome: String,
    valor: Int,
    onDecrement: (Int) -> Unit,
    onIncrement: (Int) -> Unit
) {
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
            Text(nome, style = MaterialTheme.typography.bodyLarge)
            Row {
                Button(
                    onClick = { onDecrement(valor) },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("-")
                }
                Text("$valor", style = MaterialTheme.typography.headlineSmall)
                Button(
                    onClick = { onIncrement(valor) },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("+")
                }
            }
            Text("Mod: ${calcularModificador(valor)}")
        }
    }
}

private fun calcularModificador(valor: Int): Int = when {
    valor <= 3 -> -3
    valor <= 5 -> -2
    valor <= 8 -> -1
    valor <= 12 -> 0
    valor <= 14 -> 1
    valor <= 16 -> 2
    valor <= 18 -> 3
    else -> 4
}