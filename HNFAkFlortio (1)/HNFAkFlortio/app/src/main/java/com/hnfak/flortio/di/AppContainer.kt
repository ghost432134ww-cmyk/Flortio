package com.hnfak.flortio.di

import com.hnfak.flortio.data.unconfigured.UnconfiguredAuthRepository
import com.hnfak.flortio.data.unconfigured.UnconfiguredBlockRepository
import com.hnfak.flortio.data.unconfigured.UnconfiguredConversationRepository
import com.hnfak.flortio.data.unconfigured.UnconfiguredDiscoverRepository
import com.hnfak.flortio.data.unconfigured.UnconfiguredProfileRepository
import com.hnfak.flortio.domain.repository.AuthRepository
import com.hnfak.flortio.domain.repository.BlockRepository
import com.hnfak.flortio.domain.repository.ConversationRepository
import com.hnfak.flortio.domain.repository.DiscoverRepository
import com.hnfak.flortio.domain.repository.ProfileRepository

/**
 * Bağımlılıkların tek toplandığı yer (elle yazılmış basit DI).
 * Firebase aşamasında yalnızca buradaki implementasyonlar değiştirilecek;
 * ViewModel ve UI koduna dokunmak gerekmeyecek.
 */
class AppContainer {
    val authRepository: AuthRepository by lazy { UnconfiguredAuthRepository() }
    val profileRepository: ProfileRepository by lazy { UnconfiguredProfileRepository() }
    val discoverRepository: DiscoverRepository by lazy { UnconfiguredDiscoverRepository() }
    val conversationRepository: ConversationRepository by lazy { UnconfiguredConversationRepository() }
    val blockRepository: BlockRepository by lazy { UnconfiguredBlockRepository() }
}
