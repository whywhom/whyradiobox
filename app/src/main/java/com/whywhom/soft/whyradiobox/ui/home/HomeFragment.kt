package com.whywhom.soft.whyradiobox.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.ui.discovery.FeedDiscoveryFragment
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    var podcastList:ArrayList<PodcastSearchResult> = ArrayList<PodcastSearchResult>()
    companion object {
        val TAG: String = "HomeFragment"

        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)//想让Fragment中的onCreateOptionsMenu生效必须先调用setHasOptionsMenu方法，否则Toolbar没有菜单。
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        top_en.setOnClickListener { onClick->
            var bundle : Bundle = Bundle()
            bundle.putInt("search_type", FeedDiscoveryFragment.TYPE_EN)
            findNavController(this).navigate(R.id.feedDiscoveryFragment, bundle)
        }
        top_cn.setOnClickListener { onClick->
            var bundle : Bundle = Bundle()
            bundle.putInt("search_type", FeedDiscoveryFragment.TYPE_CN)
            findNavController(this).navigate(R.id.feedDiscoveryFragment, bundle)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.search->{
                Log.d("MainFragment" ,"click search")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
