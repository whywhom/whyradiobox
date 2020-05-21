package com.whywhom.soft.whyradiobox.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.ui.episodes.EpisodesFragment
import com.whywhom.soft.whyradiobox.ui.home.HomeFragment
import com.whywhom.soft.whyradiobox.ui.setting.SettingsFragment
import com.whywhom.soft.whyradiobox.ui.subscription.SubscriptionFragment
import kotlinx.android.synthetic.main.nav_header_main_drawer.view.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    val NAV_TAGS = arrayOf<String>(
        SubscriptionFragment.TAG,//订阅信息
        EpisodesFragment.TAG,//
        HomeFragment.TAG,
        SettingsFragment.TAG
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_subscribe,
                R.id.nav_episodes
//                R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        findViewById<BottomNavigationView>(R.id.bottom_nav_view)
            .setupWithNavController(navController)
        //get head view and it's components
        val headerLayout = navView.getHeaderView(0)
        val navSetting = headerLayout.findViewById<View>(R.id.nav_settings) as ImageView
        navSetting.setOnClickListener {
            navController.navigate(R.id.nav_settings)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_drawer, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu!!.findItem(R.id.action_settings).isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }

}
