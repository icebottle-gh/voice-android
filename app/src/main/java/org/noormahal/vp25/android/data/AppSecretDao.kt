package org.noormahal.vp25.android.data

import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class AppSecretDao {

    @Query("INSERT OR REPLACE INTO app_secrets (id, secretValue) VALUES (:id, :appSecret)")
    abstract fun setSecret(appSecret: String, id: Int = AppSecret.DEFAULT_ID)

    @Query("SELECT * FROM app_secrets WHERE id = :id LIMIT 1")
    abstract fun getSecret(id: Int = AppSecret.DEFAULT_ID): AppSecret? // Nullable if not found
}