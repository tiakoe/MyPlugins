package com.a.kotlin_library.demo2.utils

import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import java.lang.reflect.ParameterizedType
import kotlin.reflect.full.memberProperties

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

@Suppress("UNCHECKED_CAST")
fun <VM> getVmClazz(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as VM
}

inline fun <reified T : Any> T.description() = this.javaClass.kotlin.memberProperties
        .map {
            "${it.name}: ${it.get(this@description)}"
        }
        .joinToString(separator = ";")
