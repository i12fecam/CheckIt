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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.BorderStroke


val FigmaPurple = Color(0xFF6A4DFF)
val FigmaTeal = Color(0xFF8DC4B3)
val FigmaBlue = Color(0xFF3B7CFF)

@Composable
fun NewChallengeScreen(
    viewModel: NewChallengeViewModel = hiltViewModel(),
    onCreation: (Long) -> Unit,
    onBack: () -> Unit
) {
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is NewChallengeEvent.createdCorrectly -> onCreation(event.challengeId)
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
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> if (uri != null) onImageUrlChange(uri.toString()) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { /* Titolo rimosso come richiesto */ },
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
        // Usiamo una Column per separare la lista dai pulsanti statici
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            LazyColumn(
                modifier = Modifier.weight(1f), // La lista occupa lo spazio restante e scorre
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // HEADER IMMAGE CON DECORAZIONI
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(240.dp).background(FigmaPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        // Cerchi decorativi sfocati (Foto 2)
                        Box(modifier = Modifier.size(150.dp).offset(x = (-100).dp, y = (-50).dp).background(Color.White.copy(alpha = 0.15f), CircleShape))
                        Box(modifier = Modifier.size(100.dp).offset(x = 80.dp, y = 40.dp).background(Color.White.copy(alpha = 0.1f), CircleShape))

                        Card(
                            onClick = { launcher.launch("image/*") },
                            modifier = Modifier.size(280.dp, 160.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = FigmaTeal),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                if (uiState.imageUrl.isNotEmpty()) {
                                    AsyncImage(model = uiState.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                                } else {
                                    Text("+ Añadir una Imagen", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                }
                            }
                        }
                    }
                }

                // CAMPI TITOLO E DESCRIZIONE SFIDA
                item {
                    Column(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        StyledTextField(value = uiState.title, onValueChange = onTitleChange, placeholder = "Nombre del desafío", maxChar = 30)
                        StyledTextField(value = uiState.description, onValueChange = onDescriptionChange, placeholder = "Descripción del desafío", maxChar = 250)
                    }
                }

                // LISTA DELLE TASK (SCORREVOLE)
                itemsIndexed(uiState.tasks) { index, task ->
                    TaskCardRefined(index = index, task = task, onUpdate = { updated -> onUpdateTask(index, updated) }, onRemove = { onRemoveTask(index) })
                }
            }

            // PULSANTI STATICI (RIMANGONO FISSI IN FONDO)
            Column(
                modifier = Modifier.fillMaxWidth().background(Color.White).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onAddTask,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FigmaBlue),
                    shape = RoundedCornerShape(25.dp)
                ) { Text("Añadir altra Tarea", fontWeight = FontWeight.Bold) }

                Button(
                    onClick = onSave,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FigmaBlue),
                    shape = RoundedCornerShape(25.dp)
                ) { Text("Crear Desafío", fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
fun StyledTextField(value: String, onValueChange: (String) -> Unit, placeholder: String, maxChar: Int) {
    Surface(shadowElevation = 6.dp, shape = RoundedCornerShape(50.dp), modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = { if (it.length <= maxChar) onValueChange(it) },
            placeholder = { Text(placeholder, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Text("${value.length}/$maxChar", fontSize = 12.sp, color = Color.LightGray, modifier = Modifier.padding(end = 16.dp))
            },
            shape = RoundedCornerShape(50.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = FigmaBlue,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )
    }
}

@Composable
fun TaskCardRefined(index: Int, task: TaskRequest, onUpdate: (TaskRequest) -> Unit, onRemove: () -> Unit) {
    Card(
        modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // RIGA SUPERIORE: NOME TASK IN PILLOLA
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Tarea ${index + 1}: ", color = Color.DarkGray, fontSize = 14.sp)
                Surface(shape = RoundedCornerShape(50), color = Color(0xFFE0E0E0), modifier = Modifier.height(32.dp)) {
                    BasicTextField(
                        value = task.name,
                        onValueChange = { onUpdate(task.copy(name = it)) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp).widthIn(min = 100.dp),
                        textStyle = TextStyle(fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onRemove) { Icon(Icons.Default.Close, contentDescription = null, tint = Color.Gray) }
            }

            Spacer(Modifier.height(12.dp))

            // BOX DESCRIZIONE: GRIGIO PIÙ SCURO
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFE2E8F0), // Colore leggermente più scuro per contrasto
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(12.dp)) {
                    BasicTextField(
                        value = task.textClue,
                        onValueChange = { if (it.length <= 200) onUpdate(task.copy(textClue = it)) },
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                        decorationBox = { innerTextField ->
                            if (task.textClue.isEmpty()) {
                                // PLACEHOLDER PIÙ VISIBILE
                                Text("Descripción de la tarea", color = Color(0xFF616161), fontSize = 13.sp)
                            }
                            innerTextField()
                        }
                    )
                }
            }

           // PUNTO 1: CAMPO RISPOSTA CORRETTA (SOLO PER TEXT) ---
            if (task.type == "TEXT") {
                Spacer(Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = FigmaTeal.copy(alpha = 0.1f), // Colore leggermente diverso per distinguerlo
                    border = BorderStroke(1.dp, FigmaTeal),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(12.dp)) {
                        BasicTextField(
                            value = task.textAnswer ?: "",
                            onValueChange = { onUpdate(task.copy(textAnswer = it)) },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Medium),
                            decorationBox = { innerTextField ->
                                if (task.textAnswer.isNullOrEmpty()) {
                                    Text("Escribe aquí la respuesta correcta", color = Color.Gray, fontSize = 13.sp)
                                }
                                innerTextField()
                            }
                        )
                    }
                }
            }

            // TESTO TIPO TAREA PIÙ EVIDENTE
            Text(
                "Tipo de tarea:",
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold
            )

            // CHIP PER LE TECNOLOGIE
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Escaneo QR", "NFC", "Entrada de Texto").forEach { label ->
                    val typeMap = mapOf("Escaneo QR" to "QR", "NFC" to "NFC", "Entrada de Texto" to "TEXT")
                    FigmaChip(text = label, selected = task.type == typeMap[label]) {
                        //reset della risposta se si cambia tipo, per pulizia dati
                        onUpdate(task.copy(
                            type = typeMap[label] ?: "TEXT",
                            textAnswer = if (typeMap[label] == "TEXT") task.textAnswer else null
                        ))
                    }
                }
            }
        }
    }
}

@Composable
fun FigmaChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (selected) FigmaTeal else Color(0xFFE0E0E0).copy(alpha = 0.5f),
        shape = RoundedCornerShape(50),
        modifier = Modifier.height(36.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold, // Testo reso più visibile
            color = if (selected) Color.White else Color.Black
        )
    }
}