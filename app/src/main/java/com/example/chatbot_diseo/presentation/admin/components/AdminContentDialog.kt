package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.network.dto.response.ContentResponse
import com.example.chatbot_diseo.ui.theme.TcsBlue
import com.example.chatbot_diseo.ui.theme.TcsGrayText
import com.example.chatbot_diseo.ui.theme.TcsTextDark
import com.example.chatbot_diseo.ui.theme.TcsWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminContentDialog(
    titleDialog: String,
    initialItem: ContentResponse? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, Int, String, List<String>, Boolean, String, String) -> Unit
) {
    var titulo by remember { mutableStateOf(initialItem?.titulo ?: "") }
    var contenido by remember { mutableStateOf(initialItem?.contenido ?: "") }
    var tipo by remember { mutableStateOf(initialItem?.tipo ?: "") }
    var diaGatillo by remember { mutableStateOf((initialItem?.diaGatillo ?: 0).toString()) }
    var prioridad by remember { mutableStateOf(initialItem?.prioridad ?: "") }
    var canales by remember { mutableStateOf(initialItem?.canal ?: emptyList()) }
    var activo by remember { mutableStateOf(initialItem?.activo ?: true) }
    var segmento by remember { mutableStateOf(initialItem?.segmento ?: "") }
    var horaEnvio by remember { mutableStateOf(initialItem?.horaEnvio ?: "") }

    // Estados de validación
    var tituloError by remember { mutableStateOf(false) }
    var contenidoError by remember { mutableStateOf(false) }
    var tipoError by remember { mutableStateOf(false) }
    var diaGatilloError by remember { mutableStateOf(false) }
    var prioridadError by remember { mutableStateOf(false) }
    var segmentoError by remember { mutableStateOf(false) }
    var horaEnvioError by remember { mutableStateOf(false) }

    // Estados de dropdowns
    var expandedTipo by remember { mutableStateOf(false) }
    var expandedPrioridad by remember { mutableStateOf(false) }
    var expandedSegmento by remember { mutableStateOf(false) }

    // Opciones
    val tipoOptions = listOf("bienvenida", "recordatorio", "informativo")
    val prioridadOptions = listOf("alta", "media", "baja")
    val segmentoOptions = listOf("todos", "practicantes", "staff", "operativos")
    val canalOptions = listOf("chatbot", "email", "push")

    // Validación de hora HH:mm
    fun isValidTimeFormat(time: String): Boolean {
        return time.matches(Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$"))
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(16.dp),
        title = {
            Column {
                Text(
                    text = titleDialog,
                    style = MaterialTheme.typography.headlineSmall,
                    color = TcsTextDark
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Configura el mensaje automático",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TcsGrayText
                )
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

                // Título
                OutlinedTextField(
                    value = titulo,
                    onValueChange = {
                        titulo = it
                        tituloError = it.isBlank()
                    },
                    label = { Text("Título") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    isError = tituloError,
                    supportingText = if (tituloError) {
                        { Text("Obligatorio", color = MaterialTheme.colorScheme.error) }
                    } else null
                )

                // Contenido
                OutlinedTextField(
                    value = contenido,
                    onValueChange = {
                        contenido = it
                        contenidoError = it.isBlank()
                    },
                    label = { Text("Contenido del mensaje") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    isError = contenidoError,
                    supportingText = if (contenidoError) {
                        { Text("Obligatorio", color = MaterialTheme.colorScheme.error) }
                    } else null
                )

                // Tipo (Dropdown)
                ExposedDropdownMenuBox(
                    expanded = expandedTipo,
                    onExpandedChange = { expandedTipo = !expandedTipo }
                ) {
                    OutlinedTextField(
                        value = tipo,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipo) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        isError = tipoError,
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedTipo,
                        onDismissRequest = { expandedTipo = false }
                    ) {
                        tipoOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    tipo = option
                                    tipoError = false
                                    expandedTipo = false
                                }
                            )
                        }
                    }
                }

                // Día Gatillo
                OutlinedTextField(
                    value = diaGatillo,
                    onValueChange = {
                        if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                            diaGatillo = it
                            diaGatilloError = it.isBlank() || it.toIntOrNull() == null || it.toIntOrNull()!! < 0
                        }
                    },
                    label = { Text("Día gatillo") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    isError = diaGatilloError,
                    supportingText = if (diaGatilloError) {
                        { Text("Debe ser un número ≥ 0", color = MaterialTheme.colorScheme.error) }
                    } else null
                )

                // Prioridad (Dropdown)
                ExposedDropdownMenuBox(
                    expanded = expandedPrioridad,
                    onExpandedChange = { expandedPrioridad = !expandedPrioridad }
                ) {
                    OutlinedTextField(
                        value = prioridad,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Prioridad") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPrioridad) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        isError = prioridadError,
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedPrioridad,
                        onDismissRequest = { expandedPrioridad = false }
                    ) {
                        prioridadOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    prioridad = option
                                    prioridadError = false
                                    expandedPrioridad = false
                                }
                            )
                        }
                    }
                }

                // Canales (Multi-selección)
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Canales",
                        style = MaterialTheme.typography.labelMedium,
                        color = TcsGrayText,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        canalOptions.forEach { canal ->
                            val isSelected = canales.contains(canal)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    canales = if (isSelected) {
                                        canales.filter { it != canal }
                                    } else {
                                        canales + canal
                                    }
                                },
                                label = { Text(canal) },
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }
                }

                // Segmento (Dropdown)
                ExposedDropdownMenuBox(
                    expanded = expandedSegmento,
                    onExpandedChange = { expandedSegmento = !expandedSegmento }
                ) {
                    OutlinedTextField(
                        value = segmento,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Segmento") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSegmento) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        isError = segmentoError,
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedSegmento,
                        onDismissRequest = { expandedSegmento = false }
                    ) {
                        segmentoOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    segmento = option
                                    segmentoError = false
                                    expandedSegmento = false
                                }
                            )
                        }
                    }
                }

                // Hora de envío
                OutlinedTextField(
                    value = horaEnvio,
                    onValueChange = {
                        horaEnvio = it
                        horaEnvioError = !isValidTimeFormat(it)
                    },
                    label = { Text("Hora de envío (HH:mm)") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("09:00") },
                    isError = horaEnvioError,
                    supportingText = if (horaEnvioError) {
                        { Text("Formato: HH:mm", color = MaterialTheme.colorScheme.error) }
                    } else null
                )

                // Switch Activo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Estado",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TcsTextDark
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (activo) "Activo" else "Inactivo",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (activo) TcsBlue else TcsGrayText,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Switch(
                            checked = activo,
                            onCheckedChange = { activo = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = TcsWhite,
                                checkedTrackColor = TcsBlue,
                                uncheckedThumbColor = TcsWhite,
                                uncheckedTrackColor = TcsGrayText
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Validación final
                    tituloError = titulo.isBlank()
                    contenidoError = contenido.isBlank()
                    tipoError = tipo.isBlank()
                    diaGatilloError = diaGatillo.isBlank() || diaGatillo.toIntOrNull() == null || diaGatillo.toIntOrNull()!! < 0
                    prioridadError = prioridad.isBlank()
                    segmentoError = segmento.isBlank()
                    horaEnvioError = !isValidTimeFormat(horaEnvio)

                    if (!tituloError && !contenidoError && !tipoError && !diaGatilloError &&
                        !prioridadError && !segmentoError && !horaEnvioError) {
                        onConfirm(
                            titulo,
                            contenido,
                            tipo,
                            diaGatillo.toInt(),
                            prioridad,
                            if (canales.isEmpty()) listOf("chatbot") else canales,
                            activo,
                            segmento,
                            horaEnvio
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = TcsBlue),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    if (initialItem == null) "Crear mensaje" else "Guardar cambios",
                    color = TcsWhite
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TcsGrayText)
            ) {
                Text("Cancelar")
            }
        }
    )
}
