package com.example.checkit.features.challenges.data


import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET

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
@Serializable
data class ChallengeDto(
    val id: Long,
    val name: String,
    val description: String? = null,
    val image: String? = null,
    val isOrdered: Boolean = false,
    val tasks: List<TaskDto> = emptyList()
)
@Serializable
data class TaskDto(
    val id: Long,
    val name: String
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
    suspend fun createChallenge(@Body loginRequest: CreateChallengeRequest): ChallengeDto
    // LISTAR desafíos creados por el usuario (GET)
    // Coincide con @GetMapping("/my-created") en tu controlador
    @GET("api/challenges/my-created")
    suspend fun getMyCreatedChallenges(): List<ChallengeDto>

    // LISTAR desafíos guardados/seguidos (GET)
    // Coincide con @GetMapping("/my-saved") en tu controlador
    @GET("api/challenges/my-saved")
    suspend fun getMySavedChallenges(): List<ChallengeDto>

    // LISTAR todos los desafíos (Explorar)
    @GET("api/challenges")
    suspend fun getAllChallenges(): List<ChallengeDto>


}