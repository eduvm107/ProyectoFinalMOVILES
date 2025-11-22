package com.example.chatbot_diseo.presentation.notificaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    val prioridades = viewModel.prioridades

    var menuExpandido by remember { mutableStateOf(false) }

    // Variable para controlar el diálogo de detalles
    var notificacionSeleccionada by remember { mutableStateOf<MensajeAutomatico?>(null) }

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

        Box(modifier = Modifier.padding(padding).fillMaxSize()) {

            Column {
                // --- BARRA DE FILTROS ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Botón Todos
                    ChipFiltro(
                        texto = "Todos",
                        seleccionado = (filtroActual == "Todos"),
                        onClick = { viewModel.seleccionarFiltro("Todos") }
                    )

                    // 2. Botones de Prioridad
                    prioridades.forEach { prio ->
                        ChipFiltro(
                            texto = prio,
                            seleccionado = (filtroActual == prio),
                            onClick = { viewModel.seleccionarFiltro(prio) }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // 3. Botón desplegable (Tipos)
                    Box {
                        // Corrección del nombre de la variable aquí 👇
                        val esFiltroMenu = filtroActual != "Todos" && !prioridades.contains(filtroActual)

                        FilledTonalButton(
                            onClick = { menuExpandido = true },
                            shape = CircleShape,
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(40.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                // Usamos la variable corregida 'esFiltroMenu'
                                containerColor = if (esFiltroMenu) Color(0xFF1A73E8) else Color.White,
                                contentColor = if (esFiltroMenu) Color.White else Color.Gray
                            ),
                            border = if (!esFiltroMenu) ButtonDefaults.outlinedButtonBorder else null
                        ) {
                            Icon(Icons.Default.ArrowDropDown, null)
                        }

                        DropdownMenu(
                            expanded = menuExpandido,
                            onDismissRequest = { menuExpandido = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            MenuTitle("Filtrar por Tipo")
                            tipos.forEach { tipo ->
                                DropdownMenuItem(
                                    text = { Text(tipo) },
                                    onClick = {
                                        viewModel.seleccionarFiltro(tipo)
                                        menuExpandido = false
                                    },
                                    leadingIcon = if (filtroActual == tipo) {
                                        { Icon(Icons.Default.Check, null, tint = Color(0xFF1A73E8)) }
                                    } else null
                                )
                            }
                        }
                    }
                }

                // --- LISTA ---
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (notificaciones.isEmpty()) {
                        item {
                            Text(
                                text = "No hay resultados ✨",
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 40.dp, start = 8.dp)
                            )
                        }
                    } else {
                        items(notificaciones) { noti ->
                            NotificacionItem(
                                noti = noti,
                                onClick = { notificacionSeleccionada = noti }
                            )
                        }
                    }
                }
            }

            // --- DIÁLOGO DE DETALLES ---
            if (notificacionSeleccionada != null) {
                val item = notificacionSeleccionada!!
                AlertDialog(
                    onDismissRequest = { notificacionSeleccionada = null },
                    title = {
                        Text(text = item.titulo ?: "Notificación", fontWeight = FontWeight.Bold)
                    },
                    text = {
                        Column {
                            Text(text = item.contenido ?: "", style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Enviado: ${item.horaEnvio ?: "--:--"}",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.Gray
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { notificacionSeleccionada = null }) {
                            Text("Cerrar")
                        }
                    },
                    containerColor = Color.White,
                    shape = RoundedCornerShape(24.dp)
                )
            }
        }
    }
}

// --- FUNCIONES AYUDANTES (IMPORTANTE: NO BORRAR) ---

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

@Composable
fun MenuTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = Color.Gray,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
    )
}