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
import com.example.chatbot_diseo.data.admin.ContentItem
import com.example.chatbot_diseo.ui.theme.*

@Composable
fun AdminContentCard(
    item: ContentItem,
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
                text = item.title,
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                color = TcsTextDark
            )

            Spacer(Modifier.height(12.dp))

            // Tipo con badge mejorado
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(TcsBlueLight)
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = item.type,
                    style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
                    color = TcsBlue
                )
            }

            Spacer(Modifier.height(14.dp))

            // Descripción con mejor espaciado
            Text(
                text = item.description,
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                color = TcsTextLight,
                lineHeight = androidx.compose.ui.unit.TextUnit(22f, androidx.compose.ui.unit.TextUnitType.Sp)
            )

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
