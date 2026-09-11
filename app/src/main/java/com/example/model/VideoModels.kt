package com.example.model

data class YouTubeVideo(
    val id: String,
    val title: String,
    val channelTitle: String,
    val channelAvatarUrl: String = "",
    val thumbnailUrl: String = "https://img.youtube.com/vi/$id/hqdefault.jpg",
    val duration: String,
    val views: String,
    val publishedAt: String,
    val category: String,
    val description: String = ""
)

object SampleVideoRepository {
    val sampleVideos = listOf(
        YouTubeVideo(
            id = "jfKfPfyJRdk",
            title = "lofi hip hop radio 📚 - beats to relax/study to",
            channelTitle = "Lofi Girl",
            channelAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100&auto=format&fit=crop&q=80",
            duration = "LIVE",
            views = "48K menonton",
            publishedAt = "Sedang Live",
            category = "Musik",
            description = "Suara lofi santai untuk menemani belajar, bersantai, dan bekerja di layar Android TV Anda."
        ),
        YouTubeVideo(
            id = "kJQP7kiw5Fk",
            title = "Luis Fonsi - Despacito ft. Daddy Yankee",
            channelTitle = "Luis Fonsi",
            channelAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&auto=format&fit=crop&q=80",
            duration = "4:42",
            views = "8.5M tayangan",
            publishedAt = "Musik Populer",
            category = "Musik",
            description = "Video musik legendaris dengan miliaran penonton di seluruh dunia."
        ),
        YouTubeVideo(
            id = "fJ9rUzIMcZQ",
            title = "Queen - Bohemian Rhapsody (Official Video Remastered)",
            channelTitle = "Queen Official",
            channelAvatarUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=100&auto=format&fit=crop&q=80",
            duration = "5:59",
            views = "1.7M tayangan",
            publishedAt = "Remastered HD",
            category = "Musik",
            description = "Lagu legendaris dari band rock Queen dengan audio dan video yang diperbarui."
        ),
        YouTubeVideo(
            id = "7wtfhZwyrcc",
            title = "Imagine Dragons - Believer (Official Music Video)",
            channelTitle = "ImagineDragons",
            channelAvatarUrl = "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=100&auto=format&fit=crop&q=80",
            duration = "3:36",
            views = "2.4M tayangan",
            publishedAt = "Official Video",
            category = "Musik",
            description = "Imagine Dragons membawakan Believer dengan visual dinamis berenergi tinggi."
        ),
        YouTubeVideo(
            id = "RgKAFK5djSk",
            title = "Wiz Khalifa - See You Again ft. Charlie Puth [Official Video]",
            channelTitle = "Wiz Khalifa",
            channelAvatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=100&auto=format&fit=crop&q=80",
            duration = "3:57",
            views = "6.1M tayangan",
            publishedAt = "Furious 7 Soundtrack",
            category = "Musik",
            description = "Soundtrack Fast & Furious 7 yang menyentuh hati jutaan penggemar."
        ),
        YouTubeVideo(
            id = "L_LUpnjgPso",
            title = "Review Smart TV 4K Terbaik untuk Ruang Keluarga: Harga Terjangkau!",
            channelTitle = "Gadget Tech Indonesia",
            channelAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&auto=format&fit=crop&q=80",
            duration = "14:28",
            views = "820K tayangan",
            publishedAt = "3 hari yang lalu",
            category = "Teknologi",
            description = "Pembahasan lengkap mengenai TV Android 4K HDR dengan remote D-pad dan panel jernih."
        ),
        YouTubeVideo(
            id = "bTqVqk7FSmY",
            title = "10 Fitur Tersembunyi Android TV yang Wajib Kamu Ketahui!",
            channelTitle = "Kanal Tutorial TV",
            channelAvatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=100&auto=format&fit=crop&q=80",
            duration = "11:05",
            views = "450K tayangan",
            publishedAt = "1 minggu yang lalu",
            category = "Teknologi",
            description = "Maksimalkan pengalaman menonton Android TV Anda dengan trik navigasi remote dan settingan rahasia."
        ),
        YouTubeVideo(
            id = "M576WGiDBdQ",
            title = "Highlights Pertandingan Sepakbola Dramatis 2026 - Gol Menit Terakhir!",
            channelTitle = "Sports Arena TV",
            channelAvatarUrl = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=100&auto=format&fit=crop&q=80",
            duration = "12:14",
            views = "1.5M tayangan",
            publishedAt = "Kemarin",
            category = "Trending",
            description = "Momen dramatis pertandingan sepak bola kelas dunia dengan gol kemenangan di babak perpanjangan waktu."
        ),
        YouTubeVideo(
            id = "ScMzIvxBSi4",
            title = "Eksplorasi Alam Bawah Laut 4K Ultra HD - Keindahan Terumbu Karang",
            channelTitle = "Nature Relax TV",
            channelAvatarUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=100&auto=format&fit=crop&q=80",
            duration = "25:40",
            views = "3.2M tayangan",
            publishedAt = "4K HDR",
            category = "Trending",
            description = "Video keindahan alam 4K Ultra HD sangat cocok dinikmati di layar TV besar bersama keluarga."
        ),
        YouTubeVideo(
            id = "aqz-KE-bpKQ",
            title = "Big Buck Bunny 4K Animation Cinema Experience",
            channelTitle = "Blender Foundation",
            channelAvatarUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=100&auto=format&fit=crop&q=80",
            duration = "9:56",
            views = "14M tayangan",
            publishedAt = "Animasi Klasik",
            category = "Trending",
            description = "Animasi legendaris open source berkualitas sinematik tinggi."
        ),
        YouTubeVideo(
            id = "LXb3EKWsInQ",
            title = "Costa Rica 4K 60fps HDR Nature Wildlife Video",
            channelTitle = "Jacob & Katie Schwarz",
            channelAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100&auto=format&fit=crop&q=80",
            duration = "5:14",
            views = "89M tayangan",
            publishedAt = "Ultra HD 60fps",
            category = "Trending",
            description = "Uji ketajaman dan warna layar Android TV Anda dengan video dokumenter alam Costa Rica."
        ),
        YouTubeVideo(
            id = "kXYiU_JCYtU",
            title = "Linkin Park - Numb (Official Music Video)",
            channelTitle = "Linkin Park",
            channelAvatarUrl = "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=100&auto=format&fit=crop&q=80",
            duration = "3:07",
            views = "2.1M tayangan",
            publishedAt = "4K Remaster",
            category = "Musik",
            description = "Lagu legendaris dari era 2000-an yang tak lekang oleh waktu."
        ),
        YouTubeVideo(
            id = "dQw4w9WgXcQ",
            title = "Rick Astley - Never Gonna Give You Up (Official Music Video)",
            channelTitle = "Rick Astley",
            channelAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&auto=format&fit=crop&q=80",
            duration = "3:32",
            views = "1.5M tayangan",
            publishedAt = "Klasik Terkenal",
            category = "Trending",
            description = "Lagu klasik ceria yang selalu menghibur penggemar musik pop dunia."
        ),
        YouTubeVideo(
            id = "C0DPdy98e4c",
            title = "Gameplay Petualangan Grafis Maksimal Next-Gen di Konsol 2026",
            channelTitle = "Gamer Nation TV",
            channelAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&auto=format&fit=crop&q=80",
            duration = "18:22",
            views = "650K tayangan",
            publishedAt = "2 hari yang lalu",
            category = "Gaming",
            description = "Cuplikan gameplay spektakuler 60fps dengan grafis memukau di layar lebar."
        ),
        YouTubeVideo(
            id = "EngW7tLk6R8",
            title = "Kejuaraan Dunia Esports 2026 - Grand Final Highlight",
            channelTitle = "Esports Global TV",
            channelAvatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=100&auto=format&fit=crop&q=80",
            duration = "22:15",
            views = "1.1M tayangan",
            publishedAt = "Minggu lalu",
            category = "Gaming",
            description = "Pertarungan epik antar tim terbaik di arena kejuaraan dunia."
        ),
        YouTubeVideo(
            id = "2Vv-BfVoq4g",
            title = "Ed Sheeran - Perfect (Official Music Video)",
            channelTitle = "Ed Sheeran",
            channelAvatarUrl = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=100&auto=format&fit=crop&q=80",
            duration = "4:39",
            views = "3.8M tayangan",
            publishedAt = "Musik Akustik",
            category = "Musik",
            description = "Lagu balada cinta yang menyentuh hati di suasana salju musim dingin."
        ),
        YouTubeVideo(
            id = "V1Pl8CzNzCw",
            title = "Berita Dunia Terkini: Perkembangan Teknologi AI dan Satelit Luar Angkasa",
            channelTitle = "Warta Global Nusantara",
            channelAvatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=100&auto=format&fit=crop&q=80",
            duration = "16:45",
            views = "380K tayangan",
            publishedAt = "Hari ini",
            category = "Berita",
            description = "Rangkuman informasi aktual dari dalam dan luar negeri seputar sains dan teknologi."
        ),
        YouTubeVideo(
            id = "y6120QOlsfU",
            title = "Darude - Sandstorm (Official Video)",
            channelTitle = "Darude",
            channelAvatarUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=100&auto=format&fit=crop&q=80",
            duration = "3:52",
            views = "240M tayangan",
            publishedAt = "Elektronik Klasik",
            category = "Musik",
            description = "Musik elektronik bersemangat tinggi yang menjadi ikon di seluruh dunia."
        )
    )

