package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.auth.GoogleAccountProfile
import com.example.ui.theme.YouTubeBlack
import com.example.ui.theme.YouTubeFocusBorder
import com.example.ui.theme.YouTubeRed
import com.example.ui.theme.YouTubeSurface
import com.example.ui.theme.YouTubeTextPrimary
import com.example.ui.theme.YouTubeTextSecondary

enum class TvDestination(val label: String, val icon: ImageVector) {
    SEARCH("Pencarian", Icons.Default.Search),
    HOME("Beranda", Icons.Default.Home),
    MUSIC("Musik", Icons.Default.MusicNote),
    GAMING("Gaming", Icons.Default.SportsEsports),
    NEWS("Berita", Icons.Default.Newspaper),
    WEB_TV("Mode Web TV", Icons.Default.Language),
    ACCOUNT("Akun Google", Icons.Default.AccountCircle),
    SETTINGS("Setelan", Icons.Default.Settings)
}

@Composable
fun TvNavigationRail(
    currentDestination: TvDestination,
    account: GoogleAccountProfile,
    onDestinationSelected: (TvDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    var isRailFocused by remember { mutableStateOf(false) }
    val railWidth by animateDpAsState(
        targetValue = if (isRailFocused) 200.dp else 74.dp,
        label = "railWidth"
    )

    Column(
        modifier = modifier
            .width(railWidth)
            .fillMaxHeight()
            .background(YouTubeBlack)
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // App Branding / Logo
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = if (isRailFocused) Alignment.Start else Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(YouTubeRed, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "▶",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                if (isRailFocused) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "YouTube",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "TV EDITION",
                            color = YouTubeRed,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Navigation Items
            TvDestination.values().forEach { destination ->
                val isSelected = destination == currentDestination
                var isItemFocused by remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .tvFocusable(
                            shape = RoundedCornerShape(10.dp),
                            focusedBorderColor = YouTubeFocusBorder,
                            scaleOnFocus = 1.04f,
                            onClick = { onDestinationSelected(destination) },
                            onFocusChange = {
                                isItemFocused = it
                                if (it) isRailFocused = true
                            }
                        )
                        .background(
                            color = when {
                                isItemFocused -> Color(0xFF333333)
                                isSelected -> YouTubeSurface
                                else -> Color.Transparent
                            },
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (destination == TvDestination.ACCOUNT && account.isLoggedIn && account.avatarUrl.isNotEmpty()) {
                        AsyncImage(
                            model = account.avatarUrl,
                            contentDescription = account.displayName,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = destination.label,
                            tint = when {
                                isSelected -> YouTubeRed
                                isItemFocused -> Color.White
                                else -> YouTubeTextSecondary
                            },
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    if (isRailFocused) {
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = destination.label,
                            color = if (isSelected || isItemFocused) Color.White else YouTubeTextSecondary,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // Bottom Remote Helper Note
        if (isRailFocused) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color(0xFF161616), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = "D-Pad [▶] Ke Konten\n[OK] Memilih",
                    color = YouTubeTextSecondary,
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                )
            }
        }
    }
}
