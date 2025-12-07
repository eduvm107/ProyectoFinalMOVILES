package com.example.chatbot_diseo.presentation.recursos.componentes

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbot_diseo.data.api.TokenHolder
import com.example.chatbot_diseo.data.remote.model.Documento
import com.example.chatbot_diseo.presentation.favoritos.FavoritosViewModel
import com.example.chatbot_diseo.presentation.recursos.RecursosViewModel

/**
 * Lista de recursos con integración completa del ViewModel
 */
@Composable
fun ResourceListFromApi(
    modifier: Modifier = Modifier,
    selectedFilter: String = "Todos",
    recursosViewModel: RecursosViewModel = viewModel(),
    favoritosViewModel: FavoritosViewModel? = null
) {
    val context = LocalContext.current
    val recursos by recursosViewModel.recursosList.collectAsState()
    val isLoading by recursosViewModel.isLoading.collectAsState()
    val errorMessage by recursosViewModel.errorMessage.collectAsState()
    val mensajeFeedback by recursosViewModel.mensajeFeedback.collectAsState()
    val usuarioId = TokenHolder.usuarioId ?: ""

    // Mostrar Toast cuando hay mensaje de feedback
    LaunchedEffect(mensajeFeedback) {
        mensajeFeedback?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            recursosViewModel.limpiarMensaje()
        }
    }

    when {
        isLoading -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF007AFF))
                Spacer(modifier = Modifier.padding(8.dp))
                Text("Cargando recursos...", color = Color.Gray)
            }
        }
        errorMessage != null -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Error al cargar recursos", fontWeight = FontWeight.Bold)
                Text(errorMessage ?: "", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.padding(8.dp))
                Button(onClick = { recursosViewModel.recargarRecursos() }) {
                    Text("Reintentar")
                }
            }
        }
        recursos.isEmpty() -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No hay recursos disponibles")
            }
        }
        else -> {
            val filtered = recursos.filter { matchesFilter(it, selectedFilter) }

            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtered) { documento ->
                    // 🔍 LOG DEBUG: Verificar ID del documento antes de renderizar
                    Log.d("RECURSOS_DEBUG", "📋 Renderizando documento: id=${documento.id}, titulo=${documento.titulo}, favorito=${documento.favorito}")

                    ResourceCard(
                        documento = documento,
                        isFavorite = documento.favorito,
                        onToggleFavorito = {
                            // 🔍 LOG DEBUG: Verificar ID justo antes de llamar toggleFavorito
                            Log.d("RECURSOS_DEBUG", "💓 onClick corazón - ID: ${documento.id}, titulo: ${documento.titulo}")
                            Log.d("RECURSOS_DEBUG", "💓 onClick corazón - ID es null? ${documento.id == null}")
                            Log.d("RECURSOS_DEBUG", "💓 onClick corazón - ID es empty? ${documento.id?.isEmpty()}")

                            recursosViewModel.toggleFavorito(usuarioId, documento, favoritosViewModel)
                        }
                    )
                }
            }
        }
    }
}

/**
 * Card de recurso con botón de favorito conectado al ViewModel
 */
@Composable
fun ResourceCard(
    documento: Documento,
    isFavorite: Boolean = false,
    onToggleFavorito: () -> Unit = {}
) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFE3F2FD), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MenuBook,
                        contentDescription = "Resource Icon",
                        tint = Color(0xFF007AFF)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = documento.titulo,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    documento.descripcion?.let { desc ->
                        Text(
                            text = desc,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                // 🎯 BOTÓN DE FAVORITO CONECTADO AL VIEWMODEL
                IconButton(
                    onClick = onToggleFavorito,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                        tint = if (isFavorite) Color.Red else Color(0xFF007AFF)
                    )
                }
            }

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            Divider()

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.MenuBook,
                    contentDescription = "Tag Icon",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    documento.tags?.firstOrNull() ?: "Sin categoría",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        if (documento.url.isNotBlank()) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(documento.url))
                            context.startActivity(intent)
                        }
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
                ) {
                    Text("Ver enlace", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.Launch, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

private fun matchesFilter(doc: Documento, filter: String): Boolean {
    val text = ((doc.tags?.firstOrNull() ?: "") + " " + (doc.descripcion ?: "")).lowercase()

    return when (filter) {
        "Todos" -> true
        "Manuales" -> text.contains("manual")
        "Cursos" -> text.contains("curso")
        "Beneficios" -> text.contains("beneficio")
        else -> true
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F2F5)
@Composable
fun ResourceCardPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        ResourceCard(
            documento = Documento(
                id = "1",
                titulo = "Manual de bienvenida",
                descripcion = "Guía completa para nuevos colaboradores de TCS",
                url = "https://example.com",
                tags = listOf("Manual"),
                favorito = false
            )
        )
    }
}
