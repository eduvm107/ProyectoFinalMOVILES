package com.example.chatbot_diseo.presentation.notificaciones

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.ArrowBack
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

    var expandido by remember { mutableStateOf(false) }
    var seleccionada by remember { mutableStateOf<MensajeAutomatico?>(null) }

    // 👉 Chips visibles en la barra (scroll horizontal)
    val chipsVisibles = tipos.take(3)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificaciones") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Regresar")
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

            // =====================================================
            // BARRA SUPERIOR — SCROLL + BOTÓN MÁS
            // =====================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 🔹 Si NO está expandido → mostrar scroll horizontal
                if (!expandido) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        item {
                            ChipFiltro(
                                texto = "Todos",
                                seleccionado = filtroActual == "Todos",
                                onClick = { viewModel.seleccionarFiltro("Todos") }
                            )
                        }

                        items(chipsVisibles) { tipo ->
                            ChipFiltro(
                                texto = tipo,
                                seleccionado = filtroActual == tipo,
                                onClick = { viewModel.seleccionarFiltro(tipo) }
                            )
                        }
                    }
                } else {
                    // 🔹 Si está expandido → QUITAR scroll horizontal y dejar espacio vacío
                    Spacer(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.width(8.dp))

                // ===== BOTÓN MÁS (SIEMPRE EN EL MISMO SITIO) =====
                FilledTonalButton(
                    onClick = { expandido = !expandido },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color(0xFFE8EBF0),
                        contentColor = Color(0xFF1A73E8)
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = if (expandido) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            }

            // =====================================================
            // PANEL INFERIOR — REJILLA DE TODOS LOS CHIPS
            // =====================================================
            AnimatedVisibility(visible = expandido) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {

                    val chipsGrid = listOf("Todos") + tipos

                    chipsGrid.chunked(3).forEach { fila ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            fila.forEach { tipo ->
                                ChipFiltro(
                                    texto = tipo,
                                    seleccionado = filtroActual == tipo,
                                    onClick = { viewModel.seleccionarFiltro(tipo) }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            // =====================================================
            // LISTA DE NOTIFICACIONES
            // =====================================================
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

            // =====================================================
            // DIÁLOGO DETALLES
            // =====================================================
            if (seleccionada != null) {
                val item = seleccionada!!
                AlertDialog(
                    onDismissRequest = { seleccionada = null },
                    title = { Text(item.titulo ?: "Notificación", fontWeight = FontWeight.Bold) },
                    text = {
                        Column {
                            Text(item.contenido ?: "")
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Enviado: ${item.horaEnvio ?: "--"}", color = Color.Gray)
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
    onClick: () -> Unit
) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = { Text(texto) },
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
