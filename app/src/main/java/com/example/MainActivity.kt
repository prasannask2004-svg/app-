package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ui.GlassPlayerScreen
import com.example.ui.HomeScreen
import com.example.ui.PlayerViewModel
import com.example.ui.ProfileScreen
import com.example.ui.theme.GlassTheme
import com.example.util.configureAppCheck

class MainActivity : ComponentActivity() {

    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure Firebase App Check with launch intent before any UI or Firebase operations
        configureAppCheck(this, intent)

        enableEdgeToEdge()
        setContent {
            GlassTheme {
                val uiState by playerViewModel.uiState.collectAsState()

                if (uiState.isProfileScreenVisible) {
                    BackHandler {
                        playerViewModel.toggleProfileScreen(false)
                    }
                    ProfileScreen(
                        viewModel = playerViewModel,
                        onBack = { playerViewModel.toggleProfileScreen(false) }
                    )
                } else if (uiState.isFullPlayerVisible) {
                    BackHandler {
                        playerViewModel.toggleFullPlayer(false)
                    }
                    GlassPlayerScreen(
                        viewModel = playerViewModel,
                        onBackToHome = { playerViewModel.toggleFullPlayer(false) }
                    )
                } else {
                    HomeScreen(viewModel = playerViewModel)
                }
            }
        }
    }
}
