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
        logger.warn("OW")
        ProcessMyAnnotation.logger = logger
        resolver
            .getSymbolsWithAnnotation("com.pcholt.MyAnnotation")
            .forEach {
                logger.warn(it.toString())
                it.accept(ProcessMyAnnotation, Unit)
            }
        return emptyList()
    }

    override fun finish() {
        codeGenerator.createNewFile(
            dependencies = Dependencies.ALL_FILES,
            packageName = "com.pcholt.gen",
            fileName = "Things",
            extensionName = "puml"
        ).use {
            PrintWriter(it).use { pw ->
                uml(pw) {
                    packages.forEach { packageEntry ->
                        package_(packageEntry.key) {
                            packageEntry.value.classes.forEach { className ->
                                class_(className)
                            }
                        }
                    }
                }
            }
        }
    }
}

val packages = mutableMapOf<String,Package>()

data class Package(
    val classes : MutableSet<String> = mutableSetOf()
)

object ProcessMyAnnotation : KSVisitorVoid() {
    lateinit var logger: KSPLogger

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        logger.logging("ProcessMyAnnotation#visitClassDeclaration", classDeclaration)
        val packageName = classDeclaration.containingFile!!.packageName.asString()
        val className = "${classDeclaration.simpleName.asString()}Print"

        if (!packages.containsKey(packageName))
            packages[packageName] = Package()

        packages[packageName]?.run {
            classes.add(className)
        }
    }

}

private fun uml(w: PrintWriter, function: Puml.() -> Unit) {
    with(Puml(w)) {
        w.println("@startuml")
        function()
        w.println("@enduml")
    }
}


data class Puml(
    val w: PrintWriter
) {
    fun package_(qualifier: String?, function: Puml.() -> Unit) {
        w.println("package $qualifier {")
        function()
        w.println("}")
    }

    fun class_(shortName: String?) {
        w.println("class $shortName")
    }
}

