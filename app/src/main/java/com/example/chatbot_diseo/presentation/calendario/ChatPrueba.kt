package com.example.chatbot_diseo.presentation.calendario

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ChatPruebaScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "HOLA, ESTE ES EL CHAT")
    }
}

@Preview
@Composable
fun ChatPruebaScreenPreview() {
    ChatPruebaScreen()
}
