package com.example.chatbot_diseo.presentation.recursos.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResourceCard(
    onCardClick: () -> Unit = {},
    onLinkClick: () -> Unit = {},
) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onCardClick) // <-- TODO EL RECTÁNGULO ES UN BOTÓN
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // --- Sección Superior ---
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Icono
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFE3F2FD), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MenuBook,
                        contentDescription = "Resource Icon",
                        tint = Color(0xFF007AFF)
                    )
                }

                // Columna de textos
                Column(modifier = Modifier.weight(1f)) {
                    Text("Manual de bienvenida", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                    Text("Guía completa para nuevos colaboradores de TCS", fontSize = 14.sp, color = Color.Gray)
                }

                // Botón de Favorito
                IconButton(onClick = { isFavorite = !isFavorite }, modifier = Modifier.size(24.dp)) { // <-- EL CORAZÓN ES UN BOTÓN
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color(0xFF007AFF)
                    )
                }
            }

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            Divider()

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            // --- Sección Inferior ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.MenuBook, contentDescription = "Tag Icon", tint = Color.Gray, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Manual", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.weight(1f))

                // Botón Ver Enlace
                Button(
                    onClick = onLinkClick, // <-- EL BOTÓN AZUL
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
                ) {
                    Text("Ver enlace", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.Launch, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F2F5)
@Composable
fun ResourceCardPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        ResourceCard()
    }
}
