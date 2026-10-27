package com.example.model

enum class AuthMethod {
    GUEST,
    INDIA_MOBILE,
    GOOGLE
}

data class UserProfile(
    val userId: String = "guest_user",
    val name: String = "Glass Listener",
    val mobileNumber: String = "",
    val email: String = "",
    val authMethod: AuthMethod = AuthMethod.GUEST,
    val isLoggedIn: Boolean = false,
    val avatarUrl: String = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
    val memberStatus: String = "Glass VIP Listener",
    val preferredLanguage: String = "Tamil"
)
