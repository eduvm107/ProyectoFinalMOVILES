package com.example.chatbot_diseo.presentation.notificaciones

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_diseo.data.model.MensajeAutomatico
import com.example.chatbot_diseo.data.repository.MensajesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale

class NotificacionesViewModel : ViewModel() {

    private val repository = MensajesRepository()

    // Datos originales de la BD
    private val _todasLasNotificaciones = MutableStateFlow<List<MensajeAutomatico>>(emptyList())

    // Filtro actual seleccionado
    private val _filtroSeleccionado = MutableStateFlow("Todos")
    val filtroSeleccionado: StateFlow<String> = _filtroSeleccionado

    // --- LISTAS DE DATOS PARA LOS FILTROS ---

    // 1. Lista Fija de Prioridades (Para los botones horizontales)
    val prioridades = listOf("Alta", "Media", "Baja")

    // 2. Lista Dinámica de Tipos (Para el menú desplegable 'v')
    // Saca los tipos de tu base de datos, quita duplicados y los pone bonitos
    val tiposDisponibles: StateFlow<List<String>> = _todasLasNotificaciones.map { lista ->
        lista.mapNotNull { it.tipo?.lowercase() } // Extrae el campo 'tipo'
            .filter { it !in listOf("alta", "media", "baja") } // Asegura que no se cuelen prioridades aquí
            .distinct()
            .map { it.replaceFirstChar { c -> c.uppercase() } } // Capitaliza (bienvenida -> Bienvenida)
            .sorted()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- LÓGICA DE FILTRADO ---
    val notificacionesVisibles: StateFlow<List<MensajeAutomatico>> = combine(
        _todasLasNotificaciones,
        _filtroSeleccionado
    ) { lista, filtro ->
        if (filtro == "Todos") {
            lista
        } else {
            // Filtramos si coincide con el TIPO o con la PRIORIDAD
            lista.filter { noti ->
                // Usamos ?. para evitar errores si el campo viene vacío
                val esTipo = noti.tipo?.equals(filtro, ignoreCase = true) == true
                val esPrioridad = noti.prioridad?.equals(filtro, ignoreCase = true) == true

                esTipo || esPrioridad
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        cargarNotificaciones()
    }

    private fun cargarNotificaciones() {
        viewModelScope.launch {
            try {
                val response = repository.obtenerNotificaciones()
                _todasLasNotificaciones.value = response
            } catch (e: Exception) {
                Log.e("NotiVM", "Error: ${e.message}")
            }
        }
    }

    fun seleccionarFiltro(nuevoFiltro: String) {
        _filtroSeleccionado.value = nuevoFiltro
    }
}