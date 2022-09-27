package com.pcholt

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid

private fun String?.strip(packageName: String): String? =
    this?.let { qualifiedName ->
        val k = listOf(
            "kotlin.collections.",
            "kotlin.",
            "java.lang.",
            "${packageName}."
        )
            .firstOrNull { qualifiedName.startsWith(it) }
            ?: ""
        qualifiedName.substring(k.length)
    }

object ProcessMyAnnotation : KSVisitorVoid() {
    lateinit var logger: KSPLogger

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val packageName = classDeclaration.containingFile!!.packageName.asString()
        val className = classDeclaration.simpleName.asString()

        if (!packages.containsKey(packageName))
            packages[packageName] = Package()

        packages[packageName]?.run {
            if (classes.containsKey(className))
                classes[className] = Class()

            classes[className] = Class().apply {
                properties.addAll(
                    classDeclaration.getAllProperties()
                        .filterNot { dec ->
                            dec.annotations.any { it.shortName.asString() == "LinkField" }
                        }
                        .map {
                            val parameters = it.type.element?.typeArguments?.joinToString(",") { ta ->
                                ta.type?.resolve()?.declaration?.simpleName
                                    ?.asString() ?: "?"
                            }.run {
                                if (!isNullOrEmpty()) "<$this>" else ""
                            }
                            Property(
                                name = it.simpleName.asString(),
                                type = it.type.resolve().declaration.qualifiedName
                                    ?.asString()
                                    .strip(packageName) + parameters
                            )
                        }
                        .toList()
                )
            }
        }
    }

}