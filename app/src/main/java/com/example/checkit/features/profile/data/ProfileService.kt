package com.example.checkit.features.profile.data



import com.example.checkit.features.challenges.data.BasicResponse
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

//Define what a single Task is for the Backend
@Serializable
data class OwnUserDetails(
    val id: Long,
    val username: String
)

@Serializable
data class ChangePasswordRequest(
    val newPassword: String
)


interface ProfileService {

    @GET("api/user/info")
    suspend fun getOwnUserDetails(): OwnUserDetails

    @POST("api/user/change-password")
    suspend fun changeOwnUserPassword(@Body changePasswordRequest: ChangePasswordRequest): BasicResponse
}


