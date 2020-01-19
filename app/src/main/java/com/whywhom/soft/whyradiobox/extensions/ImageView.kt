package com.whywhom.soft.whyradiobox.extensions

import android.widget.ImageView
import com.squareup.picasso.Picasso

/**
 * Created by wuhaoyong on 2020-01-08.
 */

fun ImageView.loadUrl(url: String) {
    Picasso.with(context).load(url).centerCrop().into(this)
}