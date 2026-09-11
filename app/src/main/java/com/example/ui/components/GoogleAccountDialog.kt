package com.example.ui.components

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.auth.GoogleAccountProfile
import com.example.ui.theme.YouTubeBlue
import com.example.ui.theme.YouTubeFocusBorder
import com.example.ui.theme.YouTubeGreen
import com.example.ui.theme.YouTubeRed
import com.example.ui.theme.YouTubeSurface
import com.example.ui.theme.YouTubeSurfaceVariant
import com.example.ui.theme.YouTubeTextPrimary
import com.example.ui.theme.YouTubeTextSecondary

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun GoogleAccountDialog(
    account: GoogleAccountProfile,
    onSignIn: (name: String, email: String) -> Unit,
    onSignOut: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showWebLogin by remember { mutableStateOf(false) }
    var showDeviceCode by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xDD000000))
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = YouTubeSurface),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.9f)
        ) {
            if (showWebLogin) {
                // Web Sign-In interface
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF161616))
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = YouTubeBlue)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Masuk Akun Google (Web TV)",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .tvFocusable(
                                    shape = CircleShape,
                                    focusedBorderColor = YouTubeFocusBorder,
                                    onClick = { showWebLogin = false }
                                )
                                .background(Color(0x33FFFFFF), CircleShape)
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Kembali", tint = Color.White)
                        }
                    }

                    AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                settings.apply {
                                    javaScriptEnabled = true
                                    domStorageEnabled = true
                                    databaseEnabled = true
                                    userAgentString =
                                        "Mozilla/5.0 (Linux; Android 10; SM-G980F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Mobile Safari/537.36"
                                }
                                CookieManager.getInstance().setAcceptCookie(true)
                                webChromeClient = WebChromeClient()
                                webViewClient = object : WebViewClient() {
                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)
                                        // If login succeeds / redirects to myaccount or youtube
                                        if (url != null && (url.contains("myaccount.google.com") || url.contains("youtube.com"))) {
                                            onSignIn("Juju Popsicle", "juju.popsicle@gmail.com")
                                            showWebLogin = false
                                        }
                                    }
                                }
                                loadUrl("https://accounts.google.com/signin/v2/identifier?service=youtube")
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else if (showDeviceCode) {
                // TV Device Code screen (youtube.com/activate)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Hubungkan Akun Google ke Android TV",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .tvFocusable(
                                    shape = CircleShape,
                                    focusedBorderColor = YouTubeFocusBorder,
                                    onClick = { showDeviceCode = false }
                                )
                                .background(Color(0x33FFFFFF), CircleShape)
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Tutup", tint = Color.White)
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 20.dp)
                    ) {
                        Text(
                            text = "1. Buka browser di Ponsel atau Komputer Anda:",
                            color = YouTubeTextSecondary,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "youtube.com/activate",
                            color = YouTubeBlue,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "2. Masukkan kode aktivasi berikut:",
                            color = YouTubeTextSecondary,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF111111), RoundedCornerShape(12.dp))
                                .border(2.dp, YouTubeRed, RoundedCornerShape(12.dp))
                                .padding(horizontal = 28.dp, vertical = 14.dp)
                        ) {
                            Text(
                                text = "YTTV - 9482",
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 4.sp
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Box(
                            modifier = Modifier
                                .tvFocusable(
                                    shape = RoundedCornerShape(12.dp),
                                    focusedBorderColor = YouTubeFocusBorder,
                                    onClick = {
                                        onSignIn("Juju Popsicle", "juju.popsicle@gmail.com")
                                        showDeviceCode = false
                                    }
                                )
                                .background(YouTubeGreen, RoundedCornerShape(12.dp))
                                .padding(horizontal = 24.dp, vertical = 12.dp)
                        ) {
                            Text("Konfirmasi Aktivasi Berhasil", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Box(
                            modifier = Modifier
                                .tvFocusable(
                                    shape = RoundedCornerShape(12.dp),
                                    focusedBorderColor = YouTubeFocusBorder,
                                    onClick = { showDeviceCode = false }
                                )
                                .background(Color(0xFF333333), RoundedCornerShape(12.dp))
                                .padding(horizontal = 24.dp, vertical = 12.dp)
                        ) {
                            Text("Batal", color = Color.White, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            } else {
                // Standard Account Overview / Sign In Screen
                Row(modifier = Modifier.fillMaxSize()) {
                    // Left Column: Profile Card & Status
                    Column(
                        modifier = Modifier
                            .weight(1.1f)
                            .fillMaxHeight()
                            .background(Color(0xFF141414))
                            .padding(28.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 20.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(YouTubeRed, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("G", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Integrasi Akun Google TV",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (account.isLoggedIn) {
                                // Profile Details
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (account.avatarUrl.isNotEmpty()) {
                                        AsyncImage(
                                            model = account.avatarUrl,
                                            contentDescription = account.displayName,
                                            modifier = Modifier
                                                .size(68.dp)
                                                .clip(CircleShape)
                                                .border(2.dp, YouTubeRed, CircleShape)
                                        )
                                    } else {
                                        Icon(
                                            Icons.Default.AccountCircle,
                                            contentDescription = null,
                                            tint = YouTubeTextSecondary,
                                            modifier = Modifier.size(68.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column {
                                        Text(
                                            text = account.displayName,
                                            color = Color.White,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = account.email,
                                            color = YouTubeTextSecondary,
                                            fontSize = 13.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        if (account.isPremium) YouTubeRed else Color(0xFF333333),
                                                        RoundedCornerShape(4.dp)
                                                    )
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = if (account.isPremium) "PREMIUM TV" else "GRATIS",
                                                    color = Color.White,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = account.channelHandle,
                                                color = YouTubeBlue,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                // Account Stats
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Liked Videos
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .background(Color(0xFF202020), RoundedCornerShape(12.dp))
                                            .padding(12.dp)
                                    ) {
                                        Column {
                                            Icon(Icons.Default.ThumbUp, contentDescription = null, tint = YouTubeRed, modifier = Modifier.size(20.dp))
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(text = "${account.likedVideosCount}", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                            Text(text = "Video Disukai", color = YouTubeTextSecondary, fontSize = 11.sp)
                                        }
                                    }

                                    // Watch History
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .background(Color(0xFF202020), RoundedCornerShape(12.dp))
                                            .padding(12.dp)
                                    ) {
                                        Column {
                                            Icon(Icons.Default.Stars, contentDescription = null, tint = YouTubeBlue, modifier = Modifier.size(20.dp))
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(text = "${account.historyCount}", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                            Text(text = "Histori Tonton", color = YouTubeTextSecondary, fontSize = 11.sp)
                                        }
                                    }
                                }
                            } else {
                                // Guest Mode View
                                Column(modifier = Modifier.padding(vertical = 12.dp)) {
                                    Icon(
                                        Icons.Default.AccountCircle,
                                        contentDescription = null,
                                        tint = YouTubeTextSecondary,
                                        modifier = Modifier.size(64.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Anda Belum Masuk Akun",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Masuk dengan akun Google untuk menyinkronkan rekomendasi, video tersimpan, dan playlist YouTube Anda.",
                                        color = YouTubeTextSecondary,
                                        fontSize = 13.sp,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }

                        // Close dialog button
                        Box(
                            modifier = Modifier
                                .tvFocusable(
                                    shape = RoundedCornerShape(10.dp),
                                    focusedBorderColor = YouTubeFocusBorder,
                                    onClick = onDismiss
                                )
                                .background(Color(0xFF282828), RoundedCornerShape(10.dp))
                                .padding(horizontal = 18.dp, vertical = 10.dp)
                        ) {
                            Text("Tutup Dialog", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                    }

                    // Right Column: Interactive Login Options
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(28.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Opsi Autentikasi Google",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // 1. One-Click Fast TV Login
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .tvFocusable(
                                    shape = RoundedCornerShape(12.dp),
                                    focusedBorderColor = YouTubeFocusBorder,
                                    scaleOnFocus = 1.04f,
                                    onClick = {
                                        onSignIn("Juju Popsicle", "juju.popsicle@gmail.com")
                                    }
                                )
                                .background(Color(0xFF222222), RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFF383838), RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = YouTubeGreen, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text("Masuk Cepat Google TV", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text("juju.popsicle@gmail.com", color = YouTubeTextSecondary, fontSize = 12.sp)
                                }
                            }
                        }

                        // 2. TV Link via Smartphone (QR / Device Code)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .tvFocusable(
                                    shape = RoundedCornerShape(12.dp),
                                    focusedBorderColor = YouTubeFocusBorder,
                                    scaleOnFocus = 1.04f,
                                    onClick = { showDeviceCode = true }
                                )
                                .background(Color(0xFF222222), RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFF383838), RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.QrCode, contentDescription = null, tint = YouTubeBlue, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text("Tautkan dengan HP / Browser", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text("youtube.com/activate", color = YouTubeTextSecondary, fontSize = 12.sp)
                                }
                            }
                        }

                        // 3. Web Google Sign-In (Direct WebView)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .tvFocusable(
                                    shape = RoundedCornerShape(12.dp),
                                    focusedBorderColor = YouTubeFocusBorder,
                                    scaleOnFocus = 1.04f,
                                    onClick = { showWebLogin = true }
                                )
                                .background(Color(0xFF222222), RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFF383838), RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Language, contentDescription = null, tint = YouTubeRed, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text("Masuk Langsung Web Google", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text("accounts.google.com/signin", color = YouTubeTextSecondary, fontSize = 12.sp)
                                }
                            }
                        }

                        // 4. Sign Out / Switch Account Button
                        if (account.isLoggedIn) {
                            Spacer(modifier = Modifier.weight(1f))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .tvFocusable(
                                        shape = RoundedCornerShape(12.dp),
                                        focusedBorderColor = YouTubeFocusBorder,
                                        scaleOnFocus = 1.04f,
                                        onClick = onSignOut
                                    )
                                    .background(Color(0xFF441818), RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFF662222), RoundedCornerShape(12.dp))
                                    .padding(14.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.ExitToApp, contentDescription = null, tint = YouTubeRed, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text("Keluar dari Akun Ini", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
