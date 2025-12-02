package com.example.chatbot_diseo.presentation.ayuda

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbot_diseo.data.model.FAQ

private val AccentBlue = Color(0xFF1A73E8)
private val BackgroundLight = Color(0xFFF8F9FA)
private val HeaderBackground = Color.White
private val HeaderTextColor = Color(0xFF1A1A1A)

/**
 * Pantalla de Ayuda y Guía
 * Incluye Timeline del Onboarding y FAQs dinámicas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyudaScreen(
    onBack: () -> Unit,
    viewModel: AyudaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val expandedFaqId by viewModel.expandedFaqId.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ayuda y Guía", color = HeaderTextColor) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = HeaderTextColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = HeaderBackground,
                    titleContentColor = HeaderTextColor,
                    navigationIconContentColor = HeaderTextColor
                )
            )
        },
        containerColor = BackgroundLight
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Espacio superior
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Sección: Timeline del Onboarding
            item {
                OnboardingTimelineSection()
            }

            // Sección: Preguntas Frecuentes
            item {
                Text(
                    text = "Preguntas Frecuentes",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = HeaderTextColor,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            // Estado de carga de FAQs
            when (uiState) {
                is AyudaUiState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = AccentBlue)
                        }
                    }
                }
                is AyudaUiState.Error -> {
                    item {
                        ErrorCard(
                            message = (uiState as AyudaUiState.Error).message,
                            onRetry = { viewModel.loadFAQs() }
                        )
                    }
                }
                is AyudaUiState.Success -> {
                    val faqs = (uiState as AyudaUiState.Success).faqs
                    items(faqs) { faq ->
                        FAQCard(
                            faq = faq,
                            isExpanded = expandedFaqId == faq.id,
                            onToggle = { viewModel.toggleFaqExpansion(faq.id) }
                        )
                    }
                }
            }

            // Espacio inferior
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

/**
 * Sección de Guías Rápidas TCS (sustituye el antiguo Onboarding)
 */
@Composable
fun OnboardingTimelineSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Guías Rápidas TCS",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = HeaderTextColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Lista de guías rápidas (hardcoded)
        val quickGuides = listOf(
            OnboardingStep(
                title = "Cómo usar el chatbot",
                description = "Aprende cómo interactuar con tu asistente virtual.",
                icon = Icons.Default.SmartToy,
                bgColor = Color(0xFFE3F2FD) ,// azul suave
                iconTint = AccentBlue,
            ),
            OnboardingStep(
                title = "Cómo enviar documentos",
                description = "Instrucciones para subir DNI, certificados y otros.",
                icon = Icons.Default.Description,
                bgColor = Color(0xFFE8F5E9), // verde suave
                iconTint = Color(0xFF43A047)
            ),
            OnboardingStep(
                title = "Cómo ver tus actividades pendientes",
                description = "Consulta tu progreso en el proceso de onboarding.",
                icon = Icons.Default.ListAlt,
                bgColor = Color(0xFFFFF4E0), // naranja suave
                iconTint = Color(0xFFFFA726)
            ),
            OnboardingStep(
                title = "Cómo contactar a RRHH",
                description = "Canales oficiales y horarios de atención.",
                icon = Icons.Default.Email,
                bgColor = Color(0xFFFDE2E2) ,// rojo suave
                iconTint = Color(0xFFE53935)
            )
        )

        quickGuides.forEachIndexed { index, step ->
            TimelineItem(
                step = step,
                isLast = index == quickGuides.lastIndex
            )
        }
    }
}

/**
 * Componente individual de Timeline adaptado a Guías Rápidas
 */
@Composable
fun TimelineItem(
    step: OnboardingStep,
    isLast: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Columna de la línea de tiempo (círculo + línea)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(56.dp)
        ) {
            // Círculo con icono usando el color de fondo solicitado
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(step.bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = step.icon,
                    contentDescription = null,
                    tint = step.iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Línea vertical (si no es el último)
            if (!isLast) {
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(56.dp)
                        .background(Color(0xFFE0E0E0))
                )
            }
        }

        // Contenido de la guía
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp)
        ) {
            Text(
                text = step.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = HeaderTextColor
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = step.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666),
                lineHeight = 20.sp
            )
        }
    }
}

/**
 * Tarjeta de FAQ con efecto acordeón - Estilo similar a notificaciones
 */
@Composable
fun FAQCard(
    faq: FAQ,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    // Animación de rotación de la flecha (0° -> 180°)
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "Arrow Rotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onToggle() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Icono de pregunta ahora usa AccentBlue
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AccentBlue.copy(alpha = 0.15f)), // AccentBlue suave
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "❓",
                    fontSize = 24.sp,
                    color = AccentBlue
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Contenido de la pregunta y respuesta
            Column(modifier = Modifier.weight(1f)) {
                // Pregunta (siempre visible)
                Text(
                    text = faq.pregunta,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    ),
                    color = Color(0xFF2D2D2D),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Respuesta (solo visible cuando está expandido)
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = faq.respuestaLarga ?: faq.respuesta,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5F6368),
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )

                        // Información adicional si existe
                        if (faq.categoria.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = faq.categoria,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = AccentBlue // sustituido morado por AccentBlue
                                )
                                if (faq.subcategoria != null) {
                                    Text(text = " • ", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                    Text(
                                        text = faq.subcategoria,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Icono de expansión con rotación animada
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ExpandMore, // Solo usamos ExpandMore
                contentDescription = if (isExpanded) "Colapsar" else "Expandir",
                tint = AccentBlue, // AccentBlue para la flecha
                modifier = Modifier
                    .size(24.dp)
                    .rotate(rotationAngle) // Rotación animada
            )
        }
    }
}

/**
 * Tarjeta de error con botón de reintento
 */
@Composable
fun ErrorCard(
    message: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Error",
                tint = Color(0xFFD32F2F),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFD32F2F)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = AccentBlue, contentColor = Color.White)
            ) {
                Text("Reintentar")
            }
        }
    }
}

/**
 * Data class para las guías/steps del onboarding (ahora incluye bgColor y iconTint)
 */
data class OnboardingStep(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val bgColor: Color,
    val iconTint: Color = AccentBlue
)
