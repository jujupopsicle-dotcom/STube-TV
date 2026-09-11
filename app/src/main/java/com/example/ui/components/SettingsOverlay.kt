package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.auth.GoogleAccountProfile
import com.example.ui.theme.YouTubeBlue
import com.example.ui.theme.YouTubeFocusBorder
import com.example.ui.theme.YouTubeGreen
import com.example.ui.theme.YouTubeRed
import com.example.ui.theme.YouTubeSurface
import com.example.ui.theme.YouTubeTextPrimary
import com.example.ui.theme.YouTubeTextSecondary

/**
 * Settings overlay accessible via Android TV Remote.
 * Compatible with Android 6.0+ (API 23+) with D-Pad focus controls.
 * Allows users to toggle Autoplay and manage Google account logout.
 */
@Composable
fun SettingsOverlay(
    isAutoplayEnabled: Boolean,
    onToggleAutoplay: () -> Unit,
    isAdBlockerEnabled: Boolean = true,
    onToggleAdBlocker: () -> Unit = {},
    account: GoogleAccountProfile,
    onSignOut: () -> Unit,
    onOpenSignIn: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLogoutConfirmation by remember { mutableStateOf(false) }
    var logoutSuccessMessage by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xE6000000))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = YouTubeSurface),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.92f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(YouTubeRed, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Pengaturan Android TV",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Navigasi remote: gunakan panah [▲/▼] lalu tekan [OK] untuk memilih",
                                color = YouTubeTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Close Button
                    Box(
                        modifier = Modifier
                            .tvFocusable(
                                shape = CircleShape,
                                focusedBorderColor = YouTubeFocusBorder,
                                scaleOnFocus = 1.15f,
                                onClick = onDismiss
                            )
                            .background(Color(0x33FFFFFF), CircleShape)
                            .padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup Pengaturan",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // ==========================================
                    // 1. PENGATURAN PEMUTARAN (AUTOPLAY TOGGLE)
                    // ==========================================
                    Text(
                        text = "PEMUTARAN VIDEO",
                        color = YouTubeBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )

                    var isAutoplayCardFocused by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .tvFocusable(
                                shape = RoundedCornerShape(14.dp),
                                focusedBorderColor = YouTubeFocusBorder,
                                scaleOnFocus = 1.02f,
                                onClick = onToggleAutoplay,
                                onFocusChange = { isAutoplayCardFocused = it }
                            )
                            .background(
                                color = if (isAutoplayCardFocused) Color(0xFF2E2E2E) else Color(0xFF1E1E1E),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isAutoplayCardFocused) YouTubeFocusBorder else Color(0xFF333333),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .background(
                                            if (isAutoplayEnabled) Color(0x33FF0000) else Color(0x22FFFFFF),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayCircle,
                                        contentDescription = null,
                                        tint = if (isAutoplayEnabled) YouTubeRed else YouTubeTextSecondary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Putar Otomatis (Autoplay)",
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    if (isAutoplayEnabled) YouTubeRed else Color(0xFF444444),
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = if (isAutoplayEnabled) "AKTIF" else "NONAKTIF",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = if (isAutoplayEnabled) {
                                            "Video berikutnya akan diputar otomatis saat video saat ini selesai."
                                        } else {
                                            "Pemutaran akan berhenti setelah video saat ini selesai."
                                        },
                                        color = YouTubeTextSecondary,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // Switch
                            Switch(
                                checked = isAutoplayEnabled,
                                onCheckedChange = { onToggleAutoplay() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = YouTubeRed,
                                    uncheckedThumbColor = Color.LightGray,
                                    uncheckedTrackColor = Color(0xFF444444)
                                )
                            )
                        }
                    }

                    // Card Bebas Iklan (YouTube Ad-Free)
                    var isAdBlockerCardFocused by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .tvFocusable(
                                shape = RoundedCornerShape(14.dp),
                                focusedBorderColor = YouTubeFocusBorder,
                                scaleOnFocus = 1.02f,
                                onClick = onToggleAdBlocker,
                                onFocusChange = { isAdBlockerCardFocused = it }
                            )
                            .background(
                                color = if (isAdBlockerCardFocused) Color(0xFF2E2E2E) else Color(0xFF1E1E1E),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isAdBlockerCardFocused) YouTubeFocusBorder else Color(0xFF333333),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .background(
                                            if (isAdBlockerEnabled) Color(0x334CAF50) else Color(0x22FFFFFF),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = if (isAdBlockerEnabled) YouTubeGreen else YouTubeTextSecondary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Bebas Iklan (Ad Blocker & Auto-Skip)",
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    if (isAdBlockerEnabled) YouTubeGreen else Color(0xFF444444),
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = if (isAdBlockerEnabled) "AKTIF" else "NONAKTIF",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = if (isAdBlockerEnabled) {
                                            "Mencegat url iklan, mempercepat video iklan seketika, dan melewati iklan YouTube otomatis."
                                        } else {
                                            "Fitur pencegat dan pelewat iklan nonaktif. Iklan YouTube akan tampil normal."
                                        },
                                        color = YouTubeTextSecondary,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // Switch
                            Switch(
                                checked = isAdBlockerEnabled,
                                onCheckedChange = { onToggleAdBlocker() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = YouTubeGreen,
                                    uncheckedThumbColor = Color.LightGray,
                                    uncheckedTrackColor = Color(0xFF444444)
                                )
                            )
                        }
                    }

                    // ==========================================
                    // 2. MANAJEMEN AKUN & LOGOUT
                    // ==========================================
                    Text(
                        text = "AKUN GOOGLE & KEAMANAN",
                        color = YouTubeBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )

                    // Account Status Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF181818), RoundedCornerShape(14.dp))
                            .border(1.dp, Color(0xFF2E2E2E), RoundedCornerShape(14.dp))
                            .padding(20.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            // User Info Row
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (account.isLoggedIn && account.avatarUrl.isNotEmpty()) {
                                    AsyncImage(
                                        model = account.avatarUrl,
                                        contentDescription = account.displayName,
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .border(2.dp, YouTubeRed, CircleShape)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = null,
                                        tint = YouTubeTextSecondary,
                                        modifier = Modifier.size(54.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = account.displayName,
                                            color = Color.White,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    if (account.isLoggedIn) YouTubeGreen else Color(0xFF555555),
                                                    RoundedCornerShape(4.dp)
                                                )
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = if (account.isLoggedIn) "TERHUBUNG" else "TAMU",
                                                color = Color.White,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Text(
                                        text = account.email,
                                        color = YouTubeTextSecondary,
                                        fontSize = 13.sp
                                    )

                                    if (account.isLoggedIn) {
                                        Text(
                                            text = "YouTube Premium TV • ${account.channelHandle}",
                                            color = YouTubeBlue,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            // Logout / Login Actions
                            if (account.isLoggedIn) {
                                if (!showLogoutConfirmation) {
                                    // Button to initiate Logout
                                    var isLogoutBtnFocused by remember { mutableStateOf(false) }

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .tvFocusable(
                                                shape = RoundedCornerShape(10.dp),
                                                focusedBorderColor = YouTubeFocusBorder,
                                                scaleOnFocus = 1.02f,
                                                onClick = { showLogoutConfirmation = true },
                                                onFocusChange = { isLogoutBtnFocused = it }
                                            )
                                            .background(
                                                color = if (isLogoutBtnFocused) Color(0xFF6B1D1D) else Color(0xFF381414),
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .border(1.dp, Color(0xFF8B2525), RoundedCornerShape(10.dp))
                                            .padding(vertical = 12.dp, horizontal = 16.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ExitToApp,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = "Keluar dari Akun Google",
                                                color = Color.White,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                } else {
                                    // Logout Confirmation Panel
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFF2A1515), RoundedCornerShape(12.dp))
                                            .border(1.5.dp, YouTubeRed, RoundedCornerShape(12.dp))
                                            .padding(16.dp)
                                    ) {
                                        Column {
                                            Text(
                                                text = "Konfirmasi Keluar dari Akun",
                                                color = Color.White,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = "Apakah Anda yakin ingin keluar? Rekomendasi pribadi dan histori tonton tidak akan tersinkronisasi lagi di perangkat Android TV ini.",
                                                color = YouTubeTextSecondary,
                                                fontSize = 12.sp,
                                                lineHeight = 16.sp
                                            )
                                            Spacer(modifier = Modifier.height(14.dp))

                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                // Confirm Yes
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .tvFocusable(
                                                            shape = RoundedCornerShape(8.dp),
                                                            focusedBorderColor = YouTubeFocusBorder,
                                                            scaleOnFocus = 1.05f,
                                                            onClick = {
                                                                onSignOut()
                                                                showLogoutConfirmation = false
                                                                logoutSuccessMessage = true
                                                            }
                                                        )
                                                        .background(YouTubeRed, RoundedCornerShape(8.dp))
                                                        .padding(vertical = 10.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(
                                                            imageVector = Icons.Default.Check,
                                                            contentDescription = null,
                                                            tint = Color.White,
                                                            modifier = Modifier.size(16.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Text(
                                                            text = "Ya, Keluar Sekarang",
                                                            color = Color.White,
                                                            fontSize = 13.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }

                                                // Cancel
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .tvFocusable(
                                                            shape = RoundedCornerShape(8.dp),
                                                            focusedBorderColor = YouTubeFocusBorder,
                                                            scaleOnFocus = 1.05f,
                                                            onClick = { showLogoutConfirmation = false }
                                                        )
                                                        .background(Color(0xFF3A3A3A), RoundedCornerShape(8.dp))
                                                        .padding(vertical = 10.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = "Batal",
                                                        color = Color.White,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                // Logged out state - show Sign In button
                                var isLoginBtnFocused by remember { mutableStateOf(false) }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .tvFocusable(
                                            shape = RoundedCornerShape(10.dp),
                                            focusedBorderColor = YouTubeFocusBorder,
                                            scaleOnFocus = 1.02f,
                                            onClick = onOpenSignIn,
                                            onFocusChange = { isLoginBtnFocused = it }
                                        )
                                        .background(
                                            color = if (isLoginBtnFocused) Color.White else YouTubeBlue,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .padding(vertical = 12.dp, horizontal = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.AccountCircle,
                                            contentDescription = null,
                                            tint = if (isLoginBtnFocused) Color.Black else Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "Masuk dengan Akun Google",
                                            color = if (isLoginBtnFocused) Color.Black else Color.White,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // ==========================================
                    // 3. INFORMASI SISTEM & ANDROID 6.0+
                    // ==========================================
                    Text(
                        text = "INFORMASI SISTEM TV",
                        color = YouTubeBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF141414), RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Tv,
                                    contentDescription = null,
                                    tint = YouTubeGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Kompatibilitas: Android 6.0+ (Marshmallow & Android TV Leanback)",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = YouTubeBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Tombol Remote: Tekan [MENU], [SETELAN], atau [KEMBALI] kapan saja untuk mengontrol",
                                    color = YouTubeTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Footer Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .tvFocusable(
                                shape = RoundedCornerShape(10.dp),
                                focusedBorderColor = YouTubeFocusBorder,
                                scaleOnFocus = 1.05f,
                                onClick = onDismiss
                            )
                            .background(Color(0xFF282828), RoundedCornerShape(10.dp))
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Selesai & Tutup",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
