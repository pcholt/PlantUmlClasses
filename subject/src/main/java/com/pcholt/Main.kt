package com.pcholt

import com.pcholt.external.ExternalData

@Target(AnnotationTarget.CLASS)
annotation class MyAnnotation
@Target(AnnotationTarget.FIELD)
annotation class LinkField(val level:Int = 1, val ownership:Boolean = true)

fun main() {
    println("Hello world!")
}

@MyAnnotation
data class AnotherOne(
    val name : List<String>,
    val a : ProcessLinkField,
    val b : String,
    @LinkField
    val factory: ProcessBuilderFactory,
    val extern : ExternalData,
    @LinkField(2, ownership = false)
    val linky : List<ExternalData>,
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

