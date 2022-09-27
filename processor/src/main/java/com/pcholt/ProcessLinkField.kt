package com.pcholt

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.*

object ProcessLinkField : KSVisitorVoid() {
    lateinit var logger: KSPLogger

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {

        logger.warn(property.annotations.joinToString(",") {
            it.annotationType.resolve().declaration.qualifiedName?.asString() ?: "X"
        })

        val arguments = property.annotations
            .firstOrNull {
                it.shortName.asString() == "LinkField" &&
                        it.annotationType.resolve().declaration.qualifiedName?.asString() == "com.pcholt.LinkField"
            }
            ?.arguments


        val level = arguments?.get(0)?.value as? Int
        val ownership = arguments?.get(1)?.value as? Boolean

        val ksClassDeclaration = property.type.resolve().declaration as KSClassDeclaration
        val isCollectionType = ksClassDeclaration.getAllSuperTypes().any {
            it.declaration.qualifiedName?.asString() == "kotlin.collections.Iterable"
        }

        links.add(
            Link(
                toQualifiedName = ksClassDeclaration.let {
                    if (isCollectionType)
                        property.type.element?.typeArguments?.first()?.type?.resolve()?.declaration?.qualifiedName
                    else
                        it.qualifiedName
                }?.asString() ?: "",
                fromQualifiedName = property.parentDeclaration?.qualifiedName?.asString() ?: "",
                fieldName = property.simpleName.asString(),
                multiplicity = if (isCollectionType) Multiplicity.Multiple else Multiplicity.Single,
                relationship = if (ownership==true) Relationship.Ownership else Relationship.Reference,
                level = level ?: 1
            )
        )

    }

}

sealed class Multiplicity(val representation: String) {
    override fun toString() = representation

    object Single : Multiplicity("")
    object Multiple : Multiplicity("\"many\"")
}

sealed class Relationship(val representation: String) {
    override fun toString() = representation

    object Ownership : Relationship("*")
    object Reference : Relationship("o")
}
