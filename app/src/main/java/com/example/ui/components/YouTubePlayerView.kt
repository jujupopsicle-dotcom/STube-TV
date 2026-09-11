package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.model.YouTubeVideo
import com.example.ui.theme.YouTubeFocusBorder
import com.example.ui.theme.YouTubeRed
import com.example.ui.theme.YouTubeTextPrimary
import com.example.ui.theme.YouTubeTextSecondary
import com.example.util.YouTubeAdBlocker
import kotlinx.coroutines.delay

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YouTubePlayerView(
    video: YouTubeVideo,
    onClose: () -> Unit,
    onLikeVideo: () -> Unit,
    autoplay: Boolean = true,
    adBlockerEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var isPlaying by remember { mutableStateOf(autoplay) }
    var showControls by remember { mutableStateOf(true) }
    var isLiked by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    // Auto hide HUD overlay after 5 seconds of inactivity
    LaunchedEffect(showControls, isPlaying) {
        if (showControls) {
            delay(5000)
            showControls = false
        }
    }

    DisposableEffect(video.id) {
        onDispose {
            webViewInstance?.apply {
                loadUrl("about:blank")
                stopLoading()
                onPause()
                destroy()
            }
            webViewInstance = null
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Hardware Accelerated YouTube WebView
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    setLayerType(View.LAYER_TYPE_HARDWARE, null)
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        mediaPlaybackRequiresUserGesture = false
                        databaseEnabled = true
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        cacheMode = WebSettings.LOAD_DEFAULT
                        // Set standard Smart TV / Modern Chrome user agent
                        userAgentString =
                            "Mozilla/5.0 (Linux; Android 9; SHIELD Android TV Build/PPR1.180610.011) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.225 Safari/537.36 SmartTV"
                    }
                    webChromeClient = object : WebChromeClient() {}
                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true
                        }
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                            if (adBlockerEnabled) {
                                view?.evaluateJavascript(YouTubeAdBlocker.AD_SKIPPER_JAVASCRIPT, null)
                            }
                        }

                        override fun shouldInterceptRequest(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): WebResourceResponse? {
                            if (adBlockerEnabled && YouTubeAdBlocker.shouldBlockUrl(request?.url?.toString())) {
                                return YouTubeAdBlocker.createEmptyResponse()
                            }
                            return super.shouldInterceptRequest(view, request)
                        }

                        @Deprecated("Deprecated in Java")
                        override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
                            if (adBlockerEnabled && YouTubeAdBlocker.shouldBlockUrl(url)) {
                                return YouTubeAdBlocker.createEmptyResponse()
                            }
                            return super.shouldInterceptRequest(view, url)
                        }
                    }

                    val playerHtml = getPlayerHtml(video.id, autoplay, adBlockerEnabled)
                    loadDataWithBaseURL("https://www.youtube.com", playerHtml, "text/html", "UTF-8", null)
                    webViewInstance = this
                }
            },
            update = { wv ->
                webViewInstance = wv
            },
            modifier = Modifier.fillMaxSize()
        )

        // Loading spinner
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = YouTubeRed, strokeWidth = 4.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Memuat Pemutar YouTube TV...",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Tap or D-pad trigger area to show controls
        Box(
            modifier = Modifier
                .fillMaxSize()
                .tvFocusable(
                    shape = RoundedCornerShape(0.dp),
                    focusedBorderColor = Color.Transparent,
                    scaleOnFocus = 1.0f,
                    onClick = {
                        showControls = !showControls
                    }
                )
        )

        // TV Remote HUD Overlay
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xCC000000),
                                Color(0x33000000),
                                Color(0xEE000000)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                // Top Header: Video Details & Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (video.channelAvatarUrl.isNotEmpty()) {
                            AsyncImage(
                                model = video.channelAvatarUrl,
                                contentDescription = video.channelTitle,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF333333))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        }

                        Column {
                            Text(
                                text = video.title,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = video.channelTitle,
                                    color = YouTubeTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = " • ${video.views} • 4K HDR",
                                    color = YouTubeTextSecondary,
                                    fontSize = 12.sp
                                )
                                if (adBlockerEnabled) {
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFF16381C), RoundedCornerShape(6.dp))
                                            .border(1.dp, Color(0xFF4CAF50), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 7.dp, vertical = 2.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Security,
                                                contentDescription = null,
                                                tint = Color(0xFF81C784),
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Bebas Iklan",
                                                color = Color(0xFF81C784),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Close Button
                    Box(
                        modifier = Modifier
                            .tvFocusable(
                                shape = CircleShape,
                                focusedBorderColor = YouTubeFocusBorder,
                                scaleOnFocus = 1.15f,
                                onClick = onClose
                            )
                            .background(Color(0x66FFFFFF), CircleShape)
                            .padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup Video",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Center / Bottom Controls
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Action Buttons Row (Rewind, Play/Pause, Forward, Like)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        // Rewind 10s
                        Box(
                            modifier = Modifier
                                .tvFocusable(
                                    shape = CircleShape,
                                    focusedBorderColor = YouTubeFocusBorder,
                                    scaleOnFocus = 1.15f,
                                    onClick = {
                                        webViewInstance?.evaluateJavascript("seekRelative(-10);", null)
                                    }
                                )
                                .background(Color(0xFF222222), CircleShape)
                                .padding(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.FastRewind,
                                    contentDescription = "Mundur 10 detik",
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("-10s", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Play/Pause Main Button
                        Box(
                            modifier = Modifier
                                .tvFocusable(
                                    shape = CircleShape,
                                    focusedBorderColor = YouTubeFocusBorder,
                                    scaleOnFocus = 1.18f,
                                    onClick = {
                                        isPlaying = !isPlaying
                                        if (isPlaying) {
                                            webViewInstance?.evaluateJavascript("playVideo();", null)
                                        } else {
                                            webViewInstance?.evaluateJavascript("pauseVideo();", null)
                                        }
                                    }
                                )
                                .background(YouTubeRed, CircleShape)
                                .padding(18.dp)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Jeda" else "Putar",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        // Forward 10s
                        Box(
                            modifier = Modifier
                                .tvFocusable(
                                    shape = CircleShape,
                                    focusedBorderColor = YouTubeFocusBorder,
                                    scaleOnFocus = 1.15f,
                                    onClick = {
                                        webViewInstance?.evaluateJavascript("seekRelative(10);", null)
                                    }
                                )
                                .background(Color(0xFF222222), CircleShape)
                                .padding(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("+10s", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.FastForward,
                                    contentDescription = "Maju 10 detik",
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }

                        // Like Video Button
                        Box(
                            modifier = Modifier
                                .tvFocusable(
                                    shape = CircleShape,
                                    focusedBorderColor = YouTubeFocusBorder,
                                    scaleOnFocus = 1.15f,
                                    onClick = {
                                        isLiked = !isLiked
                                        if (isLiked) onLikeVideo()
                                    }
                                )
                                .background(if (isLiked) Color(0xFF441111) else Color(0xFF222222), CircleShape)
                                .padding(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Sukai Video",
                                    tint = if (isLiked) YouTubeRed else Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isLiked) "Disukai" else "Suka",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // TV Remote Guide Bar
                    Box(
                        modifier = Modifier
                            .background(Color(0x99111111), RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Navigasi Remote: [OK] Putar/Jeda • [◀ / ▶] Geser 10 Detik • [Kembali] Tutup Video",
                            color = YouTubeTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

private fun getPlayerHtml(videoId: String, autoplay: Boolean, adBlockerEnabled: Boolean): String {
    val autoplayVal = if (autoplay) 1 else 0
    val onReadyJs = if (autoplay) "e.target.playVideo();" else ""
    val adCss = if (adBlockerEnabled) """
        .ytp-ad-overlay-container, .ytp-ad-message-container, .ytp-ad-player-overlay, ytd-ad-slot-renderer, ytd-banner-promo-renderer, ytd-in-feed-ad-layout-renderer, #masthead-ad {
            display: none !important;
            opacity: 0 !important;
            pointer-events: none !important;
            height: 0px !important;
        }
    """ else ""
    val adSkipperJs = if (adBlockerEnabled) YouTubeAdBlocker.AD_SKIPPER_JAVASCRIPT else ""

    return """
        <!DOCTYPE html>
        <html>
        <head>
          <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
          <style>
            * { margin: 0; padding: 0; box-sizing: border-box; }
            html, body { width: 100%; height: 100%; background: #000; overflow: hidden; }
            #player-container { width: 100vw; height: 100vh; position: absolute; top: 0; left: 0; }
            iframe { width: 100% !important; height: 100% !important; border: 0 !important; }
            $adCss
          </style>
        </head>
        <body>
          <div id="player-container">
            <div id="player"></div>
          </div>
          <script>
            $adSkipperJs

            var tag = document.createElement('script');
            tag.src = "https://www.youtube.com/iframe_api";
            var firstScriptTag = document.getElementsByTagName('script')[0];
            firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

            var player;
            function onYouTubeIframeAPIReady() {
              player = new YT.Player('player', {
                videoId: '$videoId',
                playerVars: {
                  'autoplay': $autoplayVal,
                  'controls': 1,
                  'rel': 0,
                  'playsinline': 1,
                  'enablejsapi': 1,
                  'iv_load_policy': 3,
                  'modestbranding': 1,
                  'fs': 1,
                  'origin': 'https://www.youtube.com'
                },
                events: {
                  'onReady': function(e) { $onReadyJs }
                }
              });
            }

            function playVideo() {
              if (player && player.playVideo) player.playVideo();
            }

            function pauseVideo() {
              if (player && player.pauseVideo) player.pauseVideo();
            }

            function seekRelative(sec) {
              if (player && player.getCurrentTime && player.seekTo) {
                var current = player.getCurrentTime();
                player.seekTo(Math.max(0, current + sec), true);
              }
            }
          </script>
        </body>
        </html>
    """.trimIndent()
}
