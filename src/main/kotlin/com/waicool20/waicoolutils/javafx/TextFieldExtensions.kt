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

package com.waicool20.waicoolutils.javafx

import javafx.scene.control.TextField

/**
 * Sets the textfield to only allow the given characters included in a string
 */
fun TextField.setAllowedCharacters(chars: String) = useRegexFilter(Regex("[^$chars]"))

/**
 * Rejects all substrings in a the textfield that match the given regex
 */
fun TextField.useRegexFilter(regex: Regex) {
    textProperty().addListener("$id RegexFilterListener $regex") { _ ->
        text = text.replace(regex, "")
    }
}