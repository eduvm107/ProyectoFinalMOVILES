package com.example.chatbot_diseo.presentation.chat

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.R

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
            // Icono del bot (logo)
            Image(
                painter = painterResource(id = R.drawable.logo_tata),
                contentDescription = "Icono del Bot",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(28.dp)
            )

            // Si es mensaje de "escribiendo...", mostrar animación de puntitos
            if (mensaje.texto == TYPING_MESSAGE_TEXT) {
                val transition = rememberInfiniteTransition(label = "typing")
                val scale1 by transition.animateFloat(
                    initialValue = 0.7f,
                    targetValue = 1.2f,
                    animationSpec = infiniteRepeatable(
                        tween(400, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "dot1"
                )
                val scale2 by transition.animateFloat(
                    initialValue = 0.7f,
                    targetValue = 1.2f,
                    animationSpec = infiniteRepeatable(
                        tween(400, easing = LinearEasing, delayMillis = 130),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "dot2"
                )
                val scale3 by transition.animateFloat(
                    initialValue = 0.7f,
                    targetValue = 1.2f,
                    animationSpec = infiniteRepeatable(
                        tween(400, easing = LinearEasing, delayMillis = 260),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "dot3"
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(22.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .scale(scale1)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .scale(scale2)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .scale(scale3)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            )
                    )
                }
            } else {
                // Mensaje normal del bot
                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(22.dp)
                        )
                        .padding(14.dp)
                        .widthIn(max = 260.dp)
                ) {
                    Column {
                        Text(
                            text = mensaje.texto,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        mensaje.textoAccion?.let { accionTexto ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    mensaje.accion?.invoke()
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
}

