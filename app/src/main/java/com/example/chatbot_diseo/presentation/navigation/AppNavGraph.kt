
package com.example.chatbot_diseo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
    }
}
