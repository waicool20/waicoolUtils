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

package com.waicool20.waicoolutils.controlsfx

import com.waicool20.waicoolutils.RequiresControlsFX
import javafx.application.Platform.runLater
import javafx.beans.property.ListProperty
import javafx.collections.ListChangeListener
import org.controlsfx.control.CheckComboBox

private object CFXBindings {
    val checkComboBoxBindings = mutableMapOf<CheckComboBox<*>, CheckComboBoxBinding<*>>()
}

/**
 * Binds a [CheckComboBox] list to the given [ListProperty]
 *
 * @param listProperty Binding list
 * @param readOnly Binding will be unidirectional if true
 */
@RequiresControlsFX
fun <T> CheckComboBox<T>.bind(
    listProperty: ListProperty<T>,
    readOnly: Boolean = false,
    onChange: (ListChangeListener.Change<out T>) -> Unit = {}
) {
    unbind()
    CFXBindings.checkComboBoxBindings[this] =
        CheckComboBoxBinding(this@bind, listProperty, readOnly, onChange)
}

/**
 * Unbinds a [CheckComboBox] from any bindings
 */
@RequiresControlsFX
fun CheckComboBox<*>.unbind() {
    listBinding?.unbind()
    CFXBindings.checkComboBoxBindings.remove(this)
}

@RequiresControlsFX
val CheckComboBox<*>.listBinding: CheckComboBoxBinding<*>?
    get() = CFXBindings.checkComboBoxBindings[this]

/**
 * Manages the binding between a [CheckComboBox] and [ListProperty]
 *
 * @param checkComboBox CheckComboBox to bind
 * @param listProperty ListProperty to bind to
 * @param readOnly Whether or not binding is unidirectional
 * @param onChange Listener that's executed when either list is changed
 */
@RequiresControlsFX
class CheckComboBoxBinding<T>(
    val checkComboBox: CheckComboBox<T>,
    val listProperty: ListProperty<T>,
    val readOnly: Boolean,
    onChange: (ListChangeListener.Change<out T>) -> Unit = {}
) {
    private val boxToListListener: ListChangeListener<T> = ListChangeListener { change ->
        runLater {
            synchronized(this) {
                listProperty.removeListener(listToBoxListener)
                listProperty.setAll(change.list)
                onChange(change)
                listProperty.addListener(listToBoxListener)
            }
        }
    }

    private val listToBoxListener: ListChangeListener<T> = ListChangeListener { change ->
        runLater {
            synchronized(this) {
                checkComboBox.checkModel.apply {
                    checkedItems.removeListener(boxToListListener)
                    checkAll(change.list)
                    onChange(change)
                    checkedItems.addListener(boxToListListener)
                }
            }
        }
    }

    init {
        runLater {
            synchronized(this) {
                checkComboBox.checkModel.checkAll(listProperty)
                checkComboBox.checkModel.checkedItems.addListener(boxToListListener)
                if (!readOnly) listProperty.addListener(listToBoxListener)
            }
        }
    }

    fun unbind() {
        synchronized(this) {
            checkComboBox.checkModel.checkedItems.removeListener(boxToListListener)
            listProperty.removeListener(listToBoxListener)
        }
    }
}

