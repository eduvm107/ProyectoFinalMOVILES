package com.example.chatbot_diseo.presentation.footer

import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
    val route: String, // Add route for navigation
    val icon: ImageVector,
    val hasNews: Boolean
)

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavigationItem(
            title = "Chat",
            route = "chat",
            icon = Icons.AutoMirrored.Filled.Chat,
            hasNews = true
        ),
        BottomNavigationItem(
            title = "Actividades",
            route = "calendario",
            icon = Icons.Filled.CalendarToday,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "Recursos",
            route = "recursos",
            icon = Icons.AutoMirrored.Filled.MenuBook,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "Perfil",
            route = "perfil",
            icon = Icons.Filled.Person,
            hasNews = false
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val context = LocalContext.current

    // Log cuando cambia la ruta actual o se compone por primera vez
    LaunchedEffect(currentDestination?.route) {
        Log.d("BottomNavBar", "compose - currentRoute=${currentDestination?.route}")
        Toast.makeText(context, "BottomNavBar composed (route=${currentDestination?.route})", Toast.LENGTH_SHORT).show()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Divider para separar visualmente la barra del contenido
        HorizontalDivider(color = Color.Black.copy(alpha = 0.08f), thickness = 1.dp)

        NavigationBar(
            modifier = Modifier.navigationBarsPadding(),
            containerColor = Color(0xFF000080) // <-- CORREGIDO: Dejamos solo uno
        ) {
            items.forEach { item ->
                val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
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
                        BadgedBox(
                            badge = {
                                if (item.hasNews && !selected) { // Show badge only if not selected
                                    Badge()
                                }
                            }
                        ) {
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
