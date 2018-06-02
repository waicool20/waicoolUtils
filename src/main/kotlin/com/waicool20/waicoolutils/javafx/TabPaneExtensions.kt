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

import javafx.geometry.Side
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.TabPane
import javafx.scene.layout.StackPane


/**
 * Gets the [TabPane] containing this node
 *
 * @returns [TabPane] containing this node if found, null if node is not inside a [TabPane]
 */
fun Node.getParentTabPane(): TabPane? {
    var parentNode = parent
    while (parentNode != null) {
        if (parentNode is TabPane) {
            return parentNode
        } else {
            parentNode = parentNode.parent
        }
    }
    return null
}

/**
 * Hack function to make [TabPane] tabs that are on the side display horizontal text
 *
 * @param side Which side to show the tab pane at
 * @param width Width of the tab
 */
fun TabPane.setSideWithHorizontalText(side: Side, width: Double = 100.0) {
    this.side = side
    if (side == Side.TOP || side == Side.BOTTOM) return
    tabMinHeight = width
    tabMaxHeight = width
    tabs.forEach { tab ->
        var text = tab.text
        if (text == "" && tab.properties.containsKey("text")) {
            text = tab.properties["text"].toString()
        } else {
            tab.properties["text"] = tab.text
        }
        val rotation = if (side == Side.LEFT) 90.0 else -90.0
        val label = Label(text)
        val pane = StackPane(Group(label))
        label.rotate = rotation
        pane.rotate = rotation
        tab.graphic = pane
        tab.text = ""
    }
    isRotateGraphic = true
}