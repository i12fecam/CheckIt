package com.example.checkit.features.challenges.ui

import com.example.checkit.features.challenges.model.NewChallengeViewModel



import android.annotation.SuppressLint

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.checkit.features.challenges.model.NewChallengeEvent

import kotlinx.coroutines.flow.collectLatest


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewChallengeScreen( viewModel: NewChallengeViewModel = hiltViewModel(),onCreation: () -> Unit) {
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current // Needed for showing Toasts or other context-based UI

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
        snackbarHost = {SnackbarHost(hostState = snackbarHostState)}
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
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

            // Description TextField
            OutlinedTextField(
                value = uiState.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            )



            // Sign In Button
            Button(
                onClick = { viewModel.saveChallenge() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Crear Desafio")
            }


        }
    }
}

@Preview
@Composable
fun NewChallengePreview(){
    NewChallengeScreen(onCreation = {})
}
