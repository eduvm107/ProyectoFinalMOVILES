package com.example.chatbot_diseo.presentation.admin.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chatbot_diseo.data.admin.ActivityItem
import com.example.chatbot_diseo.ui.theme.*

@Composable
fun AdminActivityCard(
    item: ActivityItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit

) {



    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = TcsWhite),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(item.title, color = TcsTextDark)
            Text("${item.date} • ${item.modality}", color = TcsTextLight)

            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = null, tint = TcsBlue)
                }
                TextButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = null, tint = TcsRed)
                }
            }
        }
    }
}
