package com.a.kotlin_library.demo2.utils

import androidx.databinding.ObservableField
import com.google.gson.Gson


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

/**
 * 将对象转为JSON字符串
 */
fun Any?.toJson(): String {
    return Gson().toJson(this)
}
