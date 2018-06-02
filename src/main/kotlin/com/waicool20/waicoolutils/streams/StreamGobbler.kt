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

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

/**
 * Consumes the output/error stream of the process and redirects it to [System.out], in case
 * redirection doesn't work
 *
 * @param process Process to consume
 * @param autorun Starts consuming immediately 8f true (Default)
 */
class StreamGobbler(val process: Process?, autorun: Boolean = true): Runnable {
    private val latch = CountDownLatch(2)

    init {
        if (autorun) run()
    }

    /**
     * Starts consuming the processes streams
     */
    override fun run() {
        val handler = Thread.UncaughtExceptionHandler { _, throwable ->
            if (throwable.message == "Stream closed") {
                latch.countDown()
            } else throw throwable
        }
        process?.apply {
            thread(isDaemon = true) {
                BufferedReader(InputStreamReader(inputStream)).forEachLine(::println)
                latch.countDown()
            }.uncaughtExceptionHandler = handler
            thread(isDaemon = true) {
                BufferedReader(InputStreamReader(errorStream)).forEachLine(::println)
                latch.countDown()
            }.uncaughtExceptionHandler = handler
        }
    }

    /**
     * Waits for the stream gobbler to close, ie. All streams of the process were closed
     */
    fun waitFor() = latch.await()
}

/**
 * Redirects process io using [StreamGobbler]
 */
fun Process.gobbleStream() = this.let { StreamGobbler(this, true) }
