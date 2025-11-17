package com.example.chatbot_diseo.presentation.admin.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbot_diseo.data.admin.ActivityItem
import com.example.chatbot_diseo.data.admin.ContentItem
import com.example.chatbot_diseo.data.admin.ResourceItem
import com.example.chatbot_diseo.presentation.admin.components.*
import com.example.chatbot_diseo.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    viewModel: AdminPanelViewModel = viewModel(),
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    var showNewDialog by remember { mutableStateOf(false) }

    var editContent by remember { mutableStateOf<ContentItem?>(null) }
    var editActivity by remember { mutableStateOf<ActivityItem?>(null) }
    var editResource by remember { mutableStateOf<ResourceItem?>(null) }

    val contentList by viewModel.contents.collectAsState()
    val activityList by viewModel.activities.collectAsState()
    val resourceList by viewModel.resources.collectAsState()

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
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Panel de Administración",
                            style = MaterialTheme.typography.titleLarge,
                            color = TcsTextDark
                        )
                        Text(
                            "Administrador: Juan Pérez",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TcsTextLight
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = TcsTextDark)
                    }
                },
                actions = {
                    TextButton(
                        onClick = onBack,
                        colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                            contentColor = TcsRed
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Salir",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Salir", style = MaterialTheme.typography.labelLarge)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TcsWhite)
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // DASHBOARD SUPERIOR (4 tarjetas)
            AdminDashboardHeader(
                totalContents = viewModel.getTotalContents(),
                totalActivities = viewModel.getTotalActivities(),
                totalResources = viewModel.getTotalResources(),
                completionRate = viewModel.getCompletionRate()
            )

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

            // CONTENIDO SEGÚN TAB
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
                            onDelete = { viewModel.deleteContent(item.id) }
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
                            onEdit = { editActivity = item },
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

            // DIALOGOS NUEVO
            if (showNewDialog) {
                when (selectedTab) {
                    0 -> AdminContentDialog(
                        titleDialog = "Crear nuevo mensaje",
                        initialItem = null,
                        onDismiss = { showNewDialog = false },
                        onConfirm = { title, type, desc ->
                            viewModel.addContent(title, type, desc)
                            showNewDialog = false
                        }
                    )

                    1 -> AdminActivityDialog(
                        titleDialog = "Crear nueva actividad",
                        initialItem = null,
                        onDismiss = { showNewDialog = false },
                        onConfirm = { title, date, modality ->
                            viewModel.addActivity(title, date, modality)
                            showNewDialog = false
                        }
                    )

                    2 -> AdminResourceDialog(
                        titleDialog = "Crear nuevo recurso",
                        initialItem = null,
                        onDismiss = { showNewDialog = false },
                        onConfirm = { title, category, url ->
                            viewModel.addResource(title, category, url)
                            showNewDialog = false
                        }
                    )
                }
            }

            // DIALOGOS EDITAR
            editContent?.let { item ->
                AdminContentDialog(
                    titleDialog = "Editar contenido",
                    initialItem = item,
                    onDismiss = { editContent = null },
                    onConfirm = { title, type, desc ->
                        viewModel.updateContent(item.id, title, type, desc)
                        editContent = null
                    }
                )
            }

            editActivity?.let { item ->
                AdminActivityDialog(
                    titleDialog = "Editar actividad",
                    initialItem = item,
                    onDismiss = { editActivity = null },
                    onConfirm = { title, date, modality ->
                        viewModel.updateActivity(item.id, title, date, modality)
                        editActivity = null
                    }
                )
            }

            editResource?.let { item ->
                AdminResourceDialog(
                    titleDialog = "Editar recurso",
                    initialItem = item,
                    onDismiss = { editResource = null },
                    onConfirm = { title, category, url ->
                        viewModel.updateResource(item.id, title, category, url)
                        editResource = null
                    }
                )
            }
        }
    }
}
