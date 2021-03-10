package com.a.kotlin_library.demo1

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel(), Observable {

    private val _text = MutableStateFlow("Hello World!")

    @Bindable
    val text: StateFlow<String> = _text

    fun onClickButton() {
        _text.value = "Hello StateFlow: ${System.currentTimeMillis()}"
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}
