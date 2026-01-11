package com.example.checkit.features.challenges.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.checkit.features.challenges.data.TaskRequest
import com.example.checkit.features.challenges.model.NewChallengeEvent
import com.example.checkit.features.challenges.model.NewChallengeUiState
import com.example.checkit.features.challenges.model.NewChallengeViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.Close


val FigmaPurple = Color(0xFF6A4DFF)
val FigmaTeal = Color(0xFF8DC4B3)
val FigmaBlue = Color(0xFF3B7CFF)

@Composable
fun NewChallengeScreen(
    viewModel: NewChallengeViewModel = hiltViewModel(),
    onCreation: () -> Unit,
    onBack: () -> Unit
) {
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is NewChallengeEvent.createdCorrectly -> onCreation()
                is NewChallengeEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    NewChallengeContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onImageUrlChange = viewModel::onImageUrlChange,
        onOrderToggle = viewModel::onOrderToggle,
        onAddTask = viewModel::addTask,
        onUpdateTask = viewModel::updateTask,
        onRemoveTask = viewModel::removeTask,
        onSave = viewModel::saveChallenge,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewChallengeContent(
    uiState: NewChallengeUiState,
    snackbarHostState: SnackbarHostState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onImageUrlChange: (String) -> Unit,
    onOrderToggle: (Boolean) -> Unit,
    onAddTask: () -> Unit,
    onUpdateTask: (Int, TaskRequest) -> Unit,
    onRemoveTask: (Int) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {

    // Launcher per selezionare l'immagine dalla galleria
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            onImageUrlChange(uri.toString()) // Salviamo l'URI locale nello stato
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Nueva Sfida", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = FigmaPurple)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //HEADER IMMAGE
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp).background(FigmaPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier.size(260.dp, 150.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = FigmaTeal)
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            if (uiState.imageUrl.isNotEmpty()) {
                                AsyncImage(
                                    model = uiState.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Text("+ Añadir una Imagen", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    StyledTextField(
                        value = uiState.title,
                        onValueChange = onTitleChange,
                        placeholder = "Nombre del desafío",
                        label = "${uiState.title.length}/30"
                    )
                    StyledTextField(
                        value = uiState.description,
                        onValueChange = onDescriptionChange,
                        placeholder = "Descripción del desafío",
                        label = "${uiState.description.length}/250"
                    )
                }
            }

            // TASK LIST
            itemsIndexed(uiState.tasks) { index, task ->
                TaskCardRefined(
                    index = index,
                    task = task,
                    onUpdate = { updated -> onUpdateTask(index, updated) },
                    onRemove = { onRemoveTask(index) }
                )
            }

            // BOTTONI FINALI
            item {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onAddTask,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FigmaBlue),
                        shape = RoundedCornerShape(25.dp)
                    ) { Text("Añadir otra Tarea") }

                    Button(
                        onClick = onSave,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FigmaBlue),
                        shape = RoundedCornerShape(25.dp)
                    ) { Text("Crear Desafío") }
                }
            }
        }
    }
}


@Composable
fun StyledTextField(value: String, onValueChange: (String) -> Unit, placeholder: String, label: String) {
    Surface(shadowElevation = 4.dp, shape = RoundedCornerShape(25.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = { Text(label, fontSize = 10.sp, color = Color.Gray) },
            shape = RoundedCornerShape(25.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = FigmaBlue
            )
        )
    }
}

@Composable
fun TaskCardRefined(index: Int, task: TaskRequest, onUpdate: (TaskRequest) -> Unit, onRemove: () -> Unit) {
    Card(
        modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Tarea ${index + 1}:", fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(Modifier.width(8.dp))
                // Campo Titolo Task
                BasicTextField(
                    value = task.name,
                    onValueChange = { onUpdate(task.copy(name = it)) },
                    textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                }
            }

            // CAMPO PISTA (CLUE) - Richiesto dal Paper
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = task.textClue,
                onValueChange = { onUpdate(task.copy(textClue = it)) },
                placeholder = { Text("Descripción de la tarea / Pista", fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth().height(80.dp),
                shape = RoundedCornerShape(12.dp)
            )

            // Selettore Tipo (Incluso GPS come da Paper)
            Row(Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("QR", "NFC", "TEXT", "GPS").forEach { type ->
                    FilterChip(
                        selected = task.type == type,
                        onClick = { onUpdate(task.copy(type = type)) },
                        label = { Text(type, fontSize = 10.sp) }
                    )
                }
            }
        }
    }
}