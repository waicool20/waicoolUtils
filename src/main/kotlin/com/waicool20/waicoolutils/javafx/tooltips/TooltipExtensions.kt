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

package com.waicool20.waicoolutils.javafx.tooltips

import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.application.Platform.runLater
import javafx.scene.Node
import javafx.scene.control.Tooltip
import javafx.util.Duration
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

/**
 * Shows a tooltip at a given nodes side
 *
 * @param node Node to show at
 * @param side Which side to show at
 */
fun Tooltip.showAt(node: Node, side: TooltipSide = TooltipSide.TOP_RIGHT) {
    with(node) {
        val bounds = localToScene(boundsInLocal)
        val x = when (side) {
            TooltipSide.TOP_LEFT, TooltipSide.CENTER_LEFT, TooltipSide.BOTTOM_LEFT -> bounds.minX
            TooltipSide.TOP, TooltipSide.CENTER, TooltipSide.BOTTOM -> (bounds.minX + bounds.maxX) / 2
            TooltipSide.TOP_RIGHT, TooltipSide.CENTER_RIGHT, TooltipSide.BOTTOM_RIGHT -> bounds.maxX
        }
        val y = when (side) {
            TooltipSide.TOP_LEFT, TooltipSide.TOP, TooltipSide.TOP_RIGHT -> bounds.minY
            TooltipSide.CENTER_LEFT, TooltipSide.CENTER, TooltipSide.CENTER_RIGHT -> (bounds.minY + bounds.maxY) / 2
            TooltipSide.BOTTOM_LEFT, TooltipSide.BOTTOM, TooltipSide.BOTTOM_RIGHT -> bounds.maxY
        }
        show(node, x + scene.window.x, y + scene.window.y)
    }
}

/**
 * Makes the tooltip fade after a given amount of time
 *
 * @param millis Length of time in milliseconds
 */
fun Tooltip.fadeAfter(millis: Long) {
    setOnShown {
        opacity = 1.0
        thread {
            TimeUnit.MILLISECONDS.sleep(millis)
            runLater {
                Timeline().apply {
                    keyFrames.add(KeyFrame(Duration.millis(500.0), KeyValue(opacityProperty(), 0)))
                    setOnFinished { hide() }
                }.play()
            }
        }
    }
}