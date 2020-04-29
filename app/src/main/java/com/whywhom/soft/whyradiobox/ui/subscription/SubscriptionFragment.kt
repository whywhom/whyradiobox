package com.whywhom.soft.whyradiobox.ui.subscription

import android.os.Bundle
import android.os.Parcel
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.adapter.SubscriptionAdapter
import com.whywhom.soft.whyradiobox.data.source.local.Podcast
import com.whywhom.soft.whyradiobox.ui.subscribedetail.SubscribeDetailActivity
import kotlinx.android.synthetic.main.fragment_subscription.*

class SubscriptionFragment() : Fragment(), SubscriptionAdapter.ItemClickListenter {
    private lateinit var subscription: ArrayList<Podcast>
    companion object {
        val TAG: String = "SubscriptionFragment"

        fun newInstance() = SubscriptionFragment()
    }

    private val numberOfColumns: Int = 4
    private lateinit var viewModel: SubscriptionViewModel

    constructor(parcel: Parcel) : this() {

    }

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
        viewModel = ViewModelProvider(this).get(SubscriptionViewModel::class.java)
        // TODO: Use the ViewModel
        subscriptions_add.setOnClickListener { onClick->
            run {
//                val intent = FeedDiscoveryActivity.newIntent(
//                    this.context,
//                    FeedDiscoveryActivity.TYPE_EN
//                )
//                startActivity(intent)
            }
        }
        viewModel.podcastlLiveData.observe(viewLifecycleOwner, Observer{
            val adapter = SubscriptionAdapter(this.context!!, viewModel.getSubscriptionData().value!!)
            adapter.setOnItemClick(this)
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

    override fun onItemClicked(position: Int) {
        var podlist = viewModel.getSubscriptionData().value
        if(podlist != null) {
            var podcast = podlist!!.get(position)
            val intent = SubscribeDetailActivity.newIntent(this.context, podcast.rssurl, podcast.coverurl, podcast.trackId)
            startActivity(intent)
        }
    }

}
