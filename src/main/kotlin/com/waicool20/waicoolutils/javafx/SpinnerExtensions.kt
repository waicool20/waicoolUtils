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

import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.control.TextFormatter
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import javafx.util.StringConverter
import java.util.concurrent.TimeUnit
import kotlin.math.abs


private val spinnerWraps = mutableMapOf<Spinner<*>, Boolean>()

/**
 * Updates another spinners value when this spinners value wraps around
 *
 * @param spinner Other spinner to update
 * @param min Minimum value
 * @para max Maximum value
 */
fun <T> Spinner<T>.updateOtherSpinnerOnWrap(spinner: Spinner<T>, min: T, max: T) {
    spinnerWraps.putIfAbsent(this, false)
    addEventHandler(MouseEvent.ANY) { event ->
        if (event.eventType == MouseEvent.MOUSE_PRESSED ||
            event.eventType == MouseEvent.MOUSE_RELEASED
        ) {
            if (event.button == MouseButton.PRIMARY) {
                val node = event.target as Node
                if (node is StackPane && node.getParent() is Spinner<*>) {
                    if (node.styleClass.contains("increment-arrow-button") ||
                        node.styleClass.contains("decrement-arrow-button")
                    ) {
                        spinnerWraps[this] = event.eventType == MouseEvent.MOUSE_PRESSED
                    }
                }
            }
        }
    }
    valueProperty().addListener { _, oldVal, newVal ->
        if (spinnerWraps[this] == true) {
            if (oldVal == max && newVal == min) {
                spinner.increment()
            } else if (oldVal == min && newVal == max) {
                spinner.decrement()
            }
        }
    }
}

/**
 * Bounds the spinners values based on a given time unit and sets it to wrap around
 *
 * @param unit Time unit to bind the spinner value, eg. [TimeUnit.HOURS] binds it to 0-23
 * @param allowInvalid Allows -1 to be a value
 *
 * @throws IllegalStateException if the time unit is not supported
 */
fun Spinner<Int>.asTimeSpinner(unit: TimeUnit, allowInvalid: Boolean = false) {
    val formatter = object : StringConverter<Int>() {
        override fun toString(i: Int?): String = if (i == null) {
            if (allowInvalid) "-01" else "00"
        } else {
            "${if (i < 0) "-" else ""}${String.format("%02d", abs(i))}"
        }

        override fun fromString(s: String): Int = s.toInt()
    }
    editor.textFormatter = TextFormatter(formatter)
    editor.alignment = Pos.CENTER
    val lowBound = if (allowInvalid) -1 else 0
    valueFactory = when (unit) {
        TimeUnit.DAYS -> SpinnerValueFactory.IntegerSpinnerValueFactory(lowBound, 31)
        TimeUnit.HOURS -> SpinnerValueFactory.IntegerSpinnerValueFactory(lowBound, 23)
        TimeUnit.MINUTES -> SpinnerValueFactory.IntegerSpinnerValueFactory(lowBound, 59)
        TimeUnit.SECONDS -> SpinnerValueFactory.IntegerSpinnerValueFactory(lowBound, 59)
        else -> kotlin.error("TimeUnit $unit is not supported")
    }
    valueFactory.isWrapAround = true
}