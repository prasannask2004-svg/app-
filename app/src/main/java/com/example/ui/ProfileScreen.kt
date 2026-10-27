package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.AuthMethod
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingOrbsBackground
import com.example.ui.theme.CosmicDarkSurface
import com.example.ui.theme.GlassBorderColor
import com.example.ui.theme.GlassBorderHighlight
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.PurpleGlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: PlayerViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val userProfile = uiState.userProfile
    val safeInsets = WindowInsets.safeDrawing.asPaddingValues()

    var isEditingName by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(userProfile.name) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
    ) {
        GlowingOrbsBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = safeInsets.calculateTopPadding())
                .padding(horizontal = 20.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .testTag("profile_back_button")
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(CosmicDarkSurface)
                        .border(1.dp, GlassBorderColor, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "User Profile & Account",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Profile Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 20.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar Image
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .shadow(16.dp, CircleShape, spotColor = PurpleGlow)
                            .clip(CircleShape)
                            .border(3.dp, Brush.horizontalGradient(listOf(PurpleGlow, PinkAccent)), CircleShape)
                    ) {
                        AsyncImage(
                            model = userProfile.avatarUrl,
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // User Name / Edit
                    if (isEditingName) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = editedName,
                                onValueChange = { editedName = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("edit_name_input"),
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = CosmicDarkSurface,
                                    unfocusedContainerColor = CosmicDarkSurface,
                                    focusedBorderColor = PurpleGlow,
                                    unfocusedBorderColor = GlassBorderColor,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    viewModel.updateProfileName(editedName)
                                    isEditingName = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PurpleGlow)
                            ) {
                                Text("Save")
                            }
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = userProfile.name,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            IconButton(
                                onClick = { isEditingName = true },
                                modifier = Modifier.size(28.dp).testTag("edit_name_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Edit,
                                    contentDescription = "Edit Name",
                                    tint = Color.White.copy(0.6f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Member Status Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.horizontalGradient(listOf(PurpleGlow.copy(0.6f), PinkAccent.copy(0.6f)))
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (userProfile.isLoggedIn) "VIP Member • ${userProfile.memberStatus}" else "Guest Listener",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProfileStatItem(
                            icon = Icons.Rounded.Favorite,
                            value = "${uiState.favoriteTrackIds.size}",
                            label = "Favorites"
                        )
                        ProfileStatItem(
                            icon = Icons.Rounded.MusicNote,
                            value = "${uiState.tracks.size}",
                            label = "Tracks"
                        )
                        ProfileStatItem(
                            icon = Icons.Rounded.Language,
                            value = userProfile.preferredLanguage,
                            label = "Language"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Account Details Card
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Account Verification Details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DetailRow(
                        icon = Icons.Rounded.Phone,
                        label = "Mobile Number",
                        value = if (userProfile.mobileNumber.isNotEmpty()) userProfile.mobileNumber else "Not linked"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DetailRow(
                        icon = Icons.Rounded.Person,
                        label = "Auth Method",
                        value = when (userProfile.authMethod) {
                            AuthMethod.INDIA_MOBILE -> "🇮🇳 India Mobile OTP (+91)"
                            AuthMethod.GOOGLE -> "🌐 Google Sign-In"
                            AuthMethod.GUEST -> "Guest Mode"
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Create Account / Sign In OR Sign Out Button
            if (!userProfile.isLoggedIn) {
                Button(
                    onClick = { viewModel.toggleAuthModal(true) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("open_auth_modal_button"),
                    shape = RoundedCornerShape(27.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleGlow)
                ) {
                    Text(
                        text = "Create Account / Sign In",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            } else {
                Button(
                    onClick = { viewModel.logout() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("logout_button"),
                    shape = RoundedCornerShape(27.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PinkAccent.copy(0.8f))
                ) {
                    Text(
                        text = "Sign Out Account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileStatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PurpleGlow,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.White.copy(0.6f)
        )
    }
}

@Composable
private fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White.copy(0.6f),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = label, fontSize = 11.sp, color = Color.White.copy(0.5f))
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}
