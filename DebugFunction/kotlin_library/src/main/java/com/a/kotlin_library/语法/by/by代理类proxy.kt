package com.a.kotlin_library.语法.by

interface Base {
    fun show()
}

open class BaseImpl : Base {
    override fun show() {
        print("BaseImpl::show()")
    }
}

class BaseProxy(base: Base) : Base by base

fun main(args: Array<String>) {
    val base = BaseImpl()
    BaseProxy(base).show()
}
