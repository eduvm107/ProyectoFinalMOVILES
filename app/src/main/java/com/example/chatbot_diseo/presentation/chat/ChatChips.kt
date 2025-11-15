package com.example.chatbot_diseo.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChatChips(sugerencias: List<String>, onClick: (String) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {

        sugerencias.forEach { chip ->

            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .border(1.dp, Color(0xFF1F78FF), RoundedCornerShape(50.dp))
                    .background(Color.White, RoundedCornerShape(50.dp))
                    .clickable { onClick(chip) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(chip, color = Color(0xFF1F78FF))
            }
        }
    }
}
