package com.hnfak.flortio.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hnfak.flortio.R
import com.hnfak.flortio.ui.common.ListState
import com.hnfak.flortio.ui.common.SubmitState
import com.hnfak.flortio.ui.common.messageRes
import com.hnfak.flortio.ui.theme.FlortioColors

// ------------------------------------------------------------------ Logo

@Composable
fun FlortioLogo(modifier: Modifier = Modifier, size: Dp = 96.dp) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.3f))
            .background(Brush.linearGradient(listOf(FlortioColors.Purple, FlortioColors.Lavender))),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            tint = FlortioColors.Background,
            modifier = Modifier.size(size * 0.5f),
        )
    }
}

// --------------------------------------------------------------- Butonlar

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    destructive: Boolean = false,
) {
    val container = if (destructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    Button(
        // Yükleme sırasında tıklamalar yok sayılır; görünüm bozulmasın diye enabled kapatılmaz.
        onClick = { if (!loading) onClick() },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = container,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = LocalContentColor.current,
            )
            Spacer(Modifier.width(10.dp))
        }
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun SecondaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, FlortioColors.Outline),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.secondary),
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

// ----------------------------------------------------------- Metin alanı

@Composable
fun FlortioTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    errorText: String? = null,
    supportingText: String? = null,
    leadingIcon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null,
    isPassword: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val support = errorText ?: supportingText

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = singleLine,
        minLines = if (singleLine) 1 else minLines,
        isError = errorText != null,
        shape = RoundedCornerShape(16.dp),
        leadingIcon = if (leadingIcon != null) {
            { Icon(imageVector = leadingIcon, contentDescription = null) }
        } else {
            null
        },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = stringResource(
                            if (passwordVisible) R.string.hide_password else R.string.show_password,
                        ),
                    )
                }
            }
        } else {
            null
        },
        supportingText = if (support != null) {
            { Text(support) }
        } else {
            null
        },
        visualTransformation = if (isPassword && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else keyboardType,
            imeAction = imeAction,
        ),
        keyboardActions = KeyboardActions(onDone = { onImeAction?.invoke() }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = FlortioColors.Card,
            unfocusedContainerColor = FlortioColors.Card,
            errorContainerColor = FlortioColors.Card,
            focusedBorderColor = FlortioColors.Purple,
            unfocusedBorderColor = FlortioColors.Outline,
            focusedLabelColor = FlortioColors.Lavender,
            unfocusedLabelColor = FlortioColors.TextSecondary,
            cursorColor = FlortioColors.Lavender,
            focusedLeadingIconColor = FlortioColors.Lavender,
            unfocusedLeadingIconColor = FlortioColors.TextSecondary,
            focusedTrailingIconColor = FlortioColors.Lavender,
            unfocusedTrailingIconColor = FlortioColors.TextSecondary,
            focusedSupportingTextColor = FlortioColors.TextSecondary,
            unfocusedSupportingTextColor = FlortioColors.TextSecondary,
        ),
    )
}

// ------------------------------------------------------------ Bilgi kutusu

enum class BannerKind { Info, Error, Success }

@Composable
fun StatusBanner(text: String, kind: BannerKind, modifier: Modifier = Modifier) {
    val container: Color
    val accent: Color
    val icon: ImageVector
    when (kind) {
        BannerKind.Info -> {
            container = FlortioColors.InfoContainer
            accent = FlortioColors.Lavender
            icon = Icons.Outlined.Info
        }
        BannerKind.Error -> {
            container = FlortioColors.ErrorContainer
            accent = FlortioColors.Error
            icon = Icons.Outlined.ErrorOutline
        }
        BannerKind.Success -> {
            container = FlortioColors.SuccessContainer
            accent = FlortioColors.Success
            icon = Icons.Outlined.CheckCircle
        }
    }
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(container)
            .border(1.dp, accent.copy(alpha = 0.35f), shape)
            .padding(14.dp)
            .semantics { liveRegion = LiveRegionMode.Polite },
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = accent)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

