package com.whywhom.soft.whyradiobox.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.adapter.ItunesPodcastListAdapter
import com.whywhom.soft.whyradiobox.event.MessageEvent
import com.whywhom.soft.whyradiobox.event.RefreshEvent
import com.whywhom.soft.whyradiobox.model.Entry
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.ui.BaseFragment
import com.whywhom.soft.whyradiobox.ui.main.HostActivity
import com.whywhom.soft.whyradiobox.utils.Constants
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment(), AdapterView.OnItemClickListener{
    var podcastList:ArrayList<PodcastSearchResult> = ArrayList<PodcastSearchResult>()
    private val NUM_SUGGESTIONS = 12
    lateinit var topEnAdapter: ItunesPodcastListAdapter
    lateinit var topCnAdapter:ItunesPodcastListAdapter
    companion object {
        val MAX_TOPLIST = 100
        val TAG: String = "HomeFragment"

        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater.inflate(R.layout.fragment_home, container, false))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)//想让Fragment中的onCreateOptionsMenu生效必须先调用setHasOptionsMenu方法，否则Toolbar没有菜单。
        super.onViewCreated(view, savedInstanceState)
        topCnAdapter = ItunesPodcastListAdapter(activity)
        grid_view_cn.setAdapter(topCnAdapter)
        grid_view_cn.setOnItemClickListener(this)

        val displayMetrics = context!!.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        if (screenWidthDp > 600) {
            grid_view_cn.setNumColumns(6)
        } else {
            grid_view_cn.setNumColumns(4)
        }

        topEnAdapter = ItunesPodcastListAdapter(activity)
        grid_view_en.setAdapter(topEnAdapter)
        grid_view_en.setOnItemClickListener(this)
        if (screenWidthDp > 600) {
            grid_view_en.setNumColumns(6)
        } else {
            grid_view_en.setNumColumns(4)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, Constants.getHomeViewModelFactory()).get(HomeViewModel::class.java)
        initObserve()
        initListener()
        viewModel.getTopPodcastList("US", "100")
        viewModel.getTopPodcastList("CN", "100")
    }

    private fun initListener() {
        iv_bbc.setOnClickListener { onClick->
            displayAssets("BBC")
        }
        iv_cnn.setOnClickListener { onClick->
            displayAssets("CNN")
        }
        iv_voa.setOnClickListener { onClick->
            displayAssets("VOA")
        }
        combinedFeedSearchBox.setOnClickListener { view->performSearch() }
    }

    private fun initObserve(){
        viewModel.podcastTopCnLiveData.observe(viewLifecycleOwner, Observer {
            var item = it?.feed?.entry;
            if (item != null) {

                // Fill with dummy elements to have a fixed height and
                // prevent the UI elements below from jumping on slow connections
                val topCn: MutableList<Entry> = ArrayList()
                for (i in 0 until NUM_SUGGESTIONS) {
                    topCn.add(item.get(i))
                }
                topCnAdapter.updateData(topCn)
            }
        })
        viewModel.podcastTopEnLiveData.observe(viewLifecycleOwner, Observer {
            var item = it?.feed?.entry;
            if (item != null) {
                val topEn: MutableList<Entry> = ArrayList()
                for (i in 0 until NUM_SUGGESTIONS) {
                    topEn.add(item.get(i))
                }
                topEnAdapter.updateData(topEn)
            }
        })

    }
    override fun onMessageEvent(messageEvent: MessageEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is RefreshEvent && this::class.java == messageEvent.activityClass) {
            Log.d(TAG, messageEvent.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.search -> {
                Log.d("HomeFragment", "click search")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }
    private fun performSearch() {
        val query = combinedFeedSearchBox.text.toString()
        if (query.matches(Regex("http[s]?://.*"))) {
            return
        }
        addUrl(query)
    }
    private fun addUrl(query: String) {
        val intent = HostActivity.newIntent(this.context!!, "Search", query)
        startActivity(intent)
    }
    private fun displayAssets(path: String) {
        val intent = HostActivity.newIntent(this.context!!, "Assets", path)
        startActivity(intent)
    }
}
