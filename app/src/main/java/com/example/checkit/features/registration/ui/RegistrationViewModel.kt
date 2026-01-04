package com.example.checkit.features.registration.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkit.features.registration.data.AuthService
import com.example.checkit.features.registration.data.LoginRequest
import com.example.checkit.features.registration.data.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
// --- Data Classes for State and Events ---

// Represents the current state of the Login screen UI
data class RegistrationUiState(
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val isLoading: Boolean = false, // To show a progress indicator
)

// Events that trigger a one-time action in the UI
sealed class RegistrationEvent {
    data object NavigateToLogin : RegistrationEvent()
    data class ShowError(val message: String) : RegistrationEvent()
}

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authService: AuthService,
): ViewModel(){
    private val TAG: String = "RegistrationViewModel"

    // --- UI State Management ---
//
    // Use mutableStateOf for data that the UI will observe and react to immediately
    var uiState by mutableStateOf(RegistrationUiState())
        private set
//
    // Use SharedFlow for single-time events (like navigation or showing a Toast/Snackbar)
    private val _events = MutableSharedFlow<RegistrationEvent>()
    val events = _events.asSharedFlow()
//
//    // --- Event Handlers (User Input) ---
//
    fun onUsernameChange(input: String) {
        uiState = uiState.copy(username = input)
    }

    fun onPasswordChange(input: String) {
        uiState = uiState.copy(password = input)
    }

    fun onEmailChange(input: String) {
        uiState = uiState.copy(email = input)
    }


    // --- Login Logic ---

    fun register() {
        // Prevent multiple simultaneous login attempts
        if (uiState.isLoading) return

        // Simple validation check
        if (uiState.username.isBlank() || uiState.password.isBlank() || uiState.email.isBlank()) {
            viewModelScope.launch {
                _events.emit(RegistrationEvent.ShowError("Username and password cannot be empty."))
            }
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true) // Start loading state

            // Simulate a network call via the repository
            try {
                val registrationRequest = RegisterRequest(uiState.username, uiState.password,uiState.email)

                val registerResponse = authService.register(registrationRequest)
                uiState = uiState.copy(isLoading = false) // Stop loading state

                if (registerResponse.errorMessage == null){
                    _events.emit(RegistrationEvent.NavigateToLogin)
                } else{
                    _events.emit(RegistrationEvent.ShowError(registerResponse.errorMessage))
                }


            } catch (e: HttpException) {
            // 3. Handle HTTP errors (e.g., 401 Unauthorized, 404 Not Found, 500 Server Error)
                val errorBody = e.response()?.errorBody()?.string() ?: "Unknown HTTP error"
                _events.emit(RegistrationEvent.ShowError("Login failed: $errorBody"))
                Log.e(TAG, "Network error: Check your connection.", e) // 'e' includes the exception's stack trace in the log                _events.emit(LoginEvent.ShowError("Network error. Please check your connection."))
            // 4. Handle Network/IO errors (e.g., no internet connection, timeout)

        } catch (e: Exception) {
            // 5. Catch any other unexpected exceptions
                _events.emit(RegistrationEvent.ShowError("An unexpected error occurred."))
                Log.e(TAG, "An unexpected error occurred: ${e.message}", e)
        }


        }
    }

}