package com.example.chatbot_diseo.presentation.menu.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NotificacionMenu() {
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFD0E0FF)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Icono de la izquierda ---
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFFEAF2FF), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Celebration,
                    contentDescription = "Celebration Icon",
                    tint = Color(0xFF007AFF),
                    modifier = Modifier.size(30.dp)
                )
            }

            // --- Contenido de la derecha ---
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Mensaje de bienvenida
                Text(
                    text = "¡Hoy es el gran día! Bienvenido a tu CHAMBA E☀️",
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )

                // Fila de etiquetas (fecha y "Nuevo")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Etiqueta de Fecha
                    Tag(icon = Icons.Default.CalendarToday, text = "5 Nov 2025", isFilled = false)
                    // Etiqueta de "Nuevo"
                    Tag(icon = Icons.Default.AutoAwesome, text = "Nuevo", isFilled = true)
                }
            }
        }
    }
}

@Composable
private fun Tag(
    icon: ImageVector,
    text: String,
    isFilled: Boolean
) {
    val backgroundColor = if (isFilled) Color(0xFF007AFF) else Color.Transparent
    val contentColor = if (isFilled) Color.White else Color.Gray
    val border = if (isFilled) null else BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))

    Surface(
        shape = RoundedCornerShape(50),
        color = backgroundColor,
        border = border
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                color = contentColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F2F5)
@Composable
fun NotificacionMenuPreview() {
    NotificacionMenu()
}
