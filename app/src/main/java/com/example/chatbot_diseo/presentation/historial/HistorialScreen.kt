package com.example.chatbot_diseo.presentation.historial

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbot_diseo.data.api.TokenHolder
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    onBack: () -> Unit,
    viewModel: HistorialViewModel = viewModel(),
    onOpenChat: (String) -> Unit = {}
) {
    val chats by viewModel.chats.collectAsState()

    // Cargar historial por usuario actual cuando se entra a la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarDatos(TokenHolder.usuarioId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Conversaciones") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.crearNuevoChat() }, // Crea chat en Mongo
                containerColor = Color(0xFF1A73E8),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Edit, "Nuevo Chat")
            }
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        if (chats.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No hay chats recientes", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(top = 10.dp, bottom = 80.dp)
            ) {
                items(chats) { chat ->
                    // Asegurarse de no pasar null; si chat.id es null no llamamos a onOpenChat
                    HistorialItem(chat = chat, onClick = { chat.id?.let { id -> onOpenChat(id) } })
                }
            }
        }
    }
}