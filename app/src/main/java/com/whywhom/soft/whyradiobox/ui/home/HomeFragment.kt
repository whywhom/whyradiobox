package com.whywhom.soft.whyradiobox.ui.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.adapter.ItunesPodcastListAdapter
import com.whywhom.soft.whyradiobox.adapter.PodcastListAdapter
import com.whywhom.soft.whyradiobox.event.MessageEvent
import com.whywhom.soft.whyradiobox.event.RefreshEvent
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.ui.BaseFragment
import com.whywhom.soft.whyradiobox.ui.detail.OnlineFeedViewActivity
import com.whywhom.soft.whyradiobox.ui.discovery.FeedDiscoveryActivity
import com.whywhom.soft.whyradiobox.ui.search.OnlineSearchActivity
import com.whywhom.soft.whyradiobox.utils.Constants
import kotlinx.android.synthetic.main.dialog_toppodcast.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment(){
    var podcastList:ArrayList<PodcastSearchResult> = ArrayList<PodcastSearchResult>()
    var topEn: MutableList<PodcastSearchResult> = ArrayList()
    var topCn: MutableList<PodcastSearchResult> = ArrayList()
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
        grid_view_cn.setOnItemClickListener{ parent, view, position, id ->
            run {
                var item: PodcastSearchResult = topCn.get(position)
                showRssDetail(item)
            }
        }
        val displayMetrics = context!!.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        if (screenWidthDp > 600) {
            grid_view_cn.setNumColumns(6)
        } else {
            grid_view_cn.setNumColumns(4)
        }

        topEnAdapter = ItunesPodcastListAdapter(activity)
        grid_view_en.setAdapter(topEnAdapter)
        grid_view_en.setOnItemClickListener { parent, view, position, id ->
            run {
                var item: PodcastSearchResult = topEn.get(position)
                showRssDetail(item)
            }
        }
        if (screenWidthDp > 600) {
            grid_view_en.setNumColumns(6)
        } else {
            grid_view_en.setNumColumns(4)
        }
        discover_more_en.setOnClickListener{
            createDialogFragment(viewModel.podcastTopEnLiveData.value)
        }
        discover_more_cn.setOnClickListener{
            createDialogFragment(viewModel.podcastTopCnLiveData.value)
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
            var item = it;
            if (item != null) {
                // Fill with dummy elements to have a fixed height and
                // prevent the UI elements below from jumping on slow connections
                topCn.clear()
                for (i in 0 until NUM_SUGGESTIONS) {
                    topCn.add(item.get(i))
                }
                topCnAdapter.updateData(topCn)
            }
        })
        viewModel.podcastTopEnLiveData.observe(viewLifecycleOwner, Observer {
            var item = it;
            if (item != null) {
                topEn.clear()
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
    fun showRssDetail(entry: PodcastSearchResult) {
        val intent = OnlineFeedViewActivity.newIntent(context, entry)
        startActivity(intent)
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

    private fun performSearch() {
        val query = combinedFeedSearchBox.text.toString()
        if (query.matches(Regex("http[s]?://.*"))) {
            return
        }
        addUrl(query)
    }
    private fun addUrl(query: String) {
        val intent = OnlineSearchActivity.newIntent(this.context!!, "Search", query)
        startActivity(intent)
    }

    private fun displayAssets(path: String) {
        val intent = FeedDiscoveryActivity.newIntent(this.context!!, "Assets", path)
        startActivity(intent)
    }

    private fun createDialogFragment(value: ArrayList<PodcastSearchResult>?) {
        showDialog(value)
    }
    private fun showDialog(value: ArrayList<PodcastSearchResult>?) {
        val newFragment = TopPodcastDialogFragment(value)
        // 如果时大屏幕的设备，显示为弹出框方式
        newFragment.show(childFragmentManager, "dialog")
    }
}

class TopPodcastDialogFragment(val value: ArrayList<PodcastSearchResult>?) : DialogFragment() ,PodcastListAdapter.ItemClickListenter{
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle: Bundle? = getArguments()
    }

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        //加载布局
        val view: View = inflater.inflate(R.layout.dialog_toppodcast, container)
        initView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        value?.let {
            val adapter = PodcastListAdapter(it, context)
            dialog_podcast_list.layoutManager = LinearLayoutManager(context)
            dialog_podcast_list.adapter = adapter
            adapter.setOnItemClick(this)
        }
    }

    override fun onStart() {
        super.onStart()
        getDialog()?.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    //初始化View
    private fun initView(view: View) {
        getDialog()?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
    }

    companion object {
        private const val TAG = "CustomDialogFragment"
        fun newInstance(item: ArrayList<PodcastSearchResult>??): TopPodcastDialogFragment {
            val customDialogFragment = TopPodcastDialogFragment(item)
            return customDialogFragment
        }
    }

    override fun onItemClicked(position: Int) {
        var item = value?.get(position)
        item?.let {
            showRssDetail(item)
        }
        this.dismissAllowingStateLoss()
    }
    private fun showRssDetail(entry: PodcastSearchResult) {
        val intent = OnlineFeedViewActivity.newIntent(context, entry)
        startActivity(intent)
    }
}