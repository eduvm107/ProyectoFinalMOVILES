package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.data.admin.ActivityItem
import com.example.chatbot_diseo.network.dto.request.ActivityRequest
import com.example.chatbot_diseo.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminActivityDialog2(
    titleDialog: String,
    initialItem: ActivityItem? = null,
    onDismiss: () -> Unit,
    onConfirm: (ActivityRequest) -> Unit
) {
    // Estados para todos los campos
    var titulo by remember { mutableStateOf(initialItem?.title ?: "") }
    var descripcion by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("1") }
    var duracionHoras by remember { mutableStateOf("8.0") }
    var horaInicio by remember { mutableStateOf("09:00") }
    var horaFin by remember { mutableStateOf("17:00") }
    var lugar by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var responsable by remember { mutableStateOf("") }
    var emailResponsable by remember { mutableStateOf("") }
    var capacidadMaxima by remember { mutableStateOf("50") }
    var obligatorio by remember { mutableStateOf(false) }
    var preparacionPrevia by remember { mutableStateOf("") }
    var fechaCreacion by remember { mutableStateOf(initialItem?.let { "Fecha de creación existente" } ?: "") }

    // Listas editables
    var materialesNecesarios by remember { mutableStateOf(mutableListOf<String>()) }
    var nuevoMaterialNecesario by remember { mutableStateOf("") }

    var materialesProporcionados by remember { mutableStateOf(mutableListOf<String>()) }
    var nuevoMaterialProporcionado by remember { mutableStateOf("") }

    var actividadesSiguientes by remember { mutableStateOf(mutableListOf<String>()) }
    var nuevaActividadSiguiente by remember { mutableStateOf("") }

    // Dropdowns
    var modalidadExpanded by remember { mutableStateOf(false) }
    var modalidad by remember { mutableStateOf("presencial") }
    val modalidadOpciones = listOf("presencial", "virtual", "hibrido", "flexible")

    var tipoExpanded by remember { mutableStateOf(false) }
    var tipo by remember { mutableStateOf("induccion") }
    val tipoOpciones = listOf("induccion", "logistica", "capacitacion", "reunion", "evaluacion", "taller", "integracion")

    var estadoExpanded by remember { mutableStateOf(false) }
    var estado by remember { mutableStateOf("activo") }
    val estadoOpciones = listOf("activo", "completado", "cancelado")

    // Selector de fecha
    var fechaActividad by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    // Generar fecha actual por defecto en formato ISO
    LaunchedEffect(Unit) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        fechaActividad = dateFormat.format(Date())
    }

    // Validación
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = TcsWhite,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        title = {
            Column {
                Text(titleDialog, style = MaterialTheme.typography.headlineSmall, color = TcsTextDark)
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
                // Mensaje de error
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
                    onValueChange = { titulo = it },
                    label = { Text("Título *") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 2. Descripción *
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción *") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )

                // 3. Día *
                OutlinedTextField(
                    value = dia,
                    onValueChange = { if (it.all { char -> char.isDigit() }) dia = it },
                    label = { Text("Día del proceso *") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Ejemplo: 1, 2, 3...") }
                )

                // 4. Duración en horas *
                OutlinedTextField(
                    value = duracionHoras,
                    onValueChange = { duracionHoras = it },
                    label = { Text("Duración (horas) *") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Ejemplo: 8.0, 4.5") }
                )

                // 5 y 6. Hora Inicio y Fin
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = horaInicio,
                        onValueChange = { horaInicio = it },
                        label = { Text("Hora Inicio *") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("09:00") }
                    )

                    OutlinedTextField(
                        value = horaFin,
                        onValueChange = { horaFin = it },
                        label = { Text("Hora Fin *") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("17:00") }
                    )
                }

                // 7. Lugar *
                OutlinedTextField(
                    value = lugar,
                    onValueChange = { lugar = it },
                    label = { Text("Lugar *") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Ejemplo: Sala de conferencias, Zoom") }
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
                            .menuAnchor(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = modalidadExpanded,
                        onDismissRequest = { modalidadExpanded = false }
                    ) {
                        modalidadOpciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion.capitalize()) },
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
                                text = { Text(opcion.capitalize()) },
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
                    onValueChange = { responsable = it },
                    label = { Text("Responsable *") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
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
                    onValueChange = { if (it.all { char -> char.isDigit() }) capacidadMaxima = it },
                    label = { Text("Capacidad Máxima *") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Ejemplo: 50") }
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

                // 15. Materiales Necesarios - Lista editable
                Text("Materiales Necesarios", style = MaterialTheme.typography.titleSmall, color = TcsTextDark, fontWeight = FontWeight.Medium)

                materialesNecesarios.forEachIndexed { index, material ->
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
                        IconButton(onClick = { materialesNecesarios.removeAt(index) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = TcsRed, modifier = Modifier.size(20.dp))
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

                // 16. Materiales Proporcionados - Lista editable
                Text("Materiales Proporcionados", style = MaterialTheme.typography.titleSmall, color = TcsTextDark, fontWeight = FontWeight.Medium)

                materialesProporcionados.forEachIndexed { index, material ->
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
                        IconButton(onClick = { materialesProporcionados.removeAt(index) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = TcsRed, modifier = Modifier.size(20.dp))
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

                // 18. Actividades Siguientes - Lista editable
                Text("Actividades Siguientes", style = MaterialTheme.typography.titleSmall, color = TcsTextDark, fontWeight = FontWeight.Medium)

                actividadesSiguientes.forEachIndexed { index, actividad ->
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
                        IconButton(onClick = { actividadesSiguientes.removeAt(index) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = TcsRed, modifier = Modifier.size(20.dp))
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
                            .menuAnchor(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = estadoExpanded,
                        onDismissRequest = { estadoExpanded = false }
                    ) {
                        estadoOpciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion.capitalize()) },
                                onClick = {
                                    estado = opcion
                                    estadoExpanded = false
                                }
                            )
                        }
                    }
                }

                // 20. Fecha de Actividad - Selector de fecha
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

                // 21. Fecha de Creación - No editable
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
                    // Validación
                    if (titulo.isBlank() || descripcion.isBlank() || dia.isBlank() ||
                        duracionHoras.isBlank() || horaInicio.isBlank() || horaFin.isBlank() ||
                        lugar.isBlank() || responsable.isBlank() || capacidadMaxima.isBlank()) {
                        showError = true
                        errorMessage = "Por favor completa todos los campos obligatorios (*)"
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
                            fechaActividad = fechaActividad
                        )
                        onConfirm(activityRequest)
                    } catch (e: Exception) {
                        showError = true
                        errorMessage = "Error en los datos: ${e.message}"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = TcsBlue)
            ) {
                Text(
                    if (initialItem == null) "Crear actividad" else "Guardar cambios",
                    color = TcsWhite
                )
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
                    Text("Puedes modificar manualmente si es necesario", style = MaterialTheme.typography.bodySmall)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDatePicker = false },
                    colors = ButtonDefaults.buttonColors(containerColor = TcsBlue)
                ) {
                    Text(if (initialItem == null) "Crear actividad" else "Guardar cambios", color = TcsWhite)

                }
            },
            dismissButton = {
                OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
            }
        )

    }
}

private fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}
