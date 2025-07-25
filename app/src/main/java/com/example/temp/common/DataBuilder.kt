package com.example.temp.common

import org.noormahal.ib.vakkic.dto.ConnectionStatus
import org.noormahal.ib.vakkic.dto.PersonalizedProfile

fun makePersonalizedProfile(
    id: String,
    fullName: String,
    nickName: String?,
    bio: String?
): PersonalizedProfile {
    val profile = PersonalizedProfile()
    profile.id = id
    profile.fullName = fullName
    profile.nickName = nickName
    profile.bio = bio
    return profile
}

fun makeConnectionStatus(
    user: String,
    isFollowing: Boolean = false,
    isFollower: Boolean = false
): ConnectionStatus {
    val connectionStatus = ConnectionStatus()
    connectionStatus.user = user
    connectionStatus.isFollowing = isFollowing
    connectionStatus.isFollower = isFollower
    return connectionStatus
}