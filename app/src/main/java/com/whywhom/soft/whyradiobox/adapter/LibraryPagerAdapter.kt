package com.whywhom.soft.whyradiobox.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.whywhom.soft.whyradiobox.ui.subscription.SubscriptionFragment

/**
 * Created by wuhaoyong on 2/09/20.
 */
class LibraryPagerAdapter (fragment: Fragment): FragmentStateAdapter(fragment) {

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        0 to { SubscriptionFragment() },
        1 to { SubscriptionFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}