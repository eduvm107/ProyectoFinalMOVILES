package com.example.chatbot_diseo.presentation.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.presentation.menu.componentes.NotificacionMenu
import com.example.chatbot_diseo.presentation.menu.componentes.QuickAccessMenu


@Composable
fun PantallaMenu() {
    // Columna principal que organiza la pantalla verticalmente
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5)) // Un fondo suave
    ) {
        // 1. Cabecera con el menú de accesos rápidos. Esta parte es FIJA.
        QuickAccessMenu()

        Spacer(modifier = Modifier.height(8.dp))

        // 2. Lista de notificaciones que SÍ se puede desplazar.
        // Usamos LazyColumn con weight(1f) para que ocupe el espacio restante
        // sin desbordarse y romper el layout.
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Añadimos varias notificaciones para demostrar el scroll
            items(10) {
                NotificacionMenu()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaMenuPreview() {
    PantallaMenu()
}
