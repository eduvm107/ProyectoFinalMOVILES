package com.example.chatbot_diseo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.chatbot_diseo.presentation.footer.PantallaPrincipal
import com.example.chatbot_diseo.ui.theme.ChatBot_DiseñoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatBot_DiseñoTheme {
                // Aquí establecemos el punto de entrada correcto de la app
                PantallaPrincipal()
            }
        }
    }
}
