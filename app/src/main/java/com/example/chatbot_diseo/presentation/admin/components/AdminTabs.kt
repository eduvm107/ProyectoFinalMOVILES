package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatbot_diseo.ui.theme.*

@Composable
fun AdminTabs(
    selected: Int,
    onSelect: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .fillMaxWidth()
            .background(
                color = TcsGraySoft,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
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

@Composable
fun TabItemFigma(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .background(
                color = if (isSelected) TcsBlueLight else Color.Transparent,
                shape = RoundedCornerShape(50)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) TcsBlue else TcsGrayText
        )
    }
}
