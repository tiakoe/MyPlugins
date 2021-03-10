package com.a.kotlin_library.语法.by

import kotlin.properties.Delegates

class User2 {
    var name: String by Delegates.observable("oldName") { kProperty, old, new ->
        println("${kProperty.returnType}, $old -> $new")
    }

    var address: String by Delegates.vetoable("wan", { kProperty, oldValue, newValue ->
        println("oldValue：$oldValue | newValue：$newValue")
        newValue.contains("wang")
    })
}

fun main(args: Array<String>) {
    val user = User2()
    println(user.name)
    user.name = "Carl"

    user.address = "abcd";
    println(user.address)
}
