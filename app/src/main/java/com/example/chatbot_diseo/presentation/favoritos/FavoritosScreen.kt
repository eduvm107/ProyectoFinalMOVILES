package com.example.chatbot_diseo.presentation.favoritos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.presentation.favoritos.components.FavoritosTopBar
import com.example.chatbot_diseo.presentation.favoritos.components.FavoritosTabs
import com.example.chatbot_diseo.presentation.favoritos.components.FavoritoResourceCard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.School

@Composable
fun FavoritosScreen(onBack: () -> Unit) {

    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
    ) {

        FavoritosTopBar(onBack = onBack)

        Spacer(modifier = Modifier.height(6.dp))

        FavoritosTabs(
            selected = selectedTab,
            onSelect = { selectedTab = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Recursos favoritos",
            modifier = Modifier.padding(horizontal = 20.dp),
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF0A0A0A)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // FAVORITOS FILTRADOS
            if (selectedTab == 0 || selectedTab == 1) {
                FavoritoResourceCard(
                    titulo = "Manual de bienvenida",
                    descripcion = "Guía completa para nuevos colaboradores de TCS",
                    categoria = "Manuales",
                    imageVector = Icons.Outlined.MenuBook,
                    iconBg = Color(0xFFE1EEFF),
                    onOpen = {}
                )
            }

            if (selectedTab == 0 || selectedTab == 3) {
                FavoritoResourceCard(
                    titulo = "Beneficios y convenios",
                    descripcion = "Descubre todos los beneficios de TCS",
                    categoria = "Beneficios",
                    imageVector = Icons.Outlined.CardGiftcard,
                    iconBg = Color(0xFFFFF2DD),
                    onOpen = {}
                )
            }

            if (selectedTab == 0 || selectedTab == 2) {
                FavoritoResourceCard(
                    titulo = "Cursos TCS Academy",
                    descripcion = "Plataforma de capacitación de TCS",
                    categoria = "Cursos",
                    imageVector = Icons.Outlined.School,
                    iconBg = Color(0xFFECE8FF),
                    onOpen = {}
                )
            }
        }
    }
}
