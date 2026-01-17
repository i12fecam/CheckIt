package com.example.checkit.features.challenges.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Importación corregida para listas dinámicas
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.checkit.features.challenges.data.ChallengeDto
import com.example.checkit.features.challenges.model.MyChallengeListViewModel

// Colores con minúscula para evitar conflictos con otros archivos del paquete
val figmaPurple = Color(0xFF6A4DFF)
val figmaTeal = Color(0xFF8DC4B3)
val figmaBlue = Color(0xFF3B7CFF)

@Composable
fun MyChallengeListScreen(
    viewModel: MyChallengeListViewModel = hiltViewModel(),
    onChallengeClick: (Long) -> Unit
) {
    val uiState = viewModel.uiState

    Scaffold(
        containerColor = Color(0xFFF7F7F7)
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = figmaPurple)
            }
        } else if (uiState.error != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.error!!, color = Color.Red)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                // Cabecera de usuario
                item { ChallengeHeaderSection("Ana Gómez") }

                /* Sección: Completados
                if (uiState.createdChallenges.isNotEmpty()) {
                    item { SectionTitle("Mis Desafíos: Creados") }
                    // Usamos items() directamente para mejor rendimiento en Compose
                    items(uiState.createdChallenges) { challenge ->
                        ChallengeListContainer {
                            ChallengeItemRow(challenge, onChallengeClick)
                        }
                    }
                }
                */
                //Desafíos en progreso
                if (uiState.inProgressChallenges.isNotEmpty()) {
                    item { SectionTitle("Mis Desafíos: En progreso") }
                    items(uiState.inProgressChallenges) { challenge ->
                        ChallengeListContainer { ChallengeItemRow(challenge, onChallengeClick) }
                    }
                }

                // Sección: Guardados
                if (uiState.savedChallenges.isNotEmpty()) {
                    item { SectionTitle("Mis Desafíos: Guardados") }
                    items(uiState.savedChallenges) { challenge ->
                        ChallengeListContainer {
                            ChallengeItemRow(challenge, onChallengeClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChallengeItemRow(challenge: ChallengeDto, onClick: (Long) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Desafío: ${challenge.name}", fontWeight = FontWeight.Bold)
            Text(
                text = "Ver más",
                color = figmaBlue,
                fontSize = 12.sp,
                modifier = Modifier.clickable { onClick(challenge.id) }
            )
        }
        Text(
            text = "Descripción: ${challenge.description ?: "Sin descripción"}",
            fontSize = 12.sp,
            color = Color.Gray
        )
        Text(
            text = "Tareas: ${challenge.tasks.size}",
            fontSize = 12.sp,
            color = Color.Gray
        )
        // Usamos HorizontalDivider de Material 3
        HorizontalDivider(
            Modifier.padding(top = 12.dp),
            thickness = 0.5.dp,
            color = Color.LightGray
        )
    }
}

@Composable
fun ChallengeListContainer(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp) // Añadido un pequeño margen vertical
            .fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) { content() }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 8.dp),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun ChallengeHeaderSection(name: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(figmaPurple),
        contentAlignment = Alignment.Center
    ) {
        // Círculos decorativos
        Box(
            modifier = Modifier
                .size(150.dp)
                .offset(x = (-100).dp, y = (-50).dp)
                .background(Color.White.copy(alpha = 0.15f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .offset(x = 80.dp, y = 40.dp)
                .background(Color.White.copy(alpha = 0.1f), CircleShape)
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Placeholder para imagen de perfil
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = Color.LightGray,
                border = androidx.compose.foundation.BorderStroke(3.dp, figmaTeal)
            ) { }
            Spacer(Modifier.height(12.dp))
            Text(
                "Bienvenido, $name",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}