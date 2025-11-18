package com.example.checkit.registration

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.checkit.R
import com.example.checkit.Registration


@Composable
fun RegistrationScreen( onRegister: () -> Unit, onNavigateToLogin: () -> Unit){
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
            value = "",
            onValueChange = {},
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true
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



        // Sign In Button
        Button(
            onClick = { onRegister() },
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