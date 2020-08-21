package com.whywhom.soft.whyradiobox.ui.discovery

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.adapter.PodcastListAdapter
import com.whywhom.soft.whyradiobox.extensions.setListener
import com.whywhom.soft.whyradiobox.interfaces.RecyclerListener
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import kotlinx.android.synthetic.main.app_bar_main_drawer.*
import kotlinx.android.synthetic.main.fragment_add_feed.*


class FeedDiscoveryFragment : Fragment(), PodcastListAdapter.ItemClickListenter {

    private var searchType :Int = TYPE_EN;
    private var title :String = "";
    var podcastList:ArrayList<PodcastSearchResult> = ArrayList<PodcastSearchResult>()
    companion object {
        val TAG: String = "FeedDiscoveryFragment"
        const val TYPE_EN : Int = 0;
        const val TYPE_CN : Int = 1;
        fun newInstance() = FeedDiscoveryFragment()
    }

    private lateinit var viewModel: FeedDiscoveryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(false)//想让Fragment中的onCreateOptionsMenu生效必须先调用setHasOptionsMenu方法，否则Toolbar没有菜单。
        super.onViewCreated(view, savedInstanceState)
        val bundle = savedInstanceState ?: arguments
        if(bundle != null){
            searchType = bundle.getInt("search_type")
            title = bundle.getString("search_title","")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FeedDiscoveryViewModel::class.java)
        activity!!.toolbar!!.title = title
        val adapter = PodcastListAdapter(podcastList,context)
        adapter.setOnItemClick(this)
        podcast_list.layoutManager = LinearLayoutManager(activity)
        podcast_list.adapter = adapter
        podcast_list.setListener(object : RecyclerListener {
            override fun loadMore() {

            }
            override fun refresh() {
                swipeRefreshLayout.setRefreshing(true)
                viewModel.getTopPodcastList(searchType)
            }
        })
        viewModel.podcastListLiveData.observe(viewLifecycleOwner, Observer { it->
            var len = it.size
            podcastList = it
            adapter.submitList(podcastList)
            adapter.notifyDataSetChanged()
            swipeRefreshLayout.setRefreshing(false);
        })
        swipeRefreshLayout.post(Runnable {
            swipeRefreshLayout.setRefreshing(true)
            viewModel.getTopPodcastList(searchType)
        })
//        swipeRefreshLayout.setEnabled(false);//设置为不能刷新
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
//        setupSearch(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onItemClicked(position: Int) {
        var entry = podcastList.get(position)
        if (entry.feedUrl == null) {
            return
        }
//        val intent = PodcastDetailActivity.newIntent(this.context, entry.feedUrl)
//        startActivity(intent)
        var bundle : Bundle = Bundle()
        bundle.putString("title", entry.title!!)
        bundle.putString("feed_url", entry.feedUrl!!)
        bundle.putString("feed_cover_url", entry.imageUrl!!)
//        NavHostFragment.findNavController(this).navigate(R.id.onlineFeedViewFragment, bundle)
    }
}
