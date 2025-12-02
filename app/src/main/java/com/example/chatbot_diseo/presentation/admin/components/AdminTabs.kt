package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatbot_diseo.ui.theme.*

@Composable
fun AdminTabs(
    selected: Int,
    onSelect: (Int) -> Unit
) {
    // Card con marco profesional y sombra
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = TcsBlue.copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = TcsWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.5.dp,
                    color = TcsBlue.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(20.dp)
                )
                .background(
                    color = Color(0xFFF8F9FA), // Fondo gris muy claro profesional
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                TabItemFigma(
                    icon = Icons.Default.Email,
                    isSelected = selected == 0,
                    onClick = { onSelect(0) }
                )

                TabItemFigma(
                    icon = Icons.Default.CalendarToday,
                    isSelected = selected == 1,
                    onClick = { onSelect(1) }
                )

                TabItemFigma(
                    icon = Icons.Default.MenuBook,
                    isSelected = selected == 2,
                    onClick = { onSelect(2) }
                )

                /*TabItemFigma(
                    icon = Icons.Default.AutoGraph,
                    isSelected = selected == 3,
                    onClick = { onSelect(3) }
                )*/
            }
        }
    }
}

@Composable
fun TabItemFigma(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(48.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) TcsBlue else Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 0.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isSelected) {
                        Modifier.border(
                            width = 2.dp,
                            color = TcsBlue.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(14.dp)
                        )
                    } else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) TcsWhite else TcsGrayText,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
