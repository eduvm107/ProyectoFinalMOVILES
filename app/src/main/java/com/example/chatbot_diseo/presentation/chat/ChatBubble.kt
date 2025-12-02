package com.example.chatbot_diseo.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChatBubble(texto: String, esUsuario: Boolean) {

    Spacer(modifier = Modifier.height(10.dp))

    if (esUsuario) {
        // BURBUJA AZUL (usuario)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(22.dp))
                    .padding(14.dp)
                    .widthIn(max = 260.dp)
            ) {
                Text(texto, color = MaterialTheme.colorScheme.onPrimary)
            }
        }

    } else {
        // BURBUJA DEL BOT
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {

            Icon(
                Icons.Default.SmartToy,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(28.dp)
            )

            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(22.dp))
                    .padding(14.dp)
                    .widthIn(max = 260.dp)
            ) {
                Text(texto, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
