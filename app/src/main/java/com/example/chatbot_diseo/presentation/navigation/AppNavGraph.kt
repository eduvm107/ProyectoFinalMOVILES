package com.example.chatbot_diseo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chatbot_diseo.presentation.admin.page.AdminPanelScreen
import com.example.chatbot_diseo.presentation.calendario.PantallaCalendario
import com.example.chatbot_diseo.presentation.chat.ChatScreen
import com.example.chatbot_diseo.presentation.favoritos.FavoritosScreen
import com.example.chatbot_diseo.presentation.footer.PlaceholderScreen
import com.example.chatbot_diseo.presentation.historial.HistorialScreen
import com.example.chatbot_diseo.presentation.notificaciones.NotificacionesScreen
import com.example.chatbot_diseo.presentation.recursos.PantallaDeRecurso

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "chat", // La pantalla por defecto es el Chat
        modifier = modifier
    ) {
        // --- Rutas de la barra de navegación principal ---
        composable("chat") { ChatScreen(navController = navController) }
        composable("calendario") { PantallaCalendario() }
        composable("recursos") { PantallaDeRecurso() }
        composable("perfil") { PlaceholderScreen(screenName = "Perfil") }

        // --- Rutas que se acceden desde otras partes (ej. menú de accesos rápidos) ---
        composable("notificaciones") { NotificacionesScreen(onBack = { navController.popBackStack() }) }
        composable("historial") { HistorialScreen(onBack = { navController.popBackStack() }) }
        composable("favoritos") { FavoritosScreen(onBack = { navController.popBackStack() }) }

        // --- Otras rutas ---
        composable("admin_panel") { AdminPanelScreen(onBack = { navController.popBackStack() }) }
    }
}
