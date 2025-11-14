package com.example.chatbot_diseo.presentation.userperfil

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class UserProfile(
    val name: String,
    val email: String,
    val position: String,
    val supervisor: String,
    val startDate: String,
    val onboardingProgress: Int
)

@Composable
fun PerfilScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    sampleProfile: UserProfile? = null
) {
    var showEmailModal by remember { mutableStateOf(false) }
    var emailMessage by remember { mutableStateOf("Hola María, tengo una consulta sobre mi proceso de onboarding…") }
    var showConfirmation by remember { mutableStateOf(false) }

    // Toggle local de tema (solo afecta esta pantalla para preview/demo)
    var isDark by remember { mutableStateOf(true) }

    val profile = sampleProfile ?: UserProfile(
        name = "José Martínez",
        email = "jose.martinez@tcs.com",
        position = "Consultor de Tecnología",
        supervisor = "María García",
        startDate = "15 noviembre 2025",
        onboardingProgress = 65
    )

    val scope = rememberCoroutineScope()

    // colores según tema local
    val bgColor = if (isDark) Color(0xFF001827) else Color(0xFFF6FAFF)
    val cardBg = if (isDark) MaterialTheme.colorScheme.surface.copy(alpha = 0.06f) else Color(0xFFEFF7FF)
    val primaryAccent = if (isDark) Color(0xFF00BFFF) else Color(0xFF0076CE)

    Surface(modifier = modifier.fillMaxSize(), color = bgColor) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cardBg)
                    .padding(horizontal = 16.dp, vertical = 18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👤", fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(text = "Mi Perfil", color = if (isDark) Color(0xFFE7F6FF) else Color(0xFF04233A), fontWeight = FontWeight.SemiBold)
                        Text(text = "Información del colaborador", color = if (isDark) Color(0xFF9FB9CA) else Color(0xFF557083), fontSize = 13.sp)
                    }
                }
            }

            // Content (ahora scrollable)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()) // hace la pantalla desplazable
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp) // padding inferior para evitar recorte
            ) {
                // Avatar y nombre
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .size(112.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Brush.verticalGradient(listOf(Color(0xFF0076CE), Color(0xFF00BFFF))))
                            .shadow(8.dp, RoundedCornerShape(18.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        val initials = profile.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.joinToString("")
                        Text(text = initials, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = profile.name, color = if (isDark) Color(0xFFE7F6FF) else Color(0xFF04233A), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = profile.position, color = if (isDark) Color(0xFF9FB9CA) else Color(0xFF557083), fontSize = 13.sp)

                    Spacer(modifier = Modifier.height(18.dp))
                }

                // Detalles
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    val items = listOf(
                        Triple("Email corporativo", profile.email, "✉️"),
                        Triple("Cargo", profile.position, "💼"),
                        Triple("Supervisor", profile.supervisor, "👩‍💼"),
                        Triple("Fecha de ingreso", profile.startDate, "📅")
                    )

                    items.forEach { item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(cardBg)
                                .padding(14.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = item.third, fontSize = 18.sp)
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = item.first, color = if (isDark) Color(0xFF9FB9CA) else Color(0xFF557083), fontSize = 12.sp)
                                        Text(text = item.second, color = if (isDark) Color(0xFFE7F6FF) else Color(0xFF04233A), fontSize = 15.sp)
                                    }
                                }

                                // Si es supervisor, mostrar el botón debajo (full width)
                                if (item.first == "Supervisor") {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = { showEmailModal = true },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(44.dp),
                                        // estilo de boton con gradiente simulado
                                    ) {
                                        Text(text = "✉️  Enviar correo al supervisor")
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Progreso
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(cardBg)
                        .padding(14.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Brush.horizontalGradient(listOf(Color(0xFF0076CE), Color(0xFF00BFFF)))),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🏆", fontSize = 18.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Progreso del Onboarding", color = if (isDark) Color(0xFFE7F6FF) else Color(0xFF04233A), fontSize = 16.sp)
                                Text(text = "${profile.onboardingProgress}% completado", color = primaryAccent, fontSize = 13.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // barra progresiva con overlay gradiente
                        val progress = (profile.onboardingProgress.coerceIn(0,100) / 100f)
                        LinearProgressIndicator(progress = progress, modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(20.dp)),
                            color = primaryAccent
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "¡Estás avanzando muy bien! Sigue así 💙", color = if (isDark) Color(0xFF9FB9CA) else Color(0xFF557083), fontSize = 13.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Theme toggle
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(cardBg)
                        .padding(12.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(text = "Tema de la app", color = if (isDark) Color(0xFFE7F6FF) else Color(0xFF04233A), fontSize = 14.sp)
                            Text(text = "Claro u oscuro", color = if (isDark) Color(0xFF9FB9CA) else Color(0xFF557083), fontSize = 12.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = if (isDark) "Oscuro" else "Claro", color = primaryAccent)
                            Spacer(modifier = Modifier.width(8.dp))
                            Switch(checked = isDark, onCheckedChange = { isDark = it })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Logout (Outlined style)
                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    border = BorderStroke(1.dp, primaryAccent),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = primaryAccent)
                ) {
                    Text(text = "\u27A1  Cerrar sesión")
                }
            }
        }

        // Email Modal
        if (showEmailModal) {
            AlertDialog(
                onDismissRequest = { showEmailModal = false },
                confirmButton = {
                    TextButton(onClick = {
                        scope.launch {
                            showConfirmation = true
                            delay(2000)
                            showConfirmation = false
                            showEmailModal = false
                            emailMessage = "Hola María, tengo una consulta sobre mi proceso de onboarding…"
                        }
                    }) {
                        Text(text = "Enviar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEmailModal = false }) { Text(text = "Cancelar") }
                },
                title = {
                    Text(text = "Enviar correo al supervisor")
                },
                text = {
                    Column {
                        Text(text = "Para: maria.garcia@tcs.com", color = if (isDark) Color(0xFF9FB9CA) else Color(0xFF557083), fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = emailMessage, onValueChange = { emailMessage = it }, modifier = Modifier.fillMaxWidth(), maxLines = 6)
                        Spacer(modifier = Modifier.height(8.dp))
                        AnimatedVisibility(visible = showConfirmation) {
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .background(Brush.horizontalGradient(listOf(Color(0xFF00B87C), Color(0xFF00E5A0))))
                                .padding(10.dp)
                            ) {
                                Text(text = "✅ Correo enviado correctamente.", color = Color.White)
                            }
                        }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 380, heightDp = 800)
@Composable
fun PerfilScreenPreview() {
    PerfilScreen(onLogout = {})
}
