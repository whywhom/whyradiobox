package com.whywhom.soft.whyradiobox.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.model.Entry
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult

class PodcastDetailFragment: Fragment() {
    companion object {
        private lateinit var itemInfo: PodcastSearchResult
        fun newInstance(item: PodcastSearchResult): PodcastDetailFragment{
            itemInfo = item
            return PodcastDetailFragment()
        }
    }

    private lateinit var viewModel: PodcastDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.podcastdetail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PodcastDetailViewModel::class.java)
        viewModel.getItemFeedUrl(itemInfo)
    }

}
