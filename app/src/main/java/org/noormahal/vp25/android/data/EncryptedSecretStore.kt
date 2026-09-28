package org.noormahal.vp25.android.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class EncryptedSecretStore(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        PREFS_FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun setSecret(secret: String) {
        prefs.edit().putString(SECRET_KEY, secret).apply()
    }

    fun getSecret(): String? = prefs.getString(SECRET_KEY, null)

    fun clearSecret() {
        prefs.edit().remove(SECRET_KEY).apply()
    }

    companion object {
        private const val PREFS_FILE_NAME = "vp25_secure_prefs"
        private const val SECRET_KEY = "app_secret"
    }
}
