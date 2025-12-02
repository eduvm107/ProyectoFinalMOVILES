package com.example.chatbot_diseo.presentation.recursos.componentes

import android.content.Intent
import androidx.core.net.toUri
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
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance
import com.example.chatbot_diseo.data.remote.model.Documento
import com.example.chatbot_diseo.data.api.TokenHolder
import com.example.chatbot_diseo.presentation.favoritos.FavoritosViewModel

// Ahora DocumentoRecurso incluye id e isFavorite
data class DocumentoRecurso(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val tag: String,
    val url: String,
    var isFavorite: Boolean = false
)

@Composable
fun ResourceCard(
    titulo: String = "Manual de bienvenida",
    descripcion: String = "Guía completa para nuevos colaboradores de TCS",
    tag: String = "Manual",
    url: String = "",
    isFavorite: Boolean = false,
    onCardClick: () -> Unit = {},
    onFavoriteToggle: () -> Unit = {}
) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onCardClick)
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
                        text = titulo,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Text(
                        text = descripcion,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                IconButton(onClick = onFavoriteToggle, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color(0xFF007AFF)
                    )
                }
            }

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.MenuBook,
                    contentDescription = "Tag Icon",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(tag, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        if (url.isNotBlank()) {
                            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                            context.startActivity(intent)
                        }
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
                ) {
                    Text("Ver enlace", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Filled.Launch, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun ResourceListFromApi(
    modifier: Modifier = Modifier,
    selectedFilter: String = "Todos",
    favoritosViewModel: FavoritosViewModel? = null // opcional: si se pasa, usar toggle centralizado
) {
    var resources by remember { mutableStateOf<List<DocumentoRecurso>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val usuarioId = TokenHolder.usuarioId
            val documentos = fetchDocumentosRaw(usuarioId)

            resources = documentos.map { doc ->
                DocumentoRecurso(
                    id = doc.id ?: "",
                    titulo = doc.titulo,
                    descripcion = doc.descripcion ?: "",
                    tag = doc.tags?.firstOrNull() ?: "",
                    url = doc.url,
                    // Usar el flag inyectado por el backend (si está) o false por defecto
                    isFavorite = (doc.isFavorite ?: false)
                )
            }

            errorMessage = null
        } catch (e: Exception) {
            errorMessage = e.message ?: "Error desconocido"
        } finally {
            isLoading = false
        }
    }

    when {
        isLoading -> {
            Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Cargando recursos...")
            }
        }
        errorMessage != null -> {
            Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Error al cargar recursos")
                Text(errorMessage ?: "", fontSize = 12.sp, color = Color.Gray)
            }
        }
        resources.isEmpty() -> {
            Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                Text("No hay recursos disponibles")
            }
        }
        else -> {
            val filtered = resources.filter { matchesFilter(it, selectedFilter) }

            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtered) { doc ->
                    // Mantener estado local isFavorite para cada item
                    var isFav by remember { mutableStateOf(doc.isFavorite) }

                    ResourceCard(
                        titulo = doc.titulo,
                        descripcion = doc.descripcion,
                        tag = doc.tag,
                        url = doc.url,
                        isFavorite = isFav,
                        onCardClick = {},
                        onFavoriteToggle = {
                            // Optimistic update
                            isFav = !isFav

                            // Si hay un ViewModel de favoritos, usarlo (centraliza token y manejo de errores)
                            val usuarioId = TokenHolder.usuarioId
                            if (favoritosViewModel != null && !usuarioId.isNullOrBlank()) {
                                favoritosViewModel.toggleFavorito("documento", doc.id) { res ->
                                    if (!res.isSuccess) {
                                        // revertir estado
                                        isFav = !isFav
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

suspend fun fetchDocumentosRaw(usuarioId: String? = null): List<Documento> {
    return if (!usuarioId.isNullOrBlank()) {
        RetrofitInstance.documentosApi.getDocumentosPersonalizados(usuarioId)
    } else {
        RetrofitInstance.documentosApi.getAllDocumentos()
    }
}

private fun matchesFilter(doc: DocumentoRecurso, filter: String): Boolean {
    val text = (doc.tag + " " + doc.descripcion).lowercase()

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
        ResourceCard()
    }
}
