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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbot_diseo.presentation.recursos.componentes.ResourceListFromApi
import com.example.chatbot_diseo.presentation.recursos.componentes.ResourcesHeader
import com.example.chatbot_diseo.presentation.favoritos.FavoritosViewModel

@Composable
fun Pantalla_de_Recurso() {
    var selectedFilter by remember { mutableStateOf("Todos") }

    // Instanciar FavoritosViewModel para poder pasarlo a ResourceListFromApi
    val favoritosViewModel: FavoritosViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA)) // fondo igual que Activities (#F5F7FA)
            .padding(16.dp)
    ) {
        ResourcesHeader(
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        ResourceListFromApi(
            modifier = Modifier.weight(1f),
            selectedFilter = selectedFilter,
            favoritosViewModel = favoritosViewModel
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Pantalla_de_RecursoPreview(){
    Pantalla_de_Recurso()
}
