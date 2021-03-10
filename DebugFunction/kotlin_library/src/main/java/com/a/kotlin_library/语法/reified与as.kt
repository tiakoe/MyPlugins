package com.a.kotlin_library.语法

inline fun <reified T> f1(): T? {
    val str = "fdfdf"
    return str as? T
}

fun <T> f2(): T? {
    val str = "fdfdf"
    return str as? T
}

fun main() {
    val a: Int? = f1<Int>()
    println("a1: " + a)
    val a2: Int? = f2<Int>()
    println("a2: " + a2)
}
