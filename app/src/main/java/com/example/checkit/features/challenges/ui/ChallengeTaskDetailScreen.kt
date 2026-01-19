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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.size.ViewSizeResolver
import com.example.checkit.features.challenges.model.ChallengeTaskDetailEvent
import com.example.checkit.features.challenges.model.ChallengeTaskDetailViewModel
import com.example.checkit.features.profile.model.ProfileDetailViewModel
import com.example.checkit.features.registration.ui.LoginEvent
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeTaskDetailScreen(
    onBack: () -> Unit,
    viewModel: ChallengeTaskDetailViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ChallengeTaskDetailEvent.taskCompletedCorrectly -> {
                    // Call the navigation callback provided to the screen
                }
                is ChallengeTaskDetailEvent.ShowError -> {
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
        containerColor = Color(0xFFF8FAFC),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
//                topBar = {
//            // Header viola che si integra con il design dell'app
//            TopAppBar(
//                title = { },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = FigmaPurple)
//            )
//        }
    ) { padding ->
        
        // Layout a colonna per mantenere il pulsante fisso in fondo
        Box (modifier = Modifier.fillMaxSize()){

            Column(modifier = Modifier.fillMaxSize()) {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    // SEZIONE IMMAGINE E TITOLO
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .background(FigmaPurple),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = viewModel.uiState.name,
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
                            //Poner de que challenge es
                            TaskInfoSection(label = "Autor", value = "Juan")
                            TaskInfoSection(
                                label = "Completado por",
                                value = "${viewModel.uiState.nCompletions} personas"
                            )
                            ClueSection(
                                viewModel.uiState.textClue,
                                viewModel.uiState.textClueRevealed,
                                viewModel::onRevealClue
                            )

                            TaskInfoSection(
                                label = "Estado",
                                value = if (viewModel.uiState.completed == true) "Completada" else "Pendiente"
                            )
                            if (!viewModel.uiState.completed) {
                                CompleteTaskSection(
                                    type = viewModel.uiState.type,
                                    onCompleteTask = { viewModel.completeTaskInScope() },
                                    responseValue = viewModel.uiState.response,
                                    onResponseChange = { viewModel.onResponseChange(it) })
                            }
                        }
                    }
                }


            }
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(top = 12.dp, start = 8.dp) // Adjust for status bar if needed
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

// COMPONENTI DI SUPPORTO CON ANNOTAZIONE @Composable

@Composable
fun TaskInfoSection(label: String, value: String) {
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
fun ClueSection(clues: List<String> , revealed : List<Boolean>,onRevealClue: (Int) -> Unit){
    Text("Pistas", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 8.dp, bottom = 4.dp))

    clues.forEachIndexed { index, clue ->
        ClueItemSection(clue = clue,
            revealed = revealed[index],
            onClick = {onRevealClue(index)}
        )
    }
}
@Composable
fun ClueItemSection(clue: String, revealed: Boolean,onClick: () -> Unit){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(25.dp),
        shadowElevation = 4.dp,
        color = Color.White
    ) {
        if(revealed) {
            Text(
                text = clue,
                modifier = Modifier.padding(16.dp),
                fontSize = 13.sp,
                color = Color.DarkGray
            )
        }else{
            Text(
                text = "Pulsa para revelar pista",
                modifier = Modifier.padding(16.dp),
                fontSize = 13.sp,
                color = Color.DarkGray
            )
        }
    }
}
@Composable
fun CompleteTaskSection(type: String,responseValue: String ,onCompleteTask :() -> Unit,onResponseChange: (String) -> Unit){
    if(type == "TEXT"){
        StyledTextField(value = responseValue, onValueChange = { onResponseChange(it) }, placeholder = "Respuesta", maxChar = 200)
        Button(
            onClick = { onCompleteTask() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Solucionar desafio", fontWeight = FontWeight.Bold)
        }
    } else if (type == "QR") {
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Escanea el QR desde otra aplicación", fontWeight = FontWeight.Bold)
        }
    } else if (type == "NFC") {
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Escanea el NFC", fontWeight = FontWeight.Bold)
        }
    }



}



//@Composable
//fun StyledTextField(value: String, onValueChange: (String) -> Unit, placeholder: String, maxChar: Int) {
//    Surface(shadowElevation = 6.dp, shape = RoundedCornerShape(50.dp), modifier = Modifier.fillMaxWidth()) {
//        OutlinedTextField(
//            value = value,
//            onValueChange = { if (it.length <= maxChar) onValueChange(it) },
//            placeholder = { Text(placeholder, color = Color.Gray) },
//            modifier = Modifier.fillMaxWidth(),
//            trailingIcon = {
//                Text("${value.length}/$maxChar", fontSize = 12.sp, color = Color.LightGray, modifier = Modifier.padding(end = 16.dp))
//            },
//            shape = RoundedCornerShape(50.dp),
//            colors = OutlinedTextFieldDefaults.colors(
//                unfocusedBorderColor = Color.Transparent,
//                focusedBorderColor = FigmaBlue,
//                unfocusedContainerColor = Color.White,
//                focusedContainerColor = Color.White
//            )
//        )
//    }
//}
