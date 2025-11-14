package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AdminStats() {

    Column(Modifier.padding(16.dp)) {

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatCard("Recursos disponibles", "18", Icons.Default.Menu)
            Spacer(Modifier.width(12.dp))
            StatCard("Tasa de completitud", "87%", Icons.Default.CheckCircle)
        }
    }
}

@Composable
fun StatCard(titulo: String, valor: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF0072CE))
        Spacer(Modifier.height(8.dp))
        Text(titulo, color = Color(0xFF083865))
        Text(valor, color = Color(0xFF0072CE), style = MaterialTheme.typography.titleLarge)
    }
}
