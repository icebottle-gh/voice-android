package org.noormahal.vp25.android.common

import org.noormahal.ib.vakkic.dto.ConnectionStatus
import org.noormahal.ib.vakkic.dto.PersonalizedProfile

fun makePersonalizedProfile(
    id: String,
    fullName: String,
    nickName: String?,
    bio: String?,
    age: String? = null,
    gender: String? = null
): PersonalizedProfile {
    val profile = PersonalizedProfile()
    profile.id = id
    profile.fullName = fullName
    profile.nickName = nickName
    profile.bio = bio
    profile.age = age
    profile.gender = gender
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