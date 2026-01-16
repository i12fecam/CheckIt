package com.example.checkit.features.profile.data



import com.example.checkit.features.challenges.data.BasicResponse
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

//Define what a single Task is for the Backend
@Serializable
data class OwnUserDetails(
    val username: String,
    val email: String
)

@Serializable
data class UserDetailsToChange(
    val realName: String,
    val password: String
)


interface ProfileService {

    @GET("api/user/info")
    suspend fun getOwnUserDetails(): OwnUserDetails

    @POST("api/user/change-details")
    suspend fun changeOwnUserPassword(@Body userDetailsToChange: UserDetailsToChange): BasicResponse
}


