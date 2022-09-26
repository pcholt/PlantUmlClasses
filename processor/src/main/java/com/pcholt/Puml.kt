package com.pcholt

import java.io.PrintWriter

data class Puml(
    val w: PrintWriter
) {
    fun property_(propertyName: String, propertyType : String = "String") {
        w.println(" + $propertyName : $propertyType")
    }

    fun package_(qualifier: String?, function: Puml.() -> Unit) {
        w.println("package $qualifier {")
        function()
        w.println("}")
    }

    fun class_(shortName: String?, function: Puml.() -> Unit) {
        w.println("class $shortName {")
        function()
        w.println("}")
    }

    fun link_(link: Link) {
        w.println("${link.fromQualifiedName} o- ${link.toQualifiedName} : ${link.fieldName}")
    }
}