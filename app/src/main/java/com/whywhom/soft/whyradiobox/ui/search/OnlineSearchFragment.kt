package com.whywhom.soft.whyradiobox.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.adapter.PodcastListAdapter
import com.whywhom.soft.whyradiobox.extensions.setListener
import com.whywhom.soft.whyradiobox.interfaces.RecyclerListener
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult
import com.whywhom.soft.whyradiobox.utils.KeyboardUtils
import kotlinx.android.synthetic.main.app_bar_main_drawer.*
import kotlinx.android.synthetic.main.fragment_add_feed.*


class OnlineSearchFragment : Fragment(), PodcastListAdapter.ItemClickListenter {
    private lateinit var queryStr: String
    private var mSearchView: SearchView? = null
    var podcastList:ArrayList<PodcastSearchResult> = ArrayList<PodcastSearchResult>()
    companion object {
        fun newInstance() = OnlineSearchFragment()
    }

    private lateinit var viewModel: OnlineSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.online_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)//想让Fragment中的onCreateOptionsMenu生效必须先调用setHasOptionsMenu方法，否则Toolbar没有菜单。
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OnlineSearchViewModel::class.java)
        // TODO: Use the ViewModel
        requireActivity().toolbar!!.title = ""
        val adapter = PodcastListAdapter(podcastList,context)
        adapter.setOnItemClick(this)
        podcast_list.layoutManager = LinearLayoutManager(activity)
        podcast_list.adapter = adapter
        podcast_list.setListener(object : RecyclerListener {
            override fun loadMore() {

            }
            override fun refresh() {
                if(queryStr.isNotEmpty()) {
                    swipeRefreshLayout.setRefreshing(true)
                    searchQueryChanged(queryStr)
                }
            }
        })
        viewModel.podcastListLiveData.observe(viewLifecycleOwner, Observer { it->
            var len = it.size
            podcastList = it
            adapter.submitList(podcastList)
            adapter.notifyDataSetChanged()
            swipeRefreshLayout.setRefreshing(false);
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        setupSearch(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupSearch(menu: Menu) {
        val searchItem = menu.findItem(R.id.search_view)
        //通过MenuItem得到SearchView
        mSearchView = searchItem.actionView as SearchView
        val searchManager = context!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        mSearchView!!.apply {
            setSearchableInfo(searchManager.getSearchableInfo(this@OnlineSearchFragment.activity!!.componentName))
            isSubmitButtonEnabled = false
            queryHint = getString(R.string.search)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String) : Boolean {
                    //提交按钮的点击事件
//                    Toast.makeText(activity, query, Toast.LENGTH_SHORT).show()
                    Log.i("OnlineSearchFragment", "内容: $query")
                    this@OnlineSearchFragment.queryStr = query
                    searchQueryChanged(query)
                    KeyboardUtils.hideKeyboard(this@OnlineSearchFragment.activity!!)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    Log.i("OnlineSearchFragment", "内容: $newText")
                    return true
                }
            })
        }
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧有叉叉 可以关闭搜索框
        mSearchView!!.setIconified(false);
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        mSearchView!!.setIconifiedByDefault(false);
        //设置搜索框直接展开显示。左侧有无放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        mSearchView!!.onActionViewExpanded();
        mSearchView!!.queryHint = getString(R.string.search_podcast_hint)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.search->{
                Log.d("SearchFragment" ,"click search")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun searchQueryChanged(searchText: String) {
        Log.d("OnlineSearchFragment","searchQueryChanged() get "+searchText)
        if(searchText.isEmpty()) return
        viewModel.itunesPodcastSearcher(searchText)
    }

    override fun onItemClicked(position: Int) {
        var entry = podcastList.get(position)
        if (entry.feedUrl == null) {
            return
        }
        var bundle : Bundle = Bundle()
        bundle.putString("title", entry.title!!)
        bundle.putString("feed_url", entry.feedUrl!!)
        bundle.putString("feed_cover_url", entry.imageUrl!!)
        NavHostFragment.findNavController(this).navigate(R.id.onlineFeedViewFragment, bundle)
    }
}
