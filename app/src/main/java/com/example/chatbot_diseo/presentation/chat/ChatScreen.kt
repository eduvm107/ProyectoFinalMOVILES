package com.example.chatbot_diseo.presentation.chat

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
    viewModel: ChatViewModel = viewModel()
) {

    var drawerOpen by remember { mutableStateOf(false) }

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
                ChatBubble(msg.texto, msg.esUsuario)
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
