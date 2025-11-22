package com.example.chatbot_diseo.presentation.recursos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.presentation.recursos.componentes.ResourceCard
import com.example.chatbot_diseo.presentation.recursos.componentes.ResourceListFromApi
import com.example.chatbot_diseo.presentation.recursos.componentes.ResourcesHeader

@Composable
fun Pantalla_de_Recurso() {
    var selectedFilter by remember { mutableStateOf("Todos") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .padding(16.dp)
    ) {
        ResourcesHeader(
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        ResourceListFromApi(
            modifier = Modifier.weight(1f),
            selectedFilter = selectedFilter
        )
    }
}

@Preview(showBackground = true)
@Composable

fun Pantalla_de_RecursoPreview(){
    Pantalla_de_Recurso()
}
