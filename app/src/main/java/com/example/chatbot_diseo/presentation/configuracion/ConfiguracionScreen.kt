package com.example.chatbot_diseo.presentation.configuracion

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.ui.theme.TcsBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración", color = Color.White) },
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
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize(), contentAlignment = Alignment.TopStart) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Ajustes de la aplicación", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Aquí puedes configurar idioma, notificaciones y preferencias de la cuenta.")
            }
        }
    }
}

