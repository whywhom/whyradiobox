package com.whywhom.soft.whyradiobox.utils

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.offline.DownloadService.startForeground
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.whywhom.soft.whyradiobox.R


/**
 * Created by wuhaoyong on 2020-02-11.
 */
object PlayerUtil{

    private var player:SimpleExoPlayer? = null
    fun resetPlayer(){
        if (null != player) {
            player!!.stop();
            player!!.release();
            player = null;
        }
    }
    fun getPlayer(context: Context):SimpleExoPlayer{
        if(player == null) {
            player = SimpleExoPlayer.Builder(context).build()
        }
        return player!!
    }
}