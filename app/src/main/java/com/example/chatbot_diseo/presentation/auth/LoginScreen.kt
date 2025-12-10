package com.example.chatbot_diseo.presentation.auth

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbot_diseo.R
import com.example.chatbot_diseo.data.api.TokenHolder
import com.example.chatbot_diseo.presentation.ui.login.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLogin: (role: String) -> Unit,
    onForgotPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    // ViewModel
    val vm: LoginViewModel = viewModel(modelClass = LoginViewModel::class.java)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var navigated by remember { mutableStateOf(false) }

    // Limpiar estado previo
    LaunchedEffect(Unit) {
        vm.clearState()
    }

    val loginResult by vm.state.collectAsState(initial = null)
    val context = LocalContext.current

    // Manejo de resultado de login
    LaunchedEffect(loginResult) {
        loginResult?.let { result ->
            isLoading = false
            result.onSuccess { resp ->
                val hasToken = !resp.token.isNullOrBlank()
                if (hasToken) {
                    val savedToken = TokenHolder.token
                    if (!savedToken.isNullOrBlank()) {
                        if (!navigated) {
                            val role = resp.usuario?.rol ?: "employee"
                            Toast.makeText(context, "Login OK - role: $role", Toast.LENGTH_SHORT).show()
                            navigated = true
                            onLogin(role)
                        }
                    } else {
                        Toast.makeText(context, "Token no guardado localmente", Toast.LENGTH_SHORT).show()
                        errorMessage = "No se pudo almacenar el token"
                    }
                } else {
                    Toast.makeText(context, resp.message ?: "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                    errorMessage = resp.message ?: "Credenciales inválidas"
                }
            }
            result.onFailure { ex ->
                Toast.makeText(context, ex.message ?: "Error en login", Toast.LENGTH_SHORT).show()
                errorMessage = ex.message ?: "Credenciales inválidas"
            }
        }
    }

    // Animación de fondo
    val transition = rememberInfiniteTransition()
    val offsetAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 20000, easing = LinearEasing)
        )
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val accent = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
            val accent2 = MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(accent, Color.Transparent),
                            center = Offset.Infinite,
                            radius = with(LocalDensity.current) { 300.dp.toPx() }
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(accent2, Color.Transparent),
                            center = Offset(0f, Float.POSITIVE_INFINITY),
                            radius = with(LocalDensity.current) { 280.dp.toPx() }
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Placeholder top (switch de tema, etc.)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Spacer(modifier = Modifier.size(4.dp))
                }

                // Contenido central
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.offset(y = (-24).dp)
                ) {
                    // Logo animado principal CON TU IMAGEN
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .rotate(offsetAnim)
                            .clip(RoundedCornerShape(20.dp))
                            .shadow(elevation = 6.dp, shape = RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_tata),
                            contentDescription = "Logo TCS Assistant",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "TATA Assistant",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Tu Agente de Onboarding Inteligente",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Card contenedor del formulario
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Iniciar Sesión",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Ingresa tus credenciales para continuar",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Correo electrónico") },
                            placeholder = { Text("tu.email@tcs.com") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña") },
                            placeholder = { Text("********") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                                    )
                                }
                            },
                            singleLine = true,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = {
                                val image = if (passwordVisible)
                                    Icons.Filled.Visibility
                                else
                                    Icons.Filled.VisibilityOff

                                val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(imageVector = image, contentDescription = description)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "¿Olvidaste tu contraseña?",
                                modifier = Modifier
                                    .clickable { onForgotPassword() }
                                    .padding(4.dp),
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Error
                        if (errorMessage != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = errorMessage!!,
                                color = Color(0xFFB00020),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }

                        Button(
                            onClick = {
                                errorMessage = null
                                if (email.isBlank() || password.isBlank()) {
                                    errorMessage = "Ingresa email y contraseña"
                                    return@Button
                                }
                                isLoading = true
                                vm.login(email.trim(), password)
                            },
                            enabled = !isLoading && !navigated,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 1.dp
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text("Accediendo...")
                            } else {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text("Iniciar sesión")
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.4f))
                                .padding(12.dp)
                        ) {
                            // espacio para mensajes/info adicional
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Footer
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "© 2025 Tata Consultancy Services",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "Innovación Digital · Transformación Global",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(onLogin = {}, onForgotPassword = {})
    }
}
