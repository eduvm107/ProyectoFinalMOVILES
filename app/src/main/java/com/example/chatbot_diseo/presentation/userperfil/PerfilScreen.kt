@file:Suppress("DEPRECATION")

package com.example.chatbot_diseo.presentation.userperfil

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class UserProfile(
    val name: String,
    val email: String,
    val position: String,
    val department: String,
    val phone: String,
    val joinDate: String
)

@Composable
fun PerfilScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    sampleProfile: UserProfile? = null,
    isDarkTheme: Boolean = false,
    onThemeToggle: (Boolean) -> Unit = {},
    viewModel: PerfilViewModel = viewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()

    // Usar los datos del usuario real si están disponibles, sino usar el sampleProfile o datos por defecto
    val profile = sampleProfile ?: currentUser?.let {
        UserProfile(
            name = it.nombreCompleto ?: it.nombre ?: "Usuario",
            email = it.email ?: "",
            position = it.puesto ?: "Empleado",
            department = it.departamento ?: "General",
            phone = it.telefono ?: "",  // Teléfono del usuario desde el backend
            joinDate = ""  // No disponible desde el backend
        )
    } ?: UserProfile(
        name = "Usuario",
        email = "",
        position = "Empleado",
        department = "General",
        phone = "",
        joinDate = ""
    )

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    val changePasswordState by viewModel.changePasswordState.collectAsState()
    var showSuccessMessage by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Manejar estados de cambio de contraseña
    LaunchedEffect(changePasswordState) {
        when (val state = changePasswordState) {
            is ChangePasswordState.Success -> {
                showSuccessMessage = true
                viewModel.resetState()
                coroutineScope.launch {
                    delay(3000)
                    showSuccessMessage = false
                }
            }
            is ChangePasswordState.Error -> {
                showErrorMessage = state.message
                viewModel.resetState()
                coroutineScope.launch {
                    delay(3000)
                    showErrorMessage = null
                }
            }
            else -> {}
        }
    }

    // Usar colores del tema de Material3 para soportar modo oscuro
    val primaryColor = MaterialTheme.colorScheme.primary
    val backgroundColor = MaterialTheme.colorScheme.background
    val textPrimary = MaterialTheme.colorScheme.onBackground
    val textSecondary = MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        modifier = modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header azul institucional full-width, sin padding externo, con padding interno 24/20
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4A6B8A))
                    .padding(vertical = 24.dp, horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar circular más pequeña (foto o iniciales)
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        // Mostrar iniciales robustas: hasta 2 letras, en mayúsculas, con fallback
                        val initials = profile.name
                            .trim()
                            .split(Regex("\\s+"))
                            .mapNotNull { it.firstOrNull()?.toString() }
                            .filter { it.isNotBlank() }
                            .take(2)
                            .joinToString("")
                            .uppercase()
                            .ifEmpty { "U" }
                        Text(
                            text = initials,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF4A6B8A)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = profile.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )

                    Text(
                        text = profile.position,
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            // Información personal
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Información Personal",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textPrimary
                )

                ProfileInfoCard(
                    icon = Icons.Default.Email,
                    label = "Correo Electrónico",
                    value = profile.email,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )

                ProfileInfoCard(
                    icon = Icons.Default.Phone,
                    label = "Teléfono",
                    value = profile.phone.ifEmpty { "No disponible" },
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )

                ProfileInfoCard(
                    icon = Icons.Default.Work,
                    label = "Departamento",
                    value = profile.department,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Botones de acción
                Text(
                    text = "Acciones",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textPrimary
                )

                ActionButton(
                    icon = Icons.Default.Lock,
                    text = "Cambiar Contraseña",
                    onClick = { showChangePasswordDialog = true }
                )

                // Switch de tema oscuro/claro dentro de card
                ThemeToggleCard(
                    isDarkTheme = isDarkTheme,
                    onToggle = onThemeToggle,
                    primaryColor = primaryColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de cerrar sesión remodelado
                Button(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    // Icono disponible en la librería actual
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cerrar Sesión",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Mensajes de estado
    Box(modifier = Modifier.fillMaxSize()) {
        if (showSuccessMessage) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                containerColor = Color(0xFF10B981),
                contentColor = Color.White
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null
                    )
                    Text("Contraseña cambiada exitosamente")
                }
            }
        }

        showErrorMessage?.let { error ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                containerColor = Color(0xFFDC2626),
                contentColor = Color.White
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null
                    )
                    Text(error)
                }
            }
        }
    }

    // Diálogo de cambio de contraseña
    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            onDismiss = { showChangePasswordDialog = false },
            onConfirm = { currentPassword, newPassword ->
                viewModel.changePassword(currentPassword, newPassword)
                showChangePasswordDialog = false
            },
            primaryColor = primaryColor,
            textSecondary = textSecondary,
            isLoading = changePasswordState is ChangePasswordState.Loading
        )
    }

    // Diálogo de confirmación de logout
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = "Cerrar Sesión",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro que deseas cerrar sesión?",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFE53935)
                    )
                ) {
                    Text("Cerrar Sesión", fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = primaryColor
                    )
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ProfileInfoCard(
    icon: ImageVector,
    label: String,
    value: String,
    textPrimary: Color,
    textSecondary: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xFFF2F3F5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF6B6B6B),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = label,
                    fontSize = 13.sp,
                    color = textSecondary,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    fontSize = 14.sp,
                    color = textPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun ActionButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    // Usar card blanca con elevación suave para mantener consistencia visual
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xFFF2F3F5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF6B6B6B),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color(0xFFB0B0B0)
            )
        }
    }
}

