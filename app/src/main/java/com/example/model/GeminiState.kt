package com.example.model

sealed interface GeminiAiState {
    data object Idle : GeminiAiState
    data object Loading : GeminiAiState
    data class Streaming(val text: String) : GeminiAiState
    data class Success(val text: String) : GeminiAiState
    data class Error(val message: String) : GeminiAiState
}

enum class GeminiAiTab {
    SONG_STORY,
    MOOD_DJ
}
