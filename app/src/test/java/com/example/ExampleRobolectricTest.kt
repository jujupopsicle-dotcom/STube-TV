package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.auth.GoogleAccountManager
import com.example.model.SampleVideoRepository
import com.example.settings.TvSettingsManager
import com.example.util.YouTubeAdBlocker
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("YouTube TV", appName)
  }

  @Test
  fun `test video search repository`() {
    val results = SampleVideoRepository.searchVideos("Musik")
    assertTrue(results.isNotEmpty())
  }

  @Test
  fun `test google account manager singleton and sign out`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val manager = GoogleAccountManager.getInstance(context)
    assertNotNull(manager.currentAccount.value)

    manager.signOut()
    assertFalse(manager.currentAccount.value.isLoggedIn)

    manager.signIn("Test User", "test@gmail.com")
    assertTrue(manager.currentAccount.value.isLoggedIn)
    assertEquals("Test User", manager.currentAccount.value.displayName)
  }

  @Test
  fun `test tv settings manager autoplay toggle`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val settings = TvSettingsManager.getInstance(context)

    settings.setAutoplay(true)
    assertTrue(settings.isAutoplayEnabled.value)

    val toggled = settings.toggleAutoplay()
    assertFalse(toggled)
    assertFalse(settings.isAutoplayEnabled.value)

    settings.setAutoplay(true)
    assertTrue(settings.isAutoplayEnabled.value)
  }

  @Test
  fun `test tv settings manager ad blocker toggle`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val settings = TvSettingsManager.getInstance(context)

    settings.setAdBlocker(true)
    assertTrue(settings.isAdBlockerEnabled.value)

    val toggled = settings.toggleAdBlocker()
    assertFalse(toggled)
    assertFalse(settings.isAdBlockerEnabled.value)

    settings.setAdBlocker(true)
    assertTrue(settings.isAdBlockerEnabled.value)
  }

  @Test
  fun `test youtube ad blocker url detection and blocking`() {
    // Ad domains must be blocked
    assertTrue(YouTubeAdBlocker.shouldBlockUrl("https://googleads.g.doubleclick.net/pagead/ads?client=ca-video"))
    assertTrue(YouTubeAdBlocker.shouldBlockUrl("https://pubads.g.doubleclick.net/gampad/ads"))
    assertTrue(YouTubeAdBlocker.shouldBlockUrl("https://www.youtube.com/api/stats/ads?adformat=1"))
    assertTrue(YouTubeAdBlocker.shouldBlockUrl("https://www.youtube.com/pagead/lvz?ai=abc"))

    // Normal video stream & iframe URLs must NOT be blocked
    assertFalse(YouTubeAdBlocker.shouldBlockUrl("https://www.youtube.com/embed/dQw4w9WgXcQ"))
    assertFalse(YouTubeAdBlocker.shouldBlockUrl("https://www.youtube.com/iframe_api"))
    assertFalse(YouTubeAdBlocker.shouldBlockUrl("https://i.ytimg.com/vi/dQw4w9WgXcQ/maxresdefault.jpg"))

    val response = YouTubeAdBlocker.createEmptyResponse()
    assertNotNull(response)
    assertEquals("text/plain", response.mimeType)
    assertEquals("UTF-8", response.encoding)
  }
}

