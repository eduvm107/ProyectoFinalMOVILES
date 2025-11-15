package com.example.chatbot_diseo.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ChatRightDrawer(
    visible: Boolean,
    onClose: () -> Unit,
    onOptionClick: (String) -> Unit
) {
    if (!visible) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.35f))
            .clickable { onClose() },
        contentAlignment = Alignment.TopEnd
    ) {

        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .width(300.dp)
                .clickable(enabled = false) { }
                .background(Color.White),
            color = Color.White,
            shadowElevation = 16.dp
        ) {

            Column(modifier = Modifier.fillMaxSize()) {

                // --------------------
                // HEADER
                // --------------------
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column {
                        Text("Menú", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Accesos rápidos",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    IconButton(onClick = onClose) {
                        Icon(
                            Icons.Outlined.Close,
                            contentDescription = null,
                            tint = Color(0xFF1A1A1A)
                        )
                    }
                }

                Divider(color = Color(0xFFE5E7EB))

                // --------------------
                // OPCIONES
                // --------------------
                MenuOption("Inicio", Icons.Outlined.Home) { onOptionClick("inicio") }
                MenuOption("Notificaciones", Icons.Outlined.Notifications) { onOptionClick("notificaciones") }
                MenuOption("Historial de chat", Icons.Outlined.Chat) { onOptionClick("historial") }
                MenuOption("Favoritos", Icons.Outlined.StarBorder) { onOptionClick("favoritos") }
                MenuOption("Supervisor", Icons.Outlined.Person) { onOptionClick("supervisor") }
                MenuOption("Checklist", Icons.Outlined.CheckBox) { onOptionClick("checklist") }
                MenuOption("Configuración", Icons.Outlined.Settings) { onOptionClick("configuracion") }
                MenuOption("Ayuda", Icons.Outlined.HelpOutline) { onOptionClick("ayuda") }

                Spacer(modifier = Modifier.weight(1f))

                // --------------------
                // CERRAR SESIÓN
                // --------------------
                Column(
                    modifier = Modifier
                        .clickable { onOptionClick("logout") }
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Logout,
                            contentDescription = null,
                            tint = Color(0xFFD32F2F)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Cerrar sesión", color = Color(0xFFD32F2F))
                            Text("Salir de la aplicación", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MenuOption(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF374151))
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(label, fontSize = 16.sp)
            Text(
                text = obtenerDescripcion(label),
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

fun obtenerDescripcion(label: String): String {
    return when (label) {
        "Inicio" -> "Chatbot asistente"
        "Notificaciones" -> "Mensajes y recordatorios"
        "Historial de chat" -> "Conversaciones anteriores"
        "Favoritos" -> "Recursos guardados"
        "Supervisor" -> "Información del líder"
        "Checklist" -> "Tareas personales"
        "Configuración" -> "Idioma, tema, permisos"
        "Ayuda" -> "Guía y pasos del proceso"
        else -> ""
    }
}
