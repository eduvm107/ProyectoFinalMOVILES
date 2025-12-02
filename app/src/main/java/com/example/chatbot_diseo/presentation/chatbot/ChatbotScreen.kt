package com.example.chatbot_diseo.presentation.chatbot

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ArrowForward

data class ChatMessage(
    val id: String,
    val sender: String, // "user" or "bot"
    val text: String
)

@Composable
fun ChatbotScreen(
    modifier: Modifier = Modifier,
    initialMessages: List<ChatMessage> = emptyList(),
    onNewChat: () -> Unit = {},
    onOpenMenu: (show: Boolean) -> Unit = {}
) {
    val messages = remember { mutableStateListOf<ChatMessage>().apply { addAll(initialMessages) } }
    var input by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }

    val unreadCount = messages.count { it.sender == "bot" } // demo

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .animateContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "TCS Assistant", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Siempre disponible para ti", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                    }
                }

                IconButton(onClick = { onNewChat() }) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = { showMenu = !showMenu; onOpenMenu(showMenu) }) {
                    Box(modifier = Modifier) {
                        Icon(
                            imageVector = if (showMenu) Icons.Default.Close else Icons.Default.Menu,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        if (!showMenu && unreadCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .align(Alignment.TopEnd)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.error)
                            ) {
                                Text(
                                    text = unreadCount.toString(),
                                    color = MaterialTheme.colorScheme.onError,
                                    fontSize = 10.sp,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }

            // Messages container
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                reverseLayout = false
            ) {
                items(messages, key = { it.id }) { message ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (message.sender == "user") Arrangement.End else Arrangement.Start
                    ) {
                        if (message.sender == "bot") {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🤖", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        // Bubble
                        val bubbleModifier = Modifier
                            .widthIn(max = 320.dp)
                            .wrapContentHeight()
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (message.sender == "user")
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp)

                        Box(modifier = bubbleModifier) {
                            Text(
                                text = message.text,
                                color = if (message.sender == "user") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 15.sp
                            )
                        }

                        if (message.sender == "user") {
                            Spacer(modifier = Modifier.width(8.dp))
                            // optional avatar for user
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                            ) {}
                        }
                    }
                }
            }

            // Input area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.Transparent),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(text = "Escribe un mensaje...") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = {
                    if (input.isNotBlank()) {
                        messages.add(ChatMessage(id = System.currentTimeMillis().toString(), sender = "user", text = input.trim()))
                        input = ""
                    }
                }) {
                    @Suppress("DEPRECATION")
                    Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = null)
                }
            }
        }
    }
}
// holamundo
@Preview(showBackground = true, widthDp = 380, heightDp = 800)
@Composable
fun ChatbotScreenPreview() {
    val sample = listOf(
        ChatMessage("1", "bot", "Hola, ¿en qué puedo ayudarte hoy?"),
        ChatMessage("2", "user", "Quiero información sobre el onboarding."),
        ChatMessage("3", "bot", "Claro. ¿Eres nuevo en la compañía?")
    )
    MaterialTheme {
        ChatbotScreen(initialMessages = sample)
    }
}
