
package com.example.chatbot_diseo.presentation.calendario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import java.util.Locale

@Composable
fun Calendario() {
    var calendar by remember { mutableStateOf(Calendar.getInstance()) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            calendar = calendar,
            onPrevMonth = {
                calendar = calendar.clone() as Calendar
                calendar.add(Calendar.MONTH, -1)
            },
            onNextMonth = {
                calendar = calendar.clone() as Calendar
                calendar.add(Calendar.MONTH, 1)
            }
        )
        DaysOfWeek()
        DaysOfMonth(calendar)
    }
}

@Composable
fun Header(calendar: Calendar, onPrevMonth: () -> Unit, onNextMonth: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrevMonth) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Mes anterior")
        }
        Text(
            text = "${calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("es", "ES"))?.replaceFirstChar { it.uppercase() }} ${calendar.get(Calendar.YEAR)}",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        IconButton(onClick = onNextMonth) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Mes siguiente")
        }
    }
}

@Composable
fun DaysOfWeek() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val days = listOf("Daaaa", "L", "M", "X", "J", "V", "S")
        for (day in days) {
            Text(
                text = day,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DaysOfMonth(calendar: Calendar) {
    val month = calendar.clone() as Calendar
    month.set(Calendar.DAY_OF_MONTH, 1)
    val firstDayOfWeek = month.get(Calendar.DAY_OF_WEEK) - 1
    val daysInMonth = month.getActualMaximum(Calendar.DAY_OF_MONTH)

    Column {
        var dayOfMonth = 1
        for (i in 0 until 6) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (j in 0 until 7) {
                    if (i == 0 && j < firstDayOfWeek) {
                        Spacer(modifier = Modifier.weight(1f))
                    } else if (dayOfMonth <= daysInMonth) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dayOfMonth.toString(),
                                modifier = Modifier.padding(8.dp),
                                color = if (isToday(dayOfMonth, calendar)) Color.Red else Color.Black
                            )
                        }
                        dayOfMonth++
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

fun isToday(day: Int, calendar: Calendar): Boolean {
    val today = Calendar.getInstance()
    return today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
            today.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
            today.get(Calendar.DAY_OF_MONTH) == day
}
