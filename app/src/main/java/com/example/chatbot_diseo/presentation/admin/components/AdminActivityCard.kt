package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.data.admin.ActivityItem
import com.example.chatbot_diseo.ui.theme.*

@Composable
fun AdminActivityCard(
    item: ActivityItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = TcsWhite),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Título de la actividad con mejor estilo
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleLarge,
                color = TcsTextDark
            )

            Spacer(Modifier.height(16.dp))

            // Fecha y modalidad con iconos mejorados
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Fecha con mejor diseño
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(TcsBlueLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = TcsBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = item.date,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TcsTextLight
                    )
                }

                // Modalidad con badge mejorado
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(TcsGraySoft)
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = item.modality,
                        style = MaterialTheme.typography.labelLarge,
                        color = TcsTextDark
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Botones de acción mejorados
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onEdit,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Editar",
                        tint = TcsBlue,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Editar",
                        color = TcsBlue,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Spacer(Modifier.width(12.dp))
                TextButton(
                    onClick = onDelete,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Eliminar",
                        tint = TcsRed,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Eliminar",
                        color = TcsRed,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
