package com.example.checkit.features.challenges.data


//import androidx.compose.ui.graphics.Path
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.DELETE



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
    val imageBase64: String?,
    val isOrdered: Boolean = false,
    val tasks: List<TaskDto> = emptyList(),
    val authorName: String? = "CheckIt Team",
    val completionCount: Int = 0,
    val author: Boolean = false,   // AGGIUNTO: per far funzionare uiState.isAuthor
    val saved: Boolean = false
)
@Serializable
data class TaskDto(
    val id: Long,
    val name: String,
    // NUEVOS CAMPOS (Opcionales para no romper el listado anterior)
    val type: String? = null,
    val taskOrder: Int? = null,
    val description: String? = null, // AGGIUNTO: per la descrizione del task
    val isLocked: Boolean = false,    // AGGIUNTO: per il Punto 4
    val isCompleted: Boolean = false  // AGGIUNTO: per lo stato completato
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

@Serializable
data class TaskDetailDto(
    val challengeID: Long,
    val id: Long,
    val name: String,
    val taskOrder: Int,
    val textClue: List<String>,
    val nCompletions: Long,
    val completed: Boolean,
    val type: String,
    val authorName: String
)

@Serializable
data class CompleteTaskRequest(
    val userResponse: String,
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

    @GET("api/challenges/in-progress")
    suspend fun getMyInProgressChallenges(): List<ChallengeDto>

    // LISTAR todos los desafíos (Explorar)
    @GET("api/challenges")
    suspend fun getAllChallenges(): List<ChallengeDto>

    @POST("api/challenges/{id}/follow")
    suspend fun followChallenge(@Path("id") id: Long): BasicResponse

    @GET("api/challenges/{id}")
    suspend fun getChallengeById(@Path("id") id: Long): ChallengeDto

    @GET("api/tasks/{taskId}")
    suspend fun getTaskById(@Path("taskId")taskId: Long): TaskDetailDto
    @POST("api/tasks/{taskId}/complete")
    suspend fun completeTask(@Path("taskId") taskId: Long,@Body userResponse: String): BasicResponse
    @DELETE("api/challenges/{id}")
    suspend fun deleteChallenge(@Path("id") id: Long): BasicResponse
}