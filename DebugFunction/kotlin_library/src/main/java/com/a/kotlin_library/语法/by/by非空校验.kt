package com.a.kotlin_library.语法.by

import kotlin.properties.Delegates

class User {
    var name: String by Delegates.notNull()

    fun init(name: String) {
        this.name = name
    }
}

fun main(args: Array<String>) {
    val user = User()
    // user.name -> IllegalStateException
    user.init("fdf")
    println(user.name)
}
