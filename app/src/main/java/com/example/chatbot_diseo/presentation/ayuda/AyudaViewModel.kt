package com.example.chatbot_diseo.presentation.ayuda

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_diseo.data.model.FAQ
import com.example.chatbot_diseo.data.remote.ApiAyudaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Estado UI para AyudaScreen
sealed class AyudaUiState {
    object Loading : AyudaUiState()
    data class Success(val faqs: List<FAQ>) : AyudaUiState()
    data class Error(val message: String) : AyudaUiState()
}

// Interfaz del repositorio (puedes implementar una versión real que llame a la API)
interface AyudaRepository {
    suspend fun getFaqs(): List<FAQ>
}

// Implementación por defecto (mock) para pruebas locales
@Suppress("unused") // Mantener el mock para pruebas locales / previews
class DefaultAyudaRepository : AyudaRepository {
    override suspend fun getFaqs(): List<FAQ> {
        // Simula una pequeña latencia
        delay(700)
        return listOf(
            FAQ(
                id = "1",
                pregunta = "¿Cómo subo mis documentos?",
                respuesta = "Sube tus documentos desde la sección 'Documentos' en tu perfil.",
                respuestaLarga = "Ingresa a tu perfil -> Documentos -> Presiona 'Agregar' y selecciona las imágenes o PDFs requeridos. Asegúrate de que las imágenes sean legibles.",
                categoria = "Onboarding",
                subcategoria = "Documentos"
            ),
            FAQ(
                id = "2",
                pregunta = "¿Cuánto tarda la verificación?",
                respuesta = "La verificación puede tardar hasta 3 días hábiles.",
                respuestaLarga = "RRHH revisa la documentación y si todo está correcto, el proceso suele completarse en un plazo de 1 a 3 días hábiles. Si hay observaciones te contactarán al correo registrado.",
                categoria = "Onboarding",
                subcategoria = null
            ),
            FAQ(
                id = "3",
                pregunta = "¿Puedo actualizar mi correo electrónico?",
                respuesta = "Sí, desde la sección de perfil puedes actualizar tu correo.",
                respuestaLarga = "Ve a Perfil > Editar > Correo electrónico. Después de guardar, recibirás un correo de verificación. Hasta verificarlo, algunos avisos llegarán al correo anterior.",
                categoria = "Cuenta",
                subcategoria = "Perfil"
            ),
            FAQ(
                id = "4",
                pregunta = "¿Cómo contacto a soporte técnico?",
                respuesta = "Puedes escribir a soporte@empresa.com o usar el chat en la app.",
                respuestaLarga = "El horario de atención es de lunes a viernes de 9:00 a 18:00. Para casos urgentes utiliza la opción 'Soporte' en el menú y adjunta capturas si es necesario.",
                categoria = "Soporte",
                subcategoria = null
            ),
            FAQ(
                id = "5",
                pregunta = "¿Qué formatos se aceptan para documentos?",
                respuesta = "Se aceptan JPG, PNG y PDF.",
                respuestaLarga = "Asegúrate de que el archivo no exceda los 5MB y que las fotos sean legibles. Para PDFs de varias páginas, sube el archivo completo.",
                categoria = "Documentos",
                subcategoria = null
            ),
            FAQ(
                id = "6",
                pregunta = "¿Cómo veo mis actividades pendientes?",
                respuesta = "En la sección 'Mis tareas' encontrarás las actividades asignadas.",
                respuestaLarga = "Desde Inicio > Mis tareas podrás filtrar por estado y fecha. Cada actividad incluye una breve descripción y el plazo de entrega.",
                categoria = "Onboarding",
                subcategoria = "Actividades"
            )
        )
    }
}

class AyudaViewModel(
    // Por defecto usa la implementación que llama a la API y ya limita a 5 preguntas
    private val repository: AyudaRepository = ApiAyudaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<AyudaUiState>(AyudaUiState.Loading)
    val uiState: StateFlow<AyudaUiState> = _uiState.asStateFlow()

    private val _expandedFaqId = MutableStateFlow<String?>(null)
    val expandedFaqId: StateFlow<String?> = _expandedFaqId.asStateFlow()

    init {
        loadFAQs()
    }

    fun loadFAQs() {
        viewModelScope.launch {
            _uiState.value = AyudaUiState.Loading
            try {
                val faqs = repository.getFaqs()
                _uiState.value = AyudaUiState.Success(faqs)
            } catch (e: Exception) {
                _uiState.value = AyudaUiState.Error(e.message ?: "Error al cargar FAQs")
            }
        }
    }

    fun toggleFaqExpansion(id: String?) {
        val newValue = if (_expandedFaqId.value == id) null else id
        Log.d("AyudaViewModel", "toggleFaqExpansion called. id=$id -> newValue=$newValue")
        _expandedFaqId.value = newValue
    }
}
