package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.data.admin.ContentItem
import com.example.chatbot_diseo.ui.theme.TcsBlue
import com.example.chatbot_diseo.ui.theme.TcsGrayText
import com.example.chatbot_diseo.ui.theme.TcsTextDark
import com.example.chatbot_diseo.ui.theme.TcsWhite

@Composable
fun AdminContentDialog(
    titleDialog: String,
    initialItem: ContentItem? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf(initialItem?.title ?: "") }
    var type by remember { mutableStateOf(initialItem?.type ?: "") }
    var desc by remember { mutableStateOf(initialItem?.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(16.dp),
        title = {
            Column {
                Text(
                    text = titleDialog,
                    style = MaterialTheme.typography.headlineSmall,
                    color = TcsTextDark
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Configura un nuevo mensaje para los colaboradores",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TcsGrayText
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título del mensaje") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Contenido del mensaje") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Disparador (Ej: Registro inicial)") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(title, type, desc) },
                colors = ButtonDefaults.buttonColors(containerColor = TcsBlue),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(if (initialItem == null) "Crear mensaje" else "Guardar cambios", color = TcsWhite)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TcsGrayText)
            ) {
                Text("Cancelar")
            }
        }
    )
}
