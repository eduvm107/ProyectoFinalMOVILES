package com.example.chatbot_diseo.presentation.historial

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.chatbot_diseo.ui.theme.TcsBlue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbot_diseo.data.repository.HistorialRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    usuarioId: String,
    onBack: () -> Unit,
    onOpenChat: (conversacionId: String) -> Unit,
    viewModel: HistorialViewModel = viewModel(factory = HistorialViewModelFactory(HistorialRepository(), usuarioId))
) {
    val chats by viewModel.chats.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Observamos uiEvent y mostramos Snackbar cuando haya mensaje
    val uiEvent by remember { viewModel.uiEvent }.collectAsState(initial = null)

    LaunchedEffect(uiEvent) {
        uiEvent?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.uiEventConsumed()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Conversaciones") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TcsBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                    // Pasamos el id de la conversación al hacer click. Si es null, mostramos Snackbar
                    HistorialItem(chat = chat, onClick = { id ->
                        if (id != null) {
                            onOpenChat(id)
                        } else {
                            coroutineScope.launch { snackbarHostState.showSnackbar("ID de conversación inválido") }
                        }
                    })
                }
            }
        }
    }
}