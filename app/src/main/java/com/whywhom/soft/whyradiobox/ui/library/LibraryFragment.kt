package com.whywhom.soft.whyradiobox.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.ui.subscription.SubscriptionFragment
import kotlinx.android.synthetic.main.fragment_library.*


class LibraryFragment : Fragment() {
    val createFragments: Array<Fragment> = arrayOf(SubscriptionFragment.newInstance()
        , SubscriptionFragment.newInstance())
    companion object {
        val TAG: String = LibraryFragment::class.java.name
        fun newInstance() = LibraryFragment()
    }

    private lateinit var viewModel: LibraryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter: VpAdapter by lazy { VpAdapter(getActivity()!!).apply { addFragments(createFragments) } }
        viewPager.adapter = adapter
        TabLayoutMediator(library_tabs, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Subscription"
                1 -> "Episodes"
                else -> null
            }
        }.attach()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LibraryViewModel::class.java)
        // TODO: Use the ViewModel
    }

    inner class VpAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

        private val fragments = mutableListOf<Fragment>()

        fun addFragments(fragment: Array<Fragment>) {
            fragments.addAll(fragment)
        }

        override fun getItemCount() = fragments.size

        override fun createFragment(position: Int) = fragments[position]
    }
}
