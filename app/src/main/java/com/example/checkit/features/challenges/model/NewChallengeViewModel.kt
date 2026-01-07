package com.example.checkit.features.challenges.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkit.features.challenges.data.ChallengeService
import com.example.checkit.features.challenges.data.CreateChallengeRequest
import com.example.checkit.features.challenges.data.TaskRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
// --- Data Classes for State and Events ---

// Represents the current state of the Login screen UI
data class NewChallengeUiState(
    val title: String = "",
    val description: String = "",
    val isOrdered: Boolean = true,
    val tasks: List<TaskRequest> = emptyList() // Dinamic List of tasks
)

// Events that trigger a one-time action in the UI
sealed class NewChallengeEvent {
    data object createdCorrectly : NewChallengeEvent()
    data class ShowError(val message: String) : NewChallengeEvent()
}

@HiltViewModel
class NewChallengeViewModel @Inject constructor(
    private val challengeService: ChallengeService,
): ViewModel(){
    private val TAG: String = "NewChallengeViewModel"

    // --- UI State Management ---
//
    // Use mutableStateOf for data that the UI will observe and react to immediately
    var uiState by mutableStateOf(NewChallengeUiState())
        private set
    //
    // Use SharedFlow for single-time events (like navigation or showing a Toast/Snackbar)
    private val _events = MutableSharedFlow<NewChallengeEvent>()
    val events = _events.asSharedFlow()
    //
//    // --- Event Handlers (User Input) ---
//
    fun onTitleChange(input: String) {
        uiState = uiState.copy(title = input)
    }

    fun onDescriptionChange(input: String) {
        uiState = uiState.copy(description = input)
    }

    fun onOrderToggle(ordered: Boolean) {
        uiState = uiState.copy(isOrdered = ordered)
    }

    // --- Dynamic Task Handling ---
    fun addTask() {
        val newTask = TaskRequest(
            name = "Nueva Tarea ${uiState.tasks.size + 1}", //Default Name
            type = "TEXT", // Default
            taskOrder = uiState.tasks.size + 1,
            qrAnswer = null,
            nfcAnswer = null,
            textAnswer = "" // Epty for deafault if is a TEXT
        )
        uiState = uiState.copy(tasks = uiState.tasks + newTask)
    }

    fun removeTask(index: Int) {
        // Removes and recalculates order if necessary
        val newList = uiState.tasks.toMutableList()
        newList.removeAt(index)
        // Optional: Recalculate taskOrder to maintain the sequence 1, 2, 3...
        val reorderedList = newList.mapIndexed { i, task -> task.copy(taskOrder = i + 1) }
        uiState = uiState.copy(tasks = reorderedList)
    }

    fun updateTask(index: Int, updatedTask: TaskRequest) {
        val newList = uiState.tasks.toMutableList()
        newList[index] = updatedTask
        uiState = uiState.copy(tasks = newList)
    }

    // --- Login Logic ---
    fun saveChallenge() {
        // Prevent multiple simultaneous login attempts

        // Simple validation check
        if (uiState.title.isBlank()) {
            viewModelScope.launch {
                _events.emit(NewChallengeEvent.ShowError("El titulo no puede estar vacio."))
            }
            return
        }

        // Additional check: There must be at least one task (as per PDF)
        if (uiState.tasks.isEmpty()) {
            viewModelScope.launch {
                _events.emit(NewChallengeEvent.ShowError("Añade al menos una tarea."))
            }
            return
        }

        viewModelScope.launch {
            // Simulate a network call via the repository
            try {
                val createChallengeRequest = CreateChallengeRequest(
                    uiState.title,
                    uiState.description,
                    isOrdered = uiState.isOrdered,
                    tasks = uiState.tasks,
                )


                val createChallengeResponse = challengeService.createChallenge(createChallengeRequest)

                when (createChallengeResponse.errorMessage) {
                    null -> {
                        // Successful
                        _events.emit(NewChallengeEvent.createdCorrectly)
                    }
                    else -> {
                        // Failed
                        _events.emit(NewChallengeEvent.ShowError(createChallengeResponse.errorMessage))
                    }
                }

            } catch (e: HttpException) {
                // 3. Handle HTTP errors (e.g., 401 Unauthorized, 404 Not Found, 500 Server Error)
                val errorBody = e.response()?.errorBody()?.string() ?: "Unknown HTTP error"
                _events.emit(NewChallengeEvent.ShowError("Login failed: $errorBody"))
                Log.e(TAG, "Network error: Check your connection.", e) // 'e' includes the exception's stack trace in the log                _events.emit(LoginEvent.ShowError("Network error. Please check your connection."))
                // 4. Handle Network/IO errors (e.g., no internet connection, timeout)

            } catch (e: Exception) {
                // 5. Catch any other unexpected exceptions
                _events.emit(NewChallengeEvent.ShowError("An unexpected error occurred."))
                Log.e(TAG, "An unexpected error occurred: ${e.message}", e)
            }


        }
    }

}