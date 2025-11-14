package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatbot_diseo.ui.theme.TcsBlue
import com.example.chatbot_diseo.ui.theme.TcsGraySoft
import com.example.chatbot_diseo.ui.theme.TcsGrayText

@Composable
fun AdminMetricsCard() {

    Column(Modifier.fillMaxWidth()) {

        Text(
            "Métricas de Onboarding",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TcsBlue
        )

        Spacer(Modifier.height(18.dp))

        MetricItem("Tasa de completitud", "87%", 0.87f)
        MetricItem("Satisfacción promedio", "4.5/5", 0.90f)
        MetricItem("Tiempo promedio", "14 días", 0.60f)
    }
}

@Composable
fun MetricItem(title:String, value:String, progress:Float) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = TcsGrayText)
        Text(value, color = TcsBlue)
    }

    Spacer(Modifier.height(6.dp))

    LinearProgressIndicator(
        progress = progress,
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(20.dp)),
        color = TcsBlue,
        trackColor = TcsGraySoft
    )

    Spacer(Modifier.height(18.dp))
}


