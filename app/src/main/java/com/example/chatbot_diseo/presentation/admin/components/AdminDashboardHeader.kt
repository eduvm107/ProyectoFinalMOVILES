package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.ui.theme.*

@Composable
fun AdminDashboardHeader(
    totalContents: Int,
    totalActivities: Int,
    totalResources: Int,
    completionRate: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        // Grid de estadísticas muy compacto - una sola fila horizontal
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            StatCard(
                icon = Icons.Filled.Group,
                title = "Mensajes",
                value = totalContents.toString(),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Filled.Today,
                title = "Actividades",
                value = totalActivities.toString(),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Filled.Book,
                title = "Recursos",
                value = totalResources.toString(),
                modifier = Modifier.weight(1f)
            )
            /*StatCard(
                icon = Icons.Filled.CheckCircle,
                title = "Completitud",
                value = "$completionRate%",
                modifier = Modifier.weight(1f)
            )*/
        }
    }
}

@Composable
private fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(TcsWhite)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono muy compacto
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TcsBlue,
                modifier = Modifier.size(16.dp)
            )

            // Valor compacto
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = TcsTextDark
            )

            // Título muy pequeño
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = TcsTextLight
            )
        }
    }
}
