package com.pcholt

@Target(AnnotationTarget.CLASS)
annotation class MyAnnotation(val i: Int, val i1: Int, val i2: Int)

fun main() {
    println("Hello world!")
}

@MyAnnotation(4,5,6)
data class Heyo(
    val name : String,
    val a : String,
    val b : String
)

@MyAnnotation(4,5,6)
data class Heyo3(
    val name : String,
    val a : String,
    val b : String
)