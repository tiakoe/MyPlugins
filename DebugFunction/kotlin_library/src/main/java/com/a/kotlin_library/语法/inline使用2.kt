package com.a.kotlin_library.语法

open class F {
    var data: Int = 5
    open fun getString() {
        data += 1
        println(data)
    }
}

class F2 : F() {
    override fun getString() {
        data += 2
        println(data)
    }

    fun update() {
        data += 3
        getString()
    }
}

fun t3() {
    println(43)
}

fun main() {
    val f = F2()
    f.test1 {
        this.update()
    }
    f.test2 { t3() }
}

//T.()  表示可以为该类的方法
inline fun <T : F> T.test1(f: T.() -> Unit) {
    f()
}

inline fun <T : F> T.test2(f: () -> Unit) {
    f()
    println("fdsf")
}
