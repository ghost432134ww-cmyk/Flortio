package com.hnfak.flortio.domain.model

data class AuthUser(val id: String, val email: String)

data class UserProfile(
    val displayName: String = "",
    val bio: String = "",
    val city: String = "",
    val birthYear: Int? = null,
)

/** Keşfet ekranında gösterilecek profil. Veri kaynağı bağlanana kadar hiç üretilmez. */
data class DiscoverProfile(
    val id: String,
    val displayName: String,
    val age: Int?,
    val city: String,
    val bio: String,
)

data class Conversation(
    val id: String,
    val partnerName: String,
    val lastMessage: String,
)

data class BlockedUser(
    val id: String,
    val displayName: String,
)
