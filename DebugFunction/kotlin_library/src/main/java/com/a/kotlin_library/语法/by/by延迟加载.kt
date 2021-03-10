package com.a.kotlin_library.语法.by

class LazySample {
    val lazy: String by lazy {
        println("init!")
        "my lazy"
    }
}

fun main(args: Array<String>) {
    val sample = LazySample()
    println("lazy = ${sample.lazy}")
    println("lazy = ${sample.lazy}")
}
