package com.pcholt

@Target(AnnotationTarget.CLASS)
annotation class MyAnnotation(val name:String)

fun main() {
    println("Hello world!")
}

@MyAnnotation("Heyo")
data class Heyo(
    val name : String,
    val a : String,
    val b : String
)

@MyAnnotation("OtherOne")
data class Heyo3(
    val name : String,
    val a : String,
    val b : String
)