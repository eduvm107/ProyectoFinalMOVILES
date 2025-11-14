package com.example.chatbot_diseo.presentation.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatbot_diseo.presentation.PermissionHandler
import com.example.chatbot_diseo.presentation.admin.page.AdminPanelScreen
import com.example.chatbot_diseo.presentation.auth.LoginScreen
import com.example.chatbot_diseo.presentation.calendario.Calendario
import com.example.chatbot_diseo.presentation.calendario.ChatPruebaScreen
import com.example.chatbot_diseo.presentation.calendario.PantallaCalendario
import com.example.chatbot_diseo.presentation.recursos.Pantalla_de_Recurso
import com.example.chatbot_diseo.presentation.footer.PlaceholderScreen
import com.example.chatbot_diseo.presentation.footer.PantallaPrincipal

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
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
                    // Validar credenciales preestablecidas
                    // Usuario: admin@tcs.com
                    // Contraseña: admin123
                    Toast.makeText(
                        context,
                        "Bienvenido! Rol: $role",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onForgotPassword = {
                    Toast.makeText(
                        context,
                        "Credenciales: admin@tcs.com / admin123",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }

        // Pantalla principal después del login
        composable("main") {
            PantallaPrincipal()
        }

        composable("permissions") {
            PermissionHandler(navController)
        }

        composable("calendario") {
            Calendario()
        }

        composable("admin_panel") {
            AdminPanelScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("chat") {
            ChatPruebaScreen()
        }

        composable("pantalla_calendario") {
            PantallaCalendario()
        }

        composable("recursos") {
            Pantalla_de_Recurso()
        }

        composable("perfil") {
            PlaceholderScreen(screenName = "Perfil")
        }
    }
}
