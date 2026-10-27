package com.example.data

import com.example.model.Artist
import com.example.model.MediaAlbum
import com.example.model.MediaType
import com.example.model.MediaTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MediaRepository(private val favoritesDao: FavoritesDao) {

    val artists = listOf(
        Artist(
            id = "art_ar_rahman",
            name = "A.R. Rahman",
            language = "Tamil",
            photoUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=400&q=80",
            role = "Oscars Master Maestro",
            songCount = 24
        ),
        Artist(
            id = "art_anirudh",
            name = "Anirudh Ravichander",
            language = "Tamil",
            photoUrl = "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?auto=format&fit=crop&w=400&q=80",
            role = "Rockstar Composer",
            songCount = 30
        ),
        Artist(
            id = "art_yuvan",
            name = "Yuvan Shankar Raja",
            language = "Tamil",
            photoUrl = "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?auto=format&fit=crop&w=400&q=80",
            role = "BGM King",
            songCount = 18
        ),
        Artist(
            id = "art_santhosh",
            name = "Santhosh Narayanan",
            language = "Tamil",
            photoUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=400&q=80",
            role = "Folk & Jazz Maestro",
            songCount = 15
        ),
        Artist(
            id = "art_imman",
            name = "D. Imman",
            language = "Tamil",
            photoUrl = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?auto=format&fit=crop&w=400&q=80",
            role = "Rural Hit Machine",
            songCount = 16
        ),
        Artist(
            id = "art_deva",
            name = "Deva",
            language = "Tamil",
            photoUrl = "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?auto=format&fit=crop&w=400&q=80",
            role = "Gaana King Deva",
            songCount = 20
        ),
        Artist(
            id = "art_sirpy",
            name = "Sirpy",
            language = "Tamil",
            photoUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
            role = "90s Melodic Master",
            songCount = 12
        ),
        Artist(
            id = "art_srikanth_deva",
            name = "Srikanth Deva",
            language = "Tamil",
            photoUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
            role = "High Energy Composer",
            songCount = 14
        ),
        Artist(
            id = "art_dsp",
            name = "Devi Sri Prasad",
            language = "Telugu",
            photoUrl = "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?auto=format&fit=crop&w=400&q=80",
            role = "Rockstar DSP",
            songCount = 22
        ),
        Artist(
            id = "art_sushin",
            name = "Sushin Shyam",
            language = "Malayalam",
            photoUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=400&q=80",
            role = "Malayalam Trendsetter",
            songCount = 16
        )
    )

    val newAlbums = listOf(
        MediaAlbum(
            id = "alb_leo",
            title = "Leo",
            artist = "Anirudh Ravichander",
            language = "Tamil",
            coverUrl = "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?auto=format&fit=crop&w=600&q=80",
            releaseYear = "2024",
            trackCount = 7,
            genre = "Action Thriller Pop"
        ),
        MediaAlbum(
            id = "alb_aavesham",
            title = "Aavesham",
            artist = "Sushin Shyam",
            language = "Malayalam",
            coverUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=600&q=80",
            releaseYear = "2024",
            trackCount = 6,
            genre = "Malayalam Beats"
        ),
        MediaAlbum(
            id = "alb_pushpa2",
            title = "Pushpa 2: The Rule",
            artist = "Devi Sri Prasad",
            language = "Telugu",
            coverUrl = "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?auto=format&fit=crop&w=600&q=80",
            releaseYear = "2024",
            trackCount = 8,
            genre = "Telugu Mass Hits"
        ),
        MediaAlbum(
            id = "alb_kantara",
            title = "Kantara: Chapter 1",
            artist = "B. Ajaneesh Loknath",
            language = "Kannada",
            coverUrl = "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?auto=format&fit=crop&w=600&q=80",
            releaseYear = "2024",
            trackCount = 5,
            genre = "Divine Folk Melodies"
        ),
        MediaAlbum(
            id = "alb_goat",
            title = "The Greatest Of All Time (GOAT)",
            artist = "Yuvan Shankar Raja",
            language = "Tamil",
            coverUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=600&q=80",
            releaseYear = "2024",
            trackCount = 6,
            genre = "Tamil Sci-Fi Beats"
        ),
        MediaAlbum(
            id = "alb_devara",
            title = "Devara Part 1",
            artist = "Anirudh Ravichander",
            language = "Telugu",
            coverUrl = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?auto=format&fit=crop&w=600&q=80",
            releaseYear = "2024",
            trackCount = 6,
            genre = "Sea Action Hymns"
        )
    )

    val sampleTracks = listOf(
        // ANIRUDH RAVICHANDER
        MediaTrack(
            id = "tam_1",
            title = "Naa Ready (Leo)",
            artist = "Anirudh Ravichander & Thalapathy Vijay",
            artistId = "art_anirudh",
            album = "Leo",
            language = "Tamil",
            type = MediaType.AUDIO,
            mediaUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
            coverUrl = "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?auto=format&fit=crop&w=600&q=80",
            durationMs = 372000L,
            genre = "Tamil Folk Pop",
            isDolbyAtmosSupported = true
        ),
        MediaTrack(
            id = "tam_2",
            title = "Halamithi Habibo (Arabic Kuthu)",
            artist = "Anirudh Ravichander & Jonita Gandhi",
            artistId = "art_anirudh",
            album = "Beast",
            language = "Tamil",
            type = MediaType.AUDIO,
            mediaUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
            coverUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=600&q=80",
            durationMs = 423000L,
            genre = "Tamil Kuthu",
            isDolbyAtmosSupported = true
        ),

        // AR RAHMAN
        MediaTrack(
            id = "tam_3",
            title = "Marakkuma Nenjam",
            artist = "A.R. Rahman",
            artistId = "art_ar_rahman",
            album = "Vendhu Thanindhathu Kaadu",
            language = "Tamil",
            type = MediaType.AUDIO,
            mediaUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
            coverUrl = "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?auto=format&fit=crop&w=600&q=80",
            durationMs = 344000L,
            genre = "Tamil Soul Melody",
            isDolbyAtmosSupported = true
        ),

        // YUVAN SHANKAR RAJA
        MediaTrack(
            id = "tam_4",
            title = "Whistle Podu (GOAT)",
            artist = "Thalapathy Vijay & Yuvan Shankar Raja",
            artistId = "art_yuvan",
            album = "The Greatest Of All Time",
            language = "Tamil",
            type = MediaType.AUDIO,
            mediaUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
            coverUrl = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?auto=format&fit=crop&w=600&q=80",
            durationMs = 302000L,
            genre = "Tamil Sci-Fi Dance",
            isDolbyAtmosSupported = true
        ),

        // SANTHOSH NARAYANAN
        MediaTrack(
            id = "tam_5",
            title = "Kalki Theme & Veera Dheera",
            artist = "Santhosh Narayanan",
            artistId = "art_santhosh",
            album = "Kalki 2898 AD",
            language = "Tamil",
            type = MediaType.AUDIO,
            mediaUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3",
            coverUrl = "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?auto=format&fit=crop&w=600&q=80",
            durationMs = 350000L,
            genre = "Santhosh Folk Fusion",
            isDolbyAtmosSupported = true
        ),

        // D. IMMAN
        MediaTrack(
            id = "tam_6",
            title = "Kannana Kanne (Viswasam)",
            artist = "D. Imman & Sid Sriram",
            artistId = "art_imman",
            album = "Viswasam",
            language = "Tamil",
            type = MediaType.AUDIO,
            mediaUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3",
            coverUrl = "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?auto=format&fit=crop&w=600&q=80",
            durationMs = 280000L,
            genre = "Imman Heartfelt Melody",
            isDolbyAtmosSupported = true
        ),

        // DEVA
        MediaTrack(
            id = "tam_7",
            title = "Salomiya & Gaana Beats",
            artist = "Deva",
            artistId = "art_deva",
            album = "Deva Hits",
            language = "Tamil",
            type = MediaType.AUDIO,
            mediaUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3",
            coverUrl = "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?auto=format&fit=crop&w=600&q=80",
            durationMs = 310000L,
            genre = "Classic Deva Gaana",
            isDolbyAtmosSupported = true
        ),

        // SIRPY
        MediaTrack(
            id = "tam_8",
            title = "Ullathai Thitha 90s Vocal Hits",
            artist = "Sirpy",
            artistId = "art_sirpy",
            album = "90s Sirpy Hits",
            language = "Tamil",
            type = MediaType.AUDIO,
            mediaUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3",
            coverUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=600&q=80",
            durationMs = 290000L,
            genre = "90s Sirpy Vocal Melody",
            isDolbyAtmosSupported = true
        ),

        // SRIKANTH DEVA
        MediaTrack(
            id = "tam_9",
            title = "Maja Maja High Energy Hits",
            artist = "Srikanth Deva",
            artistId = "art_srikanth_deva",
            album = "High Energy Kuthu",
            language = "Tamil",
            type = MediaType.AUDIO,
            mediaUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-9.mp3",
            coverUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=600&q=80",
            durationMs = 275000L,
            genre = "Srikanth Deva Kuthu",
            isDolbyAtmosSupported = true
        ),

        // DEVI SRI PRASAD (TELUGU)
        MediaTrack(
            id = "tel_1",
            title = "Pushpa Pushpa (Pushpa 2)",
            artist = "Devi Sri Prasad & Nakash Aziz",
            artistId = "art_dsp",
            album = "Pushpa 2: The Rule",
            language = "Telugu",
            type = MediaType.AUDIO,
            mediaUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-10.mp3",
            coverUrl = "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?auto=format&fit=crop&w=600&q=80",
            durationMs = 320000L,
            genre = "DSP Mass Beat",
            isDolbyAtmosSupported = true
        ),

        // SUSHIN SHYAM (MALAYALAM)
        MediaTrack(
            id = "mal_1",
            title = "Illuminati (Aavesham)",
            artist = "Sushin Shyam & Dabzee",
            artistId = "art_sushin",
            album = "Aavesham",
            language = "Malayalam",
            type = MediaType.AUDIO,
            mediaUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-11.mp3",
            coverUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=600&q=80",
            durationMs = 380000L,
            genre = "Sushin Hip-Hop",
            isDolbyAtmosSupported = true
        ),

        // MUSIC VIDEOS
        MediaTrack(
            id = "vid_1",
            title = "Leo Naa Ready Live Concert Video",
            artist = "Anirudh Ravichander",
            artistId = "art_anirudh",
            album = "Leo Live",
            language = "Tamil",
            type = MediaType.VIDEO,
            mediaUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            coverUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=600&q=80",
            durationMs = 150000L,
            genre = "Music Video",
            isDolbyAtmosSupported = true
        )
    )

    fun getFavoriteIdsFlow(): Flow<Set<String>> {
        return favoritesDao.getAllFavorites().map { list ->
            list.map { it.id }.toSet()
        }
    }

    suspend fun toggleFavorite(track: MediaTrack) {
        val isFav = favoritesDao.isFavorite(track.id)
        if (isFav) {
            favoritesDao.deleteFavorite(track.id)
        } else {
            favoritesDao.insertFavorite(
                FavoriteTrackEntity(
                    id = track.id,
                    title = track.title,
                    artist = track.artist,
                    type = track.type.name,
                    mediaUrl = track.mediaUrl,
                    coverUrl = track.coverUrl
                )
            )
        }
    }
}
