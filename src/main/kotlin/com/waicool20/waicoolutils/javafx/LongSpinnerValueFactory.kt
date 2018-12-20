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

import com.waicool20.waicoolutils.RequiresTornadoFX
import javafx.scene.control.SpinnerValueFactory
import javafx.util.StringConverter
import tornadofx.*
import kotlin.math.max
import kotlin.math.min

@RequiresTornadoFX
class LongSpinnerValueFactory(
        min: Long,
        max: Long,
        initialValue: Long = min,
        amountToStepBy: Long = 1
) : SpinnerValueFactory<Long>() {
    val minProperty = min.toProperty()
    val maxProperty = max.toProperty()
    val amountToStepByProperty = amountToStepBy.toProperty()

    var min by minProperty
    var max by maxProperty
    var amountToStepBy by amountToStepByProperty

    init {
        valueProperty().addListener { _, _, newVal ->
            min(max, max(min, newVal))
        }
        converter = object : StringConverter<Long>() {
            override fun fromString(s: String) = s.toLongOrNull()
            override fun toString(l: Long) = l.toString()
        }
        value = initialValue
    }

    override fun increment(steps: Int) {
        val newIndex = value + steps * amountToStepBy
        value = if (newIndex >= min) {
            newIndex
        } else {
            if (isWrapAround) wrapValue(newIndex) else min
        }
    }

    override fun decrement(steps: Int) {
        val newIndex = value - steps * amountToStepBy
        value = if (newIndex >= min) {
            newIndex
        } else {
            if (isWrapAround) wrapValue(newIndex) else min
        }
    }

    private fun wrapValue(v: Long): Long {
        val r = v % max
        return if (min in (max + 1)..(r - 1) || min in (r + 1)..(max - 1)) {
            r + max - min
        } else {
            r
        }
    }
}