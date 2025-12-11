@file:Suppress("DEPRECATION")

package com.example.chatbot_diseo.presentation.recursos.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ResourceFilter(
    val text: String,
    val icon: ImageVector? = null,
    val iconColor: Color? = null
)

@Composable
fun ResourcesHeader(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val filters = listOf(
        ResourceFilter("Todos"),
        ResourceFilter("Manuales", iconColor = Color(0xFF4FC3F7)),
        ResourceFilter("Cursos", Icons.Outlined.School, iconColor = Color(0xFF7E57C2)),
        ResourceFilter("Beneficios", Icons.Outlined.CardGiftcard, iconColor = Color(0xFFFFB74D))
    )

    // Header oscuro full-width con estructura igual al ChatHeader
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF4A6B8A))
            .padding(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.MenuBook,
                        contentDescription = "Recursos",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Título y subtítulo juntos al lado del icono
                Column {
                    Text(
                        "Recursos",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Material de apoyo y documentación",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // filtros poco espaciados (subirlos)
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
                                        tint = filter.iconColor ?: Color.White.copy(alpha = 0.95f)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                if (filter.text == "Manuales") {
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(filter.iconColor ?: Color(0xFF4FC3F7))
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
                                        tint = filter.iconColor ?: Color.White.copy(alpha = 0.95f)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                if (filter.text == "Manuales") {
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(filter.iconColor ?: Color(0xFF4FC3F7))
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

@Preview(showBackground = true)
@Composable
fun ResourcesHeaderPreview() {
    ResourcesHeader(selectedFilter = "Todos", onFilterSelected = {})
}
