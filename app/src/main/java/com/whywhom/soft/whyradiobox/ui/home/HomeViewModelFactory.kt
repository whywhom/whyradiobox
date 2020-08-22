package com.whywhom.soft.whyradiobox.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.whywhom.soft.whyradiobox.data.RBRepository

/**
 * Created by wuhaoyong on 22/08/20.
 */
class HomeViewModelFactory (private val repository: RBRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}