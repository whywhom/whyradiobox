package com.whywhom.soft.whyradiobox.ui.subscribedetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.whywhom.soft.whyradiobox.R

class SubscribeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscribedetail)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SubscribeDetailFragment.newInstance())
                .commitNow()
        }
    }

}
