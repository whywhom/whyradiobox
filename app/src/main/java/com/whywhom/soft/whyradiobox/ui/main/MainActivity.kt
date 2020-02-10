package com.whywhom.soft.whyradiobox.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.whywhom.soft.whyradiobox.R

class MainActivity : AppCompatActivity() {
    private lateinit var fragment: MainFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            fragment = MainFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()
        }
    }

}
