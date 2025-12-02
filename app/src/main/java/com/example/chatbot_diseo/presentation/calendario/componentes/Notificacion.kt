package com.example.chatbot_diseo.presentation.calendario.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatbot_diseo.data.remote.model.Actividad.ActividadUI
import com.example.chatbot_diseo.data.api.TokenHolder
import com.example.chatbot_diseo.data.repository.FavoritosRepository
import com.example.chatbot_diseo.presentation.favoritos.FavoritosViewModel
import kotlinx.coroutines.launch

@Composable
fun NotificacionCard(actividad: ActividadUI, favoritosViewModel: FavoritosViewModel? = null) {
    val coroutineScope = rememberCoroutineScope()
    var isFav by remember { mutableStateOf(actividad.isFavorite) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
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

                // Estado box
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

                Spacer(modifier = Modifier.width(8.dp))

                // Corazón (toggle favorito)
                IconButton(onClick = {
                    // Optimistic update
                    isFav = !isFav
                    val usuarioId = TokenHolder.usuarioId
                    if (favoritosViewModel != null && !usuarioId.isNullOrBlank()) {
                        favoritosViewModel.toggleFavorito("actividad", actividad.id) { res ->
                            if (!res.isSuccess) {
                                isFav = !isFav
                            }
                        }
                    } else {
                        if (!usuarioId.isNullOrBlank()) {
                            coroutineScope.launch {
                                val repo = FavoritosRepository()
                                val res = repo.toggleFavorito(usuarioId, "actividad", actividad.id)
                                if (!res.isSuccess) {
                                    isFav = !isFav
                                }
                            }
                        }
                    }
                }) {
                    Icon(
                        imageVector = if (isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFav) Color.Red else Color.Gray
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Build,
                    contentDescription = "Activity Icon",
                    tint = Color(0xFF9C27B0)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Próxima actividad", color = Color(0xFF007AFF), fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun NotificacionCardPreview() {
    Box(Modifier.padding(16.dp)) {
        NotificacionCard(
            actividad = ActividadUI(
                id = "1",
                titulo = "Setup de herramientas y accesos",
                fechaCorta = "15 NOV",
                estado = "Pendiente",
                horaInicio = "02:00 PM",
                lugar = "Sesión virtual"
            ),
            favoritosViewModel = null
        )
    }
}