/** Form gönderimi başarısız olduysa hata kutusunu gösterir; aksi halde hiçbir şey çizmez. */
@Composable
fun SubmitErrorBanner(submit: SubmitState, modifier: Modifier = Modifier) {
    if (submit is SubmitState.Failed) {
        StatusBanner(
            text = stringResource(submit.error.messageRes()),
            kind = BannerKind.Error,
            modifier = modifier,
        )
    }
}

// -------------------------------------------------------------- Boş durum

@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(112.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(FlortioColors.Purple.copy(alpha = 0.35f), Color.Transparent),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(FlortioColors.Card)
                    .border(1.dp, FlortioColors.Outline, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = FlortioColors.Lavender,
                    modifier = Modifier.size(32.dp),
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(20.dp))
            SecondaryButton(text = actionLabel, onClick = onAction, modifier = Modifier.widthIn(max = 280.dp))
        }
    }
}

/** Dikey ortalanmış, gerekirse kaydırılabilen durum alanı. */
@Composable
fun StateColumn(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content,
    )
}

@Composable
fun LoadingBlock() {
    StateColumn {
        CircularProgressIndicator(color = FlortioColors.Purple)
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.loading),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** Yükleniyor / boş / hata / içerik durumlarını tek yerden çizen liste gövdesi. */
@Composable
fun <T> ListStateContent(
    state: ListState<T>,
    emptyIcon: ImageVector,
    emptyTitle: String,
    emptyBody: String,
    onRetry: () -> Unit,
    itemContent: @Composable (T) -> Unit,
) {
    when (state) {
        is ListState.Loading -> LoadingBlock()
        is ListState.Empty -> StateColumn {
            EmptyState(icon = emptyIcon, title = emptyTitle, body = emptyBody)
        }
        is ListState.Error -> StateColumn {
            StatusBanner(text = stringResource(state.error.messageRes()), kind = BannerKind.Error)
            Spacer(Modifier.height(28.dp))
            EmptyState(
                icon = emptyIcon,
                title = emptyTitle,
                body = emptyBody,
                actionLabel = stringResource(R.string.retry),
                onAction = onRetry,
            )
        }
        is ListState.Content -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            items(state.items) { item -> itemContent(item) }
        }
    }
}

// ---------------------------------------------------------- Kart ve satır

@Composable
fun FlortioCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(FlortioColors.Card)
            .border(1.dp, FlortioColors.OutlineSoft, shape),
        content = content,
    )
}

@Composable
fun CardDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 56.dp),
        color = FlortioColors.OutlineSoft,
    )
}

/** Ayar satırı. onClick verilmezse satır yalnızca bilgi gösterir ve tıklanabilir görünmez. */
@Composable
fun SettingsRow(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    trailing: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint)
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, color = tint)
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        if (trailing != null) {
            trailing()
        } else if (onClick != null) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun SoonBadge() {
    Text(
        text = stringResource(R.string.soon),
        style = MaterialTheme.typography.bodyMedium,
        color = FlortioColors.Lavender,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(FlortioColors.InfoContainer)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    )
}

@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.secondary,
        modifier = modifier.padding(start = 4.dp, top = 20.dp, bottom = 8.dp),
    )
}

// ---------------------------------------------------------- Ekran iskeletleri

/** Alt sekmesi olmayan ekranlar için: üst çubuk + (isteğe bağlı) kaydırılabilir gövde. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(
    title: String,
    onBack: (() -> Unit)?,
    modifier: Modifier = Modifier,
    scrollable: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(title, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        },
    ) { inner ->
        val base = Modifier
            .fillMaxSize()
            .padding(inner)
            .consumeWindowInsets(inner)
            .imePadding()
        Column(
            modifier = (if (scrollable) base.verticalScroll(rememberScrollState()) else base)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            content = content,
        )
    }
}

/** Alt sekmeli ana ekranlar için başlıklı gövde. */
@Composable
fun TabScreen(
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 20.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f),
            )
            actions()
        }
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) { content() }
    }
}
