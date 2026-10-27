package com.example.ui.components

import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun VideoPlayerView(
    videoUrl: String,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { context ->
                VideoView(context).apply {
                    val mediaController = MediaController(context)
                    mediaController.setAnchorView(this)
                    setMediaController(mediaController)
                    setVideoURI(Uri.parse(videoUrl))
                    setOnPreparedListener { mp ->
                        mp.isLooping = true
                        if (isPlaying) {
                            start()
                        }
                    }
                }
            },
            update = { videoView ->
                if (isPlaying) {
                    if (!videoView.isPlaying) {
                        videoView.start()
                    }
                } else {
                    if (videoView.isPlaying) {
                        videoView.pause()
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
