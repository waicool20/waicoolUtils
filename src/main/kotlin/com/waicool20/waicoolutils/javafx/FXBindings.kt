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

package com.waicool20.waicoolutils.javafx

import javafx.beans.binding.*
import javafx.beans.property.*
import javafx.scene.control.ComboBox
import javafx.scene.control.Spinner

private object Bindings {
    val objectBindings = mutableMapOf<ObjectProperty<*>, MutableList<ObjectProperty<*>>>()
    val anyBinding = mutableListOf<Binding<*>>()
}

fun BooleanBinding.persist() = also { Bindings.anyBinding.add(this) }
fun IntegerBinding.persist() = also { Bindings.anyBinding.add(this) }
fun LongBinding.persist() = also { Bindings.anyBinding.add(this) }
fun FloatBinding.persist() = also { Bindings.anyBinding.add(this) }
fun DoubleBinding.persist() = also { Bindings.anyBinding.add(this) }

fun Spinner<Int>.bind(integerProperty: IntegerProperty, readOnly: Boolean = false) =
        bind(valueFactory.valueProperty(), integerProperty.asObject(), readOnly)

fun Spinner<Long>.bind(longProperty: LongProperty, readOnly: Boolean = false) =
        bind(valueFactory.valueProperty(), longProperty.asObject(), readOnly)

fun Spinner<Float>.bind(floatProperty: FloatProperty, readOnly: Boolean = false) =
        bind(valueFactory.valueProperty(), floatProperty.asObject(), readOnly)

fun Spinner<Double>.bind(doubleProperty: DoubleProperty, readOnly: Boolean = false) =
        bind(valueFactory.valueProperty(), doubleProperty.asObject(), readOnly)

fun ComboBox<Int>.bind(integerProperty: IntegerProperty, readOnly: Boolean = false) =
        bind(valueProperty(), integerProperty.asObject(), readOnly)

fun ComboBox<Long>.bind(longProperty: LongProperty, readOnly: Boolean = false) =
        bind(valueProperty(), longProperty.asObject(), readOnly)

fun ComboBox<Float>.bind(floatProperty: FloatProperty, readOnly: Boolean = false) =
        bind(valueProperty(), floatProperty.asObject(), readOnly)

fun ComboBox<Double>.bind(doubleProperty: DoubleProperty, readOnly: Boolean = false) =
        bind(valueProperty(), doubleProperty.asObject(), readOnly)

private fun <T> bind(objectProperty: ObjectProperty<T>, objectProperty1: ObjectProperty<T>, readOnly: Boolean = false) {
    if (readOnly) objectProperty.bind(objectProperty1) else objectProperty.bindBidirectional(objectProperty1)
    Bindings.objectBindings.getOrPut(objectProperty) { mutableListOf(objectProperty1) }
}
