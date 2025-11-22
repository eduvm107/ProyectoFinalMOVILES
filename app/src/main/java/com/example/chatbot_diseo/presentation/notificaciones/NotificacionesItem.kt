package com.example.chatbot_diseo.presentation.notificaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // Importante
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatbot_diseo.data.model.MensajeAutomatico
import java.util.Locale

@Composable
fun NotificacionItem(
    noti: MensajeAutomatico,
    onClick: () -> Unit // <--- Recibimos la acción de clic
) {
    val tipoSeguro = noti.tipo ?: "info"
    val tituloSeguro = noti.titulo ?: "Sin título"
    val contenidoSeguro = noti.contenido ?: "..."
    val horaSegura = noti.horaEnvio ?: "--:--"

    val (icono, colorBase) = obtenerEstiloPorTipo(tipoSeguro)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }, // <--- ¡AQUÍ HACEMOS LA MAGIA!
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        // ... (El resto del contenido de la tarjeta sigue IGUAL que antes)
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Icono
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorBase.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorBase,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Texto
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tituloSeguro,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    ),
                    color = Color(0xFF2D2D2D),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = contenidoSeguro,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF5F6368),
                    fontSize = 13.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = horaSegura, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text(text = " • ", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text(
                        text = tipoSeguro.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = colorBase
                    )
                }
            }
        }
    }
}

// (Mantén tus funciones obtenerEstiloPorTipo aquí abajo...)
fun obtenerEstiloPorTipo(tipo: String): Pair<ImageVector, Color> {
    return when (tipo.lowercase(Locale.ROOT)) {
        "bienvenida" -> Pair(Icons.Default.WavingHand, Color(0xFFF9AB00))
        "recordatorio", "alerta" -> Pair(Icons.Default.CalendarMonth, Color(0xFFD93025))
        "informativo", "noticia" -> Pair(Icons.Default.Info, Color(0xFF1A73E8))
        "motivacional" -> Pair(Icons.Default.Favorite, Color(0xFFAB47BC))
        else -> Pair(Icons.Default.Notifications, Color(0xFF5F6368))
    }
}