package com.example.checkit.core

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Interface for managing JWT and Refresh tokens securely.
 */
interface TokenManager {
    /**
     * StateFlow representing the current access token.
     * Composables/ViewModels can collect this flow to react to token changes (e.g., logging out).
     */
    val accessTokenFlow: StateFlow<String?>

    /**
     * Saves the new access and refresh tokens.
     * @param accessToken The JWT for API authorization.
     * @param refreshToken The token used to acquire new access tokens.
     */
    fun saveTokens(accessToken: String, refreshToken: String)

    /**
     * Retrieves the current access token.
     */
    fun getAccessToken(): String?

    /**
     * Retrieves the current refresh token.
     */
    fun getRefreshToken(): String?

    /**
     * Clears all stored tokens (logout).
     */
    fun clearTokens()
}

/**
 * Implementation of TokenManager using EncryptedSharedPreferences for secure storage.
 * This class should be registered as a singleton in a Dependency Injection Module (e.g., Hilt).
 *
 * @param context The application context is required to access SharedPreferences.
 */
@Suppress("DEPRECATION")
@Singleton
class TokenManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context // Hilt inyecta el Context
) : TokenManager {
    private val sharedPreferences: SharedPreferences by lazy {
        try{
        // Master Key for encrypting SharedPreferences file name and keys
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        // Creates an instance of EncryptedSharedPreferences
        EncryptedSharedPreferences.create(
            context,
            "secure_token_prefs", // The file name
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch (e: Exception){
            // SE CRASHA (Chiave corrotta o backup errato):
            // Cancelliamo il file corrotto e ne creiamo uno nuovo.
            // Questo impedisce il "loop della morte" all'avvio.
            context.getSharedPreferences("secure_token_prefs", Context.MODE_PRIVATE).edit().clear().apply()

            // Riprova a creare (ora dovrebbe funzionare perché è pulito)
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                context,
                "secure_token_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }


    // Keys for storing tokens
    private companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
    }

    // Initialize the flow with the currently stored access token
    private val _accessTokenFlow = MutableStateFlow(getStoredAccessToken())
    override val accessTokenFlow: StateFlow<String?> = _accessTokenFlow.asStateFlow()

    private fun getStoredAccessToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }

    override fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit {
            putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
        }

        // Update the StateFlow to notify observers (e.g., navigation)
        _accessTokenFlow.update { accessToken }
    }

    override fun getAccessToken(): String? {
        return getStoredAccessToken()
    }

    override fun getRefreshToken(): String? {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
    }

    override fun clearTokens() {
        sharedPreferences.edit {
            remove(KEY_ACCESS_TOKEN)
                .remove(KEY_REFRESH_TOKEN)
        }

        // Update the StateFlow to null (triggers app logout flow)
        _accessTokenFlow.update { null }
    }
}
//.
// --- Example of a simple Hilt Module structure (for context) ---


