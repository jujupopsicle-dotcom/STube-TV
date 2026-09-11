package com.example

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.auth.GoogleAccountManager
import com.example.model.SampleVideoRepository
import com.example.model.YouTubeVideo
import com.example.settings.TvSettingsManager
import com.example.ui.components.GoogleAccountDialog
import com.example.ui.components.SettingsOverlay
import com.example.ui.components.TvDestination
import com.example.ui.components.TvNavigationRail
import com.example.ui.components.YouTubePlayerView
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.WebTvScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.YouTubeBlack

class MainActivity : ComponentActivity() {

    private val accountManager by lazy { GoogleAccountManager.getInstance(this) }
    private val settingsManager by lazy { TvSettingsManager.getInstance(this) }
    private var backPressAction: (() -> Boolean)? = null
    private var settingsToggleAction: (() -> Boolean)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val currentAccount by accountManager.currentAccount.collectAsStateWithLifecycle()
                val isAutoplayEnabled by settingsManager.isAutoplayEnabled.collectAsStateWithLifecycle()
                val isAdBlockerEnabled by settingsManager.isAdBlockerEnabled.collectAsStateWithLifecycle()

                var currentDestination by remember { mutableStateOf(TvDestination.HOME) }
                var activeVideo by remember { mutableStateOf<YouTubeVideo?>(null) }
                var showAccountDialog by remember { mutableStateOf(false) }
                var showSettingsOverlay by remember { mutableStateOf(false) }

                // Register back press handler for TV Remote Back button
                BackHandler(enabled = activeVideo != null || showSettingsOverlay || showAccountDialog || currentDestination != TvDestination.HOME) {
                    when {
                        activeVideo != null -> activeVideo = null
                        showSettingsOverlay -> showSettingsOverlay = false
                        showAccountDialog -> showAccountDialog = false
                        currentDestination != TvDestination.HOME -> currentDestination = TvDestination.HOME
                    }
                }

                backPressAction = {
                    when {
                        activeVideo != null -> {
                            activeVideo = null
                            true
                        }
                        showSettingsOverlay -> {
                            showSettingsOverlay = false
                            true
                        }
                        showAccountDialog -> {
                            showAccountDialog = false
                            true
                        }
                        currentDestination != TvDestination.HOME -> {
                            currentDestination = TvDestination.HOME
                            true
                        }
                        else -> false
                    }
                }

                settingsToggleAction = {
                    showSettingsOverlay = !showSettingsOverlay
                    true
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(YouTubeBlack)
                        .safeDrawingPadding()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(YouTubeBlack)
                    ) {
                        // Main Layout: TV Side Rail + Screen Content
                        Row(modifier = Modifier.fillMaxSize()) {
                            // Left Rail is hidden while a video is playing in full screen
                            if (activeVideo == null) {
                                TvNavigationRail(
                                    currentDestination = currentDestination,
                                    account = currentAccount,
                                    onDestinationSelected = { dest ->
                                        when (dest) {
                                            TvDestination.ACCOUNT -> showAccountDialog = true
                                            TvDestination.SETTINGS -> showSettingsOverlay = true
                                            else -> currentDestination = dest
                                        }
                                    }
                                )
                            }

                            // Main Content Area
                            Box(modifier = Modifier.weight(1f)) {
                                when (currentDestination) {
                                    TvDestination.HOME,
                                    TvDestination.MUSIC,
                                    TvDestination.GAMING,
                                    TvDestination.NEWS,
                                    TvDestination.SETTINGS -> {
                                        HomeScreen(
                                            onVideoSelected = { video ->
                                                activeVideo = video
                                            }
                                        )
                                    }
                                    TvDestination.SEARCH -> {
                                        SearchScreen(
                                            onVideoSelected = { video ->
                                                activeVideo = video
                                            }
                                        )
                                    }
                                    TvDestination.WEB_TV -> {
                                        WebTvScreen()
                                    }
                                    TvDestination.ACCOUNT -> {
                                        HomeScreen(
                                            onVideoSelected = { video ->
                                                activeVideo = video
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // Fullscreen Video Player Overlay
                        activeVideo?.let { video ->
                            YouTubePlayerView(
                                video = video,
                                onClose = { activeVideo = null },
                                onLikeVideo = {
                                    accountManager.toggleLikeVideo()
                                },
                                autoplay = isAutoplayEnabled,
                                adBlockerEnabled = isAdBlockerEnabled
                            )
                        }

                        // Google Account Dialog Overlay
                        if (showAccountDialog) {
                            GoogleAccountDialog(
                                account = currentAccount,
                                onSignIn = { name, email ->
                                    accountManager.signIn(displayName = name, email = email)
                                },
                                onSignOut = {
                                    accountManager.signOut()
                                },
                                onDismiss = { showAccountDialog = false }
                            )
                        }

                        // Settings Overlay
                        if (showSettingsOverlay) {
                            SettingsOverlay(
                                isAutoplayEnabled = isAutoplayEnabled,
                                onToggleAutoplay = {
                                    settingsManager.toggleAutoplay()
                                },
                                isAdBlockerEnabled = isAdBlockerEnabled,
                                onToggleAdBlocker = {
                                    settingsManager.toggleAdBlocker()
                                },
                                account = currentAccount,
                                onSignOut = {
                                    accountManager.signOut()
                                },
                                onOpenSignIn = {
                                    showSettingsOverlay = false
                                    showAccountDialog = true
                                },
                                onDismiss = {
                                    showSettingsOverlay = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val handled = backPressAction?.invoke() ?: false
            if (handled) return true
        } else if (keyCode == KeyEvent.KEYCODE_SETTINGS || keyCode == KeyEvent.KEYCODE_MENU) {
            val handled = settingsToggleAction?.invoke() ?: false
            if (handled) return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
