package com.pcholt

@Target(AnnotationTarget.CLASS)
annotation class MyAnnotation(val name:String)
@Target(AnnotationTarget.FIELD)
annotation class LinkField(val level:Int = 1)

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

@MyAnnotation("OtherOne")
data class AnotherOne(
    val name : String,
    val a : String,
    val b : String,
    @LinkField val anotherOne: ProcessBuilderFactory
)
@MyAnnotation("OtherOne")
data class ProcessBuilderFactory(
    val name : String,
    val a : String,
    val b : String
)
