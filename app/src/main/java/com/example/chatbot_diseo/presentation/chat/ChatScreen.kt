package com.example.chatbot_diseo.presentation.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

    var drawerOpen by remember { mutableStateOf(false) }

    // Si se pasó un ID de conversación inicial, asígnalo al ViewModel al entrar
    LaunchedEffect(initialConversacionId) {
        initialConversacionId?.let { viewModel.conversacionId = it }
    }

    // Asignar callbacks de navegación en cuanto la pantalla se crea (compatibilidad)
    viewModel.navegarADocumentos = { navController.navigate("recursos") }
    viewModel.navegarAActividades = { navController.navigate("calendario") }
    viewModel.navegarAPerfil = { navController.navigate("perfil") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F4F7))
    ) {

        // HEADER
        ChatHeader(
            onNewChat = { viewModel.limpiarChat() },
            onMenuClick = { drawerOpen = true }
        )

        // LISTA DEL CHAT
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
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
                                "actividades", "calendario" -> viewModel.navegarAActividades?.invoke()
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

            item {
                ChatChips(viewModel.sugerencias) {
                    viewModel.enviarMensaje(it)
                }
            }
        }

        // FOOTER DEL CHAT (DENTRO DE LA COLUMNA)
        ChatInput { viewModel.enviarMensaje(it) }
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

}
