package com.example.chatbot_diseo.presentation.calendario.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

data class FilterOption(
    val text: String,
    val icon: ImageVector? = null
)

@Composable
fun HeaderCalendario(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val filters = listOf(
        FilterOption("Todas"),
        FilterOption("Pendientes", Icons.Default.Schedule),
        FilterOption("Próximas", Icons.AutoMirrored.Filled.ArrowForward),
        FilterOption("Completadas", Icons.Default.Check)
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendar Icon",
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Calendario de Onboarding",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tu programa personalizado de actividades",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            val firstRowFilters = filters.take(2)
            val secondRowFilters = filters.drop(2)

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    firstRowFilters.forEach { filter ->
                        val isSelected = selectedFilter == filter.text
                        if (isSelected && filter.icon == null) {
                            Button(
                                onClick = { onFilterSelected(filter.text) },
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
                            ) {
                                Text(filter.text, color = Color.White)
                            }
                        } else {
                            OutlinedButton(
                                onClick = { onFilterSelected(filter.text) },
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.dp, Color.LightGray)
                            ) {
                                filter.icon?.let {
                                    Icon(
                                        imageVector = it,
                                        contentDescription = filter.text,
                                        modifier = Modifier.size(18.dp),
                                        tint = if (isSelected) Color(0xFF007AFF) else Color.Gray
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    filter.text,
                                    color = if (isSelected) Color(0xFF007AFF) else Color.DarkGray
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    secondRowFilters.forEach { filter ->
                        val isSelected = selectedFilter == filter.text
                        if (isSelected && filter.icon == null) {
                            Button(
                                onClick = { onFilterSelected(filter.text) },
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
                            ) {
                                Text(filter.text, color = Color.White)
                            }
                        } else {
                            OutlinedButton(
                                onClick = { onFilterSelected(filter.text) },
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.dp, Color.LightGray)
                            ) {
                                filter.icon?.let {
                                    Icon(
                                        imageVector = it,
                                        contentDescription = filter.text,
                                        modifier = Modifier.size(18.dp),
                                        tint = if (isSelected) Color(0xFF007AFF) else Color.Gray
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    filter.text,
                                    color = if (isSelected) Color(0xFF007AFF) else Color.DarkGray
                                )
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
    HeaderCalendario(selectedFilter = "Todas", onFilterSelected = {})
}

