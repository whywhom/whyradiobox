package com.whywhom.soft.whyradiobox.ui.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.adapter.PodcastListAdapter
import com.whywhom.soft.whyradiobox.extensions.setListener
import com.whywhom.soft.whyradiobox.interfaces.RecyclerListener
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.ui.detail.PodcastDetailActivity
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment(), PodcastListAdapter.ItemClickListenter {
    private var searchMenuItem: MenuItem? = null
    private var isSearchOpen = false
    private var skipItemUpdating = false
    private var lastSearchedText = ""

    var podcastList:ArrayList<PodcastSearchResult> = ArrayList<PodcastSearchResult>()
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)//想让Fragment中的onCreateOptionsMenu生效必须先调用setHasOptionsMenu方法，否则Toolbar没有菜单。
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val adapter = PodcastListAdapter(podcastList,context)
        adapter.setOnItemClick(this)
        podcast_list.layoutManager = LinearLayoutManager(activity)
        podcast_list.adapter = adapter
        podcast_list.setListener(object : RecyclerListener {
            override fun loadMore() {

            }

            override fun refresh() {
                swipeRefreshLayout.setRefreshing(true)
                viewModel.getTopPodcastList()
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
            viewModel.getTopPodcastList()
        })
        swipeRefreshLayout.setEnabled(false);//设置为不能刷新
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        setupSearch(menu)
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
    private fun setupSearch(menu: Menu) {
        val searchManager = context!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchMenuItem = menu.findItem(R.id.search)
        (searchMenuItem!!.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(this@MainFragment.activity!!.componentName))
            isSubmitButtonEnabled = false
            queryHint = getString(R.string.search)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String) = false

                override fun onQueryTextChange(newText: String): Boolean {
                    if (isSearchOpen) {
                        searchQueryChanged(newText)
                    }
                    return true
                }
            })
        }

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                isSearchOpen = true
                searchOpened()
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                isSearchOpen = false
                searchClosed()
                return true
            }
        })
    }

    fun searchQueryChanged(searchText: String) {
        Log.d("MainFragment","searchQueryChanged() get "+searchText)
        if(searchText.isEmpty()) return
        viewModel.itunesPodcastSearcher(searchText)
    }

    fun searchOpened() {
        isSearchOpen = true
        lastSearchedText = ""
    }

    fun searchClosed() {
        isSearchOpen = false
        if (!skipItemUpdating) {
        }
        skipItemUpdating = false
        lastSearchedText = ""
    }

    override fun onItemClicked(position: Int) {
        var entry = podcastList.get(position)
        if (entry.feedUrl == null) {
            return
        }
        val intent = PodcastDetailActivity.newIntent(this.context, entry)
        startActivity(intent)
    }
}
