package com.pcholt

import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid

object ProcessLinkField : KSVisitorVoid() {
    lateinit var logger: KSPLogger

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
        logger.warn("ProcessLinkField", property)
        logger.warn(property.simpleName.asString())
        val fieldPackage = property.type.containingFile?.packageName?.asString()
        val typeName = property.type.resolve().toString()
        logger.warn(fieldPackage ?: "[no package]")
        logger.warn(typeName)
//        val packageName = property.containingFile!!.packageName.asString()
//        logger.logging(property.parent.toString())
    }

}