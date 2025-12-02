package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.data.admin.ResourceItem
import com.example.chatbot_diseo.ui.theme.TcsBlue
import com.example.chatbot_diseo.ui.theme.TcsGrayText
import com.example.chatbot_diseo.ui.theme.TcsRed
import com.example.chatbot_diseo.ui.theme.TcsWhite
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminResourceDialog(
    titleDialog: String,
    initialItem: ResourceItem?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String, String, List<String>, String, String?, String, String, String, Boolean, String?, String?, String, Boolean) -> Unit
) {
    // Estados para todos los campos - inicializados con valores existentes o por defecto
    var titulo by remember { mutableStateOf(initialItem?.title ?: "") }
    var descripcion by remember { mutableStateOf("") }
    var url by remember { mutableStateOf(initialItem?.url ?: "") }
    var categoria by remember { mutableStateOf(initialItem?.category ?: "") }
    var subcategoria by remember { mutableStateOf("") }
    var icono by remember { mutableStateOf("📄") }
    var tamaño by remember { mutableStateOf<String?>(null) }
    var idioma by remember { mutableStateOf("Español") }
    var version by remember { mutableStateOf("1.0") }
    var publico by remember { mutableStateOf("Nuevos empleados") }
    var obligatorio by remember { mutableStateOf(false) }
    var autor by remember { mutableStateOf("Administrador") }
    var favorito by remember { mutableStateOf(false) }

    // Tags - lista editable usando mutableStateListOf para manejo estable en Compose
    val tags = remember { mutableStateListOf<String>() }
    var nuevoTag by remember { mutableStateOf("") }

    // Dropdowns
    var tipoExpanded by remember { mutableStateOf(false) }
    var tipo by remember { mutableStateOf("PDF") }
    val tipoOpciones = listOf("PDF", "Video", "Documento", "Presentación", "Imagen", "Audio", "Enlace", "Otro")

    var categoriaExpanded by remember { mutableStateOf(false) }
    val categoriaOpciones = listOf("Bienvenida", "Políticas", "Beneficios", "Herramientas", "Capacitación", "Procedimientos", "Formularios", "Otros")

    // Fechas
    var fechaPublicacion by remember {
        mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
    }
    var fechaActualizacion by remember {
        mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
    }
    var showFechaPublicacionPicker by remember { mutableStateOf(false) }
    var showFechaActualizacionPicker by remember { mutableStateOf(false) }

    // Validación
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var tituloError by remember { mutableStateOf(false) }
    var descripcionError by remember { mutableStateOf(false) }
    var urlError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        title = {
            Column {
                Text(titleDialog, style = MaterialTheme.typography.headlineSmall)
                Text("Completa la información del documento", color = TcsGrayText)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Mensaje de error general
                if (showError) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = errorMessage,
                            color = TcsRed,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                // 1. Título *
                OutlinedTextField(
                    value = titulo,
                    onValueChange = {
                        titulo = it
                        tituloError = it.isBlank()
                    },
                    label = { Text("Título del recurso *") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = tituloError,
                    supportingText = if (tituloError) { { Text("Campo obligatorio", color = TcsRed) } } else null
                )

                // 2. Descripción *
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = {
                        descripcion = it
                        descripcionError = it.isBlank()
                    },
                    label = { Text("Descripción *") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4,
                    isError = descripcionError,
                    supportingText = if (descripcionError) { { Text("Campo obligatorio", color = TcsRed) } } else null
                )

                // 3. URL *
                OutlinedTextField(
                    value = url,
                    onValueChange = {
                        url = it
                        urlError = it.isBlank() || !it.matches(Regex("^(https?://|www\\.).*"))
                    },
                    label = { Text("URL *") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("https://ejemplo.com/documento.pdf") },
                    isError = urlError,
                    supportingText = if (urlError) { { Text("Debe ser una URL válida", color = TcsRed) } } else null
                )

                // 4. Tipo - Dropdown
                ExposedDropdownMenuBox(
                    expanded = tipoExpanded,
                    onExpandedChange = { tipoExpanded = !tipoExpanded }
                ) {
                    OutlinedTextField(
                        value = tipo,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de documento *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tipoExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = tipoExpanded,
                        onDismissRequest = { tipoExpanded = false }
                    ) {
                        tipoOpciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    tipo = opcion
                                    tipoExpanded = false
                                }
                            )
                        }
                    }
                }

                // 5. Categoría - Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoriaExpanded,
                    onExpandedChange = { categoriaExpanded = !categoriaExpanded }
                ) {
                    OutlinedTextField(
                        value = categoria,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoriaExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = categoriaExpanded,
                        onDismissRequest = { categoriaExpanded = false }
                    ) {
                        categoriaOpciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    categoria = opcion
                                    categoriaExpanded = false
                                }
                            )
                        }
                    }
                }

                // 6. Subcategoría
                OutlinedTextField(
                    value = subcategoria,
                    onValueChange = { subcategoria = it },
                    label = { Text("Subcategoría") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 7. Tags - Lista editable
                Text("Etiquetas (Tags)", style = MaterialTheme.typography.titleSmall, color = Color(0xFF0A0A0A))

                // Mostrar tags existentes con eliminación segura
                if (tags.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tags.forEach { tag ->
                            key(tag) {
                                AssistChip(
                                    onClick = {
                                        // Eliminación segura por valor, no por índice
                                        tags.remove(tag)
                                    },
                                    label = { Text(tag) },
                                    trailingIcon = {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", modifier = Modifier.size(16.dp))
                                    }
                                )
                            }
                        }
                    }
                }

                // Campo para agregar nuevo tag
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = nuevoTag,
                        onValueChange = { nuevoTag = it },
                        label = { Text("Agregar etiqueta") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    IconButton(
                        onClick = {
                            if (nuevoTag.isNotBlank()) {
                                tags.add(nuevoTag)
                                nuevoTag = ""
                            }
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar", tint = TcsBlue)
                    }
                }

                // 8. Icono
                OutlinedTextField(
                    value = icono,
                    onValueChange = { icono = it },
                    label = { Text("Icono (emoji)") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("📄 📊 📝 🎥 🖼️") }
                )

                // 9. Tamaño
                OutlinedTextField(
                    value = tamaño ?: "",
                    onValueChange = { tamaño = it.ifBlank { null } },
                    label = { Text("Tamaño del archivo") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Ejemplo: 2.5 MB") }
                )

                // 10. Idioma
                OutlinedTextField(
                    value = idioma,
                    onValueChange = { idioma = it },
                    label = { Text("Idioma") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 11. Versión
                OutlinedTextField(
                    value = version,
                    onValueChange = { version = it },
                    label = { Text("Versión") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Ejemplo: 1.0, 2.1") }
                )

                // 12. Público objetivo
                OutlinedTextField(
                    value = publico,
                    onValueChange = { publico = it },
                    label = { Text("Público objetivo") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Ejemplo: Nuevos empleados, Gerentes") }
                )

                // 13. Obligatorio - Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Documento Obligatorio", style = MaterialTheme.typography.bodyLarge)
                    Switch(
                        checked = obligatorio,
                        onCheckedChange = { obligatorio = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TcsWhite,
                            checkedTrackColor = TcsBlue,
                            uncheckedThumbColor = TcsWhite,
                            uncheckedTrackColor = TcsGrayText
                        )
                    )
                }

                // 14. Fecha de Publicación
                Button(
                    onClick = { showFechaPublicacionPicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Fecha de Publicación: $fechaPublicacion", color = TcsBlue)
                }

                // 15. Fecha de Actualización
                Button(
                    onClick = { showFechaActualizacionPicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Fecha de Actualización: $fechaActualizacion", color = TcsBlue)
                }

                // 16. Autor
                OutlinedTextField(
                    value = autor,
                    onValueChange = { autor = it },
                    label = { Text("Autor") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 17. Favorito - Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Marcar como Favorito", style = MaterialTheme.typography.bodyLarge)
                    Switch(
                        checked = favorito,
                        onCheckedChange = { favorito = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TcsWhite,
                            checkedTrackColor = TcsBlue,
                            uncheckedThumbColor = TcsWhite,
                            uncheckedTrackColor = TcsGrayText
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Validación completa
                    tituloError = titulo.isBlank()
                    descripcionError = descripcion.isBlank()
                    urlError = url.isBlank() || !url.matches(Regex("^(https?://|www\\.).*"))

                    if (tituloError || descripcionError || urlError) {
                        showError = true
                        errorMessage = "Por favor corrige los errores en los campos marcados"
                        return@Button
                    }

                    try {
                        onConfirm(
                            titulo,
                            descripcion,
                            url,
                            tipo,
                            categoria,
                            subcategoria,
                            tags.toList(),
                            icono,
                            tamaño,
                            idioma,
                            version,
                            publico,
                            obligatorio,
                            fechaPublicacion,
                            fechaActualizacion,
                            autor,
                            favorito
                        )
                    } catch (e: Exception) {
                        showError = true
                        errorMessage = "Error al procesar los datos: ${e.message}"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = TcsBlue)
            ) {
                Text(if (initialItem == null) "Crear recurso" else "Guardar cambios", color = TcsWhite)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )

    // DatePicker para Fecha de Publicación (simplificado)
    if (showFechaPublicacionPicker) {
        AlertDialog(
            onDismissRequest = { showFechaPublicacionPicker = false },
            title = { Text("Fecha de Publicación") },
            text = {
                Column {
                    Text("Fecha actual: $fechaPublicacion")
                    Text("Puedes modificar manualmente si es necesario", style = MaterialTheme.typography.bodySmall, color = TcsGrayText)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showFechaPublicacionPicker = false },
                    colors = ButtonDefaults.buttonColors(containerColor = TcsBlue)
                ) {
                    Text("Aceptar", color = TcsWhite)
                }
            }
        )
    }

    // DatePicker para Fecha de Actualización (simplificado)
    if (showFechaActualizacionPicker) {
        AlertDialog(
            onDismissRequest = { showFechaActualizacionPicker = false },
            title = { Text("Fecha de Actualización") },
            text = {
                Column {
                    Text("Fecha actual: $fechaActualizacion")
                    Text("Puedes modificar manualmente si es necesario", style = MaterialTheme.typography.bodySmall, color = TcsGrayText)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showFechaActualizacionPicker = false },
                    colors = ButtonDefaults.buttonColors(containerColor = TcsBlue)
                ) {
                    Text("Aceptar", color = TcsWhite)
                }
            }
        )
    }
}
