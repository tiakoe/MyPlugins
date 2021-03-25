package com.a.kotlin_library.demo2.view_model

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a.kotlin_library.demo2.bean.DataX

class ChildViewModel : ViewModel(), Observable {
    @Bindable
    val dataX = MutableLiveData<DataX>()


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}
