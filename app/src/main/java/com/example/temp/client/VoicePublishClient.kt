package com.example.temp.client

import com.example.temp.client.dto.ProfileDto
import com.example.temp.data.Stories
import kotlinx.coroutines.flow.Flow

interface VoicePublishClient {

    suspend fun setSubscribeOpen(subscribeOpen: Boolean)

    suspend fun getSubscribeRequests(): Flow<List<ProfileDto>>

    suspend fun acceptSubscribeRequest(accountId: String)

    suspend fun publishStory(story: Stories)

}