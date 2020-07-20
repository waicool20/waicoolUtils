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

import com.sun.jna.Library
import com.sun.jna.Native

object CLib {
    private interface StdLib : Library {
        fun setlocale(category: Int, locale: String): String
        fun setenv(name: String, value: String, overwrite: Int): Int
    }

    private val INSTANCE = Native.load(if (OS.isWindows()) "msvcrt" else "c", StdLib::class.java) as StdLib

    object Locale {
        const val LC_CTYPE = 0
        const val LC_NUMERIC = 1
        const val LC_TIME = 2
        const val LC_COLLATE = 3
        const val LC_MONETARY = 4
        const val LC_MESSAGES = 5
        const val LC_ALL = 6
        const val LC_PAPER = 7
        const val LC_NAME = 8
        const val LC_ADDRESS = 9
        const val LC_TELEPHONE = 10
        const val LC_MEASUREMENT = 11
        const val LC_IDENTIFICATION = 12

        fun setLocale(category: Int, locale: String): String = INSTANCE.setlocale(category, locale)
    }

    fun setEnv(name: String, value: String, overwrite: Boolean = true) = INSTANCE.setenv(name, value, if (overwrite) 1 else 0)
}