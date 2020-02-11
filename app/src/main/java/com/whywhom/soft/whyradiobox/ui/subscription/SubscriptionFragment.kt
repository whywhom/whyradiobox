package com.whywhom.soft.whyradiobox.ui.subscription

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.whywhom.soft.whyradiobox.R

class SubscriptionFragment : Fragment() {

    companion object {
        val TAG: String = "SubscriptionFragment"

        fun newInstance() = SubscriptionFragment()
    }

    private lateinit var viewModel: SubscriptionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subscription, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SubscriptionViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
