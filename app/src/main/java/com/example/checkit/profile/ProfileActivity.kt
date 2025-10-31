
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.checkit.R

@Composable
fun ProfileScreen() {

    ProfileCard()


}
@Composable
fun ProfileCard(){
    Card (
        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,

            ) {
            Image(
                painter = painterResource(R.drawable.alquemista_recortada),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text= "Abigail Fernández Cabrera",
                    style= MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface )
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "Nivel 3",
                    style = MaterialTheme.typography.bodySmall, // Smaller, less important text
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
@Preview
@Composable
fun ProfilePreview(){
    ProfileCard()
}