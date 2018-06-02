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

package com.waicool20.waicoolutils.logging

import com.waicool20.waicoolutils.streams.LineBufferedOutputStream
import com.waicool20.waicoolutils.streams.TeeOutputStream
import java.io.PrintStream
import java.util.concurrent.atomic.AtomicBoolean

object LoggingEventBus {
    private val listeners = mutableListOf<Listener>()

    private class LoggingEventBusOutputStream : LineBufferedOutputStream() {
        override fun writeLine(line: String) {
            listeners.forEach { (regex, action) ->
                regex.matchEntire(line.trim())?.let(action)
            }
        }
    }

    private val stream by lazy { LoggingEventBusOutputStream() }
    private val initialized = AtomicBoolean(false)

    data class Listener(val regex: Regex, val action: (match: MatchResult) -> Unit)

    init {
        initialize()
    }

    fun initialize() {
        if (initialized.get()) return

        System.setOut(PrintStream(TeeOutputStream(System.out, stream)))
        System.setErr(PrintStream(TeeOutputStream(System.err, stream)))

        initialized.set(true)
    }

    fun subscribe(regex: Regex, listener: (match: MatchResult) -> Unit) {
        listeners.add(Listener(regex, listener))
    }
}
