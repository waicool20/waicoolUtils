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

package com.waicool20.waicoolutils

import com.waicool20.waicoolutils.logging.loggerFor
import org.sikuli.script.ImagePath
import org.sikuli.script.Screen
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.TimeUnit

@RequiresSikuliX
object SikuliXLoader {
    private val logger = loggerFor<SikuliXLoader>()

    private var _working = false
    @RequiresSikuliX
    val SIKULI_WORKING get() = _working

    @RequiresSikuliX
    fun loadAndTest(path: Path) {
        logger.info("Loading Sikulix Jar at ${path.toAbsolutePath()}")
        SystemUtils.loadJarLibrary(path)
        logger.info("SikuliX jar now loaded in classpath")
        testSikuliX()
    }

    @RequiresSikuliX
    fun loadImagePath(path: Path) {
        if (_working) ImagePath.add(path.toUri().toURL())
    }

    @RequiresSikuliX
    fun loadImagePath(url: URL) {
        if (_working) ImagePath.add(url)
    }

    private fun testSikuliX() {
        try {
            preventSystemExit {
                logger.info("Testing SikuliX")
                // Delay is needed to prevent hanging
                TimeUnit.MILLISECONDS.sleep(100)
                logger.info("Testing screen: ${Screen()}")
                logger.info("Test image loading")
                Files.createTempDirectory("sikulix-test-temp").also { temp ->
                    ImagePath.add(temp.toUri().toURL())
                    ImagePath.remove("$temp")
                    Files.deleteIfExists(temp)
                }
                logger.info("Image loading passed")
                _working = true
            }
        } catch (e: NoClassDefFoundError) {
            logger.warn("SikuliX classes not found")
        } catch (e: IllegalExitException) {
            logger.warn("SikuliX ran into a fatal error and tried to exit the program")
            logger.warn("SikuliX installation might be broken! Go reinstall!")
        }
    }
}
