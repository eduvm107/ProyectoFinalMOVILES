package com.example.chatbot_diseo.presentation.calendario.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.PaddingValues

data class FilterOption(
    val text: String,
    val icon: ImageVector? = null
)

@Composable
fun HeaderCalendario(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    // Orden: Proximas, Pendientes, Completadas, Todas (último)
    val filters = listOf(
        FilterOption("Proximas", Icons.AutoMirrored.Filled.ArrowForward),
        FilterOption("Pendientes", Icons.Default.Schedule),
        FilterOption("Completadas", Icons.Default.Check),
        FilterOption("Todas")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF4A6B8A))
            .padding(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Calendar Icon",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Calendario de Onboarding",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tu programa personalizado de actividades",
                        fontSize = 13.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            // reducir espacio entre subtitulo y filtros para 'subirlos'
            Spacer(modifier = Modifier.height(8.dp))

            val firstRowFilters = filters.take(2)
            val secondRowFilters = filters.drop(2)

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    firstRowFilters.forEach { filter ->
                        val isSelected = selectedFilter == filter.text
                        if (isSelected) {
                            Button(
                                onClick = { onFilterSelected(filter.text) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F5FB8)),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                filter.icon?.let {
                                    Icon(
                                        imageVector = it,
                                        contentDescription = filter.text,
                                        modifier = Modifier.size(18.dp),
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(filter.text, color = Color.White)
                            }
                        } else {
                            OutlinedButton(
                                onClick = { onFilterSelected(filter.text) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.24f)),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                filter.icon?.let {
                                    Icon(
                                        imageVector = it,
                                        contentDescription = filter.text,
                                        modifier = Modifier.size(18.dp),
                                        tint = Color.White.copy(alpha = 0.95f)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(filter.text, color = Color.White.copy(alpha = 0.95f))
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    secondRowFilters.forEach { filter ->
                        val isSelected = selectedFilter == filter.text
                        if (isSelected) {
                            Button(
                                onClick = { onFilterSelected(filter.text) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F5FB8)),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                filter.icon?.let {
                                    Icon(
                                        imageVector = it,
                                        contentDescription = filter.text,
                                        modifier = Modifier.size(18.dp),
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(filter.text, color = Color.White)
                            }
                        } else {
                            OutlinedButton(
                                onClick = { onFilterSelected(filter.text) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.24f)),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                filter.icon?.let {
                                    Icon(
                                        imageVector = it,
                                        contentDescription = filter.text,
                                        modifier = Modifier.size(18.dp),
                                        tint = Color.White.copy(alpha = 0.95f)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(filter.text, color = Color.White.copy(alpha = 0.95f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun HeaderCalendarioPreview() {
    HeaderCalendario(selectedFilter = "Proximas", onFilterSelected = {})
}
