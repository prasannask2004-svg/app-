package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.QueueMusic
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.GeminiAiState
import com.example.model.GeminiAiTab
import com.example.model.MediaTrack
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.GlassBorderColor
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.PurpleGlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun GeminiModal(
    sheetState: SheetState,
    activeTab: GeminiAiTab,
    aiState: GeminiAiState,
    currentTrack: MediaTrack?,
    promptText: String,
    onTabSelect: (GeminiAiTab) -> Unit,
    onPromptChange: (String) -> Unit,
    onSendPrompt: (String) -> Unit,
    onRefreshStory: () -> Unit,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF0F1523).copy(alpha = 0.98f),
        contentColor = Color.White,
        scrimColor = Color.Black.copy(alpha = 0.65f),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(42.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 20.dp)
        ) {
            // Header with Gemini Gradient Title & Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(PinkAccent, PurpleGlow, CyanAccent)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = "Gemini AI",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Gemini AI Music Genie",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Powered by Google Gemini 3.8 Flash",
                            fontSize = 11.sp,
                            color = CyanAccent
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("gemini_close_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Selector: Song Story vs Mood DJ
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.07f))
                    .border(1.dp, GlassBorderColor, RoundedCornerShape(16.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val isStoryTab = activeTab == GeminiAiTab.SONG_STORY
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .then(
                            if (isStoryTab) {
                                Modifier.background(
                                    Brush.horizontalGradient(
                                        listOf(PurpleGlow.copy(alpha = 0.7f), PinkAccent.copy(alpha = 0.7f))
                                    )
                                )
                            } else {
                                Modifier.background(Color.Transparent)
                            }
                        )
                        .clickable { onTabSelect(GeminiAiTab.SONG_STORY) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.MusicNote,
                            contentDescription = null,
                            tint = if (isStoryTab) Color.White else Color.White.copy(0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Song Insights",
                            fontSize = 13.sp,
                            fontWeight = if (isStoryTab) FontWeight.Bold else FontWeight.Medium,
                            color = if (isStoryTab) Color.White else Color.White.copy(0.7f)
                        )
                    }
                }

                val isMoodTab = activeTab == GeminiAiTab.MOOD_DJ
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .then(
                            if (isMoodTab) {
                                Modifier.background(
                                    Brush.horizontalGradient(
                                        listOf(CyanAccent.copy(alpha = 0.5f), PurpleGlow.copy(alpha = 0.7f))
                                    )
                                )
                            } else {
                                Modifier.background(Color.Transparent)
                            }
                        )
                        .clickable { onTabSelect(GeminiAiTab.MOOD_DJ) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.QueueMusic,
                            contentDescription = null,
                            tint = if (isMoodTab) Color.White else Color.White.copy(0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Mood DJ",
                            fontSize = 13.sp,
                            fontWeight = if (isMoodTab) FontWeight.Bold else FontWeight.Medium,
                            color = if (isMoodTab) Color.White else Color.White.copy(0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Content Area depending on Tab
            when (activeTab) {
                GeminiAiTab.SONG_STORY -> {
                    SongStoryContent(
                        currentTrack = currentTrack,
                        aiState = aiState,
                        onRefreshStory = onRefreshStory,
                        onRetry = onRetry
                    )
                }
                GeminiAiTab.MOOD_DJ -> {
                    MoodDjContent(
                        aiState = aiState,
                        promptText = promptText,
                        onPromptChange = onPromptChange,
                        onSendPrompt = onSendPrompt,
                        onRetry = onRetry
                    )
                }
            }
        }
    }
}

@Composable
private fun SongStoryContent(
    currentTrack: MediaTrack?,
    aiState: GeminiAiState,
    onRefreshStory: () -> Unit,
    onRetry: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(bottom = 20.dp)
    ) {
        if (currentTrack != null) {
            // Track Info Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .border(1.dp, GlassBorderColor, RoundedCornerShape(16.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = currentTrack.coverUrl,
                    contentDescription = currentTrack.title,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentTrack.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${currentTrack.artist} • ${currentTrack.album}",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = currentTrack.language,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PinkAccent
                        )
                        if (currentTrack.isDolbyAtmosSupported) {
                            Text(
                                text = "• Dolby Atmos Spatial",
                                fontSize = 10.sp,
                                color = CyanAccent
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onRefreshStory,
                    modifier = Modifier.testTag("gemini_refresh_story_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "Re-analyze",
                        tint = CyanAccent
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // AI Response Render
        AiStateDisplay(
            aiState = aiState,
            loadingMessage = "Gemini is analyzing melody, ragas, instruments & lyrical meaning...",
            onRetry = onRetry
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MoodDjContent(
    aiState: GeminiAiState,
    promptText: String,
    onPromptChange: (String) -> Unit,
    onSendPrompt: (String) -> Unit,
    onRetry: () -> Unit
) {
    val scrollState = rememberScrollState()

    val quickChips = listOf(
        "🌧️ Monsoon Rain Melodies",
        "⚡ Anirudh High Energy Workout",
        "🌙 90s Rahman Nostalgia",
        "☕ Chill Acoustic Tamil Café",
        "🎻 Classic Ilaiyaraaja Solitude",
        "🔥 Telugu Mass Dance Bangers",
        "🌿 Peaceful Malayalam Nature Vibes"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(bottom = 20.dp)
    ) {
        // Quick Vibe Chips
        Text(
            text = "Instant Mood Suggestions",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(0.7f)
        )
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickChips.forEach { chip ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .border(1.dp, GlassBorderColor, RoundedCornerShape(12.dp))
                        .clickable {
                            onPromptChange(chip)
                            onSendPrompt(chip)
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = chip,
                        fontSize = 11.sp,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Input Box
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = promptText,
                onValueChange = onPromptChange,
                modifier = Modifier
                    .weight(1f)
                    .testTag("gemini_prompt_input"),
                placeholder = {
                    Text(
                        text = "e.g., Night drive Tamil songs with heavy violin...",
                        color = Color.White.copy(0.4f),
                        fontSize = 12.sp
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyanAccent,
                    unfocusedBorderColor = GlassBorderColor,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = CyanAccent,
                    focusedContainerColor = Color.White.copy(alpha = 0.05f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.03f)
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { onSendPrompt(promptText) },
                modifier = Modifier
                    .testTag("gemini_send_prompt_button")
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.linearGradient(listOf(PurpleGlow, PinkAccent)))
            ) {
                Icon(
                    imageVector = Icons.Rounded.Send,
                    contentDescription = "Ask Gemini",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // AI Response Render
        AiStateDisplay(
            aiState = aiState,
            loadingMessage = "Gemini DJ is curating your personalized playlist...",
            onRetry = onRetry
        )
    }
}

@Composable
private fun AiStateDisplay(
    aiState: GeminiAiState,
    loadingMessage: String,
    onRetry: () -> Unit
) {
    when (aiState) {
        is GeminiAiState.Idle -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = CyanAccent.copy(alpha = 0.7f),
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Ready to inspire your musical journey",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }

        is GeminiAiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        color = CyanAccent,
                        modifier = Modifier.size(36.dp),
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = loadingMessage,
                        fontSize = 13.sp,
                        color = Color.White.copy(0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        is GeminiAiState.Streaming -> {
            ResponseCard(text = aiState.text, isStreaming = true)
        }

        is GeminiAiState.Success -> {
            ResponseCard(text = aiState.text, isStreaming = false)
        }

        is GeminiAiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF3E141D).copy(alpha = 0.6f))
                    .border(1.dp, Color(0xFFFF5252).copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Warning,
                        contentDescription = "Error",
                        tint = Color(0xFFFF5252),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Gemini Insight Error",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = aiState.message,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onRetry,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PinkAccent
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Try Again", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ResponseCard(
    text: String,
    isStreaming: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .border(1.dp, GlassBorderColor, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Column {
            if (isStreaming) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(12.dp),
                        color = CyanAccent,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Generating live...",
                        fontSize = 11.sp,
                        color = CyanAccent
                    )
                }
            }
            Text(
                text = text,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = Color.White.copy(alpha = 0.95f)
            )
        }
    }
}
