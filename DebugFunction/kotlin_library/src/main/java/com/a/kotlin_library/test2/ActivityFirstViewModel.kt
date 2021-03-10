package com.a.kotlin_library.test2

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActivityFirstViewModel : ViewModel(), Observable {
    @Bindable
    val name = MutableLiveData<String>()

    @Bindable
    val age = MutableLiveData<String>()

    fun getData() {
        println("fdsf")
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

}
