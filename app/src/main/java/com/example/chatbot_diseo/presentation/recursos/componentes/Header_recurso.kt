package com.example.chatbot_diseo.presentation.recursos.componentes

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val icon: ImageVector? = null, // Icono es opcional
    val iconColor: Color? = null
)

@Composable
fun ResourcesHeader() {
    val filters = listOf(
        ResourceFilter("Todos"),
        ResourceFilter("Manuales", iconColor = Color(0xFF4FC3F7)), // Cuadrado de color
        ResourceFilter("Cursos", Icons.Outlined.School, iconColor = Color(0xFF7E57C2)),
        ResourceFilter("Beneficios", Icons.Outlined.CardGiftcard, iconColor = Color(0xFFFFB74D))
    )

    var selectedFilter by remember { mutableStateOf(filters.first()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Sección del Título ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.MenuBook, contentDescription = "Recursos", tint = Color(0xFF007AFF))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Recursos", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Text("Material de apoyo y documentación", fontSize = 14.sp, color = Color.Gray)

        // --- Fila de Filtros Desplazable ---
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filters) { filter ->
                val isSelected = selectedFilter == filter

                if (isSelected) {
                    // Botón seleccionado (relleno)
                    Button(
                        onClick = { selectedFilter = filter },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
                    ) {
                        Text(filter.text)
                    }
                } else {
                    // Botones no seleccionados (con borde)
                    OutlinedButton(
                        onClick = { selectedFilter = filter },
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.7f))
                    ) {
                        // Si el botón tiene un icono definido, lo mostramos
                        filter.icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = filter.text,
                                tint = filter.iconColor ?: Color.Gray
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        // Si no, mostramos el cuadrado de color para "Manuales"
                        if (filter.text == "Manuales") {
                            Box(modifier = Modifier.size(14.dp).clip(RoundedCornerShape(4.dp)).background(filter.iconColor!!))
                            Spacer(modifier = Modifier.width(6.dp))
                        }

                        Text(filter.text, color = Color.DarkGray)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResourcesHeaderPreview() {
    ResourcesHeader()
}
