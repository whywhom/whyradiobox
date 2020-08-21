package com.whywhom.soft.whyradiobox.ui.main

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.ui.BaseActivity
import com.whywhom.soft.whyradiobox.ui.episodes.LibraryFragment
import com.whywhom.soft.whyradiobox.ui.home.HomeFragment
import com.whywhom.soft.whyradiobox.ui.setting.PersonalFragment
import com.whywhom.soft.whyradiobox.ui.subscription.SubscriptionFragment


class MainActivity : BaseActivity() {

    private val fragmentManager: FragmentManager by lazy { supportFragmentManager }
    private val libraryFragment: LibraryFragment? = null;
    private val homeFragment: HomeFragment? = null;
    private val personalFragment: PersonalFragment? = null;
    val NAV_TAGS = arrayOf<String>(
        SubscriptionFragment.TAG,//订阅信息
        LibraryFragment.TAG,//
        HomeFragment.TAG,
        PersonalFragment.TAG
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)
    }

    override fun setupViews() {
        super.setupViews()
    }
}
