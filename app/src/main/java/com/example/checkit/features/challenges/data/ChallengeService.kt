package com.example.checkit.features.challenges.data


import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST
@Serializable
// Request body for creating challenge
data class CreateChallengeRequest(
    val name: String,
    val description: String,
)
@Serializable
data class BasicResponse(
    val errorMessage: String?

)

interface ChallengeService {

    
    @POST("api/challenges")
    suspend fun createChallenge(@Body loginRequest: CreateChallengeRequest): BasicResponse

}