package com.whywhom.soft.whyradiobox.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.IBinder
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.EventListener
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.rss.RSSItem
import com.whywhom.soft.whyradiobox.utils.PlayerUtil


class MediaService : Service() {
    val TAG = MediaService::class.java.name
    private var player: SimpleExoPlayer? = null
    private var playerNotificationManager: PlayerNotificationManager? = null
    private lateinit var mediaSource: MediaSource

    override fun onCreate() {
        super.onCreate()
    }
    companion object{
        private lateinit var rssItem: RSSItem
        fun setDataSource(mediaData: RSSItem){
            rssItem = mediaData
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        player = PlayerUtil.getPlayer(this)
        player?.let {
            if(player!!.isPlaying){
                player!!.stop()
            }

            lateinit var mineType:String
            lateinit var uri: Uri
            // Produces DataSource instances through which media data is loaded.
            if(rssItem.enclosure != null){
                mineType = rssItem.enclosure.mimeType
                uri = rssItem.enclosure.url
            } else{
                mineType = Util.getUserAgent(this, "RBApplication")
                if(rssItem.link != null){
                    uri = rssItem.link
                }
            }
            var dataSourceFactory: DataSource.Factory =
                DefaultDataSourceFactory(this, mineType)
            // This is the MediaSource representing the media to be played.
            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
            // Prepare the player with the source.
            player!!.playWhenReady = true
//        player_control_view.showTimeoutMs = -1
            player!!.prepare(mediaSource);
            val eventListener: EventListener = object : EventListener {
                fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {}
                override fun onTracksChanged(
                    trackGroups: TrackGroupArray,
                    trackSelections: TrackSelectionArray
                ) {
                }

                override fun onLoadingChanged(isLoading: Boolean) {
                    Log.d(TAG,"isLoading = " + isLoading)
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if (playbackState.toLong() == PlaybackStateCompat.ACTION_SKIP_TO_NEXT) {
//                        Toast.makeText(this@VideoActivity, "next", Toast.LENGTH_SHORT).show()
                        Log.d(TAG,"next")
                    }
                    if (playbackState.toLong() == PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) {
//                        Toast.makeText(this@VideoActivity, "previous", Toast.LENGTH_SHORT).show()
                        Log.d(TAG,"previous")
                    }
                }

                override fun onRepeatModeChanged(repeatMode: Int) {}
                override fun onPlayerError(error: ExoPlaybackException) {}
                fun onPositionDiscontinuity() {}
                override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
            }
            player!!.addListener(eventListener)
            val mediaDescriptionAdapter = object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun getCurrentSubText(player: Player): String? {
                    return rssItem.description
                }

                override fun getCurrentContentTitle(player: Player): String {
                    return rssItem.title
                }

                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    return null
                }

                override fun getCurrentContentText(player: Player): String {
                    if(rssItem.getAuthor() != null){
                        return rssItem.getAuthor()
                    }
                    return ""
                }

                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
//                    return BitmapFactory.decodeResource(resources, R.drawable.radio_station)
                    return null
                }
            }
            this?.let { initListener(it, mediaDescriptionAdapter) }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        stopMedia()
        super.onDestroy()
    }
    private fun stopMedia() {
        if (Util.SDK_INT >= 26) {
            stopForeground(true)
        } else {
            stopSelf()
        }
        PlayerUtil.resetPlayer()
        playerNotificationManager?.setPlayer(null)
    }
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    private fun initListener(
        context: Context,
        mediaDescriptionAdapter: PlayerNotificationManager.MediaDescriptionAdapter
    ) {
        val playerNotificationManager: PlayerNotificationManager
        val notificationId = 1314

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            context,
            "rb_channel_id",
            R.string.app_name,
            0,
            notificationId,
            mediaDescriptionAdapter,
            object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {
                }

                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean
                ) {

                }
            })

        playerNotificationManager.setUseNavigationActions(false)
        playerNotificationManager.setUseNavigationActionsInCompactView(false)
        playerNotificationManager.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        playerNotificationManager.setSmallIcon(R.drawable.radio_station)
        playerNotificationManager.setPlayer(player)
    }
}
