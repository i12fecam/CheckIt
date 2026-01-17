package com.example.checkit.features.registration.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(onLogin: () -> Unit, onNavigateToRegistration: () -> Unit, viewModel: LoginViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current // Needed for showing Toasts or other context-based UI

    // --- SOLUTION START ---
    // This effect will listen for one-time events from the ViewModel
    LaunchedEffect(key1 = Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is LoginEvent.NavigateToHome -> {
                    // Call the navigation callback provided to the screen
                    onLogin()
                }
                is LoginEvent.ShowError -> {
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
                text = "¡Bienvenido de nuevo!",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

//            // Image (You can use your own image here)
//            Image(
//                painter = painterResource(id = R.drawable.alquemista_recortada), // Placeholder image
//                contentDescription = "Login Image",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(150.dp)
//                    .padding(bottom = 24.dp)
//            )

            // Email TextField
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )

            // Password TextField
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )


            // Sign In Button
            Button(
                onClick = { viewModel.login() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Iniciar Sesión")
            }

            // Register link
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "¿No tienes cuenta? Registrarme",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onNavigateToRegistration() }
            )
        }
    }
}

@Preview
@Composable
fun LoginPreview(){
    LoginScreen(onLogin = {}, onNavigateToRegistration = {})
}
