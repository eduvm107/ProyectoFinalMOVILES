package com.example.chatbot_diseo.presentation.permisos

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController

@Suppress("unused")
@Composable
fun PermissionScreen(navController: NavController) {
    val context = LocalContext.current
    val permissions = arrayOf(
        Manifest.permission.READ_CALENDAR,
        Manifest.permission.WRITE_CALENDAR
    )

    fun allPermissionsGranted(): Boolean {
        return permissions.all { perm ->
            ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED
        }
    }

    var granted by remember { mutableStateOf(allPermissionsGranted()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        granted = result.values.all { it }
    }

    // Si no hay permisos, lanzar la petición al montar la pantalla
    LaunchedEffect(Unit) {
        if (!granted) launcher.launch(permissions)
    }

    // Navegar cuando se conceden los permisos
    LaunchedEffect(granted) {
        if (granted) {
            navController.navigate("calendario") {
                popUpTo("permissions") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            granted -> {
                Text("Permisos concedidos. Redirigiendo al calendario...")
            }
            else -> {
                Text("Se necesitan permisos del calendario para continuar.")
                Button(onClick = { launcher.launch(permissions) }) {
                    Text("Conceder permisos")
                }
            }
        }
    }
}
