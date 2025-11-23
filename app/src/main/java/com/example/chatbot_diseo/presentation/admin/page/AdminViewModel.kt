package com.example.chatbot_diseo.presentation.admin.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_diseo.data.admin.*
import com.example.chatbot_diseo.data.common.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel del Panel de Administración
 * Maneja toda la lógica de negocio y comunicación con el backend
 */
class AdminPanelViewModel : ViewModel() {

    private val repository = AdminRepository()

    // ============== ESTADOS DE CARGA Y ERRORES ==============

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    // ============== CONTENIDOS ==============

    private val _contents = MutableStateFlow<List<ContentItem>>(emptyList())
    val contents: StateFlow<List<ContentItem>> = _contents

    init {
        // No cargar datos automáticamente para evitar crash
        // Los datos se cargarán cuando el usuario abra el panel de administración
    }

    /**
     * Carga todos los datos al iniciar el ViewModel
     */
    fun loadAllData() {
        loadContents()
        loadActivities()
        loadResources()
        loadMetrics()
    }

    /**
     * Cargar contenidos desde el backend
     */
    fun loadContents() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.getContents()) {
                is Result.Success -> {
                    _contents.value = result.data
                }
                is Result.Error -> {
                    _errorMessage.value = "Error al cargar contenidos: ${result.message}"
                }
                is Result.Loading -> { /* No action needed */ }
            }

            _isLoading.value = false
        }
    }

    fun addContent(title: String, type: String, description: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.addContent(title, type, description)) {
                is Result.Success -> {
                    _successMessage.value = "Contenido creado exitosamente"
                    loadContents() // Recargar la lista
                }
                is Result.Error -> {
                    _errorMessage.value = "Error al crear contenido: ${result.message}"
                }
                is Result.Loading -> { /* No action needed */ }
            }

            _isLoading.value = false
        }
    }

    fun updateContent(id: String, title: String, type: String, description: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.updateContent(id, title, type, description)) {
                is Result.Success -> {
                    _successMessage.value = "Contenido actualizado exitosamente"
                    loadContents()
                }
                is Result.Error -> {
                    _errorMessage.value = "Error al actualizar contenido: ${result.message}"
                }
                is Result.Loading -> { /* No action needed */ }
            }

            _isLoading.value = false
        }
    }

    fun deleteContent(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.deleteContent(id)) {
                is Result.Success -> {
                    _successMessage.value = "Contenido eliminado exitosamente"
                    loadContents()
                }
                is Result.Error -> {
                    _errorMessage.value = "Error al eliminar contenido: ${result.message}"
                }
                is Result.Loading -> { /* No action needed */ }
            }

            _isLoading.value = false
        }
    }

    // ============== ACTIVIDADES ==============

    private val _activities = MutableStateFlow<List<ActivityItem>>(emptyList())
    val activities: StateFlow<List<ActivityItem>> = _activities

    fun loadActivities() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.getActivities()) {
                is Result.Success -> {
                    _activities.value = result.data
                }
                is Result.Error -> {
                    _errorMessage.value = "Error al cargar actividades: ${result.message}"
                }
                is Result.Loading -> { /* No action needed */ }
            }

            _isLoading.value = false
        }
    }

    fun addActivity(title: String, date: String, modality: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.addActivity(title, date, modality)) {
                is Result.Success -> {
                    _successMessage.value = "Actividad creada exitosamente"
                    loadActivities()
                }
                is Result.Error -> {
                    _errorMessage.value = "Error al crear actividad: ${result.message}"
                }
                is Result.Loading -> { /* No action needed */ }
            }

            _isLoading.value = false
        }
    }

    fun updateActivity(id: String, title: String, date: String, modality: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.updateActivity(id, title, date, modality)) {
                is Result.Success -> {
                    _successMessage.value = "Actividad actualizada exitosamente"
                    loadActivities()
                }
                is Result.Error -> {
                    _errorMessage.value = "Error al actualizar actividad: ${result.message}"
                }
                is Result.Loading -> { /* No action needed */ }
            }

            _isLoading.value = false
        }
    }

    fun deleteActivity(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.deleteActivity(id)) {
                is Result.Success -> {
                    _successMessage.value = "Actividad eliminada exitosamente"
                    loadActivities()
                }
                is Result.Error -> {
                    _errorMessage.value = "Error al eliminar actividad: ${result.message}"
                }
                is Result.Loading -> { /* No action needed */ }
            }

            _isLoading.value = false
        }
    }

    // ============== RECURSOS ==============

    private val _resources = MutableStateFlow<List<ResourceItem>>(emptyList())
    val resources: StateFlow<List<ResourceItem>> = _resources

    fun loadResources() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.getResources()) {
                is Result.Success -> {
                    _resources.value = result.data
                }
                is Result.Error -> {
                    _errorMessage.value = "Error al cargar recursos: ${result.message}"
                }
                is Result.Loading -> { /* No action needed */ }
            }

            _isLoading.value = false
        }
    }

    fun addResource(title: String, category: String, url: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.addResource(title, category, url)) {
                is Result.Success -> {
                    _successMessage.value = "Recurso creado exitosamente"
                    loadResources()
                }
                is Result.Error -> {
                    _errorMessage.value = "Error al crear recurso: ${result.message}"
                }
                is Result.Loading -> { /* No action needed */ }
            }

            _isLoading.value = false
        }
    }

    fun updateResource(id: String, title: String, category: String, url: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.updateResource(id, title, category, url)) {
                is Result.Success -> {
                    _successMessage.value = "Recurso actualizado exitosamente"
                    loadResources()
                }
                is Result.Error -> {
                    _errorMessage.value = "Error al actualizar recurso: ${result.message}"
                }
                is Result.Loading -> { /* No action needed */ }
            }

            _isLoading.value = false
        }
    }

    fun deleteResource(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.deleteResource(id)) {
                is Result.Success -> {
                    _successMessage.value = "Recurso eliminado exitosamente"
                    loadResources()
                }
                is Result.Error -> {
                    _errorMessage.value = "Error al eliminar recurso: ${result.message}"
                }
                is Result.Loading -> { /* No action needed */ }
            }

            _isLoading.value = false
        }
    }

    // ============== MÉTRICAS ==============

    private val _metrics = MutableStateFlow<AdminStats?>(null)
    val metrics: StateFlow<AdminStats?> = _metrics

    fun loadMetrics() {
        // Métricas deshabilitadas - usar valores por defecto
        _metrics.value = AdminStats(
            totalContents = _contents.value.size,
            totalActivities = _activities.value.size,
            totalResources = _resources.value.size,
            completionRate = 0,
            averageSatisfaction = 0.0,
            averageTimeDays = 0
        )
    }

    // Métodos compatibles con la UI existente
    fun getTotalContents() = _contents.value.size
    fun getTotalActivities() = _activities.value.size
    fun getTotalResources() = _resources.value.size
    fun getCompletionRate() = 0
    fun getAverageSatisfaction() = 0.0
    fun getAverageTimeDays() = 0

    // ============== UTILIDADES ==============

    /**
     * Limpiar mensajes de error/éxito
     */
    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }

    /**
     * Refrescar todos los datos
     */
    fun refreshAll() {
        loadAllData()
    }
}
