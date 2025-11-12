package com.olddragon.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.olddragon.model.Personagem
import com.olddragon.view.criacao.*
import com.olddragon.view.viewmodel.CriacaoPersonagemViewModel

@Composable
fun TelaCriacaoPersonagem(
    onBack: () -> Unit,
    onFinish: (Personagem) -> Unit,
    viewModel: CriacaoPersonagemViewModel = viewModel()
) {
    val estado by viewModel.estadoPersonagem.collectAsState()
    val etapa by viewModel.etapaAtual.collectAsState()

    LaunchedEffect(etapa) {
        if (etapa == 6) {
            onFinish(estado)
        }
    }
    
    when (etapa) {
        0 -> TelaEstiloAtributosENome(estado, viewModel, onBack)
        1 -> TelaRaca(estado, viewModel)
        2 -> TelaClasse(estado, viewModel)
        3 -> TelaAtributos(estado, viewModel)
        4 -> TelaAlinhamento(estado, viewModel)
        5 -> TelaResumo(estado, viewModel)
    }
}