package com.example.checkit.features.profile.model

import ads_mobile_sdk.my
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
import com.example.checkit.core.TokenManagerImpl
import com.example.checkit.features.challenges.data.ChallengeDto
import com.example.checkit.features.challenges.data.ChallengeService
import com.example.checkit.features.profile.data.ProfileService
import com.example.checkit.features.profile.data.UserDetailsToChange
import dagger.hilt.android.qualifiers.ApplicationContext

// --- Data Classes for State and Events ---

// Represents the current state of the Login screen UI
data class ProfileDetailUiState(
    val realname: String = "username",
    val email: String = "email",
    val password: String = "password",
    val myChallenges: List<ChallengeDto> = emptyList()
)

// Events that trigger a one-time action in the UI
sealed class ProfileDetailEvent {
    data object createdCorrectly : ProfileDetailEvent()
    data class ShowError(val message: String) : ProfileDetailEvent()
}

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val profileService: ProfileService,
    private val tokenManagerImpl: TokenManagerImpl,
    private val challengeService: ChallengeService,
    @ApplicationContext private val context: Context
): ViewModel(){
    private val TAG: String = "ProfileDetailViewModel"

    // --- UI State Management ---
//
    // Use mutableStateOf for data that the UI will observe and react to immediately
    var uiState by mutableStateOf(ProfileDetailUiState())
        private set
    //
    // Use SharedFlow for single-time events (like navigation or showing a Toast/Snackbar)
    private val _events = MutableSharedFlow<ProfileDetailEvent>()
    val events = _events.asSharedFlow()

    init{
        loadData()
    }
    fun loadData() {
        viewModelScope.launch {
            try {
                val userDetails = profileService.getOwnUserDetails()
                val myChallenges = challengeService.getMyCreatedChallenges()

                uiState = uiState.copy(
                    realname = userDetails.username,
                    password = "",
                    email = userDetails.email,
                    myChallenges = myChallenges
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error loading data", e)
            }

        }
    }


    //
//    // --- Event Handlers (User Input) ---
//

    fun onRealNameChange(input: String) {
        uiState = uiState.copy(realname = input)
    }

    fun onPasswordChange(input: String) {
        uiState = uiState.copy(password = input)
    }



    fun logout() {
        viewModelScope.launch {
                tokenManagerImpl.clearTokens();
        }
    }





    fun changePassword() {
        // Prevent multiple simultaneous login attempts


        viewModelScope.launch {
            // Simulate a network call via the repository
            try {


                val createChallengeRequest = UserDetailsToChange(
                    realName = uiState.realname,
                    password = uiState.password
                )


                val createChallengeResponse = profileService.changeOwnUserPassword(createChallengeRequest)

                when (createChallengeResponse.errorMessage) {
                    null -> {
                        // Successful
                        _events.emit(ProfileDetailEvent.createdCorrectly)
                    }
                    else -> {
                        // Failed
                        _events.emit(ProfileDetailEvent.ShowError(createChallengeResponse.errorMessage))
                    }
                }

            } catch (e: HttpException) {
                // 3. Handle HTTP errors (e.g., 401 Unauthorized, 404 Not Found, 500 Server Error)
                val errorBody = e.response()?.errorBody()?.string() ?: "Unknown HTTP error"
                _events.emit(ProfileDetailEvent.ShowError("Error HTTP: $errorBody"))
                Log.e(TAG, "Network error: Check your connection.", e) // 'e' includes the exception's stack trace in the log                _events.emit(LoginEvent.ShowError("Network error. Please check your connection."))
                // 4. Handle Network/IO errors (e.g., no internet connection, timeout)

            } catch (e: Exception) {
                // 5. Catch any other unexpected exceptions
                _events.emit(ProfileDetailEvent.ShowError("An unexpected error occurred."))
                Log.e(TAG, "An unexpected error occurred: ${e.message}", e)
            }


        }
    }


}