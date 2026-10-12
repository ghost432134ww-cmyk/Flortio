package com.hnfak.flortio.domain.repository

import com.hnfak.flortio.domain.DataResult
import com.hnfak.flortio.domain.model.AuthUser
import com.hnfak.flortio.domain.model.BlockedUser
import com.hnfak.flortio.domain.model.Conversation
import com.hnfak.flortio.domain.model.DiscoverProfile
import com.hnfak.flortio.domain.model.UserProfile
import kotlinx.coroutines.flow.StateFlow

/**
 * Kimlik doğrulama sözleşmesi. Firebase aşamasında bu arayüzü uygulayan
 * bir sınıf yazılıp AppContainer içinde değiştirilecek.
 */
interface AuthRepository {
    /** Oturum açmış kullanıcı; yoksa null. */
    val currentUser: StateFlow<AuthUser?>

    /** Gerçek bir backend bağlı mı? UI, kullanıcıya dürüst bilgi vermek için kullanır. */
    val isBackendConfigured: Boolean

    suspend fun signIn(email: String, password: String): DataResult<AuthUser>
    suspend fun signUp(email: String, password: String): DataResult<AuthUser>
    suspend fun sendPasswordReset(email: String): DataResult<Unit>
    suspend fun signOut(): DataResult<Unit>
    suspend fun deleteAccount(password: String): DataResult<Unit>
}

interface ProfileRepository {
    /** Profil henüz oluşturulmadıysa Success(null) döner. */
    suspend fun loadProfile(userId: String): DataResult<UserProfile?>
    suspend fun saveProfile(userId: String, profile: UserProfile): DataResult<Unit>
}

interface DiscoverRepository {
    suspend fun loadCandidates(): DataResult<List<DiscoverProfile>>
}

interface ConversationRepository {
    suspend fun loadConversations(): DataResult<List<Conversation>>
}

interface BlockRepository {
    suspend fun loadBlockedUsers(): DataResult<List<BlockedUser>>
    suspend fun unblock(userId: String): DataResult<Unit>
}
