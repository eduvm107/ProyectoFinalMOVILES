package com.example.chatbot_diseo.presentation.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.chatbot_diseo.presentation.menu.SideMenu

@Composable
fun ChatScreen(
    navController: NavHostController,
    initialConversacionId: String? = null,
    viewModel: ChatViewModel = viewModel()
) {

    // ✅ FIX CRÍTICO: Cargar conversación cuando cambia el initialConversacionId
    // Si initialConversacionId es null, limpiamos el chat para iniciar uno nuevo
    LaunchedEffect(initialConversacionId) {
        Log.d("ChatScreen", "🔍 LaunchedEffect triggered - initialConversacionId=$initialConversacionId")

        if (initialConversacionId != null && initialConversacionId.isNotBlank()) {
            // Cargar conversación del historial
            try {
                Log.d("ChatScreen", "📥 Cargando conversación desde historial: id=$initialConversacionId")
                viewModel.cargarConversacionPorId(initialConversacionId)
            } catch (e: Exception) {
                Log.e("ChatScreen", "❌ Error cargando conversacion id=$initialConversacionId", e)
            }
        } else {
            // Si no hay ID, asegurarse de que el chat esté limpio para nuevo chat
            Log.d("ChatScreen", "🆕 No hay conversacionId - Chat nuevo")
            // Solo limpiar si hay mensajes previos de otra conversación
            if (viewModel.conversacionId != null) {
                viewModel.limpiarChat()
            }
        }
    }

    var drawerOpen by remember { mutableStateOf(false) }

    // Asignar callbacks de navegación en cuanto la pantalla se crea (compatibilidad)
    viewModel.navegarADocumentos = { navController.navigate("recursos") }
    viewModel.navegarAActividades = { navController.navigate("calendario") }
    viewModel.navegarAPerfil = { navController.navigate("perfil") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val listState = rememberLazyListState()

        // Datos para controlar el scroll automático
        val mensajes = viewModel.mensajes
        val mensajesCount = mensajes.size
        val ultimoMensajeClave = mensajes.lastOrNull()?.let { it.texto to it.esUsuario }
        val mostrarSugerencias = viewModel.mostrarSugerencias.value

        // Cuando cambia la lista (tamaño o contenido del último mensaje) o las sugerencias,
        // hacer scroll suave al último ítem visible
        LaunchedEffect(mensajesCount, ultimoMensajeClave, mostrarSugerencias) {
            val extra = if (mostrarSugerencias) 1 else 0 // ítem de chips al final
            val targetIndex = mensajesCount - 1 + extra
            if (targetIndex >= 0) {
                listState.animateScrollToItem(targetIndex)
            }
        }

        // HEADER
        ChatHeader(
            // Mostrar diálogo de confirmación antes de crear nuevo chat
            onNewChat = {
                // Si hay mensajes (más que el mensaje inicial), mostrar diálogo
                if (viewModel.mensajes.size > 1 || viewModel.conversacionId != null) {
                    viewModel.mostrarDialogoNuevoChat.value = true
                } else {
                    // Si no hay conversación activa, crear directamente
                    viewModel.limpiarChat()
                    viewModel.crearConversacionVacia()
                }
            },
            onMenuClick = { drawerOpen = true }
        )

        // LISTA DEL CHAT
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            state = listState
        ) {
            items(viewModel.mensajes) { msg ->
                ChatBubble(msg) { mensaje ->
                    // Si la respuesta predefinida viene con una ruta, navegamos
                    val route = mensaje.actionRoute
                    if (!route.isNullOrBlank()) {
                        try {
                            Log.d("ChatAction", "Attempting to navigate to route from message: $route")

                            // Primero intentar la lambda del ViewModel (fallback seguro si hay otro NavController)
                            when (route) {
                                "recursos" -> viewModel.navegarADocumentos?.invoke()
                                "actividades" -> viewModel.navegarAActividades?.invoke()
                                "perfil" -> viewModel.navegarAPerfil?.invoke()
                            }

                            // Luego intentar la navegación directa con navController (comportamiento estándar)
                            navController.navigate(route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        } catch (ex: Exception) {
                            Log.e("ChatAction", "Navigation failed for route=$route", ex)
                        }
                    } else {
                        // Si no hay route, ejecutamos la acción si existe
                        mensaje.accion?.invoke()
                    }
                }
            }

            if (viewModel.mostrarSugerencias.value) {
                item {
                    ChatChips(viewModel.sugerencias) {
                        // Desde FAQ: NO ocultar sugerencias (usa default)
                        viewModel.enviarMensaje(it)
                    }
                }
            }
        }

        // FOOTER DEL CHAT (DENTRO DE LA COLUMNA)
        ChatInput {
            // Desde input escrito: ocultar sugerencias
            viewModel.enviarMensaje(it, ocultarSugerencias = true)
        }
    }

    // MENÚ LATERAL
    if (drawerOpen) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
                    .align(Alignment.TopEnd),
                color = Color.White,
                shadowElevation = 12.dp
            ) {
                SideMenu(
                    onNavigate = {
                        drawerOpen = false
                        navController.navigate(it)
                    },
                    onClose = { drawerOpen = false }
                )
            }
        }
    }

    // DIÁLOGO DE CONFIRMACIÓN PARA NUEVO CHAT
    if (viewModel.mostrarDialogoNuevoChat.value) {
        AlertDialog(
            onDismissRequest = { viewModel.mostrarDialogoNuevoChat.value = false },
            title = { Text("Iniciar nuevo chat") },
            text = { Text("Tu conversación actual se guardará automáticamente en el historial. ¿Deseas crear un nuevo chat?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.mostrarDialogoNuevoChat.value = false
                        viewModel.limpiarChat()
                        viewModel.crearConversacionVacia()
                    }
                ) {
                    Text("Sí, crear nuevo")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.mostrarDialogoNuevoChat.value = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

}
