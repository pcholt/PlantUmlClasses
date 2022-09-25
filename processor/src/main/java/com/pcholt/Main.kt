package com.pcholt

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import java.io.PrintWriter

class MyProcessorProvider(
) : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment) =
        MySymbolProcessor(environment.codeGenerator)
}

class MySymbolProcessor(
    private val codeGenerator: CodeGenerator
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val k = resolver.getSymbolsWithAnnotation("com.pcholt.MyAnnotation").toList()
        if (k.isEmpty())
            return emptyList()
        k.forEach { symbol: KSAnnotated ->
            val loc = symbol.location.let { location ->
                when (location) {
                    is FileLocation -> location.filePath
                    NonExistLocation -> "[no location]"
                }
            }

            try {
                codeGenerator.createNewFile(
                    dependencies = Dependencies(aggregating = false),
                    packageName = "com.pcholt.gen",
                    fileName = "Abc",
                    extensionName = "puml"
                ).use {
                    val qualifiedName = symbol.annotations.first().annotationType.resolve().declaration.qualifiedName
                    PrintWriter(it).use { w ->
                        uml(w) {
                            package_(qualifiedName?.getQualifier()) {
                                class_(qualifiedName?.getShortName())
                                w.println("' $loc")
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                return emptyList()
            }
        }

        return emptyList()
    }

    private fun uml(w: PrintWriter, function: Puml.() -> Unit) {
        with(Puml(w)) {
            start_()
            function()
            end_()
        }
    }

}

data class Puml(
    val w: PrintWriter
) {
    fun start_() = w.println("@startuml")
    fun end_() = w.println("@enduml")
    fun package_(qualifier: String?, function: Puml.() -> Unit) {
        w.println("package $qualifier {")
        function()
        w.println("}")
    }
    fun class_(shortName: String?) {
        w.println("class $shortName")
    }
}