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

            TabItemFigma(
                icon = Icons.Default.AutoGraph,
                isSelected = selected == 3,
                onClick = { onSelect(3) }
            )
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
/*package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.ui.theme.*

@Composable
fun AdminTabs(
    selected: Int,
    onSelect: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TabButton(
            index = 0,
            selected = selected,
            icon = Icons.Filled.Email,
            label = "Mensajes",
            onSelect = onSelect
        )
        TabButton(
            index = 1,
            selected = selected,
            icon = Icons.Filled.CalendarToday,
            label = "Actividad",
            onSelect = onSelect
        )
        TabButton(
            index = 2,
            selected = selected,
            icon = Icons.Filled.Create,
            label = "Recursos",
            onSelect = onSelect
        )
        TabButton(
            index = 3,
            selected = selected,
            icon = Icons.Filled.Build,
            label = "Métricas",
            onSelect = onSelect
        )
    }
}

@Composable
private fun TabButton(
    index: Int,
    selected: Int,
    icon: ImageVector,
    label: String,
    onSelect: (Int) -> Unit
) {
    val isSelected = index == selected

    Column(
        modifier = Modifier
            .width(76.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(if (isSelected) TcsBlueLight else TcsWhite)
            .border(
                2.dp,
                if (isSelected) TcsBlue else TcsGrayBorder,
                RoundedCornerShape(40.dp)
            )
            .padding(vertical = 12.dp)
            .clickable { onSelect(index) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) TcsBlue else TcsGrayText
        )
        Text(
            text = label,
            color = if (isSelected) TcsBlue else TcsGrayText,
            style = MaterialTheme.typography.labelSmall
        )
    }
}*/
