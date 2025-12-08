package com.example.checkit.core

import android.content.Context
import com.example.checkit.core.RetrofitClient.BASE_URL
import com.example.checkit.features.registration.data.AuthService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PublicClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProtectedClient


// New Qualifiers for Retrofit instances
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PublicRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProtectedRetrofit
object RetrofitClient {
    const val BASE_URL = "http://10.0.2.2:8080/"
    private val json = Json { ignoreUnknownKeys = true }
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManagerImpl(context)
    }


    // Cliente OkHttp para peticiones públicas (sin token)
    @Provides
    @Singleton
    @PublicClient
    fun providePublicHttpClient(): OkHttpClient {
        // Simple client without interceptor
        return OkHttpClient.Builder().build()
    }

    // Cliente OkHttp para peticiones protegidas (con token)
    @Provides
    @Singleton
    @ProtectedClient
    fun provideProtectedHttpClient(tokenManager: TokenManager): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager)) // <-- El interceptor añade el JWT
            // ... otros interceptors (logging)
            .build()
    }

    /**
     * Provides a singleton instance of the Json serializer.
     * This is needed by Retrofit's converter to parse JSON responses.
     */
    @Provides
    @Singleton // It's good practice to make it a singleton as it's thread-safe and reusable
    fun provideJson(): Json = Json {
        // You can configure the Json instance here if needed, for example:
        ignoreUnknownKeys = true // A common and useful configuration
        coerceInputValues = true // Another useful one for graceful error handling
    }
    // ----------------------------------------------------
    // PASO 4: Proveer Instancias de Retrofit
    // ----------------------------------------------------

    @Provides
    @Singleton
    @PublicRetrofit
    fun providePublicRetrofit(
        @PublicClient okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Apply the client HERE
            .addConverterFactory(json.asConverterFactory(contentType)) // And the converter HERE
            .build()
    }

    @Provides
    @Singleton
    @ProtectedRetrofit
    fun provideProtectedRetrofit(
        @ProtectedClient okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
    // ----------------------------------------------------
    // PASO 5: Proveer Servicios
    // ----------------------------------------------------

    // Servicio Público (usa el cliente público)

    @Provides
    @Singleton
    fun provideAuthService(
        @PublicRetrofit retrofit: Retrofit // Inject the fully configured Retrofit instance
    ): AuthService {
        return retrofit.create(AuthService::class.java)
    }


}