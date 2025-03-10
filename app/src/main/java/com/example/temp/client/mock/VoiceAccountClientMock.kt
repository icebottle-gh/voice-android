package com.example.temp.client.mock

import com.example.temp.client.VoiceAccountClient
import com.example.temp.client.dto.ProfileDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File

class VoiceAccountClientMock : VoiceAccountClient {

    var emailAddress: String? = null

    override suspend fun setEmailAddress(emailAddress: String) {
        this.emailAddress = emailAddress
    }

    override suspend fun sendOtpForLogin() {
        withContext(Dispatchers.IO) {
            Thread.sleep(500)
        }
    }

    override suspend fun loginWithOtp(otp: String) {
        withContext(Dispatchers.IO) {
            Thread.sleep(500)
        }
    }

    override suspend fun getProfile(): Flow<ProfileDto> {
        TODO("Not yet implemented")
    }

    override suspend fun declarePublicName(name: String) {
        TODO("Not yet implemented")
    }

    override suspend fun declarePublicBio(bio: String) {
        TODO("Not yet implemented")
    }

    override suspend fun declareGender(gender: String) {
        TODO("Not yet implemented")
    }

    override suspend fun declareProfilePicture(photo: File) {
        TODO("Not yet implemented")
    }

    override suspend fun setDateOfBirth(dob: String) {
        TODO("Not yet implemented")
    }

    override suspend fun requestNewAccountManager(accountId: String) {
        TODO("Not yet implemented")
    }

}