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

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->

        // Este Box es el "lienzo" para nuestro contenido. NO debe tener scroll.
        Box(modifier = Modifier.padding(innerPadding)) {
            if (hasPermissions) {
                AppNavGraph(
                    navController = navController,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                PermissionScreenForMain(
                    modifier = Modifier.fillMaxSize(),
                    onPermissionsGranted = { hasPermissions = true }
                )
            }
        }
    }
}

// --- El resto del código no cambia y es correcto ---

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

@Composable
fun PlaceholderScreen(screenName: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Pantalla de $screenName")
    }
}

@Preview
@Composable
fun PantallaPrincipalPreview() {
    PantallaPrincipal()
}
