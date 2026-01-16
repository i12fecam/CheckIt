package com.example.checkit.features.registration.data

import android.R
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

// Request body for /api/auth/login
@Serializable
data class LoginRequest(
    val email: String, // Matches the field name expected by Spring Boot
    val password: String
)

// Response body from /api/auth/login
@Serializable
data class LoginResponse(
    val errorResponse: String?,
    val token: String?,
    val username: String?
    // You can add expiration time or other user details here
)
@Serializable
data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String
)
@Serializable
data class RegisterResponse(
    val errorMessage: String?,
)



interface AuthService {

    /**
     * Sends a POST request to the Spring Boot login endpoint (/api/auth/login).
     * @param loginRequest The username and password pair.
     * @return The response containing the JWT and user data.
     */
    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    // You would add a registration function here as well:
    @POST("api/auth/register")
     suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse
}