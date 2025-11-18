package com.example.checkit.core

import android.content.Context
import com.example.checkit.features.registration.data.AuthService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

//object RetrofitClient {
//    private const val BASE_URL = "http://10.0.2.2:8080/"
//    private val json = Json { ignoreUnknownKeys = true }
//
//    // --- Authentication/Public Client ---
//
//    // Used ONLY for /api/auth/login and /api/auth/register
//    val publicAuthService: AuthService by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(OkHttpClient.Builder().build()) // Simple client without interceptor
//            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
//            .build()
//            .create(AuthService::class.java)
//    }
//
//    // --- Protected Client Setup (Requires TokenManager) ---
//    fun createProtectedHttpClient(tokenManager: TokenManager): OkHttpClient {
//        return OkHttpClient.Builder()
//            .addInterceptor(AuthInterceptor(tokenManager)) // <-- CRITICAL: Adds JWT
//            // ... other interceptors (logging)
//            .build()
//    }
//
////    // 2. Function to create any protected service
////    inline fun <reified T> createProtectedService(client: OkHttpClient): T {
////        return Retrofit.Builder()
////            .baseUrl(BASE_URL)
////            .client(client)
////            // ... converters
////            .build()
////            .create(T::class.java)
////    }
////     val protectedClient = createProtectedHttpClient(TokenManagerImpl(context = ))
//}