package com.example.checkit.features.challenges.ui

import androidx.compose.foundation.background
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

val CheckItBlue = Color(0xFF53678B)
val LightBackground = Color(0xFFF8FAFC)
val InputGray = Color(0xFFE2E8F0)

@Composable
fun NewChallengeScreen(
    viewModel: NewChallengeViewModel = hiltViewModel(),
    onCreation: () -> Unit
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
        onSave = viewModel::saveChallenge
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
    onSave: () -> Unit
) {
    Scaffold(
        containerColor = LightBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Nueva Sfida", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = CheckItBlue)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTask,
                containerColor = CheckItBlue,
                contentColor = Color.White,
                shape = CircleShape
            ) { Icon(Icons.Default.Add, contentDescription = "Añadir Tarea") }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.size(140.dp).clip(RoundedCornerShape(20.dp)).background(Color.White).padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)).background(Color(0xFFEDF2F7)),
                            contentAlignment = Alignment.Center
                        ) {
                            // Sostituita icona PhotoCamera con Edit (standard)
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(40.dp), tint = CheckItBlue)
                        }
                    }
                    Text("Subir Imagen", modifier = Modifier.padding(top = 8.dp), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    StyledTextField(value = uiState.title, onValueChange = onTitleChange, placeholder = "Titulo de la Sfida")
                    StyledTextField(value = uiState.description, onValueChange = onDescriptionChange, placeholder = "Descripción Desafio")
                    StyledTextField(value = uiState.imageUrl, onValueChange = onImageUrlChange, placeholder = "Autor (Opcional)")
                }
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("Tareas en Orden", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text(if(uiState.isOrdered) "SÍ " else "NO ", color = CheckItBlue, fontWeight = FontWeight.Bold)
                    Switch(checked = uiState.isOrdered, onCheckedChange = onOrderToggle)
                }
            }

            itemsIndexed(uiState.tasks) { index, task ->
                TaskCardRefined(index = index,
                    task = task,
                    onUpdate = { updated -> onUpdateTask(index, updated) },
                    onRemove = { onRemoveTask(index) })

            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onSave,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3498DB))
                ) {
                    Text("PUBLICAR DESAFÍO", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StyledTextField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.LightGray) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedBorderColor = InputGray,
            focusedBorderColor = CheckItBlue
        )
    )
}

@Composable
fun TaskCardRefined(index: Int, task: TaskRequest, onUpdate: (TaskRequest) -> Unit, onRemove: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CheckItBlue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("${index + 1}", color = CheckItBlue, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(12.dp))

                OutlinedTextField(
                    value = task.name,
                    onValueChange = { onUpdate(task.copy(name = it)) },
                    placeholder = { Text("Escribe el nombre o indicio...") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = InputGray,
                        focusedBorderColor = CheckItBlue
                    )
                )

                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.Red.copy(alpha = 0.7f))
                }
            }

            Text(
                "Metodo de Validación:",
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )

            // Selettore Tipologia migliorato (Touch targets più grandi)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf("TEXT", "QR", "NFC").forEach { type ->
                    FilterChip(
                        selected = task.type == type,
                        onClick = { onUpdate(task.copy(name = task.name, type = type)) },
                        label = { Text(type, fontSize = 12.sp) },
                        shape = RoundedCornerShape(8.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CheckItBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewChallengePreview() {
    // CORREZIONE: Inseriti tutti i parametri obbligatori per TaskRequest
    val exampleTask = TaskRequest(
        name = "Tarea Ejemplo",
        type = "QR",
        taskOrder = 1,
        qrAnswer = "",
        nfcAnswer = "",
        textAnswer = ""
    )

    NewChallengeContent(
        uiState = NewChallengeUiState(title = "Desafio Museo", tasks = listOf(exampleTask)),
        snackbarHostState = SnackbarHostState(),
        onTitleChange = {},
        onDescriptionChange = {},
        onImageUrlChange = {},
        onOrderToggle = {},
        onAddTask = {},
        onUpdateTask = { _, _ -> },
        onRemoveTask = {},
        onSave = {}
    )
}