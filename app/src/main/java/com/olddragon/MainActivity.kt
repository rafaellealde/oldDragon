package com.olddragon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.olddragon.model.Personagem
import com.olddragon.view.*
import com.olddragon.view.aventura.TelaAventura

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OldDragonTheme {
                OldDragonApp()
            }
        }
    }
}

 @Composable
fun OldDragonApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Inicio) }
    var selectedCharacter by remember { mutableStateOf<Personagem?>(null) }

    when (val screen = currentScreen) {
        is Screen.Inicio -> TelaInicio(
            onPlay = { currentScreen = Screen.Personagens }
        )
        is Screen.Personagens -> TelaPersonagens(
            onSelectCharacter = { personagem ->
                selectedCharacter = personagem
                currentScreen = Screen.Home
            },
            onCreateNew = { currentScreen = Screen.CriacaoPersonagem },
            onBack = { currentScreen = Screen.Inicio }
        )
        is Screen.CriacaoPersonagem -> TelaCriacaoPersonagem(
            onBack = { currentScreen = Screen.Personagens },
            onFinish = { personagem ->
                selectedCharacter = personagem
                currentScreen = Screen.Home
            }
        )
        is Screen.Home -> TelaHome(
            personagem = selectedCharacter,
            onAventura = { currentScreen = Screen.Aventura },
            onPersonagens = { currentScreen = Screen.Personagens }
        )
        is Screen.Aventura -> TelaAventura(
            personagem = selectedCharacter!!,
            onVoltar = { currentScreen = Screen.Home }
        )
    }
}

sealed class Screen {
    object Inicio : Screen()
    object Personagens : Screen()
    object CriacaoPersonagem : Screen()
    object Home : Screen()
    object Aventura : Screen()
}