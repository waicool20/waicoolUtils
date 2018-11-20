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

operator fun StringBuilder.plusAssign(value: StringBuffer?) {
    appendln(value)
}

operator fun StringBuilder.plusAssign(value: CharSequence?) {
    appendln(value)
}

operator fun StringBuilder.plusAssign(value: String?) {
    appendln(value)
}

operator fun StringBuilder.plusAssign(value: Any?) {
    appendln(value)
}

operator fun StringBuilder.plusAssign(value: StringBuilder?) {
    appendln(value)
}

operator fun StringBuilder.plusAssign(value: CharArray) {
    appendln(value)
}

operator fun StringBuilder.plusAssign(value: Char) {
    appendln(value)
}

operator fun StringBuilder.plusAssign(value: Boolean) {
    appendln(value)
}

operator fun StringBuilder.plusAssign(value: Int) {
    appendln(value)
}

operator fun StringBuilder.plusAssign(value: Short) {
    appendln(value)
}

operator fun StringBuilder.plusAssign(value: Byte) {
    appendln(value)
}

operator fun StringBuilder.plusAssign(value: Long) {
    appendln(value)
}

operator fun StringBuilder.plusAssign(value: Float) {
    appendln(value)
}

operator fun StringBuilder.plusAssign(value: Double) {
    appendln(value)
}