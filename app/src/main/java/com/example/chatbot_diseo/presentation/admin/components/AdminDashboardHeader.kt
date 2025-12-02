package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
    // Card con marco profesional alrededor de las estadísticas
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = TcsBlue.copy(alpha = 0.25f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = TcsWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        // Grid de estadísticas muy compacto - una sola fila horizontal
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
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
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = TcsGrayBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = TcsBlue.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icono compacto
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TcsBlue,
                    modifier = Modifier.size(20.dp)
                )

                // Valor destacado
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    color = TcsTextDark
                )

                // Título pequeño
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = TcsTextLight
                )
            }
        }
    }
}
