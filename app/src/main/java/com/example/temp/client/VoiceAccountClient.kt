package com.example.temp.client

import com.example.temp.client.dto.ProfileDto
import kotlinx.coroutines.flow.Flow
import java.io.File

interface VoiceAccountClient {

    suspend fun setEmailAddress(emailAddress: String)

    suspend fun sendOtpForLogin()

    suspend fun loginWithOtp(otp: String)

    suspend fun getProfile(): Flow<ProfileDto>

    suspend fun declarePublicName(name: String)

    suspend fun declarePublicBio(bio: String)

    suspend fun declareGender(gender: String)

    suspend fun declareProfilePicture(photo: File)

    suspend fun setDateOfBirth(dob: String)

    suspend fun requestNewAccountManager(accountId: String)

}