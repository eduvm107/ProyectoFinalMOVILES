package com.example.chatbot_diseo.presentation.admin.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.ui.theme.*

@Composable
fun MetricsPage(viewModel: AdminPanelViewModel) {
    val completion = viewModel.getCompletionRate()
    val satisfaction = viewModel.getAverageSatisfaction()
    val timeDays = viewModel.getAverageTimeDays()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MetricCard(
            icon = Icons.Default.CheckCircle,
            label = "Tasa de Completitud",
            value = "$completion%",
            progress = completion / 100f,
            color = TcsBlue
        )

        MetricCard(
            icon = Icons.Default.Star,
            label = "Satisfacción Promedio",
            value = "$satisfaction/5",
            progress = (satisfaction / 5.0).toFloat(),
            color = TcsBlue
        )

        MetricCard(
            icon = Icons.Default.Schedule,
            label = "Tiempo Promedio",
            value = "$timeDays días",
            progress = (30 - timeDays.coerceIn(0, 30)) / 30f,
            color = TcsBlue
        )
    }
}

@Composable
private fun MetricCard(
    icon: ImageVector,
    label: String,
    value: String,
    progress: Float,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(TcsWhite)
            .padding(20.dp)
    ) {
        // Header con icono
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(TcsBlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(Modifier.width(14.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    color = TcsTextDark,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Valor grande
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(16.dp))

        // Barra de progreso
        LinearProgressIndicator(
            progress = progress.coerceIn(0f, 1f),
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
            color = color,
            trackColor = TcsGraySoft
        )
    }
}
