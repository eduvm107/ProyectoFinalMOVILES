package com.example.chatbot_diseo.presentation.admin.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            .padding(16.dp)
    ) {

        MetricCard(
            label = "Tasa de completitud",
            valueText = "$completion%"
        )
        Spacer(Modifier.height(12.dp))

        MetricCard(
            label = "Satisfacción promedio",
            valueText = "$satisfaction/5"
        )
        Spacer(Modifier.height(12.dp))

        MetricCard(
            label = "Tiempo promedio",
            valueText = "$timeDays días"
        )
    }
}

@Composable
private fun MetricCard(
    label: String,
    valueText: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(TcsWhite)
            .padding(16.dp)
    ) {
        Text(label, color = TcsTextDark)
        Spacer(Modifier.height(4.dp))
        Text(valueText, color = TcsBlue)
    }
}
