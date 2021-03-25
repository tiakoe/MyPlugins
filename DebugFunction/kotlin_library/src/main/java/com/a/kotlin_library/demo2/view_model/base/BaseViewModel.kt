package com.a.kotlin_library.demo2.view_model.base

import androidx.databinding.Observable
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel(), Observable {
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}
