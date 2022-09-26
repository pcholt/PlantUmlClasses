package com.pcholt

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid

object ProcessLinkField : KSVisitorVoid() {
    lateinit var logger: KSPLogger

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {

        links.add(
            Link(
                toQualifiedName = property.type.resolve().declaration.qualifiedName?.asString() ?: "",
                fromQualifiedName = property.parentDeclaration?.qualifiedName?.asString() ?: "",
                fieldName = property.simpleName.asString()
            )
        )

        logger.warn(links.toString())
    }

}