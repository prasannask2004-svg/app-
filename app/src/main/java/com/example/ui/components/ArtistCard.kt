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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Artist
import com.example.ui.theme.CosmicDarkSurface
import com.example.ui.theme.GlassBorderHighlight
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.PurpleGlow

@Composable
fun ArtistCard(
    artist: Artist,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(100.dp)
            .clickable(onClick = onClick)
            .testTag("artist_card_${artist.id}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Circular Avatar with Glowing Border
        Box(
            modifier = Modifier
                .size(80.dp)
                .shadow(12.dp, CircleShape, spotColor = PurpleGlow)
                .clip(CircleShape)
                .background(CosmicDarkSurface)
                .border(
                    width = if (isSelected) 2.5.dp else 1.dp,
                    brush = if (isSelected) Brush.horizontalGradient(listOf(PurpleGlow, PinkAccent))
                    else Brush.verticalGradient(listOf(GlassBorderHighlight, Color.White.copy(0.2f))),
                    shape = CircleShape
                )
        ) {
            AsyncImage(
                model = artist.photoUrl,
                contentDescription = artist.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp)
            )

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(PinkAccent)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Artist Name & Role
        Text(
            text = artist.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) PurpleGlow else Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Text(
            text = artist.language,
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
