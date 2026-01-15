package com.example.checkit.features.challenges.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// IMPORT DEL MODELLO
import com.example.checkit.features.challenges.model.ChallengeDetailState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeDetailScreen(
    onBack: () -> Unit
) {
    // Dati simulati basati sul Paper e sui Mockup
    val uiState = ChallengeDetailState(
        name = "Ruta Romana",
        author = "Museo Arqueológico de Córdoba",
        creationDate = "20 de Noviembre de 2025",
        description = "Descubre el legado romano de Córdoba completando una serie de tareas. ¡Demuestra tus conocimientos históricos!",
        completedByCount = 76
    )

    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        topBar = {
            // Header viola che si integra con il design dell'app
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FigmaPurple)
            )
        }
    ) { padding ->
        // Layout a colonna per mantenere il pulsante fisso in fondo
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // SEZIONE IMMAGINE E TITOLO
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(240.dp).background(FigmaPurple),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Card(
                                modifier = Modifier.size(260.dp, 140.dp),
                                shape = RoundedCornerShape(24.dp),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ) {
                                AsyncImage(
                                    model = "", // This is for the URL or the Base64 for the imagine
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Text(
                                text = uiState.name,
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }
                    }
                }

                // DETTAGLI DELLA SFIDA
                item {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InfoSection(label = "Autor", value = uiState.author)
                        InfoSection(label = "Fecha de creación", value = uiState.creationDate)
                        InfoSection(label = "Descripción", value = uiState.description)
                        InfoSection(label = "Completado por", value = "${uiState.completedByCount} personas")

                        // PROGRESSO TAREAS
                        Text("Tareas: En progreso", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        TaskDetailItem(title = "Tarea 3: Voces en Latín", status = "active", desc = "Busca la inscripción romana en la sala central.")
                        TaskDetailItem(title = "Tarea 4: [BLOQUEADO]", status = "locked", desc = "Completa la tarea anterior para desbloquear")

                        Text("Tareas: Completadas", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        TaskDetailItem(title = "Tarea 1: La Puerta del Imperio", status = "completed", desc = "Entrada principal del museo.")
                    }
                }
            }

            // PULSANTE ELIMINA STATICO (Sempre visibile in fondo)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Transparent
            ) {
                Button(
                    onClick = { /* Logica eliminazione sfida salvata */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B)),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("Eliminar Desafío Guardado", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// COMPONENTI DI SUPPORTO CON ANNOTAZIONE @Composable

@Composable
fun InfoSection(label: String, value: String) {
    Column {
        Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 8.dp, bottom = 4.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            shadowElevation = 4.dp,
            color = Color.White
        ) {
            Text(text = value, modifier = Modifier.padding(16.dp), fontSize = 13.sp, color = Color.DarkGray)
        }
    }
}

@Composable
fun TaskDetailItem(title: String, status: String, desc: String) {
    val alpha = if (status == "locked") 0.5f else 1f
    Surface(
        modifier = Modifier.fillMaxWidth().alpha(alpha),
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 2.dp,
        color = Color.White,
        border = if (status == "completed") BorderStroke(1.dp, Color.LightGray) else null
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.weight(1f))
                if (status == "completed") {
                    Text("¡Finalizada!", color = Color.Gray, fontSize = 10.sp, fontStyle = FontStyle.Italic)
                }
                if (status == "locked") Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
            }
            Text(text = desc, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
        }
    }
}