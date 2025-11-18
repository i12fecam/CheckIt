package com.example.checkit

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.checkit.R



@Composable
fun LoginScreen(onLoginSuccess: Any?) {
    // Column to arrange elements vertically
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

        // Image (You can use your own image here)
        Image(
            painter = painterResource(id = R.drawable.alquemista_recortada), // Placeholder image
            contentDescription = "Login Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(bottom = 24.dp)
        )

        // Email TextField
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true
        )

        // Password TextField
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        // Forgot Password link
        Text(
            text = "¿Olvidaste la contraseña?",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Sign In Button
        Button(
            onClick = { /* Handle login */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Iniciar Sesión")
        }

        // Register link
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "¿No tienes cuenta? Registrarme",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { /* Handle registration */ }
        )
    }
}
