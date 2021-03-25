package com.a.kotlin_library.语法

class LazySingleton private constructor() {
    val data = 34

    companion object {
        val instance: LazySingleton by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { LazySingleton() }
    }
}

object SimpleSington {
    fun test() {
        println("hello world")
    }
}

fun main() {
    SimpleSington.test()
    print(LazySingleton.instance.data)
}
