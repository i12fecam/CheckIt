package com.example.checkit.features.challenges.model
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.checkit.features.challenges.data.ChallengeDto
import com.example.checkit.features.challenges.data.ChallengeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExploreUiState(
    val challenges: List<ChallengeDto> = emptyList(),
    val filteredChallenges: List<ChallengeDto> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ExploreChallengesViewModel @Inject constructor(
    private val challengeService: ChallengeService
) : ViewModel() {

    var uiState by mutableStateOf(ExploreUiState())
        private set

    init {
        loadChallenges()
    }

    private fun loadChallenges() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val result = challengeService.getAllChallenges()
                uiState = uiState.copy(
                    challenges = result,
                    filteredChallenges = result,
                    isLoading = false
                )
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, error = e.localizedMessage)
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        val filtered = if (query.isEmpty()) {
            uiState.challenges
        } else {
            uiState.challenges.filter { it.name.contains(query, ignoreCase = true) }
        }
        uiState = uiState.copy(searchQuery = query, filteredChallenges = filtered)
    }

    fun saveChallenge(challengeId: Long) {
        viewModelScope.launch {
            try {
                // Llamada al backend para guardar
                val response = challengeService.followChallenge(challengeId)

                if (response.errorMessage == null) {
                    // ÉXITO: Quitamos el desafío de la lista actual para que desaparezca de la pantalla
                    val newList = uiState.challenges.filter { it.id != challengeId }
                    uiState = uiState.copy(
                        challenges = newList,
                        filteredChallenges = newList.filter {
                            it.name.contains(uiState.searchQuery, ignoreCase = true)
                        }
                    )
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "Error al guardar")
            }
        }
    }
}
