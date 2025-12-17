package com.example.checkit.features.registration.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest


@Composable
fun RegistrationScreen( onRegister: () -> Unit, onNavigateToLogin: () -> Unit,viewModel: RegistrationViewModel = hiltViewModel()){
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current // Needed for showing Toasts or other context-based UI

    // --- SOLUTION START ---
    // This effect will listen for one-time events from the ViewModel
    LaunchedEffect(key1 = Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is RegistrationEvent.NavigateToLogin -> {
                    // Call the navigation callback provided to the screen
                    onRegister()
                }
                is RegistrationEvent.ShowError -> {
                    // Show a snackbar with the error message
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
            text = "Registrarse como nuevo usuario",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Username TextField
        OutlinedTextField(
            value = uiState.username,
            onValueChange = {viewModel.onUsernameChange(it)},
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true,
        )

        // Email TextField
        OutlinedTextField(
            value = uiState.email,
            onValueChange = {viewModel.onEmailChange(it)},
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true
        )

        // Password TextField
        OutlinedTextField(
            value = uiState.password,
            onValueChange = {viewModel.onPasswordChange(it)},
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )



        // Sign In Button
        Button(
            onClick = { viewModel.register() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrarse")
        }

        // Register link
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tienes una cuenta ya?",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onNavigateToLogin() }
        )
    }
}

@Preview
@Composable
fun RegistrationPreview(){
    RegistrationScreen(onNavigateToLogin = {}, onRegister = {})
}