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
                    package_("A") {
                        class_("Fleet")
                        class_("Empire")
                        class_("World")
                        pw.println("' $classes")
                    }
                }
            }
//            uml(PrintWriter(it)) {
//                class_("Thingy")
//                for (k in p.classes) {
//                    class_("Thing${k}")
//                }
//            }
        }
    }
}

//    fun process1(resolver: Resolver): List<KSAnnotated> {
//        val k = resolver.getSymbolsWithAnnotation("com.pcholt.MyAnnotation").toList()
//        if (k.isEmpty())
//            return emptyList()
//        try {
//            codeGenerator.createNewFile(
//                dependencies = Dependencies(aggregating = false),
//                packageName = "com.pcholt.gen",
//                fileName = "Abc",
//                extensionName = "puml"
//            ).use {
//                PrintWriter(it).use { writer ->
//                    uml(writer) {
//                        package_("pcholt.com") {
//                            k.forEach { symbol: KSAnnotated ->
//                                val loc = getLoc(symbol)
//                                class_("TheClass")
//                            }
//                        }
//                    }
//                }
//
//            }
//        } catch (e: Exception) {
//            return emptyList()
//        }
//
//        return emptyList()
//    }
//
//}

val classes = mutableListOf<String>()

object ProcessMyAnnotation : KSVisitorVoid() {
    lateinit var logger: KSPLogger

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        logger.warn("visit 1")
        classes.add(classDeclaration.location.toString())
    }

}

private fun getLoc(symbol: KSAnnotated) {
    symbol.location.let { location ->
        when (location) {
            is FileLocation -> location.filePath
            NonExistLocation -> "[no location]"
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

