package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.ui.theme.*

/**
 * Banner para mostrar mensajes de error o éxito en el panel admin
 */
@Composable
fun MessageBanner(
    message: String?,
    isError: Boolean = true,
    onDismiss: () -> Unit
) {
    if (message != null) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isError) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isError) Icons.Default.Error else Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = if (isError) TcsRed else Color(0xFF4CAF50),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isError) TcsRed else Color(0xFF2E7D32)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = if (isError) TcsRed else Color(0xFF4CAF50)
                    )
                }
            }
        }
    }
}

/**
 * Indicador de carga con mensaje
 */
@Composable
fun LoadingIndicator(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = TcsBlue
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    "Cargando datos del servidor...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TcsTextLight
                )
            }
        }
    }
}

/**
 * Tarjeta de diagnóstico de conexión (temporal para debugging)
 */
@Composable
fun DiagnosticCard(
    errorMessage: String?,
    onTest: () -> Unit,
    onRefresh: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFFFF6F00),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Diagnóstico de Conexión",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFE65100)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                "Backend: http://10.185.24.6:5288/api/",
                style = MaterialTheme.typography.bodySmall,
                color = TcsTextDark
            )

            if (errorMessage != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Error: $errorMessage",
                    style = MaterialTheme.typography.bodySmall,
                    color = TcsRed
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onRefresh,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TcsBlue
                    )
                ) {
                    Text("Reintentar")
                }

                OutlinedButton(
                    onClick = onTest
                ) {
                    Text("Ver Info", color = TcsBlue)
                }
            }
        }
    }
}

