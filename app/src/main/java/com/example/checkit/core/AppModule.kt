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

    // ----------------------------------------------------
    // PASO 4: Proveer Instancias de Retrofit
    // ----------------------------------------------------

    @Provides
    @Singleton
    fun provideRetrofitBuilder(json: Json): Retrofit.Builder {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
    }

    // ----------------------------------------------------
    // PASO 5: Proveer Servicios
    // ----------------------------------------------------

    // Servicio Público (usa el cliente público)
    @Provides
    @Singleton
    fun provideAuthService(
        retrofitBuilder: Retrofit.Builder,
        @PublicClient okHttpClient: OkHttpClient // Inyección calificada
    ): AuthService {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(AuthService::class.java)
    }
}