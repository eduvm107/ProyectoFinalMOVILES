package com.example.chatbot_diseo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.chatbot_diseo.presentation.navigation.AppNavGraph
import com.example.chatbot_diseo.presentation.userperfil.PerfilScreen
import com.example.chatbot_diseo.ui.theme.ChatBot_DiseñoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatBot_DiseñoTheme {
                   AppNavGraph()
                    // Temporalmente mostrar la pantalla de Perfil para pruebas
                    PerfilScreen(onLogout = { /* implementar logout / navegación */ })
            }
        }
    }
}
