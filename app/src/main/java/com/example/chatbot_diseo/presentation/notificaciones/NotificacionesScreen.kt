package com.example.chatbot_diseo.presentation.notificaciones

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbot_diseo.data.model.MensajeAutomatico

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacionesScreen(
    onBack: () -> Unit,
    viewModel: NotificacionesViewModel = viewModel()
) {
    val notificaciones by viewModel.notificacionesVisibles.collectAsState()
    val filtroActual by viewModel.filtroSeleccionado.collectAsState()
    val tipos by viewModel.tiposDisponibles.collectAsState()

    var seleccionada by remember { mutableStateOf<MensajeAutomatico?>(null) }

    // Mostrar filtros siempre: 3 por fila, estático
    val mostrar = listOf("Todos") + tipos

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificaciones") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // FILTROS SUPERIORES — filas de 3
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                mostrar.chunked(3).forEach { fila ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        fila.forEach { tipo ->
                            ChipFiltro(
                                texto = tipo,
                                seleccionado = filtroActual == tipo,
                                onClick = { viewModel.seleccionarFiltro(tipo) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // rellenar si la fila no tiene 3 elementos
                        val faltan = 3 - fila.size
                        repeat(faltan) { Spacer(modifier = Modifier.weight(1f)) }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // LISTA DE NOTIFICACIONES
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (notificaciones.isEmpty()) {
                    item {
                        Text("No hay resultados ✨", color = Color.Gray)
                    }
                } else {
                    items(notificaciones) { noti ->
                        NotificacionItem(
                            noti = noti,
                            onClick = { seleccionada = noti }
                        )
                    }
                }
            }

            // DIÁLOGO DETALLES
            if (seleccionada != null) {
                val item = seleccionada!!
                AlertDialog(
                    onDismissRequest = { seleccionada = null },
                    title = { Text(item.titulo, fontWeight = FontWeight.Bold) },
                    text = {
                        Column {
                            Text(item.contenido)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Enviado: ${item.horaEnvio}", color = Color.Gray)
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { seleccionada = null }) { Text("Cerrar") }
                    },
                    shape = RoundedCornerShape(24.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipFiltro(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        modifier = modifier,
        selected = seleccionado,
        onClick = onClick,
        label = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(texto)
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFF1A73E8),
            selectedLabelColor = Color.White,
            containerColor = Color.White,
            labelColor = Color.Gray
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = seleccionado,
            borderColor = if (seleccionado) Color.Transparent else Color.LightGray
        ),
        shape = RoundedCornerShape(50)
    )
}
