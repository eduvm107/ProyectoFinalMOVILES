
package com.example.chatbot_diseo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatbot_diseo.presentation.PermissionHandler
import com.example.chatbot_diseo.presentation.calendario.Calendario

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "permissions") {
        composable("permissions") {
            PermissionHandler(navController)
        }
        composable("calendario") {
            Calendario()
        }
    }
}
