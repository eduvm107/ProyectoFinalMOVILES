package com.example.chatbot_diseo.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.outlined.SmartToy
@Composable
fun ChatHeader(
    onNewChat: () -> Unit,
    onMenuClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFFE6EEF7), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.SmartToy,
                    contentDescription = "TCS Bot",
                    tint = Color(0xFF26415F),
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    "TCS Assistant",
                    color = Color(0xFF1A1A1A),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "Siempre disponible para ti",
                    color = Color(0xFF4B5563),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Row(
            modifier = Modifier.align(Alignment.TopEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Nuevo chat
            IconButton(onClick = onNewChat) {
                Icon(
                    Icons.Outlined.Create,
                    contentDescription = "Nuevo chat",
                    tint = Color(0xFF26415F)
                )
            }

            // Menú hamburguesa
            IconButton(onClick = onMenuClick) {
                Icon(
                    Icons.Outlined.Menu,
                    contentDescription = "Menú",
                    tint = Color(0xFF26415F)
                )
            }
        }
    }
}