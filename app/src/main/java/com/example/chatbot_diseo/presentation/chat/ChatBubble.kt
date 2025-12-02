package com.example.chatbot_diseo.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(22.dp))
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
                contentDescription = "Icono del Bot",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(28.dp)
            )

            // --- CÓDIGO CORREGIDO Y LIMPIO ---
            // Un único Box para la burbuja del bot
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(22.dp)
                    )
                    .padding(14.dp)
                    .widthIn(max = 260.dp)
            ) {
                // Una Columna para apilar el texto y el botón
                Column {
                    Text(
                        text = mensaje.texto,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Botón de acción (si existe)
                    mensaje.textoAccion?.let { accionTexto ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                // Ejecutar la accion asignada en el Mensaje (si existe)
                                mensaje.accion?.invoke()
                                // Luego notificar al handler de la pantalla (fallback)
                                onAction(mensaje)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1F78FF),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(accionTexto)
                        }
                    }
                }
            }
        }
    }
}
