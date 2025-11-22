package com.example.chatbot_diseo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import com.example.chatbot_diseo.presentation.footer.PantallaPrincipal

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbot_diseo.presentation.footer.PantallaPrincipal
import com.example.chatbot_diseo.presentation.theme.ThemeViewModel

import com.example.chatbot_diseo.ui.theme.ChatBot_DiseñoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            ChatBot_DiseñoTheme {
                // Punto de entrada ÚNICO y CORRECTO de la aplicación.
                PantallaPrincipal()
            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            ChatBot_DiseñoTheme(darkTheme = isDarkTheme) {
                PantallaPrincipal(themeViewModel = themeViewModel)
            }
        }
    }
}
