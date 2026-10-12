package com.hnfak.flortio.ui.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hnfak.flortio.R
import com.hnfak.flortio.ui.components.BannerKind
import com.hnfak.flortio.ui.components.FlortioLogo
import com.hnfak.flortio.ui.components.PrimaryButton
import com.hnfak.flortio.ui.components.SecondaryButton
import com.hnfak.flortio.ui.components.StatusBanner
import com.hnfak.flortio.ui.theme.FlortioColors

/**
 * Açılış ekranı. [showPreview] yalnızca debug derlemelerinde true olur:
 * hesapsız olarak ana ekranları görmek için tasarım önizlemesi sağlar,
 * oturum açılmış gibi davranmaz.
 */
@Composable
fun WelcomeScreen(
    backendConfigured: Boolean,
    showPreview: Boolean,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onPreview: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to FlortioColors.GlowTop,
                        0.55f to FlortioColors.Background,
                    ),
                ),
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(72.dp))
            FlortioLogo(size = 112.dp)
            Spacer(Modifier.height(28.dp))
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.welcome_tagline),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(56.dp))

            if (!backendConfigured) {
                StatusBanner(
                    text = stringResource(R.string.welcome_not_configured),
                    kind = BannerKind.Info,
                )
                Spacer(Modifier.height(20.dp))
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                PrimaryButton(text = stringResource(R.string.welcome_login), onClick = onLogin)
                SecondaryButton(text = stringResource(R.string.welcome_register), onClick = onRegister)
                if (showPreview) {
                    TextButton(onClick = onPreview, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text(
                            text = stringResource(R.string.welcome_preview),
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.welcome_age_note),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}
