package com.pcholt

import java.io.PrintWriter

data class Puml(
    val w: PrintWriter
) {
    fun property_(propertyName: String, propertyType : String = "String") {
        w.println(" + $propertyName : $propertyType")
    }

    fun class_(shortName: String?, function: Puml.() -> Unit) {
        w.println("class $shortName {")
        function()
        w.println("}")
    }

    fun link_(link: Link) {
        if (link.level < 1)
            w.println("${link.toQualifiedName} ${link.multiplicity} ${"-".repeat(1-link.level)}${link.relationship} ${link.fromQualifiedName}: ${link.fieldName}")
        else
            w.println("${link.fromQualifiedName} ${link.relationship}${"-".repeat(link.level)} ${link.multiplicity} ${link.toQualifiedName}: ${link.fieldName}")
    }
}