package com.example.checkit.core

import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 1. Retrieve the stored JWT (e.g., from Encrypted SharedPreferences/DataStore)
        val token = tokenManager.getAccessToken()

        // 2. Build the request with the Authorization header
        val newRequest = chain.request().newBuilder()
            .apply {
                if (token != null) {
                    // Set the header in the required Spring Security format: "Bearer <token>"
                    header("Authorization", "Bearer $token")
                }
            }
            .build()

        // 3. Proceed with the new request
        return chain.proceed(newRequest)
    }


}

