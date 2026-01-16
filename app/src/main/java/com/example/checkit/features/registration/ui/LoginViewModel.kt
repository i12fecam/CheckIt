package com.example.checkit.features.registration.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkit.core.TokenManager
import com.example.checkit.features.registration.data.AuthService
import com.example.checkit.features.registration.data.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
// --- Data Classes for State and Events ---

// Represents the current state of the Login screen UI
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false, // To show a progress indicator
)

// Events that trigger a one-time action in the UI
sealed class LoginEvent {
    data object NavigateToHome : LoginEvent()
    data class ShowError(val message: String) : LoginEvent()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val tokenManager: TokenManager
): ViewModel(){
    private val TAG: String = "LoginViewModel"

    // --- UI State Management ---
//
    // Use mutableStateOf for data that the UI will observe and react to immediately
    var uiState by mutableStateOf(LoginUiState())
        private set
//
    // Use SharedFlow for single-time events (like navigation or showing a Toast/Snackbar)
    private val _events = MutableSharedFlow<LoginEvent>()
    val events = _events.asSharedFlow()
//
//    // --- Event Handlers (User Input) ---
//
    fun onEmailChange(input: String) {
        uiState = uiState.copy(email = input)
    }

    fun onPasswordChange(input: String) {
        uiState = uiState.copy(password = input)
    }

    // --- Login Logic ---

    fun login() {
        // Prevent multiple simultaneous login attempts
        if (uiState.isLoading) return

        // Simple validation check
        if (uiState.email.isBlank() || uiState.password.isBlank()) {
            viewModelScope.launch {
                _events.emit(LoginEvent.ShowError("Username and password cannot be empty."))
            }
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true) // Start loading state

            // Simulate a network call via the repository
            try {
                val loginRequest = LoginRequest(uiState.email,uiState.password)

                val loginResponse = authService.login(loginRequest)

                uiState = uiState.copy(isLoading = false) // Stop loading state
                when (loginResponse.errorResponse) {
                    null -> {
                        // Successful
                        tokenManager.saveTokens("${loginResponse.token}","${loginResponse.username}")
                        _events.emit(LoginEvent.NavigateToHome)
                    }
                    else -> {
                        // Failed
                        _events.emit(LoginEvent.ShowError(loginResponse.errorResponse))
                    }
                }

            } catch (e: HttpException) {
            // 3. Handle HTTP errors (e.g., 401 Unauthorized, 404 Not Found, 500 Server Error)
                val errorBody = e.response()?.errorBody()?.string() ?: "Unknown HTTP error"
                _events.emit(LoginEvent.ShowError("Login failed: $errorBody"))
                Log.e(TAG, "Network error: Check your connection.", e) // 'e' includes the exception's stack trace in the log                _events.emit(LoginEvent.ShowError("Network error. Please check your connection."))
            // 4. Handle Network/IO errors (e.g., no internet connection, timeout)

        } catch (e: Exception) {
            // 5. Catch any other unexpected exceptions
                _events.emit(LoginEvent.ShowError("An unexpected error occurred."))
                Log.e(TAG, "An unexpected error occurred: ${e.message}", e)
        }


        }
    }

}