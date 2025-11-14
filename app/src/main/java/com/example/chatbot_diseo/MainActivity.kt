package com.example.chatbot_diseo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.chatbot_diseo.presentation.auth.LoginScreen
import com.example.chatbot_diseo.presentation.footer.PantallaPrincipal
import com.example.chatbot_diseo.ui.theme.ChatBot_DiseñoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatBot_DiseñoTheme {
                var isLoggedIn by remember { mutableStateOf(false) }

                if (!isLoggedIn) {
                    LoginScreen(
                        onLogin = { role ->
                            // Validar credenciales preestablecidas
                            // Usuario: admin@tcs.com
                            // Contraseña: admin123
                            isLoggedIn = true
                            Toast.makeText(
                                this,
                                "Bienvenido! Rol: $role",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onForgotPassword = {
                            Toast.makeText(
                                this,
                                "Credenciales: admin@tcs.com / admin123",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                } else {
                    // Pantalla principal después del login
                    PantallaPrincipal()
                }
            }
        }
    }
}
