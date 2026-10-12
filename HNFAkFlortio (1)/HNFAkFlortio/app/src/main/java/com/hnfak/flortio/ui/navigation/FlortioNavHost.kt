package com.hnfak.flortio.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hnfak.flortio.BuildConfig
import com.hnfak.flortio.R
import com.hnfak.flortio.di.AppContainer
import com.hnfak.flortio.ui.auth.ForgotPasswordScreen
import com.hnfak.flortio.ui.auth.LoginScreen
import com.hnfak.flortio.ui.auth.RegisterScreen
import com.hnfak.flortio.ui.discover.DiscoverScreen
import com.hnfak.flortio.ui.discover.MessagesScreen
import com.hnfak.flortio.ui.profile.EditProfileScreen
import com.hnfak.flortio.ui.profile.ProfileScreen
import com.hnfak.flortio.ui.settings.BlockedUsersScreen
import com.hnfak.flortio.ui.settings.DeleteAccountScreen
import com.hnfak.flortio.ui.settings.PrivacyScreen
import com.hnfak.flortio.ui.settings.SettingsScreen
import com.hnfak.flortio.ui.theme.FlortioColors
import com.hnfak.flortio.ui.welcome.WelcomeScreen

object Routes {
    const val Welcome = "welcome"
    const val Login = "login"
    const val Register = "register"
    const val ForgotPassword = "forgot_password"
    const val Discover = "discover"
    const val Messages = "messages"
    const val Profile = "profile"
    const val EditProfile = "edit_profile"
    const val Settings = "settings"
    const val Privacy = "privacy"
    const val Blocked = "blocked"
    const val DeleteAccount = "delete_account"
}

private data class TabItem(
    val route: String,
    val labelRes: Int,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
)

private val tabs = listOf(
    TabItem(Routes.Discover, R.string.tab_discover, Icons.Outlined.Explore, Icons.Filled.Explore),
    TabItem(Routes.Messages, R.string.tab_messages, Icons.Outlined.ChatBubbleOutline, Icons.Filled.ChatBubble),
    TabItem(Routes.Profile, R.string.tab_profile, Icons.Outlined.Person, Icons.Filled.Person),
)

private val tabRoutes = tabs.map { it.route }.toSet()

/** Uygulamanın ana ekranlarına geçer ve geri yığınını temizler. */
private fun NavController.enterApp() = navigate(Routes.Discover) {
    popUpTo(graph.id) { inclusive = true }
    launchSingleTop = true
}

/** Karşılama ekranına döner ve geri yığınını temizler. */
private fun NavController.returnToWelcome() = navigate(Routes.Welcome) {
    popUpTo(graph.id) { inclusive = true }
    launchSingleTop = true
}

@Composable
fun FlortioRoot(container: AppContainer) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val startDestination = remember {
        if (container.authRepository.currentUser.value != null) Routes.Discover else Routes.Welcome
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            AnimatedVisibility(visible = currentRoute in tabRoutes) {
                FlortioBottomBar(
                    currentRoute = currentRoute,
                    onSelect = { route ->
                        navController.navigate(route) {
                            popUpTo(Routes.Discover) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Routes.Welcome) {
                WelcomeScreen(
                    backendConfigured = container.authRepository.isBackendConfigured,
                    showPreview = BuildConfig.DEBUG,
                    onLogin = { navController.navigate(Routes.Login) },
                    onRegister = { navController.navigate(Routes.Register) },
                    onPreview = { navController.enterApp() },
                )
            }
            composable(Routes.Login) {
                LoginScreen(
                    onBack = { navController.popBackStack() },
                    onSuccess = { navController.enterApp() },
                    onRegister = { navController.navigate(Routes.Register) { launchSingleTop = true } },
                    onForgotPassword = { navController.navigate(Routes.ForgotPassword) },
                )
            }
            composable(Routes.Register) {
                RegisterScreen(
                    onBack = { navController.popBackStack() },
                    onSuccess = { navController.enterApp() },
                    onLogin = { navController.navigate(Routes.Login) { launchSingleTop = true } },
                )
            }
            composable(Routes.ForgotPassword) {
                ForgotPasswordScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.Discover) { DiscoverScreen() }
            composable(Routes.Messages) { MessagesScreen() }
            composable(Routes.Profile) {
                ProfileScreen(
                    onEditProfile = { navController.navigate(Routes.EditProfile) },
                    onSettings = { navController.navigate(Routes.Settings) },
                    onLogin = { navController.navigate(Routes.Login) },
                )
            }
            composable(Routes.EditProfile) {
                EditProfileScreen(
                    onBack = { navController.popBackStack() },
                    onSaved = { navController.popBackStack() },
                )
            }
            composable(Routes.Settings) {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    onPrivacy = { navController.navigate(Routes.Privacy) },
                    onBlocked = { navController.navigate(Routes.Blocked) },
                    onDeleteAccount = { navController.navigate(Routes.DeleteAccount) },
                    onLoggedOut = { navController.returnToWelcome() },
                )
            }
            composable(Routes.Privacy) { PrivacyScreen(onBack = { navController.popBackStack() }) }
            composable(Routes.Blocked) { BlockedUsersScreen(onBack = { navController.popBackStack() }) }
            composable(Routes.DeleteAccount) {
                DeleteAccountScreen(
                    onBack = { navController.popBackStack() },
                    onDeleted = { navController.returnToWelcome() },
                )
            }
        }
    }
}

@Composable
private fun FlortioBottomBar(currentRoute: String?, onSelect: (String) -> Unit) {
    NavigationBar(containerColor = FlortioColors.Card) {
        tabs.forEach { tab ->
            val selected = currentRoute == tab.route
            NavigationBarItem(
                selected = selected,
                onClick = { if (!selected) onSelect(tab.route) },
                icon = {
                    Icon(
                        imageVector = if (selected) tab.selectedIcon else tab.icon,
                        contentDescription = null,
                    )
                },
                label = { Text(stringResource(tab.labelRes)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = FlortioColors.Background,
                    selectedTextColor = FlortioColors.Lavender,
                    indicatorColor = FlortioColors.Purple,
                    unselectedIconColor = FlortioColors.TextSecondary,
                    unselectedTextColor = FlortioColors.TextSecondary,
                ),
            )
        }
    }
}
