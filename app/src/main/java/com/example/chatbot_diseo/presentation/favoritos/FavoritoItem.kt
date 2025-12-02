package com.example.chatbot_diseo.presentation.favoritos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.data.model.RecursoFavorito

@Composable
fun FavoritoItem(
    item: RecursoFavorito,
    onRemoveClick: () -> Unit
) {
    // Icono y color según el tipo
    val (icono, colorBase) = when (item.tipo) {
        "Manual" -> Pair(Icons.Default.Book, Color(0xFF1A73E8)) // Azul
        "Beneficio" -> Pair(Icons.Default.FitnessCenter, Color(0xFFE91E63)) // Rosa
        "Curso" -> Pair(Icons.Default.School, Color(0xFFF9AB00)) // Amarillo
        else -> Pair(Icons.Default.Star, Color.Gray)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. ICONO
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorBase.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, null, tint = colorBase, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 2. TEXTO
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.titulo,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF2D2D2D)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.descripcion ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.tipo,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorBase
                )
            }

            // 3. BOTÓN DESMARCAR (corazón)
            IconButton(onClick = onRemoveClick) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Desmarcar favorito",
                    tint = Color.Red
                )
            }
        }
    }
}