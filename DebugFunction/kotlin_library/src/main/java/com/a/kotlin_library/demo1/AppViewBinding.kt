package com.a.kotlin_library.demo1

import android.widget.TextView
import androidx.databinding.BindingAdapter
import kotlinx.coroutines.flow.StateFlow

interface AppViewBinding {
    @BindingAdapter("textStateFlow")
    fun setText(view: TextView, stateFlow: StateFlow<String>)
}
