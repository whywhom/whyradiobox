package com.whywhom.soft.whyradiobox.ui.subscribedetail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.whywhom.soft.whyradiobox.R

class SubscribeDetailActivity : AppCompatActivity() {

    companion object{
        private lateinit var url: String
        fun newIntent(context: Context?, item: String): Intent {
            url = item
            return Intent(context, SubscribeDetailActivity::class.java)
        }
    }
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
