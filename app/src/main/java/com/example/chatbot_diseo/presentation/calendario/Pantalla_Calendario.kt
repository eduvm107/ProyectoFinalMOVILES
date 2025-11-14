package com.example.chatbot_diseo.presentation.calendario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.presentation.calendario.componentes.HeaderCalendario
import com.example.chatbot_diseo.presentation.calendario.componentes.NotificacionCard

@Composable
fun PantallaCalendario() {
    // 1. La Columna principal ocupa todo el espacio que le da su contenedor (el NavHost).
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .padding(16.dp)
    ) {
        // 2. El encabezado se muestra normalmente.
        HeaderCalendario()

        Spacer(modifier = Modifier.height(24.dp))

        // 3. La LazyColumn ocupa el espacio SOBRANTE gracias al weight(1f).
        //    Ahora SÍ funciona porque la Column padre tiene un tamaño definido y limitado.
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(2) { // Mostrar exactamente 2 notificaciones como solicitaste
                NotificacionCard()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaCalendarioPreview() {
    PantallaCalendario()
}
