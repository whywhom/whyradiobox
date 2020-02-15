package com.whywhom.soft.whyradiobox.ui.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.ui.add.AddFeedFragment
import com.whywhom.soft.whyradiobox.ui.setting.SettingsFragment
import com.whywhom.soft.whyradiobox.ui.subscription.SubscriptionFragment
import kotlinx.android.synthetic.main.activity_main.*

class AddFeedActivity : AppCompatActivity() {

    private lateinit var fragment: AddFeedFragment
    companion object {

        fun newIntent(context: Context?):Intent {
            return Intent(context, AddFeedActivity::class.java)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_feed)

        if (savedInstanceState == null) {
            fragment = AddFeedFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()

        }
    }
}
