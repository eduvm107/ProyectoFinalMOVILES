package com.example.chatbot_diseo.presentation.admin.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbot_diseo.data.admin.ActivityItem
import com.example.chatbot_diseo.data.admin.ResourceItem
import com.example.chatbot_diseo.presentation.admin.components.*
import com.example.chatbot_diseo.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    viewModel: AdminPanelViewModel = viewModel(),
    onBack: () -> Unit,
    onLogout: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    var showNewDialog by remember { mutableStateOf(false) }

    // ESTADO PARA MOSTRAR EL DIÁLOGO DEL ADMINISTRADOR
    var showAdminInfo by remember { mutableStateOf(false) }

    // ESTADO PARA MOSTRAR EL DIÁLOGO DE CONFIRMACIÓN DE LOGOUT
    var showLogoutDialog by remember { mutableStateOf(false) }

    var editContent by remember { mutableStateOf<com.example.chatbot_diseo.network.dto.response.ContentResponse?>(null) }
    var editActivity by remember { mutableStateOf<ActivityItem?>(null) }
    var editResource by remember { mutableStateOf<ResourceItem?>(null) }

    // Estado para editar actividad completa
    val selectedActivity by viewModel.selectedActivity.collectAsState()

    // Estado para editar contenido completo
    val selectedContent by viewModel.selectedContent.collectAsState()

    val contentList by viewModel.contents.collectAsState()
    val activityList by viewModel.activities.collectAsState()
    val resourceList by viewModel.resources.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()

    // Cargar datos cuando se abre el panel
    LaunchedEffect(Unit) {
        viewModel.loadAllData()
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            // El mensaje se muestra en la UI
        }
    }

    LaunchedEffect(successMessage) {
        successMessage?.let {
            delay(3000)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        containerColor = TcsGrayBackground,
        floatingActionButton = {
            if (selectedTab in 0..2) {
                FloatingActionButton(
                    onClick = { showNewDialog = true },
                    containerColor = TcsBlue
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Nuevo", tint = TcsWhite)
                }
            }
        },
        topBar = {
            Surface(
                shadowElevation = 6.dp,
                tonalElevation = 0.dp
            ) {
                TopAppBar(
                    title = {
                        Text(
                            "Panel de Administración",
                            style = MaterialTheme.typography.titleLarge,
                            color = TcsTextDark,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        // 1. ÍCONO DEL ADMINISTRADOR
                        IconButton(onClick = { showAdminInfo = true }) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Info Administrador",
                                tint = TcsBlue
                            )
                        }
                        // 2. BOTÓN DE SALIR
                        TextButton(
                            onClick = { showLogoutDialog = true },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = TcsRed
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Salir",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Salir", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = TcsWhite)
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            // DASHBOARD SUPERIOR (4 tarjetas)
            AdminDashboardHeader(
                totalContents = contentList.size,
                totalActivities = activityList.size,
                totalResources = resourceList.size,
                completionRate = viewModel.getCompletionRate()
            )

            // Mostrar indicador de carga
            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = TcsBlue
                )
            }

            // Mostrar mensaje de error
            if (errorMessage != null) {
                MessageBanner(
                    message = errorMessage,
                    isError = true,
                    onDismiss = { viewModel.clearMessages() }
                )
            }

            // Mostrar mensaje de éxito
            if (successMessage != null) {
                MessageBanner(
                    message = successMessage,
                    isError = false,
                    onDismiss = { viewModel.clearMessages() }
                )
            }


            // TABS TIPO FIGMA
            AdminTabs(
                selected = selectedTab,
                onSelect = { selectedTab = it }
            )

            // Título de la sección activa
            if (selectedTab != 3) {
                Text(
                    text = when (selectedTab) {
                        0 -> "Mensajes Automáticos"
                        1 -> "Actividades Programadas"
                        2 -> "Recursos Disponibles"
                        else -> ""
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    color = TcsTextDark,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            } else {
                Spacer(Modifier.height(4.dp))
            }

            // CONTENIDO SEGÚN TAB (SOLO ESTO HACE SCROLL)
            when (selectedTab) {

                // MENSAJES AUTOMÁTICOS
                0 -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(contentList) { item ->
                        AdminContentCard(
                            item = item,
                            onEdit = { editContent = item },
                            onDelete = {
                                item.id?.let { id ->
                                    viewModel.deleteContent(id)
                                }
                            }
                        )
                    }
                }

                // ACTIVIDADES
                1 -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(activityList) { item ->
                        AdminActivityCard(
                            item = item,
                            onEdit = {
                                // Cargar actividad completa por ID
                                viewModel.getActivityById(item.id)
                                editActivity = item
                            },
                            onDelete = { viewModel.deleteActivity(item.id) }
                        )
                    }
                }

                // RECURSOS
                2 -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(resourceList) { item ->
                        AdminResourceCard(
                            item = item,
                            onEdit = { editResource = item },
                            onDelete = { viewModel.deleteResource(item.id) }
                        )
                    }
                }

                // MÉTRICAS
                3 -> MetricsPage(viewModel)
            }
        }

        // DIÁLOGO DE INFORMACIÓN DEL ADMINISTRADOR - ESTILIZADO
        if (showAdminInfo) {
            AlertDialog(
                onDismissRequest = { showAdminInfo = false },
                containerColor = TcsWhite, // Usar color blanco para el contenedor
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = TcsBlue,
                            modifier = Modifier.size(28.dp).padding(end = 12.dp)
                        )
                        Text("Información del Administrador", style = MaterialTheme.typography.titleLarge, color = TcsTextDark)
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                        // Nombre: Juan Pérez (Texto enriquecido para negrita)
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = TcsTextDark)) {
                                    append("Nombre:")
                                }
                                append(" Juan Pérez")
                            },
                            style = MaterialTheme.typography.bodyLarge
                        )

                        // Rol: Administrador Principal (Texto enriquecido para negrita)
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = TcsTextDark)) {
                                    append("Rol:")
                                }
                                append(" Administrador Principal")
                            },
                            style = MaterialTheme.typography.bodyLarge
                        )

                        // Sistema: Chatbot TCS Admin (Texto enriquecido para negrita)
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = TcsTextDark)) {
                                    append("Sistema:")
                                }
                                append(" Chatbot TCS Admin")
                            },
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showAdminInfo = false },
                        colors = ButtonDefaults.buttonColors(containerColor = TcsBlue)
                    ) {
                        Text("Cerrar", color = TcsWhite)
                    }
                }
            )
        }

        // DIÁLOGO DE CONFIRMACIÓN DE LOGOUT
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                containerColor = TcsWhite,
                icon = {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = TcsRed,
                        modifier = Modifier.size(32.dp)
                    )
                },
                title = {
                    Text(
                        "Cerrar Sesión",
                        style = MaterialTheme.typography.titleLarge,
                        color = TcsTextDark
                    )
                },
                text = {
                    Text(
                        "¿Estás seguro de que deseas cerrar sesión?",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TcsTextDark
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showLogoutDialog = false
                            onLogout()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TcsRed)
                    ) {
                        Text("Cerrar Sesión", color = TcsWhite)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showLogoutDialog = false }
                    ) {
                        Text("Cancelar", color = TcsTextDark)
                    }
                }
            )
        }

        // DIALOGOS NUEVO (RESTO DE CÓDIGO INALTERADO)
        if (showNewDialog) {
            when (selectedTab) {
                0 -> AdminContentDialog(
                    titleDialog = "Crear nuevo mensaje",
                    initialItem = null,
                    onDismiss = { showNewDialog = false },
                    onConfirm = { titulo, contenido, tipo, diaGatillo, prioridad, canales, activo, segmento, horaEnvio ->
                        viewModel.addContent(titulo, contenido, tipo, diaGatillo, prioridad, canales, activo, segmento, horaEnvio)
                        showNewDialog = false
                    }
                )

                1 -> AdminActivityDialog2(
                    titleDialog = "Crear nueva actividad",
                    initialItem = null,
                    onDismiss = { showNewDialog = false },
                    onConfirm = { activityRequest ->
                        viewModel.addActivity(
                            activityRequest.titulo,
                            "Día ${activityRequest.dia} - ${activityRequest.horaInicio}",
                            activityRequest.modalidad
                        )
                        showNewDialog = false
                    }
                )

                2 -> AdminResourceDialog(
                    titleDialog = "Crear nuevo recurso",
                    initialItem = null,
                    onDismiss = { showNewDialog = false },
                    onConfirm = { titulo, descripcion, url, tipo, categoria, subcategoria, tags, icono, tamaño, idioma, version, publico, obligatorio, fechaPublicacion, fechaActualizacion, autor, favorito ->
                        // Por ahora solo usamos los 3 campos básicos que ya soporta el ViewModel
                        // TODO: Actualizar viewModel.addResource para aceptar todos los campos
                        viewModel.addResource(titulo, categoria, url)
                        showNewDialog = false
                    }
                )
            }
        }

        // DIALOGOS EDITAR - Editar contenido completo
        editContent?.let { item ->
            // Cargar contenido completo por ID solo si el id no es nulo
            item.id?.let { id ->
                LaunchedEffect(id) {
                    viewModel.getContentById(id)
                }
            }
        }

        // Mostrar diálogo cuando selectedContent está cargado
        selectedContent?.let { contentResponse ->
            AdminContentDialog(
                titleDialog = "Editar contenido",
                initialItem = contentResponse,
                onDismiss = {
                    viewModel.clearSelectedContent()
                    editContent = null
                },
                onConfirm = { titulo, contenido, tipo, diaGatillo, prioridad, canales, activo, segmento, horaEnvio ->
                    editContent?.id?.let { id ->
                        viewModel.updateContent(id, titulo, contenido, tipo, diaGatillo, prioridad, canales, activo, segmento, horaEnvio)
                        viewModel.clearSelectedContent()
                        editContent = null
                    }
                }
            )
        }

        // Editar actividad with the new complete dialog
        selectedActivity?.let { activityResponse ->
            EditarActividadDialog(
                actividadInicial = activityResponse,
                onDismiss = {
                    viewModel.clearSelectedActivity()
                    editActivity = null
                },
                onConfirm = { activityRequest ->
                    editActivity?.let { item ->
                        viewModel.updateActivityComplete(item.id, activityRequest)
                        viewModel.clearSelectedActivity()
                        editActivity = null
                    }
                }
            )
        }

        editResource?.let { item ->
            AdminResourceDialog(
                titleDialog = "Editar recurso",
                initialItem = item,
                onDismiss = { editResource = null },
                onConfirm = { titulo, descripcion, url, tipo, categoria, subcategoria, tags, icono, tamaño, idioma, version, publico, obligatorio, fechaPublicacion, fechaActualizacion, autor, favorito ->
                    // Por ahora solo usamos los 3 campos básicos que ya soporta el ViewModel
                    // TODO: Actualizar viewModel.updateResource para aceptar todos los campos
                    viewModel.updateResource(item.id, titulo, categoria, url)
                    editResource = null
                }
            )
        }
    }
}