package com.example.checkit.features.challenges.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkit.features.challenges.data.ChallengeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChallengeDetailState(
    val name: String = "Cargando...",
    val author: String = "",
    val creationDate: String = "",
    val description: String = "",
    val completedByCount: Int = 0,
    val tasksInProgress: List<TaskDetail> = emptyList(),
    val tasksCompleted: List<TaskDetail> = emptyList(),
    val isSaved: Boolean = false,
    val isAuthor: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isOwner: Boolean = false
)

data class TaskDetail(
    val id: Long,
    val name: String,
    val description: String,
    val isLocked: Boolean = false,
    val isCompleted: Boolean = false
)

@HiltViewModel
class ChallengeDetailViewModel @Inject constructor(
    private val challengeService: ChallengeService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val challengeId: Long = savedStateHandle["challengeId"] ?: 0L

    var uiState by mutableStateOf(ChallengeDetailState())
        private set

    var isAuthor by mutableStateOf(false)
        private set

    init {
        loadChallengeDetails()
    }

    private fun loadChallengeDetails() {
        viewModelScope.launch {
            try {
                val challenge = challengeService.getChallengeDetail(challengeId)
                uiState = ChallengeDetailState(
                    isLoading = false,
                    name = challenge.name,
                    author = challenge.authorName ?: "CheckIt Team",
                    description = challenge.description ?: "",
                    completedByCount = challenge.completionCount,
                    tasksInProgress = challenge.tasks.map {
                        TaskDetail(it.id, it.name, "Instrucciones de la tarea")
                    }
                )
                // Simulazione logica autore
                isAuthor = challenge.authorName == "admin@checkit.com"
            } catch (e: Exception) {
                uiState = ChallengeDetailState(
                    isLoading = false,
                    errorMessage = "Error loading challenge details")
            }
        }
    }

    fun deleteChallenge(onSuccess: () -> Unit) {
        if (!uiState.isAuthor) return // Sicurezza: solo l'autore può procedere

        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true)
                val response = challengeService.deleteChallenge(challengeId)

                if (response.errorMessage == null) {
                    // Se non c'è errore nel backend, eseguiamo l'azione di successo
                    onSuccess()
                } else {
                    uiState = uiState.copy(isLoading = false, errorMessage = response.errorMessage)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, errorMessage = "Error al intentar eliminar el desafío")
            }
        }
    }
}