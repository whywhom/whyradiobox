package com.whywhom.soft.whyradiobox.ui.episodes

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.whywhom.soft.whyradiobox.R

class LibraryFragment : Fragment() {

    companion object {
        val TAG: String = "AddFeedFragment"
        fun newInstance() = LibraryFragment()
    }

    private lateinit var viewModel: EpisodesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_episodes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EpisodesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
