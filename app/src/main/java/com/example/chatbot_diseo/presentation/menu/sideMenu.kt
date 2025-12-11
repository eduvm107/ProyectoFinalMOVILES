@file:Suppress("DEPRECATION", "UNUSED_PARAMETER", "UNUSED_VARIABLE")

package com.example.chatbot_diseo.presentation.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment

@Composable
fun SideMenu(
    onNavigate: (String) -> Unit,
    onClose: () -> Unit
) {
    // No usar onClose aquí (parámetro preservado por API) — suprimir advertencias a nivel de archivo

    // Fondo blanco y padding horizontal 20dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {

        // Header: título + subtítulo
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Menú",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Accesos rápidos",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF6B7280)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Items: usar estilo consistente, separadores sutiles y padding entre items (16dp)
        // ✅ FIX: "Inicio" ahora solo cierra el menú en lugar de recargar el chat
        DrawerItem("Inicio", Icons.Outlined.Home, "close", onNavigate)
        HorizontalDivider(color = Color(0xFFE5E7EB).copy(alpha = 0.4f))
        Spacer(modifier = Modifier.height(16.dp))

        DrawerItem("Notificaciones", Icons.Outlined.Notifications, "notificaciones", onNavigate)
        HorizontalDivider(color = Color(0xFFE5E7EB).copy(alpha = 0.4f))
        Spacer(modifier = Modifier.height(16.dp))

        // Favoritos: cambiar a icono de corazón amigable
        DrawerItem("Favoritos", Icons.Filled.Favorite, "favoritos", onNavigate)
        HorizontalDivider(color = Color(0xFFE5E7EB).copy(alpha = 0.4f))
        Spacer(modifier = Modifier.height(16.dp))

        DrawerItem("Historial", Icons.Outlined.ChatBubbleOutline, "historial", onNavigate)
        HorizontalDivider(color = Color(0xFFE5E7EB).copy(alpha = 0.4f))
        Spacer(modifier = Modifier.height(16.dp))

        DrawerItem("Configuración", Icons.Outlined.Settings, "configuracion", onNavigate)
        HorizontalDivider(color = Color(0xFFE5E7EB).copy(alpha = 0.4f))
        Spacer(modifier = Modifier.height(16.dp))

        DrawerItem("Ayuda", Icons.Outlined.HelpOutline, "ayuda", onNavigate)

        Spacer(modifier = Modifier.height(40.dp))

        DrawerItem(
            "Cerrar sesión",
            Icons.Outlined.Logout,
            "logout",
            onNavigate,
            color = Color(0xFFE53935)
        )
    }
}

@Composable
fun DrawerItem(
    label: String,
    icon: ImageVector,
    route: String,
    onNavigate: (String) -> Unit,
    color: Color = Color(0xFF1A1A1A),
    subtitle: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigate(route) }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF6B6B6B)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                color = color,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            subtitle?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    color = Color(0xFF6B7280),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
