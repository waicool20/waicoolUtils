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

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.stage.WindowEvent

/**
 * Sets the initial size of [Scene] containing this node as the minimum size
 */
fun Parent.setInitialSceneSizeAsMin() = sceneProperty().setInitialSizeAsMin()

/**
 * Sets the initial size of [Scene] in this property as the minimum size
 */
fun ReadOnlyObjectProperty<Scene>.setInitialSizeAsMin() = setInitialSize(null, null, true)

/**
 * Sets the initial scene size of the scene containing this node
 *
 * @param width Width
 * @param height Height
 * @param asMinimum Sets this size as the scenes minimum size if true
 */
fun Parent.setInitialSceneSize(width: Double, height: Double, asMinimum: Boolean) = sceneProperty().setInitialSize(width, height, asMinimum)

/**
 * Sets the initial scene size of this property
 *
 * @param width Width
 * @param height Height
 * @param asMinimum Sets this size as the scenes minimum size if true
 */
fun ReadOnlyObjectProperty<Scene>.setInitialSize(width: Double?, height: Double?, asMinimum: Boolean) {
    addListener { _, _, newVal ->
        newVal?.windowProperty()?.addListener { _, _, newWindow ->
            newWindow?.addEventFilter(WindowEvent.WINDOW_SHOWN, {
                with(it.target as Stage) {
                    if (width != null && height != null) {
                        this.width = width
                        this.height = height
                    }
                    if (asMinimum) {
                        minHeight = this.height + 25
                        minWidth = this.width + 25
                    }
                }
            })
        }
    }
}