package com.example.temp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_secrets")
data class AppSecret(
    @PrimaryKey val id: Int = DEFAULT_ID, // Use a fixed ID if you only store one secret
    val secretValue: String
) {
    companion object {
        const val DEFAULT_ID = 1 // A constant ID for the single secret entry
    }
}