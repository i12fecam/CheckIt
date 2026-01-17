package com.example.checkit.features.challenges.model

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkit.features.challenges.data.ChallengeDto
import com.example.checkit.features.challenges.data.ChallengeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// Representa el estado de la pantalla
data class MyChallengeListUiState(
    // CORRECCIÓN: La lista debe ser de ChallengeDto
    //val createdChallenges: List<ChallengeDto> = emptyList(),
    val savedChallenges: List<ChallengeDto> = emptyList(),
    val inProgressChallenges: List<ChallengeDto> = emptyList(),
    //val completedChallenges: List<ChallengeDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MyChallengeListViewModel @Inject constructor(
    private val challengeService: ChallengeService
) : ViewModel() {

    var uiState by mutableStateOf(MyChallengeListUiState())
        private set

    init {
        loadChallenges()
    }

    fun loadChallenges() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                // LLAMADAS REALES AL BACKEND (Controller: /my-created y /my-saved)
                //val created = challengeService.getMyCreatedChallenges()
                val inProgress = challengeService.getMyInProgressChallenges()
                val saved = challengeService.getMySavedChallenges()


                uiState = uiState.copy(
                    //createdChallenges = created,
                    inProgressChallenges = inProgress,
                    savedChallenges = saved,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Error al conectar con el servidor: ${e.message}"
                )
            }
        }
    }
}
