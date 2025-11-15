package com.example.chatbot_diseo.presentation.notificaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.presentation.notificaciones.components.NotificacionesTopBar
import com.example.chatbot_diseo.presentation.notificaciones.components.NotificacionesTabs
import com.example.chatbot_diseo.presentation.notificaciones.components.NotificacionItem

@Composable
fun NotificacionesScreen(onBack: () -> Unit) {

    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
    ) {

        NotificacionesTopBar(onBack = onBack)

        Spacer(modifier = Modifier.height(6.dp))

        NotificacionesTabs(
            selected = selectedTab,
            onSelect = { selectedTab = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // LISTA DE NOTIFICACIONES
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            if (selectedTab == 0 || selectedTab == 1) {
                NotificacionItem(
                    title = "¡Faltan 3 días para tu inicio!",
                    message = "Tu primer día en TCS será el 17 de noviembre.",
                    date = "14 Nov 2025",
                    iconBg = Color(0xFFFFF7D9),
                    icon = "⚠️"
                )
            }

            if (selectedTab == 0) {
                NotificacionItem(
                    title = "Completa tu checklist",
                    message = "Tienes tareas pendientes antes de tu onboarding.",
                    date = "13 Nov 2025",
                    iconBg = Color(0xFFE0F3FF),
                    icon = "🗂️"
                )
            }

            if (selectedTab == 0) {
                NotificacionItem(
                    title = "Nueva actividad programada",
                    message = "Hay una nueva actividad en tu calendario.",
                    date = "12 Nov 2025",
                    iconBg = Color(0xFFE7FFE4),
                    icon = "🗓️"
                )
            }

            if (selectedTab == 0) {
                NotificacionItem(
                    title = "Tu bienvenida está lista 🎉",
                    message = "Te damos la bienvenida oficialmente a TCS.",
                    date = "11 Nov 2025",
                    iconBg = Color(0xFFFFE8EC),
                    icon = "🏆"
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
