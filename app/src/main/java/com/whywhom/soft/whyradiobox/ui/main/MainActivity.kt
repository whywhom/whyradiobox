package com.whywhom.soft.whyradiobox.ui.main

import android.os.Bundle
import android.provider.SyncStateContract
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.event.RefreshEvent
import com.whywhom.soft.whyradiobox.ui.BaseActivity
import com.whywhom.soft.whyradiobox.ui.episodes.LibraryFragment
import com.whywhom.soft.whyradiobox.ui.home.HomeFragment
import com.whywhom.soft.whyradiobox.ui.setting.PersonalFragment
import com.whywhom.soft.whyradiobox.ui.subscription.SubscriptionFragment
import com.whywhom.soft.whyradiobox.utils.Constants
import kotlinx.android.synthetic.main.layout_bottom_navigation_bar.*
import org.greenrobot.eventbus.EventBus


class MainActivity : BaseActivity() {
    private var backPressTime = 0L
    private val fragmentManager: FragmentManager by lazy { supportFragmentManager }
    private var libraryFragment: LibraryFragment? = null;
    private var homeFragment: HomeFragment? = null;
    private var personalFragment: PersonalFragment? = null;
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
        initOnClickListener()
        setTabSelection(0)
    }

    private fun initOnClickListener() {
        ivDiscover.setOnClickListener { v->
            setTabSelection(0)
        }
        ivLibrary.setOnClickListener { v->
            setTabSelection(1)
        }
        ivPersonal.setOnClickListener { v->
            setTabSelection(2)
        }
        tvDiscover.setOnClickListener { v->
            setTabSelection(0)
        }
        tvLibrary.setOnClickListener { v->
            setTabSelection(1)
        }
        tvPersonal.setOnClickListener { v->
            setTabSelection(2)
        }
    }

    private fun setTabSelection(index: Int) {
        clearAllSelected()
        fragmentManager.beginTransaction().apply {
            hideFragments(this)
            when (index) {
                0 -> {
                    ivDiscover.isSelected = true
                    tvDiscover.isSelected = true
                    if (homeFragment == null) {
                        homeFragment = HomeFragment.newInstance()
                        add(R.id.fragmentContainer, homeFragment!!)
                    } else {
                        show(homeFragment!!)
                    }
                }
                1 -> {
                    ivLibrary.isSelected = true
                    tvLibrary.isSelected = true
                    if (libraryFragment == null) {
                        libraryFragment = LibraryFragment.newInstance()
                        add(R.id.fragmentContainer, libraryFragment!!)
                    } else {
                        show(libraryFragment!!)
                    }
                }
                2 -> {
                    ivPersonal.isSelected = true
                    tvPersonal.isSelected = true
                    if (personalFragment == null) {
                        personalFragment = PersonalFragment.newInstance()
                        add(R.id.fragmentContainer, personalFragment!!)
                    } else {
                        show(personalFragment!!)
                    }
                }
            }
        }.commitAllowingStateLoss()
    }

    private fun hideFragments(transaction: FragmentTransaction) {
        transaction.run {
            if (homeFragment != null) hide(homeFragment!!)
            if (libraryFragment != null) hide(libraryFragment!!)
            if (personalFragment != null) hide(personalFragment!!)
        }
    }

    private fun clearAllSelected() {
        ivDiscover.isSelected = false
        tvDiscover.isSelected = false
        ivLibrary.isSelected = false
        tvLibrary.isSelected = false
        ivPersonal.isSelected = false
        tvPersonal.isSelected = false
    }

    private fun notificationUiRefresh(selectionIndex: Int) {
        when (selectionIndex) {
            0 -> {
                EventBus.getDefault().post(RefreshEvent(HomeFragment::class.java))
            }
            1 -> {
                EventBus.getDefault().post(RefreshEvent(LibraryFragment::class.java))
            }
            2 -> {
                EventBus.getDefault().post(RefreshEvent(PersonalFragment::class.java))
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            processBackPressed()
        }
    }

    private fun processBackPressed() {
        val now = System.currentTimeMillis()
        if (now - backPressTime > 2000) {
            Toast.makeText(this, String.format(getString(R.string.press_again_to_exit), Constants.appName), Toast.LENGTH_SHORT).show()
            backPressTime = now
        } else {
            super.onBackPressed()
        }
    }
}
