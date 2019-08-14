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

package com.waicool20.waicoolutils.streams

import javafx.application.Platform.runLater
import javafx.scene.control.TextArea
import java.io.OutputStream

/**
 * Creates an [OutputStream] for a [TextArea] that can be written to
 *
 * @param textArea TextArea to write to
 * @param maxLines Maximum lines to show in the text area
 */
class TextAreaOutputStream(
        private val textArea: TextArea,
        private val maxLines: Int = 1000
) : LineBufferedOutputStream() {
    private var lineCount = 0

    override fun writeLine(line: String) {
        if (line.isEmpty()) return
        runLater {
            if (line.contains("\u001b[2J\u001b[H")) {
                textArea.clear()
                lineCount = 0
                return@runLater
            }
            if (lineCount >= maxLines) {
                textArea.deleteText(0, textArea.text.indexOf('\n') + 1)
            } else lineCount++
            textArea.appendText(line.replace(Regex("\\u001b\\[.+?m"), ""))
        }
    }
}
