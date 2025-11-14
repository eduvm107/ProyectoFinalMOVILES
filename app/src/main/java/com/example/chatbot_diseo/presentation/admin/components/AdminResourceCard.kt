package com.example.chatbot_diseo.presentation.admin.components

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
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = TcsWhite),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(item.title, color = TcsTextDark)
            Text("Categoría: ${item.category}", color = TcsTextLight)
            Spacer(Modifier.height(4.dp))
            Text(item.url, color = TcsBlue)

            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = null, tint = TcsBlue)
                }
                TextButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = null, tint = TcsRed)
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