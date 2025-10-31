package com.example.checkit


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun ChallengeListScreen(message: String?) {
    val openDialog = remember { mutableStateOf(message!= null) }
    AlertaDesafioCompletado(message ?: "EJEMPLO", active = openDialog.value, onAccept = {
        openDialog.value = false
    })

    LazyColumn(
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ){
        val tasks = getChallengeTasks(20)
        items(tasks.count()){ index ->
            ChallengeListItem(id=index,
                order=index,
                name = "Tarea n${index}",
                icon=  if ( index%2 == 0) R.drawable.qr_code else R.drawable.photo_icon
            )

        }
    }
}

 @Composable
 fun ChallengeListItem(id: Int, order:Int, name: String, icon: Int) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().height(30.dp)
    ){
        Text( text="$order",
            style= MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary)

        Text( text = name,
            style= MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary)

        Icon(
            painter = painterResource(icon),
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = "A description of the custom icon",
            modifier = Modifier
        )

    }

 }

@Preview
@Composable
fun ChallengeListPreview(){
    //ChallengeListScreen()
}


fun getChallengeTasks(count:Int) : List<ChallengeTask>{
    return (1..count).map { index ->
        ChallengeTask(
            id = index,
            order = index, // The order is the same as the index
            taskName = "Task #$index: Complete step ${index}" // Programmatic name generation
        )
    }
}

data class ChallengeTask(
    val id: Int,
    val order: Int?,
    val taskName: String,

)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertaDesafioCompletado(nombreDesafio:String, active: Boolean, onAccept: () -> Unit){
    if(active){
        BasicAlertDialog(
            onAccept,
            Modifier, DialogProperties()){
            Surface(
                modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "¡Enhorabuena! Has completado el desafio '${nombreDesafio}'"

                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(
                        onClick = onAccept,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Aceptar")
                    }
                }
            }
        }
    }

}
