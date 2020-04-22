package com.whywhom.soft.whyradiobox.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.whywhom.soft.whyradiobox.R

class HomeActivity : AppCompatActivity() {

    private lateinit var fragment: HomeFragment
    companion object {

        fun newIntent(context: Context?):Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_feed)
        title = getString(R.string.add_podcast)
        if (savedInstanceState == null) {
            fragment = HomeFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()
        }
    }
}
