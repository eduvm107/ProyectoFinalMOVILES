package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.network.dto.response.ContentResponse
import com.example.chatbot_diseo.ui.theme.*

@Composable
fun AdminContentCard(
    item: ContentResponse,
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
            // Título principal con mejor estilo
            Text(
                text = item.titulo,
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                color = TcsTextDark
            )

            Spacer(Modifier.height(12.dp))

            // Row con tipo y estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Tipo con badge mejorado
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(TcsBlueLight)
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = item.tipo,
                        style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
                        color = TcsBlue
                    )
                }

                // Estado activo/inactivo
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (item.activo) TcsGreen.copy(alpha = 0.15f) else TcsGrayLight)
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (item.activo) "Activo" else "Inactivo",
                        style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
                        color = if (item.activo) TcsGreen else TcsGrayText
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // Contenido del mensaje
            Text(
                text = item.contenido,
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                color = TcsTextLight,
                lineHeight = androidx.compose.ui.unit.TextUnit(22f, androidx.compose.ui.unit.TextUnitType.Sp),
                maxLines = 3
            )

            Spacer(Modifier.height(12.dp))

            // Información adicional
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Día gatillo
                Column {
                    Text(
                        text = "Día",
                        style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                        color = TcsGrayText
                    )
                    Text(
                        text = item.diaGatillo.toString(),
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                        color = TcsTextDark
                    )
                }

                // Prioridad
                Column {
                    Text(
                        text = "Prioridad",
                        style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                        color = TcsGrayText
                    )
                    Text(
                        text = item.prioridad,
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                        color = when (item.prioridad.lowercase()) {
                            "alta" -> TcsRed
                            "media" -> TcsOrange
                            else -> TcsGreen
                        }
                    )
                }

                // Hora de envío
                Column {
                    Text(
                        text = "Hora",
                        style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                        color = TcsGrayText
                    )
                    Text(
                        text = item.horaEnvio,
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                        color = TcsTextDark
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Canales
            if (item.canal.isNotEmpty()) {
                Text(
                    text = "Canales: ${item.canal.joinToString(", ")}",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    color = TcsGrayText
                )
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(8.dp))

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
                        style = androidx.compose.material3.MaterialTheme.typography.labelLarge
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
                        style = androidx.compose.material3.MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
