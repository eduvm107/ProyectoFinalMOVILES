package com.example.chatbot_diseo.presentation.recursos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.presentation.recursos.componentes.ResourceCard
import com.example.chatbot_diseo.presentation.recursos.componentes.ResourcesHeader

@Composable
fun Pantalla_de_Recurso(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .padding(16.dp)
    ){
        // Header arriba
        ResourcesHeader()

        Spacer(modifier = Modifier.height(8.dp))

        // Lista que ocupa el espacio restante
        LazyColumn (
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(2){ // Mostrar exactamente 2 recursos como solicitaste
                ResourceCard()
            }

        }
    }

}

@Preview(showBackground = true)
@Composable

fun Pantalla_de_RecursoPreview(){
    Pantalla_de_Recurso()
}