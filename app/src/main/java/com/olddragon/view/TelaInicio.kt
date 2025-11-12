package com.olddragon.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.olddragon.R

 @Composable
fun TelaInicio(onPlay: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Imagem de fundo old school (você precisará adicionar a imagem em res/drawable)
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Old Dragon Background",
            modifier = Modifier.fillMaxSize()
        )
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "OLD DRAGON",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(50.dp))
            
            Button(
                onClick = onPlay,
                modifier = Modifier.width(200.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text("JOGAR", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}