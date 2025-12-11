package com.example.chatbot_diseo.presentation.footer

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chatbot_diseo.presentation.navigation.AppNavGraph

@Composable
fun PantallaPrincipal() {
    val context = LocalContext.current
    val navController = rememberNavController()

    var hasPermissions by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
        )
    }

    if (hasPermissions) {
        // Observar la ruta actual para mostrar/ocultar el BottomNavBar
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // Lista de rutas donde NO debe aparecer el BottomNavBar
        val routesWithoutBottomBar = listOf("login", "admin_panel")
        val showBottomBar = currentRoute !in routesWithoutBottomBar

        // Sistema de navegación principal
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    BottomNavBar(navController = navController)
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                AppNavGraph(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController
                )
            }
        }
    } else {
        // Pantalla de permisos sin BottomNavBar
        PermissionScreenForMain(
            modifier = Modifier.fillMaxSize(),
            onPermissionsGranted = { hasPermissions = true }
        )
    }
}

@Composable
fun PermissionScreenForMain(modifier: Modifier = Modifier, onPermissionsGranted: () -> Unit) {
    var shouldShowRationale by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions.values.all { it }) {
                onPermissionsGranted()
            } else {
                shouldShowRationale = permissions.entries.any { (perm, isGranted) ->
                    !isGranted && !(context as Activity).shouldShowRequestPermissionRationale(perm)
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR))
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Se necesitan permisos del calendario para usar la app.")
        Button(onClick = {
            if (shouldShowRationale) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", context.packageName, null)
                context.startActivity(intent)
            } else {
                permissionLauncher.launch(arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR))
            }
        }) {
            Text(if (shouldShowRationale) "Abrir configuración" else "Conceder permisos")
        }
    }
}

@Preview
@Composable
fun PantallaPrincipalPreview() {
    PantallaPrincipal()
}
