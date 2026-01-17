package com.example.checkit.features.challenges.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.example.checkit.core.TokenManagerImpl
import com.example.checkit.features.challenges.data.BasicResponse
import com.example.checkit.features.challenges.data.ChallengeService
import com.example.checkit.features.profile.data.ProfileService
import com.example.checkit.features.profile.data.UserDetailsToChange
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlin.Long

// --- Data Classes for State and Events ---

// Represents the current state of the Login screen UI
data class ChallengeTaskDetailUiState(
    val challengeID: Long = -1,
    val id: Long = -1,
    val name: String = "Nombre de la tarea",
    val taskOrder: Int = 1,
    val textClue: List<String> = emptyList(),
    val nCompletions: Long = 0,
    val completed: Boolean = false,
    val type: String = "QR",
    val textClueRevealed: List<Boolean> = emptyList(),
    val response: String = ""
)

// Events that trigger a one-time action in the UI
sealed class ChallengeTaskDetailEvent {
    data object taskCompletedCorrectly : ChallengeTaskDetailEvent()
    data class ShowError(val message: String) : ChallengeTaskDetailEvent()
}

@HiltViewModel
class ChallengeTaskDetailViewModel @Inject constructor(
    private val challengeService: ChallengeService,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
): ViewModel(){
    private val TAG: String = "ChallengeTaskDetailViewModel"

    private val taskId: Long = savedStateHandle["taskId"] ?: 0L
    // --- UI State Management ---
//
    // Use mutableStateOf for data that the UI will observe and react to immediately
    var uiState by mutableStateOf(ChallengeTaskDetailUiState())
        private set
    //
    // Use SharedFlow for single-time events (like navigation or showing a Toast/Snackbar)
    private val _events = MutableSharedFlow<ChallengeTaskDetailEvent>()
    val events = _events.asSharedFlow()

    init{
        loadData()
    }
    fun loadData() {
        viewModelScope.launch {
            try {
                val taskDetail = challengeService.getTaskById(taskId)
                uiState = uiState.copy(
                    challengeID = taskDetail.challengeID,
                    id = taskDetail.id,
                    name = taskDetail.name,
                    taskOrder = taskDetail.taskOrder,
                    textClue = taskDetail.textClue,
                    textClueRevealed = List(taskDetail.textClue.size) { false },
                    nCompletions = taskDetail.nCompletions,
                    completed = taskDetail.completed,
                    type = taskDetail.type

                )
            } catch (e: Exception) {
                Log.e(TAG, "Error loading data", e)
            }

        }
    }





    fun onRevealClue(index: Int) {
        uiState = uiState.copy(textClueRevealed =  uiState.textClueRevealed.mapIndexed { i, item -> if (i == index) true else item })

    }







    fun completeTask() {
        viewModelScope.launch {
            // Simulate a network call via the repository
            try {
                val createChallengeResponse = challengeService.completeTask(taskId = uiState.id,
                    userResponse = uiState.response)

                _events.emit(ChallengeTaskDetailEvent.taskCompletedCorrectly)


            } catch (e: HttpException) {
                // 3. Handle HTTP errors (e.g., 401 Unauthorized, 404 Not Found, 500 Server Error)
                val errorBody = e.response()?.errorBody()?.string() ?: "Unknown HTTP error"
                _events.emit(ChallengeTaskDetailEvent.ShowError("Error HTTP: $errorBody"))
                Log.e(TAG, "Network error: Check your connection.", e) // 'e' includes the exception's stack trace in the log                _events.emit(LoginEvent.ShowError("Network error. Please check your connection."))
                // 4. Handle Network/IO errors (e.g., no internet connection, timeout)

            } catch (e: Exception) {
                // 5. Catch any other unexpected exceptions
                _events.emit(ChallengeTaskDetailEvent.ShowError("An unexpected error occurred."))
                Log.e(TAG, "An unexpected error occurred: ${e.message}", e)
            }


        }
    }


}