package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.data.admin.ActivityItem
import com.example.chatbot_diseo.data.admin.ResourceItem
import com.example.chatbot_diseo.ui.theme.TcsBlue
import com.example.chatbot_diseo.ui.theme.TcsGrayText
import com.example.chatbot_diseo.ui.theme.TcsWhite

@Composable
fun AdminResourceDialog(
    titleDialog: String,
    initialItem: ResourceItem? ,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf(initialItem?.title ?: "") }
    var category by remember { mutableStateOf(initialItem?.category ?: "") }
    var url by remember { mutableStateOf(initialItem?.url ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        title = {
            Column {
                Text(titleDialog, style = MaterialTheme.typography.headlineSmall)
                Text("Registra un recurso disponible", color = TcsGrayText)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título del recurso") }
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Categoría") }
                )

                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("URL") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && category.isNotBlank() && url.isNotBlank()) {
                        onConfirm(title, category, url)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = TcsBlue)
            ) {
                Text(if (initialItem == null) "Crear recurso" else "Guardar cambios", color = TcsWhite)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
