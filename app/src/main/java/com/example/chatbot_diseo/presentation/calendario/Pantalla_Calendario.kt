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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatbot_diseo.data.api.TokenHolder
import com.example.chatbot_diseo.data.remote.apiChatBot.ActividadRemota
import com.example.chatbot_diseo.data.remote.apiChatBot.RetrofitInstance
import com.example.chatbot_diseo.data.remote.model.Actividad.ActividadUI
import com.example.chatbot_diseo.presentation.calendario.componentes.HeaderCalendario
import com.example.chatbot_diseo.presentation.calendario.componentes.NotificacionCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun Pantalla_Calendario() {
    var actividades by remember { mutableStateOf<List<ActividadUI>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    // Filtro por defecto: "Todas" para mostrar todas las actividades al inicio
    var selectedFilter by remember { mutableStateOf("Todas") }

    LaunchedEffect(Unit) {
        try {
            val userId = TokenHolder.usuarioId
            if (userId.isNullOrBlank()) {
                errorMessage = "No se pudo obtener el usuario actual. Inicia sesión nuevamente."
                isLoading = false
                return@LaunchedEffect
            }

            // Log para debug
            println("DEBUG CALENDARIO - UserId actual: '$userId'")

            // Usar el endpoint específico para obtener actividades del usuario
            val remotas: List<ActividadRemota> =
                RetrofitInstance.actividadesApi.getByUsuario(userId)

            println("DEBUG CALENDARIO - Actividades del usuario recibidas: ${remotas.size}")

            // Mostrar las primeras actividades para debug
            remotas.take(5).forEachIndexed { index, actividad ->
                println("DEBUG CALENDARIO - Actividad[$index] UsuarioId: '${actividad.UsuarioId}' | Titulo: '${actividad.titulo}'")
            }

            actividades = remotas.map { it.toUI() }
            errorMessage = null
        } catch (e: Exception) {
            errorMessage = "Error: ${e.message ?: "Desconocido"}\nTipo: ${e::class.simpleName}"
            println("DEBUG CALENDARIO - Error: ${e.message}")
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        HeaderCalendario(
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

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

                // Log para debug de filtrado
                println("DEBUG FILTRO - Filtro seleccionado: '$selectedFilter'")
                println("DEBUG FILTRO - Total actividades: ${actividades.size}, Filtradas: ${filtradas.size}")

                if (filtradas.isEmpty()) {
                    val mensaje = when (selectedFilter) {
                        "Proximas" -> "No hay actividades para hoy o mañana."
                        "Pendientes" -> "No hay actividades pendientes."
                        "Completadas" -> "No hay actividades completadas."
                        else -> "No hay actividades."
                    }
                    Text(
                        text = mensaje,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filtradas) { actividad ->
                            NotificacionCard(
                                actividad = actividad,
                                isFromProximas = selectedFilter == "Proximas"
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun ActividadRemota.toUI(): ActividadUI =
    ActividadUI(
        id = id ?: "",
        titulo = titulo,
        fechaCorta = fechaDeActividad.substringBefore("T"),
        estado = estado,
        horaInicio = horaInicio,
        lugar = lugar
    )

private fun matchesActividadFilter(actividad: ActividadUI, filtro: String): Boolean {
    val estado = actividad.estado.lowercase()
    val hoy = LocalDate.now()
    val manana = hoy.plusDays(1)
    val fecha = parseFechaActividad(actividad.fechaCorta, hoy.year)

    return when (filtro) {
        "Todas" -> true

        // Completadas: estado contiene "complet"
        "Completadas" -> estado.contains("complet")

        // Pendientes: actividades que no están completadas
        // Si tiene fecha válida, debe ser hoy o futura
        // Si no tiene fecha válida, se incluye igual si no está completada
        "Pendientes" -> {
            val noCompletada = !estado.contains("complet")
            if (fecha == null) {
                // Si no se puede parsear la fecha, incluir si no está completada
                noCompletada
            } else {
                // Si tiene fecha válida, debe ser hoy o futura
                noCompletada && (fecha.isEqual(hoy) || fecha.isAfter(hoy))
            }
        }

        // Proximas: actividades de HOY o MAÑANA que no están completadas
        "Proximas" -> {
            !estado.contains("complet") &&
            fecha != null &&
            (fecha.isEqual(hoy) || fecha.isEqual(manana))
        }

        else -> true
    }
}

private fun parseFechaActividad(fechaStr: String, defaultYear: Int): LocalDate? {
    val formatos = listOf(
        DateTimeFormatter.ISO_LOCAL_DATE,          // ej: 2025-12-02
        DateTimeFormatter.ofPattern("d-M-yyyy"),   // ej: 2-12-2025
        DateTimeFormatter.ofPattern("d-M")         // ej: 2-12 (asume año actual)
    )

    formatos.forEachIndexed { index, formatter ->
        try {
            val fecha = LocalDate.parse(fechaStr, formatter)
            return when (index) {
                2 -> fecha.withYear(defaultYear)
                else -> fecha
            }
        } catch (_: DateTimeParseException) {
        }
    }

    return null
}

@Preview(showBackground = true)
@Composable
fun PantallaCalendarioPreview() {
    Pantalla_Calendario()
}
