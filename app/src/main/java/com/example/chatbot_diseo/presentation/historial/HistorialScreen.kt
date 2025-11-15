package com.example.chatbot_diseo.presentation.historial

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HistorialScreen(
    onBack: () -> Unit,
    vm: HistorialViewModel = viewModel()
) {

    val lista = vm.historial

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
    ) {

        TopBarHistorial(onBack)

        if (lista.isEmpty()) {
            EmptyHistorial()
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(lista) { item ->
                    HistorialCard(item)
                }
            }
        }
    }
}

@Composable
fun TopBarHistorial(onBack: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            Icons.Default.ArrowBack,
            contentDescription = "",
            tint = Color.Black,
            modifier = Modifier
                .size(26.dp)
                .clickable { onBack() }
        )

        Spacer(Modifier.width(12.dp))

        Text(
            "Historial",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black
        )
    }
}

@Composable
fun HistorialCard(item: HistorialItem) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {

        Column(Modifier.padding(16.dp)) {

            Text(
                text = item.mensaje,
                color = Color.Black
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = item.fecha,
                color = Color(0xFF6B7280),
                fontSize = MaterialTheme.typography.labelSmall.fontSize
            )
        }
    }
}

@Composable
fun EmptyHistorial() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "No hay conversaciones aún",
            color = Color.Gray
        )
    }
}
