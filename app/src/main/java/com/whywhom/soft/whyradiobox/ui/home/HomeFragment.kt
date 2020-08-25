package com.whywhom.soft.whyradiobox.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.event.MessageEvent
import com.whywhom.soft.whyradiobox.event.RefreshEvent
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.ui.BaseFragment
import com.whywhom.soft.whyradiobox.ui.discovery.FeedDiscoveryFragment
import com.whywhom.soft.whyradiobox.utils.Constants
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment() {
    var podcastList:ArrayList<PodcastSearchResult> = ArrayList<PodcastSearchResult>()

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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, Constants.getHomeViewModelFactory()).get(HomeViewModel::class.java)
        viewModel.podcastTopCnLiveData.observe(viewLifecycleOwner, Observer{
            var item = it?.feed;
            var list = item?.link
        })
        viewModel.podcastTopEnLiveData.observe(viewLifecycleOwner, Observer{
            var item = it?.feed;
            var list = item?.link
        })
        viewModel.podcastBbcLiveData.observe(viewLifecycleOwner, Observer{
            var item = it?.feed;
            var list = item?.link
        })
        viewModel.podcastCnnLiveData.observe(viewLifecycleOwner, Observer{
            var item = it?.feed;
            var list = item?.link
        })
        viewModel.podcastVoaLiveData.observe(viewLifecycleOwner, Observer{
            var item = it?.feed;
            var list = item?.link
        })
        iv_bbc.setOnClickListener { onClick->
            viewModel.getJsonDataFromAsset(context!!,"BBC")
        }
        iv_cnn.setOnClickListener { onClick->
            viewModel.getJsonDataFromAsset(context!!,"CNN")
        }
        iv_voa.setOnClickListener { onClick->
            viewModel.getJsonDataFromAsset(context!!,"VOA")
        }
        viewModel.getTopPodcastList("US", "100")
        viewModel.getTopPodcastList("CN", "100")
    }

    override fun onMessageEvent(messageEvent: MessageEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is RefreshEvent && this::class.java == messageEvent.activityClass) {
            Log.d(TAG ,messageEvent.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.search->{
                Log.d("HomeFragment" ,"click search")
//                findNavController(this).navigate(R.id.onlineSearchFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
