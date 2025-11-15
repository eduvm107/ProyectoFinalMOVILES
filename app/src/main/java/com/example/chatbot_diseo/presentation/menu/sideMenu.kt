package com.example.chatbot_diseo.presentation.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
@Composable
fun SideMenu(
    onNavigate: (String) -> Unit,
    onClose: () -> Unit
) {
    Column(modifier = Modifier.padding(20.dp)) {

        Text(
            text = "Menú",
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF1A1A1A)
        )

        Spacer(modifier = Modifier.height(30.dp))

        DrawerItem("Inicio", Icons.Outlined.Home, "chat", onNavigate)
        DrawerItem("Notificaciones", Icons.Outlined.Notifications, "notificaciones", onNavigate)
        DrawerItem("Favoritos", Icons.Outlined.StarBorder, "favoritos", onNavigate)
        // DrawerItem("Checklist", Icons.Outlined.Checklist, "checklist", onNavigate)
        DrawerItem("Historial", Icons.Outlined.ChatBubbleOutline, "historial", onNavigate)

        DrawerItem("Configuración", Icons.Outlined.Settings, "configuracion", onNavigate)
        DrawerItem("Ayuda", Icons.Outlined.HelpOutline, "ayuda", onNavigate)

        Spacer(modifier = Modifier.height(40.dp))

        DrawerItem(
            "Cerrar sesión",
            Icons.Outlined.Logout,
            "logout",
            onNavigate,
            color = Color.Red
        )
    }
}

@Composable
fun DrawerItem(
    label: String,
    icon: ImageVector,
    route: String,
    onNavigate: (String) -> Unit,
    color: Color = Color.Black
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigate(route) }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(icon, contentDescription = null, tint = color)
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, color = color)
    }
}
