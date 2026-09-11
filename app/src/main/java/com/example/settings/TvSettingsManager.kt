package com.example.settings

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages user settings for YouTube Android TV.
 * Compatible with Android 6.0 (API 23) and higher using standard SharedPreferences.
 */
class TvSettingsManager private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _isAutoplayEnabled = MutableStateFlow(loadAutoplay())
    val isAutoplayEnabled: StateFlow<Boolean> = _isAutoplayEnabled.asStateFlow()

    private val _isAdBlockerEnabled = MutableStateFlow(loadAdBlocker())
    val isAdBlockerEnabled: StateFlow<Boolean> = _isAdBlockerEnabled.asStateFlow()

    private fun loadAutoplay(): Boolean {
        return prefs.getBoolean(KEY_AUTOPLAY, true)
    }

    private fun loadAdBlocker(): Boolean {
        return prefs.getBoolean(KEY_AD_BLOCKER, true)
    }

    fun setAutoplay(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AUTOPLAY, enabled).apply()
        _isAutoplayEnabled.value = enabled
    }

    fun toggleAutoplay(): Boolean {
        val next = !_isAutoplayEnabled.value
        setAutoplay(next)
        return next
    }

    fun setAdBlocker(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AD_BLOCKER, enabled).apply()
        _isAdBlockerEnabled.value = enabled
    }

    fun toggleAdBlocker(): Boolean {
        val next = !_isAdBlockerEnabled.value
        setAdBlocker(next)
        return next
    }

    companion object {
        private const val PREFS_NAME = "youtube_tv_settings_prefs"
        private const val KEY_AUTOPLAY = "key_autoplay_enabled"
        private const val KEY_AD_BLOCKER = "key_ad_blocker_enabled"

        @Volatile
        private var instance: TvSettingsManager? = null

        fun getInstance(context: Context): TvSettingsManager {
            return instance ?: synchronized(this) {
                instance ?: TvSettingsManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
