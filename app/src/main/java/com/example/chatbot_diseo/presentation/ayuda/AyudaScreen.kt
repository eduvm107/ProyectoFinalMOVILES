package com.example.chatbot_diseo.presentation.ayuda.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AyudaTopBar(onBack: () -> Unit) {

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

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Ayuda",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0A0A0A)
        )
    }
}
