package com.example.checkit.features.challenges.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.checkit.features.challenges.model.ChallengeDetailState
import com.example.checkit.features.challenges.model.ChallengeDetailViewModel
import com.example.checkit.features.challenges.model.TaskDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeDetailScreen(
    onBack: () -> Unit,
    onDeleteSuccess: () -> Unit,
    onTaskClick: (Long) -> Unit = {},
    viewModel: ChallengeDetailViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val isAuthor = viewModel.isAuthor
    val FigmaPurple = Color(0xFF6A4DFF)

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
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(bottom = 16.dp)) {
                item {
                    ChallengeHeader(name = uiState.name, imageUrl = "", figmaPurple = FigmaPurple)
                }
                item {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        InfoSection(label = "Autor", value = uiState.author)
                        InfoSection(label = "Descripción", value = uiState.description)
                        InfoSection(label = "Completado por", value = "${uiState.completedByCount} personas")

                        Text("Tareas: En progreso", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        uiState.tasksInProgress.forEach { task ->
                            TaskDetailItem(title = task.name,
                                status = "active",
                                desc = task.description,
                                type = task.type,
                                onTaskClick = {onTaskClick(task.id)})

                        }
                    }
                }
            }

            if (isAuthor) {
                Button(
                    onClick = { viewModel.deleteChallenge(onSuccess = onDeleteSuccess) },
                    modifier = Modifier.fillMaxWidth().padding(20.dp).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B)),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    if (uiState.isLoading) CircularProgressIndicator(color = Color.White)
                    Text("Eliminar Desafío", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// COMPONENTI DI SUPPORTO

@Composable
fun ChallengeHeader(name: String, imageUrl: String, figmaPurple: Color) {
    Box(
        modifier = Modifier.fillMaxWidth().height(240.dp).background(figmaPurple),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Card(
                modifier = Modifier.size(260.dp, 140.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                AsyncImage(model = imageUrl, contentDescription = null, contentScale = ContentScale.Crop)
            }
            Text(text = name, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 12.dp))
        }
    }
}
@Composable
fun InfoSection(label: String, value: String){
    Column {
        Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 8.dp, bottom = 4.dp))
        Surface(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            shape = RoundedCornerShape(25.dp),
            shadowElevation = 4.dp,
            color = Color.White
        ) {
            Text(text = value, modifier = Modifier.padding(16.dp), fontSize = 13.sp, color = Color.DarkGray)
        }
    }
}

@Composable
fun TaskDetailItem(title: String, status: String, desc: String, type: String, onTaskClick : () -> Unit) {

    val alpha = if (status == "locked") 0.5f else 1f
    Surface(
        modifier = Modifier.fillMaxWidth().alpha(alpha).clickable(enabled = status != "locked", onClick = {onTaskClick()}),
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 2.dp,
        color = Color.White,
        //border = if (status == "completed") BorderStroke(1.dp, Color.LightGray) else null
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    // MOSTRA IL TIPO DI TAREA (PUNTO 5)
                    Text(
                        text = "Tipo: $type",
                        fontSize = 10.sp,
                        color = FigmaPurple,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (status == "completed") {
                    Text("¡Finalizada!", color = Color.Gray, fontSize = 10.sp, fontStyle = FontStyle.Italic)
                }
                if (status == "locked") Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Bloqueado",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(text = desc, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
        }
    }
}