    fun searchVideos(query: String): List<YouTubeVideo> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return sampleVideos
        
        // Check if query is a YouTube Video ID (e.g. 11 characters) or full URL
        val extractedId = extractVideoId(trimmed)
        if (extractedId != null) {
            return listOf(
                YouTubeVideo(
                    id = extractedId,
                    title = "Video YouTube: $extractedId",
                    channelTitle = "YouTube Video Direct",
                    duration = "Video",
                    views = "Hasil Langsung",
                    publishedAt = "ID Ditemukan",
                    category = "Pencarian",
                    description = "Video yang diputar langsung menggunakan ID atau link URL YouTube yang dimasukkan."
                )
            ) + sampleVideos.filter { it.id == extractedId }
        }

        return sampleVideos.filter { video ->
            video.title.contains(trimmed, ignoreCase = true) ||
            video.channelTitle.contains(trimmed, ignoreCase = true) ||
            video.category.contains(trimmed, ignoreCase = true) ||
            video.description.contains(trimmed, ignoreCase = true)
        }
    }

    private fun extractVideoId(input: String): String? {
        if (input.length == 11 && !input.contains(" ") && !input.contains("/")) {
            return input
        }
        val regex = "(?:youtube(?:-nocookie)?\\.com/(?:[^/\\n\\s]+/.+/|(?:v|e(?:mbed)?)/|.*[?&]v=)|youtu\\.be/)([a-zA-Z0-9_-]{11})".toRegex()
        val match = regex.find(input)
        return match?.groupValues?.getOrNull(1)
    }
}
