package com.whywhom.soft.whyradiobox.ui.subscription

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.adapter.SubscriptionAdapter
import com.whywhom.soft.whyradiobox.data.source.local.Podcast
import com.whywhom.soft.whyradiobox.ui.add.AddFeedActivity
import com.whywhom.soft.whyradiobox.ui.add.AddFeedFragment
import kotlinx.android.synthetic.main.fragment_subscription.*

class SubscriptionFragment : Fragment() {
    private lateinit var subscription: ArrayList<Podcast>
    companion object {
        val TAG: String = "SubscriptionFragment"

        fun newInstance() = SubscriptionFragment()
    }

    private val numberOfColumns: Int = 4
    private lateinit var viewModel: SubscriptionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subscription, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribe_list.layoutManager = GridLayoutManager(activity, numberOfColumns)
        subscribe_list.addItemDecoration( GridSpacingItemDecoration(numberOfColumns, getResources().getDimensionPixelSize(R.dimen.gridview_space), true));
        subscribe_list.setHasFixedSize(true);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SubscriptionViewModel::class.java)
        // TODO: Use the ViewModel
        subscriptions_add.setOnClickListener { onClick->
            run {
                val intent = AddFeedActivity.newIntent(this.context)
                startActivity(intent)
            }
        }
        viewModel.podcastlLiveData.observe(this, Observer{
            val adapter = SubscriptionAdapter(this.context!!, viewModel.getSubscriptionData().value!!)
            subscribe_list.adapter = adapter
        })
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        readSubscriptionData()
    }

    private fun readSubscriptionData() {
        if (viewModel != null) {
            viewModel.readSubscriptionData(this.context!!)
        }
    }
}
