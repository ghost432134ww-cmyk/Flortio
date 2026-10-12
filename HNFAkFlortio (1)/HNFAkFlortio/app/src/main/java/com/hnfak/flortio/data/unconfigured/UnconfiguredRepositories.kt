package com.hnfak.flortio.data.unconfigured

import com.hnfak.flortio.domain.AppError
import com.hnfak.flortio.domain.DataResult
import com.hnfak.flortio.domain.model.AuthUser
import com.hnfak.flortio.domain.model.BlockedUser
import com.hnfak.flortio.domain.model.Conversation
import com.hnfak.flortio.domain.model.DiscoverProfile
import com.hnfak.flortio.domain.model.UserProfile
import com.hnfak.flortio.domain.repository.AuthRepository
import com.hnfak.flortio.domain.repository.BlockRepository
import com.hnfak.flortio.domain.repository.ConversationRepository
import com.hnfak.flortio.domain.repository.DiscoverRepository
import com.hnfak.flortio.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/*
 * Backend bağlanana kadar kullanılan yer tutucu implementasyonlar.
 * Hiçbiri veri üretmez, hiçbiri başarı döndürmez: her çağrı açıkça
 * AppError.NotConfigured ile başarısız olur. Böylece UI, olmayan bir
 * sunucuyu varmış gibi göstermez.
 */

private fun notConfigured(): DataResult.Failure = DataResult.Failure(AppError.NotConfigured)

class UnconfiguredAuthRepository : AuthRepository {
    private val user = MutableStateFlow<AuthUser?>(null)
    override val currentUser: StateFlow<AuthUser?> = user.asStateFlow()
    override val isBackendConfigured: Boolean = false

    override suspend fun signIn(email: String, password: String): DataResult<AuthUser> = notConfigured()
    override suspend fun signUp(email: String, password: String): DataResult<AuthUser> = notConfigured()
    override suspend fun sendPasswordReset(email: String): DataResult<Unit> = notConfigured()
    override suspend fun signOut(): DataResult<Unit> = notConfigured()
    override suspend fun deleteAccount(password: String): DataResult<Unit> = notConfigured()
}

class UnconfiguredProfileRepository : ProfileRepository {
    override suspend fun loadProfile(userId: String): DataResult<UserProfile?> = notConfigured()
    override suspend fun saveProfile(userId: String, profile: UserProfile): DataResult<Unit> = notConfigured()
}

class UnconfiguredDiscoverRepository : DiscoverRepository {
    override suspend fun loadCandidates(): DataResult<List<DiscoverProfile>> = notConfigured()
}

class UnconfiguredConversationRepository : ConversationRepository {
    override suspend fun loadConversations(): DataResult<List<Conversation>> = notConfigured()
}

class UnconfiguredBlockRepository : BlockRepository {
    override suspend fun loadBlockedUsers(): DataResult<List<BlockedUser>> = notConfigured()
    override suspend fun unblock(userId: String): DataResult<Unit> = notConfigured()
}
