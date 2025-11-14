package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.data.admin.ActivityItem
import com.example.chatbot_diseo.ui.theme.TcsBlue
import com.example.chatbot_diseo.ui.theme.TcsGrayText
import com.example.chatbot_diseo.ui.theme.TcsWhite

@Composable
fun AdminActivityDialog(
    titleDialog: String,
    initialItem: ActivityItem? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var modality by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        title = {
            Column {
                Text(titleDialog, style = MaterialTheme.typography.headlineSmall)
                Text("Registra una nueva actividad", color = TcsGrayText)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Fecha") },
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = modality,
                    onValueChange = { modality = it },
                    label = { Text("Modalidad (Presencial / Virtual)") },
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(title, date, modality) },
                colors = ButtonDefaults.buttonColors(containerColor = TcsBlue)
            ) {
                Text("Crear actividad", color = TcsWhite)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
