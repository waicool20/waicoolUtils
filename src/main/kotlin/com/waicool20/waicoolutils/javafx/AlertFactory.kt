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

import javafx.scene.control.Alert
import javafx.stage.Stage

object AlertFactory {
    private fun alert(
            type: Alert.AlertType,
            stage: Stage?,
            title: String,
            header: String?,
            content: String
    ) = Alert(type).apply {
        this.title = title
        this.headerText = header
        this.contentText = content
        setOnHidden { stage?.toFront() }
    }

    fun info(stage: Stage? = null,
             title: String = "Info",
             header: String? = null,
             content: String) =
            alert(Alert.AlertType.INFORMATION, stage, title, header, content)

    fun warn(stage: Stage? = null,
             title: String = "Warning",
             header: String? = null,
             content: String) =
            alert(Alert.AlertType.WARNING, stage, title, header, content)

    fun error(stage: Stage? = null,
              title: String = "Error",
              header: String? = null,
              content: String) =
            alert(Alert.AlertType.ERROR, stage, title, header, content)
}