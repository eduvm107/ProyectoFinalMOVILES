package com.example.chatbot_diseo.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChatBubble(mensaje: Mensaje, onAction: (Mensaje) -> Unit = {}) {

    Spacer(modifier = Modifier.height(10.dp))

    if (mensaje.esUsuario) {
        // BURBUJA AZUL (usuario)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF1F78FF), RoundedCornerShape(22.dp))
                    .padding(14.dp)
                    .widthIn(max = 260.dp)
            ) {
                Text(mensaje.texto, color = Color.White)
            }
        }

    } else {
        // BURBUJA DEL BOT
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {

            Icon(
                Icons.Default.SmartToy,
                contentDescription = "",
                tint = Color(0xFFADB4C0),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(28.dp)
            )

            Column {
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(22.dp))
                        .padding(14.dp)
                        .widthIn(max = 260.dp)
                ) {
                    Text(mensaje.texto, color = Color(0xFF333D47))
                }

                // Botón de acción (si existe)
                mensaje.textoAccion?.let { accionTexto ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            // Ejecutar la accion asignada en el Mensaje (si existe) como primer intento
                            mensaje.accion?.invoke()
                            // Luego notificar al handler de la pantalla (fallback / navegación por route)
                            onAction(mensaje)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F78FF), contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(start = 4.dp)
                    ) {
                        Text(accionTexto)
                    }
                }
            }
        }
    }
}
