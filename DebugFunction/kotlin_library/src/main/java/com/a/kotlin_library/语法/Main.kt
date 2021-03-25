package com.a.kotlin_library.语法

import androidx.core.util.toRange

fun main() {
    val data = arrayOf(1, 2, 3, 4)
    val mock = (-5.0..5.0).toRange()
    println(mock)
    println(data)
}
