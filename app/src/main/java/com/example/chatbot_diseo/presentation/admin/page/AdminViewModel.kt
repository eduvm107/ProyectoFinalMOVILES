package com.example.chatbot_diseo.presentation.admin.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_diseo.data.admin.ActivityItem
import com.example.chatbot_diseo.data.admin.AdminRepository
import com.example.chatbot_diseo.data.admin.AdminStats
import com.example.chatbot_diseo.data.admin.ResourceItem
import com.example.chatbot_diseo.data.common.Result
import com.example.chatbot_diseo.network.dto.request.ActivityRequest
import com.example.chatbot_diseo.network.dto.response.ActivityResponse
import com.example.chatbot_diseo.network.dto.response.UsuarioCompleto
import com.example.chatbot_diseo.network.api.AdminApiService
import com.example.chatbot_diseo.network.client.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel del Panel de Administración
 * Maneja toda la lógica de negocio y comunicación con el backend
 */
class AdminPanelViewModel : ViewModel() {

    private val repository = AdminRepository()
    private val apiService: AdminApiService = RetrofitClient.createService(AdminApiService::class.java)

    // ============== ESTADOS DE CARGA Y ERRORES ==============

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    // ============== USUARIO ACTUAL Y VALIDACIÓN DE ROL ==============

    private val _adminUser = MutableStateFlow<UsuarioCompleto?>(null)
    val adminUser: StateFlow<UsuarioCompleto?> = _adminUser

    private val _isAdmin = MutableStateFlow<Boolean>(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    // ============== CONTENIDOS ==============

    private val _contents = MutableStateFlow<List<com.example.chatbot_diseo.network.dto.response.ContentResponse>>(emptyList())
    val contents: StateFlow<List<com.example.chatbot_diseo.network.dto.response.ContentResponse>> = _contents

    // Nuevo: Contenido completo para edición
    private val _selectedContent = MutableStateFlow<com.example.chatbot_diseo.network.dto.response.ContentResponse?>(null)
    val selectedContent: StateFlow<com.example.chatbot_diseo.network.dto.response.ContentResponse?> = _selectedContent

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
                is Result.Success<*> -> {
                    _contents.value = result.data as List<com.example.chatbot_diseo.network.dto.response.ContentResponse>
                }
                is Result.Error -> {
                    _errorMessage.value = "Error al cargar contenidos: ${result.message}"
                }
                is Result.Loading -> { /* No action needed */ }
            }