@Composable
fun ThemeToggleCard(
    isDarkTheme: Boolean,
    onToggle: (Boolean) -> Unit,
    primaryColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFFF2F3F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = null,
                        tint = Color(0xFF6B6B6B),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Tema",
                        fontSize = 14.sp,
                        color = textPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isDarkTheme) "Modo Oscuro" else "Modo Claro",
                        fontSize = 13.sp,
                        color = textSecondary,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Switch(
                checked = isDarkTheme,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = primaryColor,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.LightGray
                )
            )
        }
    }
}

@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
    primaryColor: Color,
    textSecondary: Color,
    isLoading: Boolean = false
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = "Cambiar Contraseña",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (errorMessage.isNotEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFEE2E2)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = errorMessage,
                            color = Color(0xFFDC2626),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                // Contraseña actual
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = {
                        currentPassword = it
                        errorMessage = ""
                    },
                    label = { Text("Contraseña Actual") },
                    visualTransformation = if (currentPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { currentPasswordVisible = !currentPasswordVisible }) {
                            Icon(
                                imageVector = if (currentPasswordVisible)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = if (currentPasswordVisible)
                                    "Ocultar contraseña"
                                else
                                    "Mostrar contraseña"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        cursorColor = primaryColor
                    )
                )

                // Nueva contraseña
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        errorMessage = ""
                    },
                    label = { Text("Nueva Contraseña") },
                    visualTransformation = if (newPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                            Icon(
                                imageVector = if (newPasswordVisible)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = if (newPasswordVisible)
                                    "Ocultar contraseña"
                                else
                                    "Mostrar contraseña"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        cursorColor = primaryColor
                    )
                )

                // Confirmar contraseña
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        errorMessage = ""
                    },
                    label = { Text("Confirmar Nueva Contraseña") },
                    visualTransformation = if (confirmPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = if (confirmPasswordVisible)
                                    "Ocultar contraseña"
                                else
                                    "Mostrar contraseña"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        cursorColor = primaryColor
                    )
                )

                Text(
                    text = "La contraseña debe tener al menos 6 caracteres",
                    fontSize = 12.sp,
                    color = textSecondary
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    when {
                        currentPassword.isEmpty() -> {
                            errorMessage = "Ingresa tu contraseña actual"
                        }
                        newPassword.isEmpty() -> {
                            errorMessage = "Ingresa una nueva contraseña"
                        }
                        newPassword.length < 6 -> {
                            errorMessage = "La contraseña debe tener al menos 6 caracteres"
                        }
                        newPassword != confirmPassword -> {
                            errorMessage = "Las contraseñas no coinciden"
                        }
                        currentPassword == newPassword -> {
                            errorMessage = "La nueva contraseña debe ser diferente a la actual"
                        }
                        else -> {
                            onConfirm(currentPassword, newPassword)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor
                ),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Cambiar")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = textSecondary
                )
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PerfilScreenPreview() {
    PerfilScreen(onLogout = {})
}
