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

package com.waicool20.waicoolutils.javafx.columns

import com.waicool20.waicoolutils.javafx.listen
import javafx.collections.ListChangeListener
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.ComboBoxTableCell
import javafx.stage.WindowEvent
import javafx.util.StringConverter

/**
 * Column similar to [ComboBoxTableCell] but adds an "<Add Item>" entry at the end of the list
 * for adding new entries and an empty entry at the start for item deletion.
 *
 * @param title Title of the column
 * @param options Options that can be selected
 * @param table [TableView] containing this column
 * @param filter Optional filter to determine whether or not a certain item should be shown
 * @param maxRows Maximum rows that should be shown
 */
class OptionsColumn(
    title: String = "", var options: List<String>, table: TableView<String>,
    var filter: (cell: TableCell<String, String>, string: String) -> Boolean = { _, _ -> true },
    var maxRows: Int = Integer.MAX_VALUE
) : TableColumn<String, String>(title) {

    init {
        val addText = "<Add Item>"
        setCellFactory {
            ComboBoxTableCell<String, String>().apply {
                converter = object : StringConverter<String>() {
                    override fun toString(string: String?): String {
                        return if (index != table.items.size - 1) {
                            if (string == addText) "" else string ?: ""
                        } else {
                            string ?: ""
                        }
                    }

                    override fun fromString(string: String?): String = ""
                }
                indexProperty().listen {
                    items.setAll(if (index != table.items.size - 1) addText else "")
                    items.addAll(options.filter { filter.invoke(this, it) })
                }
            }
        }
        setOnEditCommit { event ->
            with(table.items) {
                val index = event.tablePosition.row
                if (index != size - 1) {
                    removeAt(index)
                    if (event.newValue != addText) add(index, event.newValue)
                    table.selectionModel.select(index)
                } else {
                    if (event.newValue != addText && index < maxRows && event.newValue != "") {
                        add(size - 1, event.newValue)
                    }
                }
                table.refresh()
                event.consume()
            }
        }
        table.items.addListener(ListChangeListener<String> {
            if (it.next()) {
                if (table.items[table.items.size - 1] != addText) {
                    table.items.add(addText)
                }
            }
        })
        table.sceneProperty().addListener { _, _, newVal ->
            newVal?.windowProperty()?.addListener { _, _, newWindow ->
                newWindow?.addEventFilter(WindowEvent.WINDOW_SHOWN, {
                    if (table.items.size == 0) table.items.add(addText)
                })
            }
        }
    }

}