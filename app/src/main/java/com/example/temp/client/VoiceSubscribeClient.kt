package com.example.temp.client

import com.example.temp.client.dto.ProfileDto
import com.example.temp.data.Stories
import com.example.temp.data.StoryList
import kotlinx.coroutines.flow.Flow

interface VoiceSubscribeClient {

    suspend fun getUserProfile(accountId: String): Flow<ProfileDto>

    suspend fun sendSubscribeRequest(accountId: String)

    suspend fun getSubscribeStatus(accountId: String): Flow<String>

    suspend fun getStoryUserList(): Flow<StoryList>

    suspend fun getStories(): Flow<Stories>

}