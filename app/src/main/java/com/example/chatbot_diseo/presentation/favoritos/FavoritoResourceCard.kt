package com.example.chatbot_diseo.presentation.favoritos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FavoritoResourceCard(
    titulo: String,
    descripcion: String,
    categoria: String,
    imageVector: ImageVector,
    iconBg: Color,
    onOpen: () -> Unit
) {

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                // ICONO DE LA CATEGORIA
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(iconBg, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = null,
                        tint = Color(0xFF0A0A0A)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(titulo, fontSize = 18.sp, color = Color.Black)
                    Text(descripcion, fontSize = 14.sp, color = Color.Gray)
                }

                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = Color(0xFF0C63E7)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = categoria,
                color = Color(0xFF0C63E7),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height  (16.dp))

            Button(
                onClick = onOpen,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C63E7)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver enlace")
            }
        }
    }
}
