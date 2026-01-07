package com.example.checkit.features.challenges.ui

import com.example.checkit.features.challenges.model.NewChallengeViewModel



import android.annotation.SuppressLint

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.checkit.features.challenges.data.TaskRequest
import com.example.checkit.features.challenges.model.NewChallengeEvent

import kotlinx.coroutines.flow.collectLatest


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewChallengeScreen( viewModel: NewChallengeViewModel = hiltViewModel(),onCreation: () -> Unit) {
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    //val context = LocalContext.current // Needed for showing Toasts or other context-based UI

    // --- SOLUTION START ---
    // This effect will listen for one-time events from the ViewModel
    LaunchedEffect(key1 = Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is NewChallengeEvent.createdCorrectly -> {
                    // Call the navigation callback provided to the screen
                    onCreation()
                }
                is NewChallengeEvent.ShowError -> {
                    // Show a snackbar with the error message
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
    Scaffold(
        snackbarHost = {SnackbarHost(hostState = snackbarHostState)},
        // NEW: Floating Action Button (FAB) to add Tasks
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.addTask() }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Tarea")
            }
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = "Nuevo Desafio",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )


            // Title TextField
            OutlinedTextField(
                value = uiState.title,
                onValueChange = { viewModel.onTitleChange(it) },
                label = { Text("Nombre") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Description TextField
            OutlinedTextField(
                value = uiState.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            )

            // NEW: Switch for the order
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(text = "¿Orden secuencial?", modifier = Modifier.weight(1f))
                Switch(
                    checked = uiState.isOrdered,
                    onCheckedChange = { viewModel.onOrderToggle(it) }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(text = "Tareas (${uiState.tasks.size})", style = MaterialTheme.typography.titleMedium)

            // --- 2. Dynamic Task List ---
            // Use LazyColumn to enable scrolling when there are many tasks
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // Prende tutto lo spazio disponibile
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(uiState.tasks) { index, task ->
                    TaskItemCard(
                        index = index,
                        task = task,
                        onUpdate = { updatedTask -> viewModel.updateTask(index, updatedTask) },
                        onRemove = { viewModel.removeTask(index) }
                    )
                }
            }


            // Sign In Button
            Button(
                onClick = { viewModel.saveChallenge() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(text = "Crear Desafio")
            }

        }
    }
}

// --- NEW COMPONENT: The "Card" for each single Task ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItemCard(
    index: Int,
    task: TaskRequest,
    onUpdate: (TaskRequest) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Top Row: Number, Name, and Delete Button
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "#${index + 1}",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(30.dp)
                )
                OutlinedTextField(
                    value = task.name,
                    onValueChange = { onUpdate(task.copy(name = it)) },
                    label = { Text("Nombre de la tarea") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Red)
                }
            }

            // Type Selector (TEXT / QR / NFC)
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                listOf("TEXT", "QR", "NFC").forEach { type ->
                    FilterChip(
                        selected = task.type == type,
                        onClick = { onUpdate(task.copy(type = type)) },
                        label = { Text(type) }
                    )
                }
            }

            // Dynamic field for the correct answer
            val label = when(task.type) {
                "QR" -> "Contenido del código QR"
                "NFC" -> "ID del tag NFC"
                else -> "Respuesta correcta (Texto)"
            }

            // Logic to update the relevant field based on type
            val currentAnswer = when(task.type) {
                "QR" -> task.qrAnswer ?: ""
                "NFC" -> task.nfcAnswer ?: ""
                else -> task.textAnswer ?: ""
            }

            OutlinedTextField(
                value = currentAnswer,
                onValueChange = { newValue ->
                    val updatedTask = when(task.type) {
                        "QR" -> task.copy(qrAnswer = newValue)
                        "NFC" -> task.copy(nfcAnswer = newValue)
                        else -> task.copy(textAnswer = newValue)
                    }
                    onUpdate(updatedTask)
                },
                label = { Text(label) },
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                singleLine = true
            )
        }
    }
}

@Preview
@Composable
fun NewChallengePreview(){
    NewChallengeScreen(onCreation = {})
}
