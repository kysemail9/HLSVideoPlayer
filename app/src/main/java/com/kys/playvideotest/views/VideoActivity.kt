package com.kys.playvideotest.views

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.kys.playvideotest.R
import com.kys.playvideotest.databinding.ActivityVideoBinding

class VideoActivity : AppCompatActivity() {

    lateinit var binding: ActivityVideoBinding
    private lateinit var root: ConstraintLayout
    private lateinit var exoPlayerView: PlayerView
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaSource: MediaSource
    private lateinit var urlType: URLType
    private lateinit var videoURL: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = setContentView(this, R.layout.activity_video)
        setContentView(R.layout.activity_video)

        videoURL = intent.getStringExtra("videoURL").toString()

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        findView()
        initPlayer()
    }

    private fun findView() {
        root = findViewById(R.id.cl_root)
        exoPlayerView = findViewById(R.id.exo_player_view)
    }

    private fun initPlayer() {
        simpleExoPlayer = SimpleExoPlayer.Builder(this).build()
        simpleExoPlayer.addListener(playerListener)

        exoPlayerView.player = simpleExoPlayer

        createMediaSource()

        simpleExoPlayer.setMediaSource(mediaSource)
        simpleExoPlayer.prepare()
    }

    private fun createMediaSource() {
        urlType = URLType.HSL
        urlType.url = videoURL
        // "https://d3ttymzjrqen25.cloudfront.net/videos/61DC4F70-876F-48AE-A87F-A292BFFE7419.m3u8"
        simpleExoPlayer.seekTo(0)
        when (urlType) {
            URLType.HSL -> {

                val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                    this,
                    Util.getUserAgent(this, applicationInfo.name)
                )

                mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(
                    MediaItem.fromUri(Uri.parse(urlType.url))
                )
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val constraintSet = ConstraintSet()

        constraintSet.connect(
            exoPlayerView.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP,
            0
        )
        constraintSet.connect(
            exoPlayerView.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            0
        )
        constraintSet.connect(
            exoPlayerView.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START,
            0
        )
        constraintSet.connect(
            exoPlayerView.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END,
            0
        )

        constraintSet.applyTo(root)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            hideSystemUI()

        } else {
            showSystemUI()

            val layoutParams = exoPlayerView.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.dimensionRatio = "16:9"
        }

        window.decorView.requestLayout()

    }

    // for landscape
    private fun hideSystemUI() {
        actionBar?.hide()
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }

    private fun showSystemUI() {
        actionBar?.show()
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                )
    }

    override fun onResume() {
        super.onResume()

        simpleExoPlayer.playWhenReady = true
        simpleExoPlayer.play()
    }

    override fun onPause() {
        super.onPause()

        simpleExoPlayer.pause()
        simpleExoPlayer.playWhenReady = false
    }

    override fun onStop() {
        super.onStop()

        simpleExoPlayer.pause()
        simpleExoPlayer.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()

        simpleExoPlayer.removeListener(playerListener)
        simpleExoPlayer.stop()
        simpleExoPlayer.clearMediaItems()

        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private var playerListener = object : Player.Listener {
        override fun onRenderedFirstFrame() {
            super.onRenderedFirstFrame()

            if (urlType == URLType.HSL) { // hls stream doesn't need seek bar
                exoPlayerView.useController = false
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Toast.makeText(this@VideoActivity, "${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    enum class URLType(var url: String) {
        HSL("")
    }
}