            _isLoading.value = false
        }
    }

    fun addContent(titulo: String, contenido: String, tipo: String, diaGatillo: Int, prioridad: String, canal: List<String>, activo: Boolean, segmento: String, horaEnvio: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.addContent(titulo, contenido, tipo, diaGatillo, prioridad, canal, activo, segmento, horaEnvio)) {
                is Result.Success<*> -> {
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

    fun updateContent(id: String, titulo: String, contenido: String, tipo: String, diaGatillo: Int, prioridad: String, canal: List<String>, activo: Boolean, segmento: String, horaEnvio: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.updateContent(id, titulo, contenido, tipo, diaGatillo, prioridad, canal, activo, segmento, horaEnvio)) {
                is Result.Success<*> -> {
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
                is Result.Success<*> -> {
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

    /**
     * Obtener contenido completo por ID para edición
     */
    fun getContentById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.getContentById(id)) {
                is Result.Success<*> -> {
                    _selectedContent.value = result.data as com.example.chatbot_diseo.network.dto.response.ContentResponse
                }
                is Result.Error -> {
                    _errorMessage.value = "Error al cargar contenido: ${result.message}"
                }
                is Result.Loading -> { /* No action needed */ }
            }

            _isLoading.value = false
        }
    }

    /**
     * Limpiar contenido seleccionado
     */
    fun clearSelectedContent() {
        _selectedContent.value = null
    }

    // ============== ACTIVIDADES ==============

    private val _activities = MutableStateFlow<List<ActivityItem>>(emptyList())
    val activities: StateFlow<List<ActivityItem>> = _activities

    // Nueva: Actividad completa para edición
    private val _selectedActivity = MutableStateFlow<ActivityResponse?>(null)
    val selectedActivity: StateFlow<ActivityResponse?> = _selectedActivity

    fun loadActivities() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                when (val result = repository.getActivities()) {
                    is Result.Success<*> -> {
                        _activities.value = result.data as List<ActivityItem>
                        // NO mostrar error si la lista está vacía, solo actualizar la lista
                    }
                    is Result.Error -> {
                        _errorMessage.value = when {
                            result.message.contains("500") ->
                                "❌ Error 500 del servidor:\n${result.message}\n\n🔧 Verifica:\n• El servicio DocumentoService está registrado correctamente\n• La cadena de conexión a MongoDB es correcta\n• La base de datos 'ChatbotTCS' existe"
                            result.message.contains("404") ->
                                "❌ Error 404:\n\nEl endpoint /api/Actividad no fue encontrado.\n\n🔧 Verifica que el controlador ActividadController esté registrado."
                            result.message.contains("conexión") || result.message.contains("UnknownHost") ->
                                "❌ Error de red:\n\nNo se puede conectar al servidor en http://10.185.24.6:5288\n\n🔧 Verifica:\n• El backend está ejecutándose\n• El firewall permite conexiones\n• La IP es correcta"
                            else ->
                                "❌ Error: ${result.message}"
                        }
                    }
                    is Result.Loading -> { /* No action needed */ }
                }
            } catch (e: Exception) {
                _errorMessage.value = "❌ Error inesperado:\n${e.message}\n\nContacta al administrador."
            }

            _isLoading.value = false
        }
    }

    fun addActivity(title: String, date: String, modality: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.addActivity(title, date, modality)) {
                is Result.Success<*> -> {
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
                is Result.Success<*> -> {
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
                is Result.Success<*> -> {
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

    /**
     * Obtener actividad completa por ID para edición
     */
    fun getActivityById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.getActivityById(id)) {
                is Result.Success<*> -> {
                    _selectedActivity.value = result.data as ActivityResponse
                }
                is Result.Error -> {
                    _errorMessage.value = "Error al cargar actividad: ${result.message}"
                }
                is Result.Loading -> { /* No action needed */ }
            }

            _isLoading.value = false
        }
    }

    /**
     * Limpiar actividad seleccionada
     */
    fun clearSelectedActivity() {
        _selectedActivity.value = null
    }

    /**
     * Actualizar actividad completa con todos los campos
     */
    fun updateActivityComplete(id: String, activityRequest: ActivityRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.updateActivityComplete(id, activityRequest)) {
                is Result.Success<*> -> {
                    _successMessage.value = "Actividad actualizada exitosamente"
                    _selectedActivity.value = null
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

    // ============== RECURSOS ==============

    private val _resources = MutableStateFlow<List<ResourceItem>>(emptyList())
    val resources: StateFlow<List<ResourceItem>> = _resources

    fun loadResources() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                when (val result = repository.getResources()) {
                    is Result.Success<*> -> {
                        _resources.value = result.data as List<ResourceItem>
                        // NO mostrar error si la lista está vacía, solo actualizar la lista
                    }
                    is Result.Error -> {
                        _errorMessage.value = when {
                            result.message.contains("500") ->
                                "❌ Error 500 del servidor al cargar recursos:\n${result.message}\n\n🔧 Verifica:\n• DocumentoService está correctamente configurado\n• MongoDB está conectado (cadena: 'mongodb://localhost:27017')\n• La colección 'Documentos' existe en la base de datos"
                            result.message.contains("404") ->
                                "❌ Error 404:\n\nEl endpoint /api/Documento no fue encontrado.\n\n🔧 El controlador DocumentoController debe estar en la ruta correcta."
                            result.message.contains("conexión") || result.message.contains("UnknownHost") ->
                                "❌ Error de red:\n\nNo se puede conectar al servidor backend.\n\n🔧 Asegúrate de que el backend ASP.NET Core esté ejecutándose."
                            else ->
                                "❌ Error al cargar recursos:\n${result.message}\n\n🔧 Revisa los logs del backend para más detalles."
                        }
                    }
                    is Result.Loading -> { /* No action needed */ }
                }
            } catch (e: Exception) {
                _errorMessage.value = "❌ Error inesperado al cargar recursos:\n${e.message}\n\nContacta al administrador del sistema."
            }

            _isLoading.value = false
        }
    }

    fun addResource(title: String, category: String, url: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                when (val result = repository.addResource(title, category, url)) {
                    is Result.Success<*> -> {
                        _successMessage.value = "Recurso creado exitosamente"
                        loadResources()
                    }
                    is Result.Error -> {
                        _errorMessage.value = "Error al crear recurso: ${result.message}"
                    }
                    is Result.Loading -> { /* No action needed */ }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado al crear recurso: ${e.message}"
            }

            _isLoading.value = false
        }
    }

    fun updateResource(id: String, title: String, category: String, url: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                when (val result = repository.updateResource(id, title, category, url)) {
                    is Result.Success<*> -> {
                        _successMessage.value = "Recurso actualizado exitosamente"
                        loadResources()
                    }
                    is Result.Error -> {
                        _errorMessage.value = "Error al actualizar recurso: ${result.message}"
                    }
                    is Result.Loading -> { /* No action needed */ }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado al actualizar recurso: ${e.message}"
            }

            _isLoading.value = false
        }
    }

    fun deleteResource(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                when (val result = repository.deleteResource(id)) {
                    is Result.Success<*> -> {
                        _successMessage.value = "Recurso eliminado exitosamente"
                        loadResources()
                    }
                    is Result.Error -> {
                        _errorMessage.value = "Error al eliminar recurso: ${result.message}"
                    }
                    is Result.Loading -> { /* No action needed */ }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado al eliminar recurso: ${e.message}"
            }

            _isLoading.value = false
        }
    }

    // ============== MÉTRICAS ==============

    private val _metrics = MutableStateFlow<AdminStats?>(null)
    val metrics: StateFlow<AdminStats?> = _metrics

    /**
     * Cargar métricas desde el backend
     */
    fun loadMetrics() {
        viewModelScope.launch {
            try {
                when (val result = repository.getMetrics()) {
                    is Result.Success<*> -> {
                        _metrics.value = result.data as AdminStats
                    }
                    is Result.Error -> {
                        // Si falla, usar valores locales como fallback
                        _metrics.value = AdminStats(
                            totalContents = _contents.value.size,
                            totalActivities = _activities.value.size,
                            totalResources = _resources.value.size,
                            completionRate = 0,
                            averageSatisfaction = 0.0,
                            averageTimeDays = 0
                        )
                    }
                    is Result.Loading -> { /* No action needed */ }
                }
            } catch (e: Exception) {
                // Fallback silencioso - usar valores locales
                _metrics.value = AdminStats(
                    totalContents = _contents.value.size,
                    totalActivities = _activities.value.size,
                    totalResources = _resources.value.size,
                    completionRate = 0,
                    averageSatisfaction = 0.0,
                    averageTimeDays = 0
                )
            }
        }
    }

    // Métodos compatibles con la UI existente - ahora retornan StateFlow para actualización en tiempo real
    fun getTotalContents() = _contents.value.size
    fun getTotalActivities() = _activities.value.size
    fun getTotalResources() = _resources.value.size
    fun getCompletionRate() = _metrics.value?.completionRate ?: 0
    fun getAverageSatisfaction() = _metrics.value?.averageSatisfaction ?: 0.0
    fun getAverageTimeDays() = _metrics.value?.averageTimeDays ?: 0

    // ============== USUARIO ACTUAL Y VALIDACIÓN DE ROL ==============

    /**
     * Cargar usuario actual por email y validar si es administrador
     * @param email Email del usuario a buscar
     */
    fun loadUsuarioActual(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = apiService.getUsuarioByEmail(email)

                if (response.isSuccessful && response.body() != null) {
                    val usuario = response.body()!!
                    _adminUser.value = usuario

                    // Validar el rol del usuario
                    _isAdmin.value = usuario.rol.equals("admin", ignoreCase = true)

                    if (!_isAdmin.value) {
                        _errorMessage.value = "Acceso denegado: El usuario no tiene permisos de administrador"
                    }
                } else {
                    _adminUser.value = null
                    _isAdmin.value = false
                    _errorMessage.value = "Usuario no encontrado o sin permisos"
                }
            } catch (e: Exception) {
                _adminUser.value = null
                _isAdmin.value = false
                _errorMessage.value = "Error al cargar usuario: ${e.message}"
            }

            _isLoading.value = false
        }
    }

    /**
     * Limpiar datos del usuario actual
     */
    fun clearUsuarioActual() {
        _adminUser.value = null
        _isAdmin.value = false
    }

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
