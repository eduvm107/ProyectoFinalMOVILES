package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
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
import com.example.chatbot_diseo.data.admin.ResourceItem
import com.example.chatbot_diseo.ui.theme.*

@Composable
fun AdminResourceCard(
    item: ResourceItem,
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
            // Título del recurso con mejor estilo
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleLarge,
                color = TcsTextDark
            )

            Spacer(Modifier.height(14.dp))

            // Categoría con badge mejorado
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(TcsGraySoft)
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = item.category,
                    style = MaterialTheme.typography.labelLarge,
                    color = TcsTextDark
                )
            }

            Spacer(Modifier.height(14.dp))

            // URL con icono mejorado
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(TcsBlueLight)
                    .padding(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = null,
                    tint = TcsBlue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = item.url,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TcsBlue,
                    maxLines = 1
                )
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

/*package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.data.admin.ResourceItem

@Composable
fun AdminResourceCard(
    item: ResourceItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {

        Column {

            // Título
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF0A0A0A)
            )

            Spacer(Modifier.height(6.dp))

            // Categoría
            Text(
                text = "Categoría: ${item.category}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6D6D6D)
            )
        }

        // ICONOS DERECHA
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Editar",
                tint = Color(0xFF1565C0),
                modifier = Modifier
                    .size(22.dp)
                    .clickable { onEdit() }
            )

            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Eliminar",
                tint = Color(0xFFD32F2F),
                modifier = Modifier
                    .size(22.dp)
                    .clickable { onDelete() }
            )
        }
    }
}
*/