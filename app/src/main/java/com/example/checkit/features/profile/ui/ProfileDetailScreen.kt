import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.checkit.features.challenges.ui.FigmaPurple
import com.example.checkit.features.challenges.ui.FigmaTeal
import com.example.checkit.features.challenges.ui.StyledTextField
import com.example.checkit.features.profile.model.ProfileDetailEvent
import com.example.checkit.features.profile.model.ProfileDetailViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    onCloseSession: () -> Unit, viewModel: ProfileDetailViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current // Needed for showing Toasts or other context-based UI

    LaunchedEffect(key1 = Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ProfileDetailEvent.createdCorrectly -> {
                    // Call the navigation callback provided to the screen
                }
                is ProfileDetailEvent.ShowError -> {
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
        containerColor = Color(0xFFF8F9FF), // Un blanco azulado muy suave para resaltar tarjetas
        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text("Mi Perfil", color = Color.White, fontWeight = FontWeight.Bold) },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
//                    }
//                },
//                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = FigmaPurple)
//            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.padding(padding)
                .verticalScroll(rememberScrollState()) // Scroll simple para perfil
        ) {
            // --- HEADER DE PERFIL ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(FigmaPurple),
                contentAlignment = Alignment.Center
            ) {
                // Decoraciones circulares de fondo
                Box(modifier = Modifier.size(180.dp).offset(x = (-120).dp, y = (-60).dp).background(Color.White.copy(alpha = 0.1f), CircleShape))
                Box(modifier = Modifier.size(120.dp).offset(x = 130.dp, y = 50.dp).background(Color.White.copy(alpha = 0.08f), CircleShape))

                // Foto de Perfil Circular
                Box(contentAlignment = Alignment.BottomEnd) {
                    Surface(
                        modifier = Modifier
                            .size(130.dp)
                            .padding(4.dp),
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 8.dp
                    ) {
//                        if (uiState.imageUrl.isNotEmpty()) {
//                            AsyncImage(
//                                model = uiState.imageUrl,
//                                contentDescription = "Profile Photo",
//                                modifier = Modifier.clip(CircleShape),
//                                contentScale = ContentScale.Crop
//                            )
//                        } else {
                            // Placeholder si no hay imagen
                            Box(Modifier.fillMaxSize().background(FigmaTeal), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                            }
//                        }
                    }
//                    // Botón flotante para editar foto
//                    SmallFloatingActionButton(
//                        onClick = { launcher.launch("image/*") },
//                        containerColor = FigmaBlue,
//                        contentColor = Color.White,
//                        shape = CircleShape,
//                        modifier = Modifier.size(40.dp).offset(x = (-4).dp, y = (-4).dp)
//                    ) {
//                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(20.dp))
//                    }
                }
            }

            // --- CAMPOS DE EDICIÓN ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Información Personal",
                    style = MaterialTheme.typography.titleLarge, // Ya incluye peso y tamaño óptimo
                    color = MaterialTheme.colorScheme.onSurface, // Color adaptativo (oscuro en modo claro)
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Reutilizamos tu StyledTextField pero con labels
                Column {
                    Text("Nombre Completo", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 12.dp, bottom = 8.dp))
                    StyledTextField(value = uiState.realname, onValueChange ={ viewModel.onRealNameChange(it)}, placeholder = "Ej. Juan Pérez", maxChar = 30)
                }

                Column {
                    Text("Email", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 12.dp, bottom = 8.dp))
                    StyledTextField(value = uiState.email, onValueChange ={}, placeholder = "thomas.a.hendricks@example.com", maxChar = 30)
                }
                Column {
                    Text("Contraseña", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 12.dp, bottom = 8.dp))
                    StyledTextField(value = uiState.password, onValueChange = {viewModel.onPasswordChange(it)}, placeholder = "********", maxChar = 250)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Botón de Guardar
                Button(
                    onClick = {viewModel.changePassword()},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FigmaPurple),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text("Actualizar Perfil", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                // Botón de logout
                Button(
                    onClick = {
                        viewModel.logout();
                        onCloseSession()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text("Cerrar sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}