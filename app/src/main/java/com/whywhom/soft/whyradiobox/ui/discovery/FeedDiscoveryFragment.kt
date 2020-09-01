package com.whywhom.soft.whyradiobox.ui.discovery

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.adapter.PodcastListAdapter
import com.whywhom.soft.whyradiobox.event.FragmentEvent
import com.whywhom.soft.whyradiobox.extensions.setListener
import com.whywhom.soft.whyradiobox.interfaces.RecyclerListener
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_discovery_feed.*
import org.greenrobot.eventbus.EventBus


class FeedDiscoveryFragment : BaseFragment(), PodcastListAdapter.ItemClickListenter {
    var podcastList:ArrayList<PodcastSearchResult> = ArrayList<PodcastSearchResult>()
    companion object {
        val TAG: String = "FeedDiscoveryFragment"
        private lateinit var actionStr: String
        fun newInstance(query: String):Fragment{
            actionStr = query
            return FeedDiscoveryFragment()
        }
    }

    private lateinit var viewModel: FeedDiscoveryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_discovery_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(false)//想让Fragment中的onCreateOptionsMenu生效必须先调用setHasOptionsMenu方法，否则Toolbar没有菜单。
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FeedDiscoveryViewModel::class.java)
        initToolbar()
        val adapter = PodcastListAdapter(podcastList, context)
        adapter.setOnItemClick(this)
        podcast_list.layoutManager = LinearLayoutManager(activity)
        podcast_list.adapter = adapter
        podcast_list.setListener(object : RecyclerListener {
            override fun loadMore() {

            }
            override fun refresh() {
                swipeRefreshLayout.setRefreshing(true)
                viewModel.getJsonDataFromAsset(context!!, actionStr)
            }
        })
        viewModel.podcastBbcLiveData.observe(viewLifecycleOwner, Observer {
            podcastList = it as ArrayList<PodcastSearchResult>;
            adapter.submitList(podcastList)
            adapter.notifyDataSetChanged()
            swipeRefreshLayout.setRefreshing(false);
        })
        viewModel.podcastCnnLiveData.observe(viewLifecycleOwner, Observer {
            podcastList = it as ArrayList<PodcastSearchResult>;
            adapter.submitList(podcastList)
            adapter.notifyDataSetChanged()
            swipeRefreshLayout.setRefreshing(false);
        })
        viewModel.podcastVoaLiveData.observe(viewLifecycleOwner, Observer {
            podcastList = it as ArrayList<PodcastSearchResult>;
            adapter.submitList(podcastList)
            adapter.notifyDataSetChanged()
            swipeRefreshLayout.setRefreshing(false);
        })
        swipeRefreshLayout.post {
            swipeRefreshLayout.setRefreshing(true)
            viewModel.getJsonDataFromAsset(context!!, actionStr)
        }
        swipeRefreshLayout.setEnabled(false);//设置为不能刷新
    }
    private fun getActionbar() : ActionBar? {
        return (activity as AppCompatActivity).supportActionBar
    }
    private fun initToolbar() {
        getActionbar()?.title = actionStr
        getActionbar()?.setDisplayHomeAsUpEnabled(true)//显示返回图标
        getActionbar()?.setHomeButtonEnabled(true);//主键按钮能否可点击
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onItemClicked(position: Int) {
        var entry = podcastList.get(position)
        if (entry.feedUrl == null) {
            return
        }
        EventBus.getDefault().post(FragmentEvent(FeedDiscoveryActivity::class.java, entry))
    }
}
