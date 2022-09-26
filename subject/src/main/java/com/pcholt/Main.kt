package com.pcholt

@Target(AnnotationTarget.CLASS)
annotation class MyAnnotation
@Target(AnnotationTarget.FIELD)
annotation class LinkField(val level:Int = 1)

fun main() {
    println("Hello world!")
}

@MyAnnotation
data class AnotherOne(
    val name : String,
    val a : String,
    val b : String,
    @LinkField val anotherOne: ProcessBuilderFactory
)


// Output:

// com.pcholt.AnotherOne o- com.pcholt.ProcessBuilderFactory : anotherOne

// {this file package}.{this class name} o- {file package of parameter class} : {field name}

@MyAnnotation
data class ProcessBuilderFactory(
    val name : String,
    val a : String,
    val b : String
)

