package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CosmicDarkBackground
import com.example.ui.theme.CosmicDarkSurface
import com.example.ui.theme.GlassBorderColor
import com.example.ui.theme.GlassBorderHighlight
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.PurpleGlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthModal(
    sheetState: SheetState,
    onMobileLogin: (mobile: String, name: String) -> Unit,
    onGoogleLogin: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("MOBILE") } // MOBILE or GOOGLE
    var userName by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CosmicDarkBackground.copy(alpha = 0.95f),
        scrimColor = Color.Black.copy(alpha = 0.7f),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(44.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Modal Title & Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Create Account / Sign In",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Access personalized playlists & favorite songs",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.testTag("auth_close_button")) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Auth Method Selector Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .background(CosmicDarkSurface)
                    .border(1.dp, GlassBorderColor, CircleShape)
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .background(if (selectedTab == "MOBILE") PurpleGlow.copy(0.8f) else Color.Transparent)
                        .clickable { selectedTab = "MOBILE" }
                        .padding(vertical = 10.dp)
                        .testTag("mobile_tab_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🇮🇳 Mobile Number",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .background(if (selectedTab == "GOOGLE") PurpleGlow.copy(0.8f) else Color.Transparent)
                        .clickable { selectedTab = "GOOGLE" }
                        .padding(vertical = 10.dp)
                        .testTag("google_tab_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🌐 Google Sign-In",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (selectedTab == "MOBILE") {
                // TAB 1: India Mobile Number Auth
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Full Name Input
                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("user_name_input"),
                        placeholder = { Text("Your Full Name (e.g. Rahul Sharma)", color = Color.White.copy(0.4f)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Rounded.Person, contentDescription = null, tint = Color.White.copy(0.6f))
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CosmicDarkSurface,
                            unfocusedContainerColor = CosmicDarkSurface,
                            focusedBorderColor = PurpleGlow,
                            unfocusedBorderColor = GlassBorderColor,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Phone Number Input with +91 Prefix
                    OutlinedTextField(
                        value = mobileNumber,
                        onValueChange = { if (it.length <= 10 && it.all { char -> char.isDigit() }) mobileNumber = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("india_mobile_input"),
                        placeholder = { Text("10-Digit Mobile Number", color = Color.White.copy(0.4f)) },
                        leadingIcon = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 12.dp, end = 4.dp)
                            ) {
                                Text(
                                    text = "🇮🇳 +91",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(20.dp)
                                        .background(Color.White.copy(0.3f))
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CosmicDarkSurface,
                            unfocusedContainerColor = CosmicDarkSurface,
                            focusedBorderColor = PurpleGlow,
                            unfocusedBorderColor = GlassBorderColor,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // OTP Input step
                    AnimatedVisibility(visible = isOtpSent) {
                        Column {
                            OutlinedTextField(
                                value = otpCode,
                                onValueChange = { if (it.length <= 4) otpCode = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("otp_code_input"),
                                placeholder = { Text("Enter 4-Digit OTP (e.g. 1234)", color = Color.White.copy(0.4f)) },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Rounded.CheckCircle, contentDescription = null, tint = PinkAccent)
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                shape = RoundedCornerShape(20.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = CosmicDarkSurface,
                                    unfocusedContainerColor = CosmicDarkSurface,
                                    focusedBorderColor = PinkAccent,
                                    unfocusedBorderColor = GlassBorderColor,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = PinkAccent,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // Action Button
                    Button(
                        onClick = {
                            if (mobileNumber.length != 10) {
                                errorMessage = "Please enter a valid 10-digit Indian mobile number."
                                return@Button
                            }
                            if (userName.trim().isEmpty()) {
                                errorMessage = "Please enter your full name."
                                return@Button
                            }

                            if (!isOtpSent) {
                                isOtpSent = true
                                errorMessage = "OTP sent to +91 $mobileNumber. Use 1234 to verify."
                            } else {
                                if (otpCode.length == 4) {
                                    onMobileLogin("+91 $mobileNumber", userName.trim())
                                } else {
                                    errorMessage = "Please enter 4-digit OTP code."
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("mobile_submit_button"),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PurpleGlow
                        )
                    ) {
                        Text(
                            text = if (!isOtpSent) "Send Verification OTP" else "Verify & Create Account",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            } else {
                // TAB 2: Google Sign In
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sign in instantly with your Google Account",
                        fontSize = 13.sp,
                        color = Color.White.copy(0.7f)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clip(RoundedCornerShape(27.dp))
                            .background(Color.White)
                            .border(1.dp, GlassBorderHighlight, RoundedCornerShape(27.dp))
                            .clickable(onClick = onGoogleLogin)
                            .testTag("google_login_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🌐 ", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Continue with Google",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F1F1F)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
