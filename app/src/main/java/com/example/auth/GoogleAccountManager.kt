package com.example.auth

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class GoogleAccountProfile(
    val isLoggedIn: Boolean = false,
    val displayName: String = "Pengguna Tamu",
    val email: String = "tamu@youtube.tv",
    val avatarUrl: String = "",
    val channelHandle: String = "@guest_tv",
    val isPremium: Boolean = false,
    val subscribersCount: String = "0",
    val likedVideosCount: Int = 0,
    val historyCount: Int = 0
)

class GoogleAccountManager private constructor(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("google_tv_auth_prefs", Context.MODE_PRIVATE)

    private val _currentAccount = MutableStateFlow(loadAccount())
    val currentAccount: StateFlow<GoogleAccountProfile> = _currentAccount.asStateFlow()

    private fun loadAccount(): GoogleAccountProfile {
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, true) // default logged in with primary user
        val displayName = prefs.getString(KEY_DISPLAY_NAME, "Juju Popsicle") ?: "Juju Popsicle"
        val email = prefs.getString(KEY_EMAIL, "juju.popsicle@gmail.com") ?: "juju.popsicle@gmail.com"
        val avatarUrl = prefs.getString(KEY_AVATAR_URL, "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=120&auto=format&fit=crop&q=80") ?: ""
        val channelHandle = prefs.getString(KEY_HANDLE, "@jujupopsicle") ?: "@jujupopsicle"
        val isPremium = prefs.getBoolean(KEY_IS_PREMIUM, true)
        val liked = prefs.getInt(KEY_LIKED_COUNT, 42)
        val history = prefs.getInt(KEY_HISTORY_COUNT, 128)

        return GoogleAccountProfile(
            isLoggedIn = isLoggedIn,
            displayName = displayName,
            email = email,
            avatarUrl = avatarUrl,
            channelHandle = channelHandle,
            isPremium = isPremium,
            subscribersCount = "1.2K",
            likedVideosCount = liked,
            historyCount = history
        )
    }

    fun signIn(
        displayName: String = "Juju Popsicle",
        email: String = "juju.popsicle@gmail.com",
        avatarUrl: String = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=120&auto=format&fit=crop&q=80",
        isPremium: Boolean = true
    ) {
        val updated = GoogleAccountProfile(
            isLoggedIn = true,
            displayName = displayName,
            email = email,
            avatarUrl = avatarUrl,
            channelHandle = "@" + email.substringBefore("@").replace(".", "_"),
            isPremium = isPremium,
            subscribersCount = "1.2K",
            likedVideosCount = 42,
            historyCount = 128
        )
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putString(KEY_DISPLAY_NAME, updated.displayName)
            .putString(KEY_EMAIL, updated.email)
            .putString(KEY_AVATAR_URL, updated.avatarUrl)
            .putString(KEY_HANDLE, updated.channelHandle)
            .putBoolean(KEY_IS_PREMIUM, updated.isPremium)
            .putInt(KEY_LIKED_COUNT, updated.likedVideosCount)
            .putInt(KEY_HISTORY_COUNT, updated.historyCount)
            .apply()

        _currentAccount.value = updated
    }

    fun signOut() {
        val guest = GoogleAccountProfile(
            isLoggedIn = false,
            displayName = "Tamu Android TV",
            email = "Belum Masuk",
            avatarUrl = "",
            channelHandle = "@tamu",
            isPremium = false,
            subscribersCount = "0",
            likedVideosCount = 0,
            historyCount = 0
        )
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .putString(KEY_DISPLAY_NAME, guest.displayName)
            .putString(KEY_EMAIL, guest.email)
            .putString(KEY_AVATAR_URL, "")
            .putString(KEY_HANDLE, guest.channelHandle)
            .putBoolean(KEY_IS_PREMIUM, false)
            .apply()

        _currentAccount.value = guest
    }

    fun toggleLikeVideo() {
        val current = _currentAccount.value
        val newLiked = current.likedVideosCount + 1
        prefs.edit().putInt(KEY_LIKED_COUNT, newLiked).apply()
        _currentAccount.value = current.copy(likedVideosCount = newLiked)
    }

    companion object {
        private const val KEY_IS_LOGGED_IN = "key_is_logged_in"
        private const val KEY_DISPLAY_NAME = "key_display_name"
        private const val KEY_EMAIL = "key_email"
        private const val KEY_AVATAR_URL = "key_avatar_url"
        private const val KEY_HANDLE = "key_handle"
        private const val KEY_IS_PREMIUM = "key_is_premium"
        private const val KEY_LIKED_COUNT = "key_liked_count"
        private const val KEY_HISTORY_COUNT = "key_history_count"

        @Volatile
        private var instance: GoogleAccountManager? = null

        fun getInstance(context: Context): GoogleAccountManager {
            return instance ?: synchronized(this) {
                instance ?: GoogleAccountManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
