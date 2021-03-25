package com.a.kotlin_library.demo2.utils

import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

fun EditText.textString(): String {
    return this.text.toString()
}


fun Fragment.nav(): NavController {
    return NavHostFragment.findNavController(this)
}

//防止短时间内多次快速跳转Fragment出现的bug
var lastNavTime = 0L
fun NavController.navigateAction(resId: Int, bundle: Bundle? = null, interval: Long = 500) {
    val currentTime = System.currentTimeMillis()
    if (currentTime >= lastNavTime + interval) {
        lastNavTime = currentTime
        try {
            navigate(resId, bundle)
        } catch (e: Exception) {
        }
    }
}
