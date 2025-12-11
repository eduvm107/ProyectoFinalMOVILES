package com.example.chatbot_diseo.presentation.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.chatbot_diseo.R

@Composable
fun ChatHeader(
    onNewChat: () -> Unit,
    onMenuClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF4A6B8A))
            .padding(vertical = 24.dp)
    ) {
        // Contenido alineado con padding horizontal interno
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_tata),
                        contentDescription = "Logo TCS Assistant",
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        "TCS Assistant",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp
                    )
                    Text(
                        "Siempre disponible para ti",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp
                    )
                }
            }

            // Botones a la derecha
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNewChat) {
                    Icon(
                        Icons.Outlined.Create,
                        contentDescription = "Nuevo chat",
                        tint = Color.White
                    )
                }

                IconButton(onClick = onMenuClick) {
                    Icon(
                        Icons.Outlined.Menu,
                        contentDescription = "Menú",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
