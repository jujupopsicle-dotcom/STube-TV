package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SpaceBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.YouTubeFocusBorder
import com.example.ui.theme.YouTubeRed
import com.example.ui.theme.YouTubeSurface
import com.example.ui.theme.YouTubeTextPrimary
import com.example.ui.theme.YouTubeTextSecondary

val QUICK_SEARCH_TAGS = listOf(
    "Trending",
    "Musik Indonesia",
    "Lofi Hip Hop",
    "Gaming",
    "Teknologi",
    "Trailer Film",
    "Berita Hari Ini",
    "Review Gadget",
    "Shorts Viral",
    "Podcast",
    "Animasi 4K"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TvSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isSearchFocused by remember { mutableStateOf(false) }
    var showVirtualKeyboard by remember { mutableStateOf(true) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Main Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(YouTubeSurface, RoundedCornerShape(24.dp))
                .border(
                    width = if (isSearchFocused) 2.5.dp else 1.dp,
                    color = if (isSearchFocused) YouTubeFocusBorder else Color(0xFF383838),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = if (isSearchFocused) YouTubeRed else YouTubeTextSecondary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(
                        text = "Ketik judul video, artis, atau masukkan link YouTube...",
                        color = YouTubeTextSecondary,
                        fontSize = 14.sp
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        color = YouTubeTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    cursorBrush = SolidColor(YouTubeRed),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .tvFocusable(
                            shape = RoundedCornerShape(8.dp),
                            focusedBorderColor = Color.Transparent,
                            scaleOnFocus = 1.0f,
                            onFocusChange = { isSearchFocused = it },
                            onClick = { onSearch(query) }
                        )
                )
            }

            if (query.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .tvFocusable(
                            shape = RoundedCornerShape(12.dp),
                            focusedBorderColor = YouTubeRed,
                            scaleOnFocus = 1.1f,
                            onClick = {
                                onQueryChange("")
                                onSearch("")
                            }
                        )
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear Search",
                        tint = YouTubeTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Search Trigger Button
            Box(
                modifier = Modifier
                    .tvFocusable(
                        shape = RoundedCornerShape(16.dp),
                        focusedBorderColor = YouTubeFocusBorder,
                        scaleOnFocus = 1.05f,
                        onClick = { onSearch(query) }
                    )
                    .background(YouTubeRed, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Cari",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Search Tags Pills
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(QUICK_SEARCH_TAGS) { tag ->
                var isTagFocused by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .tvFocusable(
                            shape = RoundedCornerShape(20.dp),
                            focusedBorderColor = YouTubeFocusBorder,
                            scaleOnFocus = 1.08f,
                            onClick = {
                                onQueryChange(tag)
                                onSearch(tag)
                            },
                            onFocusChange = { isTagFocused = it }
                        )
                        .background(
                            color = if (isTagFocused) Color(0xFF3D3D3D) else Color(0xFF222222),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = tag,
                        color = if (isTagFocused) Color.White else YouTubeTextSecondary,
                        fontSize = 12.sp,
                        fontWeight = if (isTagFocused) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        // TV Remote On-Screen Keyboard for easy D-Pad typing
        if (showVirtualKeyboard) {
            Spacer(modifier = Modifier.height(10.dp))
            TvVirtualKeyboard(
                onKeyPress = { char ->
                    val newQuery = query + char
                    onQueryChange(newQuery)
                    onSearch(newQuery)
                },
                onBackspace = {
                    if (query.isNotEmpty()) {
                        val newQuery = query.dropLast(1)
                        onQueryChange(newQuery)
                        onSearch(newQuery)
                    }
                },
                onSpace = {
                    val newQuery = "$query "
                    onQueryChange(newQuery)
                },
                onClear = {
                    onQueryChange("")
                    onSearch("")
                }
            )
        }
    }
}

@Composable
fun TvVirtualKeyboard(
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit,
    onSpace: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rows = listOf(
        listOf("A", "B", "C", "D", "E", "F", "1", "2", "3"),
        listOf("G", "H", "I", "J", "K", "L", "4", "5", "6"),
        listOf("M", "N", "O", "P", "Q", "R", "7", "8", "9"),
        listOf("S", "T", "U", "V", "W", "X", "Y", "Z", "0")
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF141414), RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Papan Ketik Remote D-Pad",
                color = YouTubeTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Gunakan panah remote & [OK] untuk mengetik",
                color = YouTubeTextSecondary.copy(alpha = 0.7f),
                fontSize = 10.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        rows.forEach { rowKeys ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                rowKeys.forEach { key ->
                    var isFocused by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .tvFocusable(
                                shape = RoundedCornerShape(6.dp),
                                focusedBorderColor = YouTubeFocusBorder,
                                scaleOnFocus = 1.12f,
                                onClick = { onKeyPress(key) },
                                onFocusChange = { isFocused = it }
                            )
                            .background(
                                color = if (isFocused) YouTubeRed else Color(0xFF242424),
                                shape = RoundedCornerShape(6.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = key,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Action Keys Row (Space, Backspace, Clear)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Space
            var isSpaceFocused by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .weight(2f)
                    .height(36.dp)
                    .tvFocusable(
                        shape = RoundedCornerShape(6.dp),
                        focusedBorderColor = YouTubeFocusBorder,
                        scaleOnFocus = 1.08f,
                        onClick = onSpace,
                        onFocusChange = { isSpaceFocused = it }
                    )
                    .background(
                        color = if (isSpaceFocused) Color(0xFF444444) else Color(0xFF282828),
                        shape = RoundedCornerShape(6.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.SpaceBar,
                        contentDescription = "Spasi",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Spasi", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // Backspace
            var isBackFocused by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .weight(1.5f)
                    .height(36.dp)
                    .tvFocusable(
                        shape = RoundedCornerShape(6.dp),
                        focusedBorderColor = YouTubeFocusBorder,
                        scaleOnFocus = 1.08f,
                        onClick = onBackspace,
                        onFocusChange = { isBackFocused = it }
                    )
                    .background(
                        color = if (isBackFocused) Color(0xFF555555) else Color(0xFF282828),
                        shape = RoundedCornerShape(6.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Backspace,
                        contentDescription = "Hapus",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Hapus", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // Clear All
            var isClearFocused by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .height(36.dp)
                    .tvFocusable(
                        shape = RoundedCornerShape(6.dp),
                        focusedBorderColor = YouTubeFocusBorder,
                        scaleOnFocus = 1.08f,
                        onClick = onClear,
                        onFocusChange = { isClearFocused = it }
                    )
                    .background(
                        color = if (isClearFocused) Color(0xFF882222) else Color(0xFF282828),
                        shape = RoundedCornerShape(6.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Bersihkan", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
