package com.example.chatbot_diseo.presentation.favoritos.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack

@Composable
fun FavoritosTopBar(onBack: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 16.dp)
    ) {

        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = "Volver",
                tint = Color(0xFF0A0A0A)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Favoritos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0A0A0A)
        )

        Text(
            text = "Accede rápidamente a tus recursos y conversaciones guardadas",
            fontSize = 14.sp,
            color = Color(0xFF707070)
        )
    }
}
