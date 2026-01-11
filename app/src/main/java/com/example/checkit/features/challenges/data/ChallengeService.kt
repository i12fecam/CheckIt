package com.example.checkit.features.challenges.data


import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

//Define what a single Task is for the Backend
@Serializable
data class TaskRequest(
    val name: String,
    val type: String, // "QR", "NFC", "TEXT"
    val taskOrder: Int,
    // Optional fields for responses (nullable depending on the type)
    val qrAnswer: String? = null,
    val nfcAnswer: String? = null,
    val textClue: String = "", // Added for Clues
    val textAnswer: String? = null
)

//Update the main request to include everything
@Serializable
// Request body for creating challenge
data class CreateChallengeRequest(
    val name: String,
    val description: String,
    val isOrdered: Boolean,       // <-- Added
    val tasks: List<TaskRequest>,  // <-- Added
    val imageBase64: String? = null // <--Field for the image
)

@Serializable
data class BasicResponse(
    val errorMessage: String?

)

interface ChallengeService {

    @POST("api/challenges")
    suspend fun createChallenge(@Body loginRequest: CreateChallengeRequest): BasicResponse

}