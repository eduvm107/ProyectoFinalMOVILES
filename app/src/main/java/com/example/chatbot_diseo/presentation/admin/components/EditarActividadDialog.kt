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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.network.dto.request.ActivityRequest
import com.example.chatbot_diseo.network.dto.response.ActivityResponse
import com.example.chatbot_diseo.network.dto.response.UsuarioCompleto
import com.example.chatbot_diseo.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarActividadDialog(
    actividadInicial: ActivityResponse,
    onDismiss: () -> Unit,
    onConfirm: (ActivityRequest) -> Unit,
    usuariosAsignables: List<UsuarioCompleto> = emptyList()
) {
    // Estados para todos los campos - inicializados con los valores existentes
    var titulo by remember { mutableStateOf(actividadInicial.titulo) }
    var descripcion by remember { mutableStateOf(actividadInicial.descripcion) }
    var dia by remember { mutableStateOf(actividadInicial.dia.toString()) }
    var duracionHoras by remember { mutableStateOf(actividadInicial.duracionHoras.toString()) }
    var horaInicio by remember { mutableStateOf(actividadInicial.horaInicio) }
    var horaFin by remember { mutableStateOf(actividadInicial.horaFin) }
    var lugar by remember { mutableStateOf(actividadInicial.lugar) }
    var categoria by remember { mutableStateOf(actividadInicial.categoria) }
    var responsable by remember { mutableStateOf(actividadInicial.responsable) }
    var emailResponsable by remember { mutableStateOf(actividadInicial.emailResponsable ?: "") }
    var capacidadMaxima by remember { mutableStateOf(actividadInicial.capacidadMaxima.toString()) }
    var obligatorio by remember { mutableStateOf(actividadInicial.obligatorio) }
    var preparacionPrevia by remember { mutableStateOf(actividadInicial.preparacionPrevia ?: "") }
    var fechaCreacion by remember { mutableStateOf(actividadInicial.fechaCreacion ?: "") }

    // Listas editables - usando mutableStateListOf para manejo estable en Compose
    val materialesNecesarios = remember {
        mutableStateListOf<String>().apply {
            addAll(actividadInicial.materialesNecesarios)
        }
    }
    var nuevoMaterialNecesario by remember { mutableStateOf("") }

    val materialesProporcionados = remember {
        mutableStateListOf<String>().apply {
            addAll(actividadInicial.materialesProporcionados)
        }
    }
    var nuevoMaterialProporcionado by remember { mutableStateOf("") }

    val actividadesSiguientes = remember {
        mutableStateListOf<String>().apply {
            addAll(actividadInicial.actividadesSiguientes)
        }
    }
    var nuevaActividadSiguiente by remember { mutableStateOf("") }

    // Dropdowns - inicializados con valores existentes
    var modalidadExpanded by remember { mutableStateOf(false) }
    var modalidad by remember { mutableStateOf(actividadInicial.modalidad) }
    val modalidadOpciones = listOf("presencial", "virtual", "hibrido", "flexible")

    var tipoExpanded by remember { mutableStateOf(false) }
    var tipo by remember { mutableStateOf(actividadInicial.tipo) }
    val tipoOpciones = listOf("induccion", "logistica", "capacitacion", "reunion", "evaluacion", "taller", "integracion")

    var estadoExpanded by remember { mutableStateOf(false) }
    var estado by remember { mutableStateOf(actividadInicial.estado) }
    val estadoOpciones = listOf("activo", "completado", "cancelado")

    // Nuevo: Usuario asignable - preseleccionar si existe
    var usuarioExpanded by remember { mutableStateOf(false) }
    var usuarioSeleccionadoId by remember { mutableStateOf(actividadInicial.usuarioId) }
    var usuarioSeleccionadoNombre by remember {
        mutableStateOf(
            if (actividadInicial.usuarioId != null) {
                usuariosAsignables.find { it.id == actividadInicial.usuarioId }?.nombreCompleto ?: "Sin asignar"
            } else {
                "Sin asignar"
            }
        )
    }

    // Selector de fecha - inicializado con valor existente
    var fechaActividad by remember { mutableStateOf(actividadInicial.fechaDeActividad ?: "") }
    var showDatePicker by remember { mutableStateOf(false) }

    // Si no hay fecha, generar una por defecto
    LaunchedEffect(Unit) {
        if (fechaActividad.isEmpty()) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            fechaActividad = dateFormat.format(Date())
        }
    }

    // Validación
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Errores por campo
    var tituloError by remember { mutableStateOf(false) }
    var descripcionError by remember { mutableStateOf(false) }
    var diaError by remember { mutableStateOf(false) }
    var duracionError by remember { mutableStateOf(false) }
    var horaInicioError by remember { mutableStateOf(false) }
    var horaFinError by remember { mutableStateOf(false) }
    var lugarError by remember { mutableStateOf(false) }
    var responsableError by remember { mutableStateOf(false) }
    var capacidadError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = TcsWhite,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        title = {
            Column {
                Text("Editar Actividad", style = MaterialTheme.typography.headlineSmall, color = TcsTextDark)
                Text("Completa todos los campos obligatorios (*)", color = TcsGrayText, style = MaterialTheme.typography.bodySmall)
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
                    label = { Text("Título *") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = tituloError,
                    supportingText = if (tituloError) { { Text("Campo obligatorio", style = TextStyle(color = TcsRed)) } } else null
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
                    minLines = 3,
                    maxLines = 5,
                    isError = descripcionError,
                    supportingText = if (descripcionError) { { Text("Campo obligatorio", style = TextStyle(color = TcsRed)) } } else null
                )

                // 3. Día *
                OutlinedTextField(
                    value = dia,
                    onValueChange = {
                        if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                            dia = it
                            diaError = it.isBlank() || it.toIntOrNull() == null || it.toInt() < 1
                        }
                    },
                    label = { Text("Día del proceso *") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Ejemplo: 1, 2, 3...") },
                    isError = diaError,
                    supportingText = if (diaError) { { Text("Debe ser un número mayor a 0", style = TextStyle(color = TcsRed)) } } else null
                )

                // 4. Duración en horas *
                OutlinedTextField(
                    value = duracionHoras,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            duracionHoras = it
                            duracionError = it.isBlank() || it.toDoubleOrNull() == null || it.toDouble() <= 0
                        }
                    },
                    label = { Text("Duración (horas) *") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Ejemplo: 8.0, 4.5") },
                    isError = duracionError,
                    supportingText = if (duracionError) { { Text("Debe ser un número mayor a 0", style = TextStyle(color = TcsRed)) } } else null
                )

                // 5 y 6. Hora Inicio y Fin
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = horaInicio,
                        onValueChange = {
                            horaInicio = it
                            horaInicioError = !it.matches(Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$"))
                        },
                        label = { Text("Hora Inicio *") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("09:00") },
                        isError = horaInicioError,
                        supportingText = if (horaInicioError) { { Text("Formato HH:mm", style = MaterialTheme.typography.bodySmall.copy(color = TcsRed)) } } else null
                    )

                    OutlinedTextField(
                        value = horaFin,
                        onValueChange = {
                            horaFin = it
                            horaFinError = !it.matches(Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$"))
                        },
                        label = { Text("Hora Fin *") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("17:00") },
                        isError = horaFinError,
                        supportingText = if (horaFinError) { { Text("Formato HH:mm", style = MaterialTheme.typography.bodySmall.copy(color = TcsRed)) } } else null
                    )
                }

                // 7. Lugar *
                OutlinedTextField(
                    value = lugar,
                    onValueChange = {
                        lugar = it
                        lugarError = it.isBlank()
                    },
                    label = { Text("Lugar *") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Ejemplo: Sala de conferencias, Zoom") },
                    isError = lugarError,
                    supportingText = if (lugarError) { { Text("Campo obligatorio", style = TextStyle(color = TcsRed)) } } else null
                )

                // 8. Modalidad * - Dropdown
                ExposedDropdownMenuBox(
                    expanded = modalidadExpanded,
                    onExpandedChange = { modalidadExpanded = !modalidadExpanded }
                ) {
                    OutlinedTextField(
                        value = modalidad,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Modalidad *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = modalidadExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(10.dp),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = modalidadExpanded,
                        onDismissRequest = { modalidadExpanded = false }
                    ) {
                        modalidadOpciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }) },
                                onClick = {
                                    modalidad = opcion
                                    modalidadExpanded = false
                                }
                            )
                        }
                    }
                }

                // 9. Tipo * - Dropdown
                ExposedDropdownMenuBox(
                    expanded = tipoExpanded,
                    onExpandedChange = { tipoExpanded = !tipoExpanded }
                ) {
                    OutlinedTextField(
                        value = tipo,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tipoExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(10.dp),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = tipoExpanded,
                        onDismissRequest = { tipoExpanded = false }
                    ) {
                        tipoOpciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }) },
                                onClick = {
                                    tipo = opcion
                                    tipoExpanded = false
                                }
                            )
                        }
                    }
                }

                // 10. Categoría
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 11. Responsable *
                OutlinedTextField(
                    value = responsable,
                    onValueChange = {
                        responsable = it
                        responsableError = it.isBlank()
                    },
                    label = { Text("Responsable *") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = responsableError,
                    supportingText = if (responsableError) { { Text("Campo obligatorio", style = TextStyle(color = TcsRed)) } } else null
                )

                // 12. Email Responsable
                OutlinedTextField(
                    value = emailResponsable,
                    onValueChange = { emailResponsable = it },
                    label = { Text("Email del Responsable") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("ejemplo@tcs.com") }
                )

                // 13. Capacidad Máxima *
                OutlinedTextField(
                    value = capacidadMaxima,
                    onValueChange = {
                        if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                            capacidadMaxima = it
                            capacidadError = it.isBlank() || it.toIntOrNull() == null || it.toInt() < 1
                        }
                    },
                    label = { Text("Capacidad Máxima *") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Ejemplo: 50") },
                    isError = capacidadError,
                    supportingText = if (capacidadError) { { Text("Debe ser un número mayor a 0", style = TextStyle(color = TcsRed)) } } else null
                )

                // 14. Obligatorio - Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Actividad Obligatoria", style = MaterialTheme.typography.bodyLarge, color = TcsTextDark)
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

                // 15. Materiales Necesarios - Lista editable con eliminación segura
                Text("Materiales Necesarios", style = MaterialTheme.typography.titleSmall, color = TcsTextDark, fontWeight = FontWeight.Medium)

                materialesNecesarios.forEach { material ->
                    key(material) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "• $material",
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            IconButton(onClick = { materialesNecesarios.remove(material) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = TcsRed, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = nuevoMaterialNecesario,
                        onValueChange = { nuevoMaterialNecesario = it },
                        label = { Text("Agregar material") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    IconButton(
                        onClick = {
                            if (nuevoMaterialNecesario.isNotBlank()) {
                                materialesNecesarios.add(nuevoMaterialNecesario)
                                nuevoMaterialNecesario = ""
                            }
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar", tint = TcsBlue)
                    }
                }

                // 16. Materiales Proporcionados - Lista editable con eliminación segura
                Text("Materiales Proporcionados", style = MaterialTheme.typography.titleSmall, color = TcsTextDark, fontWeight = FontWeight.Medium)

                materialesProporcionados.forEach { material ->
                    key(material) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "• $material",
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            IconButton(onClick = { materialesProporcionados.remove(material) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = TcsRed, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = nuevoMaterialProporcionado,
                        onValueChange = { nuevoMaterialProporcionado = it },
                        label = { Text("Agregar material") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    IconButton(
                        onClick = {
                            if (nuevoMaterialProporcionado.isNotBlank()) {
                                materialesProporcionados.add(nuevoMaterialProporcionado)
                                nuevoMaterialProporcionado = ""
                            }
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar", tint = TcsBlue)
                    }
                }

                // 17. Preparación Previa
                OutlinedTextField(
                    value = preparacionPrevia,
                    onValueChange = { preparacionPrevia = it },
                    label = { Text("Preparación Previa") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )

                // 18. Actividades Siguientes - Lista editable con eliminación segura
                Text("Actividades Siguientes", style = MaterialTheme.typography.titleSmall, color = TcsTextDark, fontWeight = FontWeight.Medium)

                actividadesSiguientes.forEach { actividad ->
                    key(actividad) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "• $actividad",
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            IconButton(onClick = { actividadesSiguientes.remove(actividad) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = TcsRed, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = nuevaActividadSiguiente,
                        onValueChange = { nuevaActividadSiguiente = it },
                        label = { Text("Agregar actividad") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    IconButton(
                        onClick = {
                            if (nuevaActividadSiguiente.isNotBlank()) {
                                actividadesSiguientes.add(nuevaActividadSiguiente)
                                nuevaActividadSiguiente = ""
                            }
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar", tint = TcsBlue)
                    }
                }

                // 19. Estado - Dropdown
                ExposedDropdownMenuBox(
                    expanded = estadoExpanded,
                    onExpandedChange = { estadoExpanded = !estadoExpanded }
                ) {
                    OutlinedTextField(
                        value = estado,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Estado *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = estadoExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(10.dp),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = estadoExpanded,
                        onDismissRequest = { estadoExpanded = false }
                    ) {
                        estadoOpciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }) },
                                onClick = {
                                    estado = opcion
                                    estadoExpanded = false
                                }
                            )
                        }
                    }
                }

                // 20. Asignar Usuario - Dropdown
                ExposedDropdownMenuBox(
                    expanded = usuarioExpanded,
                    onExpandedChange = { usuarioExpanded = !usuarioExpanded }
                ) {
                    OutlinedTextField(
                        value = usuarioSeleccionadoNombre,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Asignar usuario") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = usuarioExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(10.dp),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = usuarioExpanded,
                        onDismissRequest = { usuarioExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sin asignar") },
                            onClick = {
                                usuarioSeleccionadoId = null
                                usuarioSeleccionadoNombre = "Sin asignar"
                                usuarioExpanded = false
                            }
                        )

                        if (usuariosAsignables.isEmpty()) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "No hay usuarios disponibles",
                                        style = TextStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                        color = Color.Gray
                                    )
                                },
                                onClick = { usuarioExpanded = false },
                                enabled = false
                            )
                        }

                        usuariosAsignables.forEach { usuario ->
                            DropdownMenuItem(
                                text = { Text(usuario.nombreCompleto ?: "Usuario sin nombre") },
                                onClick = {
                                    usuarioSeleccionadoId = usuario.id
                                    usuarioSeleccionadoNombre = usuario.nombreCompleto ?: "Usuario sin nombre"
                                    usuarioExpanded = false
                                }
                            )
                        }
                    }
                }

                // 21. Fecha de Actividad - Selector de fecha
                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = TcsBlueLight),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        "Fecha de Actividad: $fechaActividad",
                        color = TcsBlue
                    )
                }

                // 22. Fecha de Creación - No editable
                if (fechaCreacion.isNotEmpty()) {
                    OutlinedTextField(
                        value = fechaCreacion,
                        onValueChange = {},
                        label = { Text("Fecha de Creación") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false,
                        singleLine = true
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
                    diaError = dia.isBlank() || dia.toIntOrNull() == null || dia.toInt() < 1
                    duracionError = duracionHoras.isBlank() || duracionHoras.toDoubleOrNull() == null || duracionHoras.toDouble() <= 0
                    horaInicioError = !horaInicio.matches(Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$"))
                    horaFinError = !horaFin.matches(Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$"))
                    lugarError = lugar.isBlank()
                    responsableError = responsable.isBlank()
                    capacidadError = capacidadMaxima.isBlank() || capacidadMaxima.toIntOrNull() == null || capacidadMaxima.toInt() < 1

                    if (tituloError || descripcionError || diaError || duracionError ||
                        horaInicioError || horaFinError || lugarError || responsableError || capacidadError) {
                        showError = true
                        errorMessage = "Por favor corrige los errores en los campos marcados"
                        return@Button
                    }

                    try {
                        val activityRequest = ActivityRequest(
                            titulo = titulo,
                            descripcion = descripcion,
                            dia = dia.toInt(),
                            duracionHoras = duracionHoras.toDouble(),
                            horaInicio = horaInicio,
                            horaFin = horaFin,
                            lugar = lugar,
                            modalidad = modalidad,
                            tipo = tipo,
                            categoria = categoria,
                            responsable = responsable,
                            emailResponsable = emailResponsable.ifBlank { null },
                            capacidadMaxima = capacidadMaxima.toInt(),
                            obligatorio = obligatorio,
                            materialesNecesarios = materialesNecesarios.toList(),
                            materialesProporcionados = materialesProporcionados.toList(),
                            preparacionPrevia = preparacionPrevia.ifBlank { null },
                            actividadesSiguientes = actividadesSiguientes.toList(),
                            estado = estado,
                            fechaActividad = fechaActividad,
                            usuarioID = usuarioSeleccionadoId
                        )
                        onConfirm(activityRequest)
                    } catch (e: Exception) {
                        showError = true
                        errorMessage = "Error en los datos: ${e.message}"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = TcsBlue)
            ) {
                Text("Guardar cambios", color = TcsWhite)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TcsBlue)
            ) {
                Text("Cancelar")
            }
        }
    )

    // Selector de fecha (DatePicker) - implementación simplificada
    if (showDatePicker) {
        AlertDialog(
            onDismissRequest = { showDatePicker = false },
            title = { Text("Seleccionar Fecha") },
            text = {
                Column {
                    Text("Fecha seleccionada: $fechaActividad")
                    Text("Puedes modificar manualmente si es necesario", style = MaterialTheme.typography.bodySmall, color = TcsGrayText)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDatePicker = false },
                    colors = ButtonDefaults.buttonColors(containerColor = TcsBlue)
                ) {
                    Text("Aceptar", color = TcsWhite)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDatePicker = false },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TcsBlue)
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
