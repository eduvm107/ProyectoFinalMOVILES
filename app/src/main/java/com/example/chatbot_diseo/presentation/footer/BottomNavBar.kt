package com.example.chatbot_diseo.presentation.footer

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.chatbot_diseo.ui.theme.TcsBlue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.unit.dp

data class BottomNavigationItem(
    val title: String,
    val route: String,
    val icon: ImageVector,
    val hasNews: Boolean = false
)

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavigationItem("Chat", "chat", Icons.AutoMirrored.Filled.Chat, hasNews = true),
        BottomNavigationItem("Actividades", "calendario", Icons.Filled.CalendarToday),
        BottomNavigationItem("Recursos", "recursos", Icons.AutoMirrored.Filled.MenuBook),
        BottomNavigationItem("Perfil", "perfil", Icons.Filled.Person)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(color = Color.Black.copy(alpha = 0.08f), thickness = 1.dp)

        NavigationBar(
            modifier = Modifier.navigationBarsPadding(),
            containerColor = TcsBlue
        ) {
            items.forEach { item ->
                val selected = currentDestination?.hierarchy?.any { dest -> dest.route == item.route } == true

                NavigationBarItem(
                    selected = selected,
                    // Siempre navegar a la ruta al hacer click (evita clicks sin efecto)
                    onClick = {
                        Log.d("BottomNavBar", "Clicked bottom item: ${item.title}, route=${item.route}, selected=$selected")

                        // ✅ FIX: Si ya estamos en la ruta seleccionada, NO navegar (evita reiniciar el chat)
                        if (selected) {
                            Log.d("BottomNavBar", "Ya estamos en ${item.route}, no navegar")
                            return@NavigationBarItem
                        }

                        if (item.route == "chat") {
                            // Navegar a chat sin parámetros (nueva conversación o mantener actual)
                            navController.navigate("chat") {
                                popUpTo("chat") { inclusive = false }
                                launchSingleTop = true
                                restoreState = true
                            }
                        } else {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    label = {
                        Text(
                            text = item.title,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            color = if (selected) Color.White else Color.White.copy(alpha = 0.9f)
                        )
                    },
                    icon = {
                        BadgedBox(badge = {
                            if (item.hasNews && !selected) { Badge() }
                        }) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = if (selected) Color.White else Color.White.copy(alpha = 0.9f)
                            )
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.White.copy(alpha = 0.12f),
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.White.copy(alpha = 0.9f),
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.White.copy(alpha = 0.9f)
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    val navController = rememberNavController()
    BottomNavBar(navController = navController)
}
