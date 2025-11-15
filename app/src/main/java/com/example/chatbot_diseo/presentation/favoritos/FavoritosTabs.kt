package com.example.chatbot_diseo.presentation.favoritos.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip

@Composable
fun FavoritosTabs(
    selected: Int,
    onSelect: (Int) -> Unit
) {
    val tabs = listOf("Todos", "Manuales", "Cursos", "Beneficios")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        tabs.forEachIndexed { index, label ->
            val isSelected = selected == index

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(40.dp))
                    .background(if (isSelected) Color(0xFF0C63E7) else Color(0xFFE7ECF3))
                    .clickable { onSelect(index) }
                    .padding(horizontal = 22.dp, vertical = 10.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = if (isSelected) Color.White else Color(0xFF0A0A0A)
                )
            }
        }
    }
}
