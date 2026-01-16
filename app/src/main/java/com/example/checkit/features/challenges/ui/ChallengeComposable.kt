package com.example.checkit.features.challenges.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChallengeComposable(
    title: String,
    description: String,
    authorName: String,
    peopleCompleted: Int,
    onSeeMore: () -> Unit,
    onSave:() -> Unit,
    showSaveButton: Boolean = false,
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(40.dp), // Esquinas muy redondeadas como en la imagen
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            // Fila de Título y "Ver más"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Desafío: $title",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF4A4A4A)
                    )
                )
                Text(
                    text = "Ver más",
                    color = Color(0xFFADB5BD), // Gris claro
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {  onSeeMore()}
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Información del desafío
            ChallengeInfoText(label = "Autor", content = authorName)
            ChallengeInfoText(label = "Descripción", content = description)
            ChallengeInfoText(label = "Personas que lo han completado", content = peopleCompleted.toString())

            Spacer(modifier = Modifier.height(20.dp))

            // Botón Guardar
            if(showSaveButton){
                Button(
                    onClick = {onSeeMore()},
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.6f) // El botón no ocupa todo el ancho
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8DC4B3) // El color FigmaTeal que definimos antes
                    ),
                    shape = RoundedCornerShape(24.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "Guardar Desafío",
                        color = Color(0xFF333333), // Texto oscuro sobre el verde
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

        }
    }

}




@Composable
fun ChallengeInfoText(label: String, content: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = "$label: ",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF4A4A4A)
            )
        )
        Text(
            text = content,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 18.sp // Mejora la lectura de la descripción
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChallengeCardPreview() {
    Box(Modifier.padding(10.dp)) {
        ChallengeComposable(
            title = "Patios Escondidos",
            authorName = "Turismo Córdoba",
            description = "Explora los rincones más coloridos y tradicionales. Encuentra los QR ocultos entre las flores y descubre la historia secreta que guardan los patios.",
            peopleCompleted = 154,
            onSeeMore = {},
            onSave = {},
            showSaveButton = false
        )
    }
}