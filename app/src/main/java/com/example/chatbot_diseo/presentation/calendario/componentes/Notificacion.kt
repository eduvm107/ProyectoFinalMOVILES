package com.example.chatbot_diseo.presentation.calendario.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatbot_diseo.data.remote.model.Actividad.ActividadUI
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

@Composable
fun NotificacionCard(
    actividad: ActividadUI,
    isFromProximas: Boolean = false,
    filtro: String = "Todas" // <-- NUEVO PARÁMETRO PARA EL FILTRO
) {
    // --- LÓGICA DE FILTRADO ---
    val hoy = LocalDate.now()
    val fechaActividad = try { LocalDate.parse(actividad.fechaCorta) } catch (e: DateTimeParseException) { null }

    val mostrarTarjeta = when (filtro) {
        "Próximas" -> fechaActividad != null && fechaActividad.isEqual(hoy) // <-- LÓGICA PARA "PRÓXIMAS"
        // Aquí puedes añadir más casos para otros filtros si quieres
        else -> true // Para "Todas" o si no hay filtro, se muestra siempre
    }

    if (!mostrarTarjeta) {
        return // Si no se debe mostrar, no se dibuja nada y se acaba aquí.
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.CalendarToday,
                    contentDescription = "Date Icon",
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = actividad.fechaCorta,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF007AFF))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = actividad.estado,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = actividad.titulo,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = "Time Icon",
                    tint = Color(0xFFE91E63)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = actividad.horaInicio, color = Color.DarkGray, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Videocam,
                    contentDescription = "Session Icon",
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = actividad.lugar, color = Color(0xFF007AFF), fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)

            Spacer(modifier = Modifier.height(16.dp))

            val estadoLower = actividad.estado.lowercase()
            val etiqueta = try {
                val fecha = LocalDate.parse(actividad.fechaCorta)
                val dias = ChronoUnit.DAYS.between(hoy, fecha).toInt()

                when {
                    estadoLower.contains("complet") -> "Actividad finalizada"
                    dias < 0 -> "La actividad ya pasó"
                    dias == 0 && isFromProximas -> "Tu actividad es hoy ¡Suerte!"
                    dias == 0 -> "La actividad es hoy"
                    dias == 1 -> "La actividad inicia en 1 día"
                    else -> "La actividad inicia en $dias días"
                }
            } catch (_: DateTimeParseException) {
                if (estadoLower.contains("complet")) "Actividad finalizada" else "Próxima actividad"
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Build,
                    contentDescription = "Activity Icon",
                    tint = Color(0xFF9C27B0)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = etiqueta, color = Color(0xFF007AFF), fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F2F5)
@Composable
fun NotificacionCardPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Ejemplo de tarjeta que SÍ se mostrará con el filtro "Próximas"
        NotificacionCard(
            actividad = ActividadUI(
                id = "1",
                titulo = "Actividad de Hoy",
                fechaCorta = LocalDate.now().toString(),
                estado = "pendiente",
                horaInicio = "10:00",
                lugar = "Virtual"
            ),
            filtro = "Próximas"
        )
        // Ejemplo de tarjeta que NO se mostrará con el filtro "Próximas"
        NotificacionCard(
            actividad = ActividadUI(
                id = "2",
                titulo = "Actividad de Mañana",
                fechaCorta = LocalDate.now().plusDays(1).toString(),
                estado = "pendiente",
                horaInicio = "11:00",
                lugar = "Oficina"
            ),
            filtro = "Próximas"
        )
    }
}
