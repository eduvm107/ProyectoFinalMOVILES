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
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.chatbot_diseo.data.api.TokenHolder
import com.example.chatbot_diseo.presentation.admin.page.AdminPanelScreen
import com.example.chatbot_diseo.presentation.auth.ForgotPasswordScreen
import com.example.chatbot_diseo.presentation.auth.LoginScreen
import com.example.chatbot_diseo.presentation.ayuda.AyudaScreen
import com.example.chatbot_diseo.presentation.configuracion.ConfiguracionScreen
import com.example.chatbot_diseo.presentation.calendario.Pantalla_Calendario
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
                onBack = { navController.popBackStack() },
                onLogout = {
                    // Limpiar token e ID del usuario
                    TokenHolder.clear()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("chat") {
            ChatScreen(navController = navController)
        }

        composable("calendario") {
            Pantalla_Calendario()
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

        // Historial: pasamos usuarioId desde TokenHolder
        composable("historial") {
            val usuarioId = TokenHolder.usuarioId ?: ""
            HistorialScreen(
                usuarioId = usuarioId,
                onBack = { navController.popBackStack() },
                onOpenChat = { conversacionId ->
                    // Navegar a chat con el id de conversacion
                    navController.navigate("chat/$conversacionId") {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable("favoritos") {
            FavoritosScreen(
                onBack = { navController.popBackStack() },
                onOpenRecurso = { /* navegar a la pantalla de recursos */
                    navController.navigate("recursos") {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable("ayuda") {
            AyudaScreen(onBack = { navController.popBackStack() })
        }

        composable("configuracion") {
            ConfiguracionScreen(onBack = { navController.popBackStack() })
        }

        // Ruta para abrir chat con una conversacionId opcional
        composable(
            route = "chat/{conversacionId}",
            arguments = listOf(navArgument("conversacionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val convId = backStackEntry.arguments?.getString("conversacionId")
            // usar llamada posicional para evitar problemas con nombres de parámetros
            ChatScreen(navController, convId)
        }
    }
}
