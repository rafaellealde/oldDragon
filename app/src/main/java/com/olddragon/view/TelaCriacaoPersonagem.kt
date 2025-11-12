// view/TelaCriacaoPersonagem.kt
package com.olddragon.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.olddragon.model.Personagem
import com.olddragon.view.criacao.*
import com.olddragon.view.home.TelaPrincipal
import com.olddragon.view.viewmodel.CriacaoPersonagemViewModel

@Composable
fun TelaCriacaoPersonagem(
    viewModel: CriacaoPersonagemViewModel = viewModel()
) {
    val estado by viewModel.estadoPersonagem.collectAsState()
    val etapa by viewModel.etapaAtual.collectAsState()
    
    when (etapa) {
        0 -> TelaEstiloAtributosENome(estado, viewModel)
        1 -> TelaRaca(estado, viewModel)
        2 -> TelaClasse(estado, viewModel)
        3 -> TelaAtributos(estado, viewModel)
        4 -> TelaAlinhamento(estado, viewModel)
        5 -> TelaResumo(estado, viewModel)
        6 -> TelaPrincipal(estado, viewModel)
    }
}