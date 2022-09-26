package com.pcholt

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid

object ProcessMyAnnotation : KSVisitorVoid() {
    lateinit var logger: KSPLogger

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        logger.logging("ProcessMyAnnotation#visitClassDeclaration", classDeclaration)
        val packageName = classDeclaration.containingFile!!.packageName.asString()
        val className = "${classDeclaration.simpleName.asString()}Print"

        if (!packages.containsKey(packageName))
            packages[packageName] = Package()

        packages[packageName]?.run {
            if (classes.containsKey(className))
                classes[className] = Class()

            classes[className] = Class().apply { properties.addAll(listOf("A","B","C")) }
        }
    }

}