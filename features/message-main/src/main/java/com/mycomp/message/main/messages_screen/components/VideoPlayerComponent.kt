package com.mycomp.message.main.messages_screen.components

import android.annotation.SuppressLint
import android.net.Uri
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.mycomp.message.main.R
import com.mycomp.message.main.messages_screen.utils.toPx
import kotlinx.coroutines.delay

@Composable
internal fun VideoPlayer(
    uri: Uri,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = false,
    onPlayPause: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(autoPlay) }
    var videoEnded by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0L) }
    var videoDuration by remember { mutableStateOf(0L) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = autoPlay

            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY) {
                        videoDuration = duration
                        videoEnded = false
                    }
                    if (playbackState == Player.STATE_ENDED) {
                        videoEnded = true
                        isPlaying = false
                    }
                }
            })
        }
    }

    LaunchedEffect(exoPlayer) {
        while (true) {
            if (!videoEnded) {
                currentPosition = exoPlayer.currentPosition
            }
            delay(1000L)
        }
    }

    val countdownFormatted = remember(currentPosition, videoDuration) {
        formatCountdown(currentPosition, videoDuration)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .width(260.dp)
            .height(200.dp)
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    layoutParams = ViewGroup.LayoutParams(
                        260.dp.toPx(context).toInt(),
                        200.dp.toPx(context).toInt()
                    )
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
            }
        )

        Text(
            text = countdownFormatted,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .background(Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(4.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )

        Surface(
            shape = RoundedCornerShape(50),
            onClick = {
                if (videoEnded) {
                    exoPlayer.seekTo(0)
                    exoPlayer.playWhenReady = true
                    videoEnded = false
                } else {
                    isPlaying = !isPlaying
                    exoPlayer.playWhenReady = isPlaying
                    onPlayPause(isPlaying)
                }
            }
        ) {
            Icon(
                painter = painterResource(id = if (isPlaying) R.drawable.ic_pause_24 else R.drawable.ic_play_24),
                contentDescription = stringResource(R.string.play_content_description),
                tint = Color.Black,
                modifier = Modifier.size(50.dp)
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}

@SuppressLint("DefaultLocale")
private fun formatCountdown(currentPositionMs: Long, durationMs: Long): String {
    val remainingTimeMs = durationMs - currentPositionMs
    val remainingMinutes = (remainingTimeMs / 1000) / 60
    val remainingSeconds = (remainingTimeMs / 1000) % 60
    return String.format("%02d:%02d", remainingMinutes, remainingSeconds)
}
