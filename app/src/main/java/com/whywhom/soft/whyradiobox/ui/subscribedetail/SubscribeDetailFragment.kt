package com.whywhom.soft.whyradiobox.ui.subscribedetail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whywhom.soft.whyradiobox.ui.subscribedetail.R

class SubscribeDetailFragment : Fragment() {

    companion object {
        fun newInstance() =
            SubscribeDetailFragment()
    }

    private lateinit var viewModel: SubscribeDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_subscribedetail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SubscribeDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
