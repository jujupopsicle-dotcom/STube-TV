package com.example.util

import android.webkit.WebResourceResponse
import java.io.ByteArrayInputStream

/**
 * YouTube Ad Blocker & Skipper utility.
 * Intercepts ad tracking / banner URLs and injects continuous ad-skipping logic.
 * Compatible with Android 6.0+ (API 23+).
 */
object YouTubeAdBlocker {

    private val BLOCKED_DOMAINS_AND_PATHS = listOf(
        "googleads.g.doubleclick.net",
        "pubads.g.doubleclick.net",
        "adservice.google.com",
        "pagead2.googlesyndication.com",
        "securepubads.g.doubleclick.net",
        "ads.youtube.com",
        "youtube.com/pagead/",
        "youtube.com/api/stats/ads",
        "youtube.com/ptracking",
        "youtube.com/get_midroll_info",
        "video-stats.l.google.com"
    )

    fun shouldBlockUrl(url: String?): Boolean {
        if (url.isNullOrEmpty()) return false
        val lower = url.lowercase()
        return BLOCKED_DOMAINS_AND_PATHS.any { lower.contains(it) }
    }

    fun createEmptyResponse(): WebResourceResponse {
        return WebResourceResponse(
            "text/plain",
            "UTF-8",
            ByteArrayInputStream(ByteArray(0))
        )
    }

    const val AD_SKIPPER_JAVASCRIPT = """
        (function() {
            if (window.__ytAdBlockerInjected) return;
            window.__ytAdBlockerInjected = true;

            function runAdSkipper() {
                try {
                    // 1. Click skip buttons immediately
                    var skipSelectors = [
                        '.ytp-ad-skip-button',
                        '.ytp-ad-skip-button-modern',
                        '.ytp-skip-ad-button',
                        '.videoAdUiSkipButton',
                        '.ytp-ad-overlay-close-button',
                        'button[id*="skip-button"]',
                        'button[class*="skip-button"]',
                        'button.ytp-ad-skip-button',
                        '.ytp-ad-preview-container',
                        '[aria-label*="Skip ad"]',
                        '[aria-label*="Lewati iklan"]',
                        '[aria-label*="Skip"]'
                    ];

                    for (var i = 0; i < skipSelectors.length; i++) {
                        var btn = document.querySelector(skipSelectors[i]);
                        if (btn && typeof btn.click === 'function') {
                            btn.click();
                        }
                    }

                    // 2. Fast forward and mute unskippable video ads
                    var adOverlay = document.querySelector('.ad-showing, .ad-interrupting, .ytp-ad-player-overlay');
                    var video = document.querySelector('video');
                    if (adOverlay && video) {
                        video.muted = true;
                        video.playbackRate = 16.0;
                        if (isFinite(video.duration) && video.duration > 0) {
                            video.currentTime = video.duration;
                        }
                    } else if (video && video.playbackRate > 2.0) {
                        video.playbackRate = 1.0;
                        video.muted = false;
                    }

                    // 3. Hide banner ads and overlay sponsors
                    var banners = document.querySelectorAll(
                        '.ytp-ad-overlay-container, .ytp-ad-message-container, .ytp-ad-player-overlay, ytd-ad-slot-renderer, ytd-banner-promo-renderer, ytd-in-feed-ad-layout-renderer, #masthead-ad'
                    );
                    for (var j = 0; j < banners.length; j++) {
                        banners[j].style.setProperty('display', 'none', 'important');
                        banners[j].style.setProperty('opacity', '0', 'important');
                        banners[j].style.setProperty('height', '0px', 'important');
                    }
                } catch (e) {
                    // silent catch
                }
            }

            setInterval(runAdSkipper, 200);
            runAdSkipper();
        })();
    """
}
