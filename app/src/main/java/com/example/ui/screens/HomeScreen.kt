package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.SampleVideoRepository
import com.example.model.YouTubeVideo
import com.example.ui.components.VideoCard
import com.example.ui.components.tvFocusable
import com.example.ui.theme.YouTubeFocusBorder
import com.example.ui.theme.YouTubeRed
import com.example.ui.theme.YouTubeTextPrimary
import com.example.ui.theme.YouTubeTextSecondary

val CATEGORIES = listOf("Semua", "Trending", "Musik", "Gaming", "Berita", "Teknologi")

@Composable
fun HomeScreen(
    onVideoSelected: (YouTubeVideo) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("Semua") }
    val featuredVideo = remember { SampleVideoRepository.sampleVideos.first() }

    val filteredVideos = remember(selectedCategory) {
        if (selectedCategory == "Semua") {
            SampleVideoRepository.sampleVideos
        } else {
            SampleVideoRepository.sampleVideos.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        }
    }

    val trendingVideos = remember { SampleVideoRepository.sampleVideos.filter { it.category == "Trending" || it.duration == "LIVE" } }
    val musicVideos = remember { SampleVideoRepository.sampleVideos.filter { it.category == "Musik" } }
    val techAndGaming = remember { SampleVideoRepository.sampleVideos.filter { it.category == "Gaming" || it.category == "Teknologi" } }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F0F)),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Hero Featured Banner
        item {
            HeroBanner(
                video = featuredVideo,
                onPlay = { onVideoSelected(featuredVideo) }
            )
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Category Filter Chips
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(CATEGORIES) { cat ->
                    val isSelected = cat == selectedCategory
                    var isFocused by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier
                            .tvFocusable(
                                shape = RoundedCornerShape(20.dp),
                                focusedBorderColor = YouTubeFocusBorder,
                                scaleOnFocus = 1.08f,
                                onClick = { selectedCategory = cat },
                                onFocusChange = { isFocused = it }
                            )
                            .background(
                                color = when {
                                    isSelected -> Color.White
                                    isFocused -> Color(0xFF383838)
                                    else -> Color(0xFF222222)
                                },
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 18.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) Color.Black else Color.White,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // If category is "Semua", show rich multiple horizontal sections
        if (selectedCategory == "Semua") {
            item {
                VideoRowSection(
                    title = "🔥 Sedang Populer & Live",
                    videos = trendingVideos,
                    onVideoSelected = onVideoSelected
                )
                Spacer(modifier = Modifier.height(22.dp))
            }

            item {
                VideoRowSection(
                    title = "🎵 Musik & Audio Pilihan",
                    videos = musicVideos,
                    onVideoSelected = onVideoSelected
                )
                Spacer(modifier = Modifier.height(22.dp))
            }

            item {
                VideoRowSection(
                    title = "🎮 Gaming & Teknologi Smart TV",
                    videos = techAndGaming,
                    onVideoSelected = onVideoSelected
                )
            }
        } else {
            // Filtered row
            item {
                VideoRowSection(
                    title = "Video Kategori: $selectedCategory",
                    videos = filteredVideos,
                    onVideoSelected = onVideoSelected
                )
            }
        }
    }
}

@Composable
fun HeroBanner(
    video: YouTubeVideo,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlayFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
    ) {
        // Backdrop Image
        AsyncImage(
            model = video.thumbnailUrl,
            contentDescription = video.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark Gradients for 10-foot readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0F0F0F),
                            Color(0xEE0F0F0F),
                            Color(0x770F0F0F),
                            Color.Transparent
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x990F0F0F),
                            Color(0xFF0F0F0F)
                        )
                    )
                )
        )

        // Banner Content
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .fillMaxWidth(0.65f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .background(YouTubeRed, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "SOROTAN UTAMA",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = video.channelTitle,
                    color = YouTubeTextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = video.title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 26.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = video.description,
                color = YouTubeTextSecondary,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Play Button with D-Pad focus
            Box(
                modifier = Modifier
                    .tvFocusable(
                        shape = RoundedCornerShape(24.dp),
                        focusedBorderColor = YouTubeFocusBorder,
                        scaleOnFocus = 1.08f,
                        onClick = onPlay,
                        onFocusChange = { isPlayFocused = it }
                    )
                    .background(
                        color = if (isPlayFocused) Color.White else YouTubeRed,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 22.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Putar Sekarang",
                        tint = if (isPlayFocused) Color.Black else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Putar Sekarang",
                        color = if (isPlayFocused) Color.Black else Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun VideoRowSection(
    title: String,
    videos: List<YouTubeVideo>,
    onVideoSelected: (YouTubeVideo) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(videos) { video ->
                VideoCard(
                    video = video,
                    cardWidth = 240.dp,
                    onClick = { onVideoSelected(video) }
                )
            }
        }
    }
}
