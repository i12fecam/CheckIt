@Composable
fun LoginScreen() {
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
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Image (You can use your own image here)
        Image(
            painter = painterResource(id = R.drawable.login_image), // Placeholder image
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
            color = MaterialTheme.colors.primary,
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
            color = MaterialTheme.colors.primary,
            modifier = Modifier.clickable { /* Handle registration */ }
        )
    }
}
