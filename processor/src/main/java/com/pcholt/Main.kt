package com.pcholt

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import java.io.PrintWriter

class MyProcessorProvider(
) : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        MySymbolProcessor(environment.codeGenerator, environment.logger)
}

class MySymbolProcessor(
    private val codeGenerator: CodeGenerator,
    val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        ProcessMyAnnotation.logger = logger
        ProcessLinkField.logger = logger
        resolver
            .getSymbolsWithAnnotation("com.pcholt.MyAnnotation")
            .forEach {
                it.accept(ProcessMyAnnotation, Unit)
            }
        resolver
            .getSymbolsWithAnnotation("com.pcholt.LinkField")
            .forEach {
                it.accept(ProcessLinkField, Unit)
            }
        return emptyList()
    }

    override fun finish() {
        codeGenerator.createNewFile(
            dependencies = Dependencies.ALL_FILES,
            packageName = "",
            fileName = "Things",
            extensionName = "puml"
        ).use {
            PrintWriter(it).use { pw ->
                uml(pw) {
                    for (packageEntry in packages) {
                        packageEntry.value.classes.forEach { _class ->
                            class_("${packageEntry.key}.${_class.key}") {
                                _class.value.properties.forEach { p ->
                                    property_(p.name, p.type)
                                }
                            }
                        }
                    }
                    for (link in links) {
                        link_(link)
                    }
                }
            }
        }
    }

}

val packages = mutableMapOf<String, Package>()

data class Package(
    val classes: MutableMap<String, Class> = mutableMapOf()
)

data class Class(
    val properties: MutableSet<Property> = mutableSetOf()
)

data class Property(
    val name: String,
    val type: String
)

data class Link(
    val fromQualifiedName: String,
    val toQualifiedName: String,
    val fieldName: String,
    val multiplicity: Multiplicity,
    val relationship: Relationship,
    val level: Int
)

val links = mutableSetOf<Link>()

private fun uml(w: PrintWriter, function: Puml.() -> Unit) {
    with(Puml(w)) {
        w.println("@startuml")
        function()
        w.println("@enduml")
    }
}


