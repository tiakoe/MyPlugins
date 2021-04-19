package com.a.kotlin_library.demo2.view_model

import androidx.databinding.Observable
import androidx.lifecycle.ViewModel

class ChildViewModel : ViewModel(), Observable {
//    @Bindable
//    val homeTable = MutableLiveData<HomeTable>()


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}
