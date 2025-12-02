package com.example.chatbot_diseo.presentation.recursos.componentes

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material3.Divider
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

data class DocumentoRecurso(
    val titulo: String,
    val descripcion: String,
    val tag: String,
    val url: String
)

@Composable
fun ResourceCard(
    titulo: String = "Manual de bienvenida",
    descripcion: String = "Guía completa para nuevos colaboradores de TCS",
    tag: String = "Manual",
    url: String = "",
    onCardClick: () -> Unit = {},
) {
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }

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

                IconButton(onClick = { isFavorite = !isFavorite }, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
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
                Text(tag, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        if (url.isNotBlank()) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
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

@Composable
fun ResourceListFromApi(
    modifier: Modifier = Modifier,
    selectedFilter: String = "Todos"
) {
    var resources by remember { mutableStateOf<List<DocumentoRecurso>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            println("🔄 Cargando recursos desde API...")
            resources = fetchDocumentos()
            println("✅ Recursos cargados: ${resources.size}")
            errorMessage = null
        } catch (e: Exception) {
            println("❌ Error cargando recursos: ${e.message}")
            e.printStackTrace()
            errorMessage = "Error: ${e.message ?: "Desconocido"}\nTipo: ${e::class.simpleName}"
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
                    ResourceCard(
                        titulo = doc.titulo,
                        descripcion = doc.descripcion,
                        tag = doc.tag,
                        url = doc.url
                    )
                }
            }
        }
    }
}

suspend fun fetchDocumentos(): List<DocumentoRecurso> {
    val documentos: List<Documento> = RetrofitInstance.documentosApi.getAllDocumentos()

    return documentos.map { doc ->
        DocumentoRecurso(
            titulo = doc.titulo,
            descripcion = doc.descripcion ?: "",
            tag = doc.tags?.firstOrNull() ?: "",
            url = doc.url
        )
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
