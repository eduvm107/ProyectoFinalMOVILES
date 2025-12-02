package com.example.chatbot_diseo.presentation.calendario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbot_diseo.data.remote.apiChatBot.ActividadRemota
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance
import com.example.chatbot_diseo.data.remote.model.Actividad.ActividadUI
import com.example.chatbot_diseo.presentation.calendario.componentes.HeaderCalendario
import com.example.chatbot_diseo.presentation.calendario.componentes.NotificacionCard
import com.example.chatbot_diseo.presentation.favoritos.FavoritosViewModel
import com.example.chatbot_diseo.data.model.RecursoFavorito

@Composable
fun PantallaCalendario(
    favoritosViewModel: FavoritosViewModel = viewModel()
) {
    var actividades by remember { mutableStateOf<List<ActividadUI>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedFilter by remember { mutableStateOf("Todas") }

    // Leer favoritos desde ViewModel para marcar actividades
    val favoritos by favoritosViewModel.favoritos.collectAsState(initial = emptyList())
    val favIds = favoritos.mapNotNull { it.id }.toSet()

    LaunchedEffect(Unit) {
        try {
            val remotas: List<ActividadRemota> = RetrofitInstance.actividadesApi.getAllActividades()
            actividades = remotas.map { rem ->
                ActividadUI(
                    id = rem.id,
                    titulo = rem.titulo,
                    fechaCorta = rem.fechaDeActividad.substringBefore("T"),
                    estado = rem.estado,
                    horaInicio = rem.horaInicio,
                    lugar = rem.lugar,
                    isFavorite = (rem.isFavorite ?: false) || favIds.contains(rem.id)
                )
            }
            errorMessage = null
        } catch (e: Exception) {
            errorMessage = e.message ?: "Error al cargar actividades"
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .padding(16.dp)
    ) {
        HeaderCalendario(
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it }
        )

        Spacer(modifier = androidx.compose.ui.Modifier.height(24.dp))

        when {
            isLoading -> {
                Text("Cargando actividades...")
            }
            errorMessage != null -> {
                Text("Error al cargar actividades")
                Text(errorMessage ?: "", color = Color.Gray, fontSize = 12.sp)
            }
            else -> {
                val filtradas = actividades.filter { matchesActividadFilter(it, selectedFilter) }
                LazyColumn(
                    modifier = androidx.compose.ui.Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filtradas) { actividad ->
                        NotificacionCard(actividad = actividad, favoritosViewModel = favoritosViewModel)
                    }
                }
            }
        }
    }
}

private fun ActividadRemota.toUI(): ActividadUI =
    ActividadUI(
        id = id,
        titulo = titulo,
        fechaCorta = fechaDeActividad.substringBefore("T"),
        estado = estado,
        horaInicio = horaInicio,
        lugar = lugar,
        isFavorite = false
    )

private fun matchesActividadFilter(actividad: ActividadUI, filtro: String): Boolean {
    val estado = actividad.estado.lowercase()

    return when (filtro) {
        "Todas" -> true
        "Pendientes" -> estado.contains("pendiente")
        "Completadas" -> estado.contains("complet")
        "Pr��ximas" -> !estado.contains("complet")
        else -> true
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaCalendarioPreview() {
    PantallaCalendario()
}
