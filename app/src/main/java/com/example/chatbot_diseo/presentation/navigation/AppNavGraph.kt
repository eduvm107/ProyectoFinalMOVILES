package com.example.chatbot_diseo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

// IMPORTA TODAS TUS PANTALLAS
import com.example.chatbot_diseo.presentation.chat.ChatScreen
import com.example.chatbot_diseo.presentation.historial.HistorialScreen
import com.example.chatbot_diseo.presentation.calendario.PantallaCalendario
import com.example.chatbot_diseo.presentation.footer.PlaceholderScreen
import com.example.chatbot_diseo.presentation.notificaciones.NotificacionesScreen
import com.example.chatbot_diseo.presentation.favoritos.FavoritosScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = "chat",
        modifier = modifier
    ) {

        composable("chat") {
            ChatScreen(navController = navController)
        }

        composable("calendario") {
            PantallaCalendario()
        }

        composable("notificaciones") {
            NotificacionesScreen(onBack = { navController.popBackStack() })
        }

        composable("perfil") {
            PlaceholderScreen(screenName = "Perfil")
        }

        composable("historial") {
            HistorialScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("favoritos") {
            FavoritosScreen(onBack = { navController.popBackStack() })
        }

    }
}
