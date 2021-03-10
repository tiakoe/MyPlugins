package com.a.kotlin_library.语法.by

class UserX(map: Map<String, Any?>) {
    val name: String by map
    val age: Int by map
}

fun main() {
    val user = UserX(mapOf(
            "name" to "John Doe",
            "age" to 123
    ))
    println("name = ${user.name}, age = ${user.age}")
}
