package com.example.chatbot_diseo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chatbot_diseo.presentation.calendario.ChatPruebaScreen
import com.example.chatbot_diseo.presentation.calendario.PantallaCalendario
import com.example.chatbot_diseo.presentation.footer.PlaceholderScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier // Acepta un modificador, pero el padding ya no es su responsabilidad
) {
    NavHost(
        navController = navController,
        startDestination = "chat",
        modifier = modifier // Se aplica el modificador que le pase el contenedor (el Box)
    ) {
        composable("chat") { ChatPruebaScreen() }
        composable("calendario") { PantallaCalendario() }
        composable("recursos") { PlaceholderScreen(screenName = "Recursos") }
        composable("perfil") { PlaceholderScreen(screenName = "Perfil") }
    }
}
