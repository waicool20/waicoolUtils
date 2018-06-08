/*
 * GPLv3 License
 *
 *  Copyright (c) WAI2K by waicool20
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.waicool20.waicoolutils

import java.awt.Desktop
import java.io.File
import java.net.URI
import java.net.URL
import java.nio.file.Path
import kotlin.concurrent.thread

object DesktopUtils {

    private val desktop = if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop()
    } else null

    fun browse(uri: URI) {
        thread { desktop?.browse(uri) }
    }

    fun browse(uri: String) = browse(URI(uri))
    fun browse(url: URL) = browse(url.toURI())

    fun open(file: File) {
        thread { desktop?.open(file) }
    }

    fun open(file: Path) = open(file.toFile())
}