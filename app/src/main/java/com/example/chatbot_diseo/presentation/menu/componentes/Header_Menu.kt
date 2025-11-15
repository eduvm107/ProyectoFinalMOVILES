package com.example.chatbot_diseo.presentation.menu.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val badgeCount: Int = 0
)

@Composable
fun QuickAccessMenu() {
    val menuItems = listOf(
        MenuItem("Notificaciones", Icons.Outlined.Notifications, 2),
        MenuItem("Historial de chat", Icons.Outlined.Chat),
        MenuItem("Supervisor", Icons.Outlined.Person),
        MenuItem("Favoritos", Icons.Outlined.FavoriteBorder)
    )

    var selectedItem by remember { mutableStateOf(menuItems.first()) }
    var isDarkMode by remember { mutableStateOf(false) }

    Surface(
        color = Color(0xFF0061AF),
        shape = RoundedCornerShape(16.dp),
        // <-- CONTROLA EL MARGEN EXTERIOR DE TODO EL COMPONENTE
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            // <-- CONTROLA EL RELLENO INTERIOR DE TODO EL COMPONENTE
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // --- Fila Superior: Título y Controles ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    // <-- TAMAÑO DEL TÍTULO PRINCIPAL
                    Text("Menú", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    // <-- TAMAÑO DEL SUBTÍTULO
                    Text("Accesos rápidos", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { isDarkMode = it },
                    thumbContent = {
                        Icon(
                            imageVector = Icons.Outlined.WbSunny,
                            contentDescription = null,
                            tint = Color(0xFFFFC107)
                        )
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF37474F),
                        checkedTrackColor = Color.Gray,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.LightGray.copy(alpha = 0.5f),
                    )
                )
                IconButton(onClick = { /* Acción de cerrar */ }) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                }
            }

            // <-- CONTROLA LA ALTURA DEL ESPACIO ENTRE EL TÍTULO Y LOS BOTONES
            Spacer(modifier = Modifier.height(16.dp))

            // --- Cuadrícula de Botones ---
            // <-- CONTROLA EL ESPACIO VERTICAL ENTRE LAS FILAS DE BOTONES
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // <-- CONTROLA EL ESPACIO HORIZONTAL ENTRE LOS BOTONES
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MenuItemButton(menuItems[0], selectedItem == menuItems[0], Modifier.weight(1f)) { selectedItem = menuItems[0] }
                    MenuItemButton(menuItems[1], selectedItem == menuItems[1], Modifier.weight(1f)) { selectedItem = menuItems[1] }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MenuItemButton(menuItems[2], selectedItem == menuItems[2], Modifier.weight(1f)) { selectedItem = menuItems[2] }
                    MenuItemButton(menuItems[3], selectedItem == menuItems[3], Modifier.weight(1f)) { selectedItem = menuItems[3] }
                }
            }
        }
    }
}

@Composable
private fun MenuItemButton(item: MenuItem, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color(0xFF004A9C) else Color.White
    val contentColor = if (isSelected) Color.White else Color.DarkGray

    Box(
        modifier = modifier
            // <-- ¡AQUÍ ESTÁ LA CLAVE! Aumentamos este número para hacerlo más corto.
            .aspectRatio(2.5f) // se cambia la altura de el BOXXxxX
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Ajustamos el tamaño del icono y el texto para que quepan bien
            Icon(item.icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(2.dp))
            Text(item.title, color = contentColor, fontSize = 12.sp, textAlign = TextAlign.Center)
        }

        // Esto es para la insignia de notificación, no afecta al tamaño del botón
        if (item.badgeCount > 0) {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp)
                    .align(Alignment.TopEnd)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF007AFF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.badgeCount.toString(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F2F5)
@Composable
fun QuickAccessMenuPreview() {
    QuickAccessMenu()
}
