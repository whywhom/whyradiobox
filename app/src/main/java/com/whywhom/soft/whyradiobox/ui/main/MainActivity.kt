package com.whywhom.soft.whyradiobox.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.ui.add.AddFeedFragment
import com.whywhom.soft.whyradiobox.ui.episodes.EpisodesFragment
import com.whywhom.soft.whyradiobox.ui.setting.SettingsFragment
import com.whywhom.soft.whyradiobox.ui.subscription.SubscriptionFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var menuItem: MenuItem
    private lateinit var fragment: Fragment

    val NAV_TAGS = arrayOf<String>(
        SubscriptionFragment.TAG,
        EpisodesFragment.TAG,
        AddFeedFragment.TAG,
        SettingsFragment.TAG
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (savedInstanceState == null) {
            bottom_navigation.selectedItemId = R.id.nav_subscribe

        }
    }
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        menuItem = item
        when (item.itemId) {
            R.id.nav_subscribe -> {
                fragment = SubscriptionFragment.newInstance()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commitNow()
                title = getString(R.string.subscribe)
                return@OnNavigationItemSelectedListener true
            }
//            R.id.nav_find -> {
//                fragment = AddFeedFragment.newInstance()
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, fragment)
//                    .commitNow()
//                return@OnNavigationItemSelectedListener true
//            }
            R.id.nav_episodes -> {
                fragment = EpisodesFragment.newInstance()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commitNow()
                title = getString(R.string.episodes)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_settings -> {
                fragment = SettingsFragment.newInstance()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commitNow()
                title = getString(R.string.setting)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
