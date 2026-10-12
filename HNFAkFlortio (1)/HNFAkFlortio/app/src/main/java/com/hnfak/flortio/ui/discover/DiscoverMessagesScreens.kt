package com.hnfak.flortio.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hnfak.flortio.R
import com.hnfak.flortio.domain.model.Conversation
import com.hnfak.flortio.domain.model.DiscoverProfile
import com.hnfak.flortio.ui.common.AppViewModelProvider
import com.hnfak.flortio.ui.components.FlortioCard
import com.hnfak.flortio.ui.components.ListStateContent
import com.hnfak.flortio.ui.components.TabScreen
import com.hnfak.flortio.ui.theme.FlortioColors

@Composable
fun DiscoverScreen(viewModel: DiscoverViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TabScreen(title = stringResource(R.string.tab_discover)) {
        ListStateContent(
            state = state,
            emptyIcon = Icons.Outlined.Explore,
            emptyTitle = stringResource(R.string.discover_empty_title),
            emptyBody = stringResource(R.string.discover_empty_body),
            onRetry = viewModel::load,
        ) { profile -> DiscoverProfileCard(profile) }
    }
}

/** Gerçek veri kaynağı bağlandığında kullanılacak profil kartı. */
@Composable
private fun DiscoverProfileCard(profile: DiscoverProfile) {
    FlortioCard {
        Column(modifier = Modifier.padding(16.dp)) {
            val heading = if (profile.age != null) "${profile.displayName}, ${profile.age}" else profile.displayName
            Text(
                text = heading,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (profile.city.isNotBlank()) {
                Text(
                    text = profile.city,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            if (profile.bio.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = profile.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
fun MessagesScreen(viewModel: MessagesViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TabScreen(title = stringResource(R.string.tab_messages)) {
        ListStateContent(
            state = state,
            emptyIcon = Icons.Outlined.ChatBubbleOutline,
            emptyTitle = stringResource(R.string.messages_empty_title),
            emptyBody = stringResource(R.string.messages_empty_body),
            onRetry = viewModel::load,
        ) { conversation -> ConversationRow(conversation) }
    }
}

/** Gerçek veri kaynağı bağlandığında kullanılacak sohbet satırı. */
@Composable
private fun ConversationRow(conversation: Conversation) {
    FlortioCard {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(FlortioColors.CardHigh),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = conversation.partnerName.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = FlortioColors.Lavender,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = conversation.partnerName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = conversation.lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
