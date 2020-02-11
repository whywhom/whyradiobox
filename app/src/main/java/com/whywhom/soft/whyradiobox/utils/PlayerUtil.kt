package com.whywhom.soft.whyradiobox.utils

import android.content.Context
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector

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
            player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
        }
        return player!!
    }
}