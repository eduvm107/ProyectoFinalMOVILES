package com.example.chatbot_diseo.presentation.favoritos

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbot_diseo.data.api.TokenHolder
import com.example.chatbot_diseo.data.model.RecursoFavorito

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(
    onBack: () -> Unit,
    viewModel: FavoritosViewModel = viewModel()
) {
    val context = LocalContext.current
    val favoritos by viewModel.favoritos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val mensajeFeedback by viewModel.mensajeFeedback.collectAsState()
    val usuarioId = TokenHolder.usuarioId ?: ""

    // Mostrar Toast cuando hay mensaje de feedback
    LaunchedEffect(mensajeFeedback) {
        mensajeFeedback?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.limpiarMensaje()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Favoritos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    // Botón para recargar
                    IconButton(onClick = { viewModel.recargarFavoritos() }) {
                        Icon(Icons.Outlined.Refresh, contentDescription = "Recargar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->

        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when {
                // Estado de carga
                isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF007AFF))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Cargando favoritos...", color = Color.Gray)
                    }
                }

                // Estado de error
                errorMessage != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Outlined.ErrorOutline,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            errorMessage ?: "Error desconocido",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.recargarFavoritos() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF007AFF)
                            )
                        ) {
                            Icon(Icons.Outlined.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Reintentar")
                        }
                    }
                }

                // Lista vacía
                favoritos.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Aún no tienes favoritos guardados",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                }

                // 🎨 LISTA DE FAVORITOS CON DISEÑO DINÁMICO
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        items(favoritos) { recurso ->
                            // 🔑 CLAVE: Diseño condicional según el tipo
                            when (recurso.tipo.lowercase()) {
                                "documento" -> DocumentoFavoritoItem(
                                    recurso = recurso,
                                    onToggleFavorito = { viewModel.toggleFavorito(usuarioId, recurso) }
                                )
                                "actividad" -> ActividadFavoritoItem(
                                    recurso = recurso,
                                    onToggleFavorito = { viewModel.toggleFavorito(usuarioId, recurso) }
                                )
                                "chat" -> ChatFavoritoItem(
                                    recurso = recurso,
                                    onToggleFavorito = { viewModel.toggleFavorito(usuarioId, recurso) }
                                )
                                else -> RecursoGenericoItem(
                                    recurso = recurso,
                                    onToggleFavorito = { viewModel.toggleFavorito(usuarioId, recurso) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============== COMPONENTES PARA CADA TIPO DE RECURSO ==============

/**
 * Diseño específico para DOCUMENTOS
 */
@Composable
fun DocumentoFavoritoItem(
    recurso: RecursoFavorito,
    onToggleFavorito: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 16.dp)
            .clickable {
                // Abrir URL si existe
                recurso.url?.let { url ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                }
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de documento
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.MenuBook,
                    contentDescription = null,
                    tint = Color(0xFF007AFF),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recurso.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF2D2D2D)
                )
                recurso.descripcion?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                recurso.tags?.firstOrNull()?.let { tag ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "📑 $tag",
                        fontSize = 12.sp,
                        color = Color(0xFF007AFF)
                    )
                }
            }

            // Botón de corazón para desfavoritear
            IconButton(onClick = onToggleFavorito) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Quitar de favoritos",
                    tint = Color.Red
                )
            }
        }
    }
}

/**
 * Diseño específico para ACTIVIDADES
 */
@Composable
fun ActividadFavoritoItem(
    recurso: RecursoFavorito,
    onToggleFavorito: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de actividad
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFF3E0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Event,
                    contentDescription = null,
                    tint = Color(0xFFF9AB00),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recurso.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF2D2D2D)
                )
                recurso.descripcion?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    recurso.fechaInicio?.let {
                        Text(
                            text = "📅 $it",
                            fontSize = 12.sp,
                            color = Color(0xFFF9AB00)
                        )
                    }
                    recurso.modalidad?.let {
                        Text(
                            text = "📍 $it",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Botón de corazón para desfavoritear
            IconButton(onClick = onToggleFavorito) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Quitar de favoritos",
                    tint = Color.Red
                )
            }
        }
    }
}

/**
 * Diseño específico para CHATS
 */
@Composable
fun ChatFavoritoItem(
    recurso: RecursoFavorito,
    onToggleFavorito: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de chat
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF3E5F5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.ChatBubbleOutline,
                    contentDescription = null,
                    tint = Color(0xFFE91E63),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recurso.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF2D2D2D)
                )
                recurso.descripcion?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                recurso.fecha?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "💬 $it",
                        fontSize = 12.sp,
                        color = Color(0xFFE91E63)
                    )
                }
            }

            // Botón de corazón para desfavoritear
            IconButton(onClick = onToggleFavorito) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Quitar de favoritos",
                    tint = Color.Red
                )
            }
        }
    }
}

/**
 * Diseño genérico para recursos no reconocidos
 */
@Composable
fun RecursoGenericoItem(
    recurso: RecursoFavorito,
    onToggleFavorito: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEEEEEE)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Star,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recurso.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF2D2D2D)
                )
                Text(
                    text = "Tipo: ${recurso.tipo}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // Botón de corazón para desfavoritear
            IconButton(onClick = onToggleFavorito) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Quitar de favoritos",
                    tint = Color.Red
                )
            }
        }
    }
}