package com.a.kotlin_library.语法.by

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Example {
    var p: String by Delegate()
    var w: String? by A()
    val x: String? by B()
}

class Delegate() {
    operator fun getValue(thisRef: Any?, prop: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${prop.name}' to me!"
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: String) {
        println("$value has been assigned to ${prop.name} in $thisRef")
    }
}


class A : ReadWriteProperty<Any?, String?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): String? {
        return "aaaa"
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
        println("setValue=" + value)
    }

}

class B : ReadOnlyProperty<Any?, String?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): String? {
        println()
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}


interface ReadWriteProperty<in R, T> {
    operator fun getValue(thisRef: R, property: KProperty<*>): T
    operator fun setValue(thisRef: R, property: KProperty<*>, value: T)
}

