package com.example.chatbot_diseo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
// IMPORTA TODAS TUS PANTALLAS
import com.example.chatbot_diseo.presentation.chat.ChatScreen
import com.example.chatbot_diseo.presentation.historial.HistorialScreen
import androidx.navigation.compose.rememberNavController
import com.example.chatbot_diseo.presentation.PermissionHandler
import com.example.chatbot_diseo.presentation.admin.page.AdminPanelScreen
import com.example.chatbot_diseo.presentation.calendario.Calendario

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "admin_panel") {
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
import com.example.chatbot_diseo.presentation.calendario.ChatPruebaScreen
import com.example.chatbot_diseo.presentation.calendario.PantallaCalendario
import com.example.chatbot_diseo.presentation.recursos.Pantalla_de_Recurso
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


        composable("chat") { ChatPruebaScreen() }
        composable("calendario") { PantallaCalendario() }
        composable("recursos") { Pantalla_de_Recurso() }
        composable("perfil") { PlaceholderScreen(screenName = "Perfil") }

    }
}
