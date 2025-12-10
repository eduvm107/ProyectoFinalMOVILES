package com.example.chatbot_diseo.presentation.calendario.componentes

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
    val fechaActividad = try { LocalDate.parse(actividad.fechaCorta) } catch (_: DateTimeParseException) { null }

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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), // sombra muy leve
        colors = CardDefaults.cardColors(containerColor = Color.White),
        // quitar border para apariencia más limpia
    ) {
        Column(
            modifier = Modifier.padding(20.dp) // padding interno 20dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Ícono superior izquierdo dentro de cuadrado suave
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE9EEF5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = "Date Icon",
                        tint = Color(0xFF4B5563),
                        modifier = Modifier.width(20.dp).height(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = actividad.titulo,
                        fontWeight = FontWeight.SemiBold, // Inter Semibold aproximado
                        fontSize = 16.sp,
                        color = Color(0xFF1A1A1A)
                    )

                    Spacer(modifier = Modifier.height(6.dp)) // entre título y hora: 6dp

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = "Time Icon",
                            tint = Color(0xFFEF4444), // rosado/rojizo suave
                            modifier = Modifier.width(16.dp).height(16.dp) // tamaño 16dp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = actividad.horaInicio, color = Color.DarkGray, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(6.dp)) // entre hora y lugar 6-8dp

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Videocam,
                            contentDescription = "Session Icon",
                            tint = Color(0xFF1976D2), // icono azul
                            modifier = Modifier.width(16.dp).height(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = actividad.lugar, color = Color(0xFF1976D2), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }

                // Etiqueta "activo" más pequeña y alineada arriba a la derecha
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF1976D2))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = actividad.estado,
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp)) // entre lugar y línea: 10dp

            HorizontalDivider(color = Color(0xFFE5E7EB), thickness = 0.5.dp) // línea muy suave

            Spacer(modifier = Modifier.height(12.dp)) // entre línea y "Próxima actividad": 12dp

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
                    tint = Color(0xFF6C63FF),
                    modifier = Modifier.width(16.dp).height(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = etiqueta, color = Color(0xFF6C63FF), fontSize = 14.sp)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F2F5)
@Composable
fun NotificacionCardPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {


    }
}
