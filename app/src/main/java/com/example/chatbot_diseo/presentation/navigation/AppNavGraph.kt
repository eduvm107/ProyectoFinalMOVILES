package com.example.chatbot_diseo.presentation.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chatbot_diseo.data.api.TokenHolder
import com.example.chatbot_diseo.presentation.admin.page.AdminPanelScreen
import com.example.chatbot_diseo.presentation.auth.ForgotPasswordScreen
import com.example.chatbot_diseo.presentation.auth.LoginScreen
import com.example.chatbot_diseo.presentation.ayuda.AyudaScreen
import com.example.chatbot_diseo.presentation.calendario.PantallaCalendario
import com.example.chatbot_diseo.presentation.chat.ChatScreen
import com.example.chatbot_diseo.presentation.favoritos.FavoritosScreen
import com.example.chatbot_diseo.presentation.historial.HistorialScreen
import com.example.chatbot_diseo.presentation.notificaciones.NotificacionesScreen
import com.example.chatbot_diseo.presentation.recursos.Pantalla_de_Recurso
import com.example.chatbot_diseo.presentation.userperfil.PerfilScreen
import com.example.chatbot_diseo.presentation.theme.ThemeViewModel

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    themeViewModel: ThemeViewModel? = null
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        // Pantalla de Login
        composable("login") {
            LoginScreen(
                onLogin = { role ->
                    // Verificar token guardado antes de navegar
                    val token = TokenHolder.token
                    if (token.isNullOrBlank()) {
                        Toast.makeText(context, "Login inválido (sin token)", Toast.LENGTH_SHORT).show()
                        return@LoginScreen
                    }
                    Toast.makeText(context, "Bienvenido! Rol: $role", Toast.LENGTH_SHORT).show()

                    // Redirigir según el rol
                    val destination = if (role == "admin") "admin_panel" else "chat"

                    navController.navigate(destination) {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onForgotPassword = {
                    navController.navigate("forgot_password")
                }
            )
        }

        // Pantalla de Recuperar Contraseña
        composable("forgot_password") {
            ForgotPasswordScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("admin_panel") {
            AdminPanelScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("chat") {
            ChatScreen(navController = navController)
        }

        composable("calendario") {
            PantallaCalendario()
        }

        composable("recursos") {
            Pantalla_de_Recurso()
        }

        composable("perfil") {
            val isDarkTheme by themeViewModel?.isDarkTheme?.collectAsState() ?: remember { mutableStateOf(false) }
            PerfilScreen(
                onLogout = {
                    // Limpiar token e ID del usuario
                    TokenHolder.clear()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                isDarkTheme = isDarkTheme,
                onThemeToggle = { newValue ->
                    themeViewModel?.setDarkTheme(newValue)
                }
            )
        }

        composable("notificaciones") {
            NotificacionesScreen(onBack = { navController.popBackStack() })
        }

        composable("historial") {
            HistorialScreen(onBack = { navController.popBackStack() })
        }

        composable("favoritos") {
            FavoritosScreen(onBack = { navController.popBackStack() })
        }

        composable("ayuda") {
            AyudaScreen(onBack = { navController.popBackStack() })
        }
    }
}
