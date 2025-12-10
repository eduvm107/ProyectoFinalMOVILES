package com.example.chatbot_diseo.presentation.historial

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.data.model.Conversacion
import com.example.chatbot_diseo.data.model.formatearFechaSimple

@Composable
fun HistorialItem(
    chat: Conversacion,
    onClick: (conversacionId: String?) -> Unit
) {
    val colorEstado = if (chat.activa) Color(0xFF4CAF50) else Color.Gray
    val textoEstado = if (chat.activa) "Activo" else "Cerrado"

    // Formato simple de hora (usamos la utilidad formatearFechaSimple para mayor legibilidad)
    val fechaMostrar = formatearFechaSimple(chat.fechaInicio)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick(chat.id) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(Color.LightGray.copy(0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = Color.Gray)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                // Usar el título calculado por el DTO
                Text(
                    text = chat.tituloMostrado,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text("Fecha: $fechaMostrar", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }

            // Estado
            Surface(
                color = colorEstado.copy(alpha = 0.1f),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = textoEstado,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = colorEstado
                )
            }
        }
    }
}