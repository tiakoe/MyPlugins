package com.a.kotlin_library.demo2.utils

import androidx.databinding.ObservableField


open class StringObservableField(value: String = "") : ObservableField<String>(value) {
    override fun get(): String {
        return super.get()!!
    }
}

class BooleanObservableField(value: Boolean = false) : ObservableField<Boolean>(value) {
    override fun get(): Boolean {
        return super.get()!!
    }

    override fun set(value: Boolean?) {
        super.set(value)
    }
}
