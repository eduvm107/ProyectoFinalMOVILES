package com.example.chatbot_diseo.presentation.chat

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun ChatMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = { Text("Inicio") },
            onClick = { onSelect("inicio") }
        )
        DropdownMenuItem(
            text = { Text("Notificaciones") },
            onClick = { onSelect("notificaciones") }
        )
        DropdownMenuItem(
            text = { Text("Historial de chat") },
            onClick = { onSelect("historial") }
        )
        DropdownMenuItem(
            text = { Text("Cursos favoritos") },
            onClick = { onSelect("favoritos") }
        )
        DropdownMenuItem(
            text = { Text("Configuración") },
            onClick = { onSelect("configuracion") }
        )
        DropdownMenuItem(
            text = { Text("Ayuda") },
            onClick = { onSelect("ayuda") }
        )
        DropdownMenuItem(
            text = { Text("Cerrar sesión") },
            onClick = { onSelect("logout") }
        )
    }
}
