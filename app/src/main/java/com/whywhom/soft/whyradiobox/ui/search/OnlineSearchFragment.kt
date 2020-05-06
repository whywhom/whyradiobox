package com.whywhom.soft.whyradiobox.ui.search

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.whywhom.soft.whyradiobox.R
import kotlinx.android.synthetic.main.app_bar_main_drawer.*


class OnlineSearchFragment : Fragment() {
    private var mSearchView: SearchView? = null
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
        activity!!.toolbar!!.title = ""

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.search_view)
        //通过MenuItem得到SearchView
        mSearchView = searchItem.actionView as SearchView
        onSearchInit()
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun onSearchInit() {
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧有叉叉 可以关闭搜索框
        mSearchView!!.setIconified(false);
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        mSearchView!!.setIconifiedByDefault(false);
        //设置搜索框直接展开显示。左侧有无放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        mSearchView!!.onActionViewExpanded();
        mSearchView!!.queryHint = getString(R.string.search_podcast_hint)
        mSearchView!!.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //提交按钮的点击事件
                Toast.makeText(activity, query, Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //当输入框内容改变的时候回调
                Log.i("OnlineSearchFragment", "内容: $newText")
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.search->{
                Log.d("SearchFragment" ,"click search")
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
