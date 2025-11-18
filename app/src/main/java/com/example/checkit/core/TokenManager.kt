//package com.example.checkit.core
//
//import android.content.Context
//import android.content.SharedPreferences
//import androidx.security.crypto.EncryptedSharedPreferences
//import androidx.security.crypto.MasterKeys
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//
///**
// * Interface for managing JWT and Refresh tokens securely.
// */
//interface TokenManager {
//    /**
//     * StateFlow representing the current access token.
//     * Composables/ViewModels can collect this flow to react to token changes (e.g., logging out).
//     */
//    val accessTokenFlow: StateFlow<String?>
//
//    /**
//     * Saves the new access and refresh tokens.
//     * @param accessToken The JWT for API authorization.
//     * @param refreshToken The token used to acquire new access tokens.
//     */
//    fun saveTokens(accessToken: String, refreshToken: String)
//
//    /**
//     * Retrieves the current access token.
//     */
//    fun getAccessToken(): String?
//
//    /**
//     * Retrieves the current refresh token.
//     */
//    fun getRefreshToken(): String?
//
//    /**
//     * Clears all stored tokens (logout).
//     */
//    fun clearTokens()
//}
//
///**
// * Implementation of TokenManager using EncryptedSharedPreferences for secure storage.
// * This class should be registered as a singleton in a Dependency Injection Module (e.g., Hilt).
// *
// * @param context The application context is required to access SharedPreferences.
// */
//class TokenManagerImpl(private val context: Context) : TokenManager {
//
//    private val sharedPreferences: SharedPreferences by lazy {
//        // Master Key for encrypting SharedPreferences file name and keys
//        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
//
//        // Creates an instance of EncryptedSharedPreferences
//        EncryptedSharedPreferences.create(
//            context,
//            "secure_token_prefs", // The file name
//            masterKeyAlias,
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//        )
//    }
//
//    // Keys for storing tokens
//    private companion object {
//        const val KEY_ACCESS_TOKEN = "access_token"
//        const val KEY_REFRESH_TOKEN = "refresh_token"
//    }
//
//    // Initialize the flow with the currently stored access token
//    private val _accessTokenFlow = MutableStateFlow(getStoredAccessToken())
//    override val accessTokenFlow: StateFlow<String?> = _accessTokenFlow.asStateFlow()
//
//    private fun getStoredAccessToken(): String? {
//        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
//    }
//
//    override fun saveTokens(accessToken: String, refreshToken: String) {
//        sharedPreferences.edit()
//            .putString(KEY_ACCESS_TOKEN, accessToken)
//            .putString(KEY_REFRESH_TOKEN, refreshToken)
//            .apply()
//
//        // Update the StateFlow to notify observers (e.g., navigation)
//        _accessTokenFlow.update { accessToken }
//    }
//
//    override fun getAccessToken(): String? {
//        return getStoredAccessToken()
//    }
//
//    override fun getRefreshToken(): String? {
//        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
//    }
//
//    override fun clearTokens() {
//        sharedPreferences.edit()
//            .remove(KEY_ACCESS_TOKEN)
//            .remove(KEY_REFRESH_TOKEN)
//            .apply()
//
//        // Update the StateFlow to null (triggers app logout flow)
//        _accessTokenFlow.update { null }
//    }
//}
//
//// --- Example of a simple Hilt Module structure (for context) ---
///*
//@Module
//@InstallIn(SingletonComponent::class)
//object AppModule {
//    @Provides
//    @Singleton
//    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
//        return TokenManagerImpl(context)
//    }
//}
//*/