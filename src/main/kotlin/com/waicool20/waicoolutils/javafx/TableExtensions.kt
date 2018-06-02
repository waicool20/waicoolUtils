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

import com.sun.javafx.scene.control.skin.TableHeaderRow
import javafx.collections.ListChangeListener
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView

/**
 * Locks the column width
 */
fun TableView<*>.lockColumnWidths() {
    columns.addListener(ListChangeListener<TableColumn<*, *>> {
        while (it.next()) {
            it.addedSubList.forEach { it.isResizable = false }
        }
    })
    columns.forEach { it.isResizable = false }
}

/**
 * Disable the table header from moving
 */
fun TableView<*>.disableHeaderMoving() {
    widthProperty().listen {
        val row = lookup("TableHeaderRow") as TableHeaderRow
        row.reorderingProperty().listen { row.isReordering = false }
    }
}

/**
 * Sets the ratio of the width that the column should take in a table
 *
 * @param tableView [TableView] that contains the column
 * @param ratio Ratio of width
 */
fun TableColumn<*, *>.setWidthRatio(tableView: TableView<*>, ratio: Double) =
        prefWidthProperty().bind(tableView.widthProperty().subtract(20).multiply(ratio))