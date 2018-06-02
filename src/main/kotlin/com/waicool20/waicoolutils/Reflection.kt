/*
 * The MIT License (MIT)
 *
 * Copyright (c) waicoolUtils by waicool20
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.waicool20.waicoolutils

import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure


/**
 * Tries to convert the string to the corresponding data type specified by clazz.
 *
 * @param clazz Target data type as [java.lang.Class]
 *
 * @return Converted data type object, null if it failed or the original string if the target
 * data type is not supported.
 */
fun String.toObject(clazz: Class<*>): Any? {
    return when (clazz) {
        Boolean::class.javaObjectType -> toBoolean()
        Byte::class.javaObjectType -> toByteOrNull()
        Short::class.javaObjectType -> toShortOrNull()
        Int::class.javaObjectType -> toIntOrNull()
        Long::class.javaObjectType -> toLongOrNull()
        Float::class.javaObjectType -> toFloatOrNull()
        Double::class.javaObjectType -> toDoubleOrNull()
        else -> this
    }
}


/**
 * Tries to convert the string to the corresponding data type specified by clazz.
 *
 * @param clazz Target data type as [kotlin.reflect.KClass]
 *
 * @return Converted data type object, null if it failed or the original string if the target
 * data type is not supported.
 */
fun String.toObject(clazz: KClass<*>) = toObject(clazz.java)

//<editor-fold desc="JVM Reflection">


/**
 * Tries to convert the given [java.lang.Class] from its wrapper form into primitive class form.
 *
 * @return The converted class if the primitive class for it exists else returns the original class.
 */
fun Class<*>.toPrimitive(): Class<*> {
    return when (this) {
        Boolean::class.javaObjectType -> Boolean::class.java
        Byte::class.javaObjectType -> Byte::class.java
        Short::class.javaObjectType -> Short::class.java
        Int::class.javaObjectType -> Int::class.java
        Long::class.javaObjectType -> Long::class.java
        Float::class.javaObjectType -> Float::class.java
        Double::class.javaObjectType -> Double::class.java
        else -> this
    }
}


/**
 * Checks if a [java.lang.reflect.Field] is a generic typed field.
 *
 * @return True if it field is generic.
 */
fun Field.hasGenericType() = genericType is ParameterizedType


/**
 * Attempts to retrieve the [java.lang.Class] of generic type of a [java.lang.reflect.Field]
 *
 * @param level Specifies which generic class should be retrieved, as retrieved classes may also
 * have their own generics. Eg. Level 0 will retrieve List from List<List<String>>, but 1 will
 * retrieve String. Defaults to 0.
 *
 * @return The generic class.
 */
fun Field.getGenericClass(level: Int = 0): Class<*> {
    var paramType = genericType as ParameterizedType
    var objType = paramType.actualTypeArguments[0]
    for (i in level downTo 0) {
        if (objType is ParameterizedType) {
            paramType = objType
            objType = paramType.actualTypeArguments[0]
        }
    }
    return objType as Class<*>
}

//</editor-fold>

//<editor-fold desc="Kotlin Reflection">

/**
 * Checks if a [kotlin.reflect.KProperty] is a generic typed field.
 *
 * @return True if it property is generic.
 */
fun KProperty<*>.hasGenericType() = returnType.jvmErasure.typeParameters.isNotEmpty()


/**
 * Attempts to retrieve the [kotlin.reflect.KType] of generic class of a [kotlin.reflect.KProperty]
 *
 * @param level Specifies which generic class should be retrieved, as retrieved classes may also
 * have their own generics. Eg. Level 0 will retrieve List from List<List<String>>, but 1 will
 * retrieve String. Defaults to 0.
 *
 * @return The generic type.
 */
fun KProperty<*>.getGenericType(level: Int = 0): KType {
    var type = returnType
    for (i in level downTo 0) {
        type.arguments.firstOrNull()?.type?.let { type = it }
    }
    return type
}

/**
 * Attempts to retrieve the [kotlin.reflect.KClass] of generic class of a [kotlin.reflect.KProperty]
 *
 * @param level Specifies which generic class should be retrieved, as retrieved classes may also
 * have their own generics. Eg. Level 0 will retrieve List from List<List<String>>, but 1 will
 * retrieve String. Defaults to 0.
 *
 * @return The generic class.
 */
fun KProperty<*>.getGenericClass(level: Int = 0) = getGenericType(level).jvmErasure


/**
 * Checks if the [kotlin.reflect.KClass] represents an enum value.
 *
 * @return True if the [kotlin.reflect.KClass] represents an enum value.
 */
fun KClass<*>.isEnum() = starProjectedType.isEnum()


/**
 * Checks if the [kotlin.reflect.KType] represents an enum value.
 *
 * @return True if the [kotlin.reflect.KType] represents an enum value.
 */
fun KType.isEnum() = isSubtypeOf(Enum::class.starProjectedType)


/**
 * Attempts to find the enum instance from the given string of the same underlying type that
 * this class represents.
 *
 * @param string Name of enum value
 *
 * @return The enum instance if it was found else null
 */
fun KClass<*>.enumValueOf(string: String): Any? = java.enumConstants.find {
    it.toString().equals(string, true) ||
            it.toString().equals(string.replace("_", "-"), true)
}

//</editor-fold>
