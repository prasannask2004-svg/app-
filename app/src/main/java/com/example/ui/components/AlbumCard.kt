package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.MediaAlbum
import com.example.ui.theme.CosmicDarkSurface
import com.example.ui.theme.GlassBorderColor
import com.example.ui.theme.GlassBorderHighlight
import com.example.ui.theme.PurpleGlow

@Composable
fun AlbumCard(
    album: MediaAlbum,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(140.dp)
            .clickable(onClick = onClick)
            .testTag("album_card_${album.id}")
    ) {
        // Album Art Surface
        Box(
            modifier = Modifier
                .size(140.dp)
                .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = PurpleGlow)
                .clip(RoundedCornerShape(20.dp))
                .background(CosmicDarkSurface)
                .border(1.dp, GlassBorderHighlight, RoundedCornerShape(20.dp))
        ) {
            AsyncImage(
                model = album.coverUrl,
                contentDescription = album.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(140.dp)
            )

            // Language Badge Accent
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(0.65f))
                    .border(0.5.dp, GlassBorderColor, RoundedCornerShape(8.dp))
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    text = album.language,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Title & Artist
        Text(
            text = album.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1
        )
        Text(
            text = "${album.artist} • ${album.releaseYear}",
            fontSize = 11.sp,
            color = Color.White.copy(0.6f),
            maxLines = 1
        )
    }
}
