package com.example.gymroutine.presentation.common

import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

/**
 * YouTube video player using WebView
 */
@Composable
fun YouTubePlayer(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    val videoId = extractVideoId(videoUrl)

    if (videoId != null) {
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .height(220.dp),
            factory = { context ->
                WebView(context).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        mediaPlaybackRequiresUserGesture = false
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    }

                    val html = """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta name="viewport" content="width=device-width, initial-scale=1">
                            <style>
                                body {
                                    margin: 0;
                                    padding: 0;
                                    background-color: #000;
                                }
                                .video-container {
                                    position: relative;
                                    padding-bottom: 56.25%;
                                    height: 0;
                                    overflow: hidden;
                                }
                                .video-container iframe {
                                    position: absolute;
                                    top: 0;
                                    left: 0;
                                    width: 100%;
                                    height: 100%;
                                    border: 0;
                                }
                            </style>
                        </head>
                        <body>
                            <div class="video-container">
                                <iframe
                                    src="https://www.youtube.com/embed/$videoId?rel=0&modestbranding=1"
                                    frameborder="0"
                                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                                    allowfullscreen>
                                </iframe>
                            </div>
                        </body>
                        </html>
                    """.trimIndent()

                    loadDataWithBaseURL(
                        "https://www.youtube.com",
                        html,
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
            }
        )
    }
}

/**
 * Extract video ID from YouTube URL
 * Supports formats:
 * - https://www.youtube.com/watch?v=VIDEO_ID
 * - https://youtu.be/VIDEO_ID
 * - https://www.youtube.com/embed/VIDEO_ID
 */
private fun extractVideoId(url: String): String? {
    val patterns = listOf(
        "(?:v=|/)([0-9A-Za-z_-]{11}).*",
        "youtu\\.be/([0-9A-Za-z_-]{11})",
        "embed/([0-9A-Za-z_-]{11})"
    )

    patterns.forEach { pattern ->
        val regex = Regex(pattern)
        val match = regex.find(url)
        if (match != null && match.groupValues.size > 1) {
            return match.groupValues[1]
        }
    }

    return null
}
