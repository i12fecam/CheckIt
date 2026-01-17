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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.size.ViewSizeResolver
import com.example.checkit.features.challenges.model.ChallengeTaskDetailViewModel
import com.example.checkit.features.profile.model.ProfileDetailViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeTaskDetailScreen(
    onBack: () -> Unit,
    viewModel: ChallengeTaskDetailViewModel = hiltViewModel()
) {
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
//                            Card(
//                                modifier = Modifier.size(260.dp, 140.dp),
//                                shape = RoundedCornerShape(24.dp),
//                                elevation = CardDefaults.cardElevation(8.dp)
//                            ) {
//                                AsyncImage(
//                                    model = "", // This is for the URL or the Base64 for the imagine
//                                    contentDescription = null,
//                                    modifier = Modifier.fillMaxSize(),
//                                    contentScale = ContentScale.Crop
//                                )
//                            }
                            Text(
                                text =viewModel.uiState.name ,
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
                        TaskInfoSection(label = "Autor", value = "Juan")
                        TaskInfoSection(label = "Completado por", value = "${viewModel.uiState.nCompletions} personas")
                        CompleteTaskSection(type = viewModel.uiState.type, onCompleteTask = { viewModel.completeTask() })
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
fun CompleteTaskSection(type: String,onCompleteTask :() -> Unit){
    if(type == "TEXT"){
        StyledTextField(value = "", onValueChange = {onCompleteTask()}, placeholder = "Respuesta", maxChar = 200)
        Button(
            onClick = { /* Logica eliminazione sfida salvata */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Eliminar Desafío Guardado", fontWeight = FontWeight.Bold)
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
