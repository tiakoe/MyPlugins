package com.a.kotlin_library.语法

data class Car(var name: String, var color: String, var weight: Int) {
}

fun main() {
    var car = Car("bus", "red", 1000);
    var newCar: Car = car.copy(name = "bicycle", weight = 10);
    var equalsResult = car.equals(newCar)
    println(car)
    println(newCar)
    println(equalsResult)
}
