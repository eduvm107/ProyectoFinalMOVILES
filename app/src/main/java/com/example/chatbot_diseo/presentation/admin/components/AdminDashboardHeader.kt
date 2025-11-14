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
    completionRate: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Panel RRHH",
            style = MaterialTheme.typography.titleMedium,
            color = TcsTextDark
        )
        Text(
            text = "Gestión de onboarding",
            style = MaterialTheme.typography.bodySmall,
            color = TcsTextLight
        )

        Spacer(Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    icon = Icons.Filled.Group,
                    title = "Nuevos colaboradores",
                    value = totalContents.toString(), // puedes cambiar a otra métrica si quieres
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = Icons.Filled.Today,
                    title = "Actividades programadas",
                    value = totalActivities.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    icon = Icons.Filled.Book,
                    title = "Recursos disponibles",
                    value = totalResources.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = Icons.Filled.CheckCircle,
                    title = "Tasa de completitud",
                    value = "$completionRate%",
                    modifier = Modifier.weight(1f)
                )
            }
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
            .clip(RoundedCornerShape(18.dp))
            .background(TcsWhite)
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(TcsBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TcsBlue
                )
            }
            Spacer(Modifier.width(8.dp))
            Column {
                Text(title, style = MaterialTheme.typography.bodySmall, color = TcsTextLight)
                Text(value, style = MaterialTheme.typography.titleMedium, color = TcsTextDark)
            }
        }
    }
}
