package com.example.temp.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users-table")
@Parcelize
data class Users(
    @PrimaryKey
    val userName: String,
    @ColumnInfo
    val fullName: String,
    @ColumnInfo
    val nickName: String?,
    @ColumnInfo
    val displayName: String?,
    @ColumnInfo
    val bio: String?,
): Parcelable
