package com.example.checkit.features.challenges.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.checkit.features.challenges.model.ExploreChallengesViewModel

@Composable
fun ExploreChallengesScreen(
    viewModel: ExploreChallengesViewModel = hiltViewModel(),
    onChallengeClick: (Long) -> Unit
) {
    val uiState = viewModel.uiState

    Scaffold(
        containerColor = Color(0xFFF7F7F7)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 80.dp) // Espacio para el menú inferior
        ) {
            // CABECERA CON TÍTULO Y BUSCADOR
            item {
                ExploreHeader(
                    query = uiState.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange
                )
            }

            // SECCIÓN DE TÍTULO
            item {
                Text(
                    text = "Desafíos Destacados:",
                    modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 8.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
            }

            // LISTADO DE DESAFÍOS (Usando tu componente)
            if (uiState.isLoading) {
                item {
                    Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF6A4DFF))
                    }
                }
            } else {
                items(uiState.filteredChallenges, key = { it.id }) { challenge ->
                    ChallengeComposable(
                        title = challenge.name,
                        description = challenge.description ?: "Sin descripción",
                        authorName = challenge.authorName ?: "CheckIt Team",
                        peopleCompleted = challenge.completionCount,
                        onSeeMore = { onChallengeClick(challenge.id) },
                        onSave = {
                            // Al ejecutar esto, el ViewModel filtrará la lista y el item desaparecerá
                            viewModel.saveChallenge(challenge.id)
                        },
                        showSaveButton = true
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreHeader(query: String, onQueryChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(Color(0xFF6A4DFF)), // figmaPurple
        contentAlignment = Alignment.TopCenter
    ) {
        // Círculos decorativos (Réplica de tu diseño anterior)
        Box(Modifier.size(180.dp).offset(x = (-100).dp, y = (-20).dp).background(Color.White.copy(alpha = 0.15f), CircleShape))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 40.dp)
        ) {
            Text(
                text = "Desafíos",
                color = Color(0xFF333333),
                fontSize = 44.sp,
                fontWeight = FontWeight.ExtraBold,
                textDecoration = TextDecoration.Underline // Subrayado como en la imagen
            )

            Spacer(Modifier.height(24.dp))

            // BARRA DE BÚSQUEDA REDONDEADA
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                TextField(
                    value = query,
                    onValueChange = onQueryChange,
                    placeholder = { Text("Nombre del desafío", color = Color.Gray) },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = Color.Gray
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

}

