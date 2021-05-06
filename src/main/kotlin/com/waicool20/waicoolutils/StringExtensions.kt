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

/**
 * Splits a target string based on a reference string on its character groups eg. AB123!@ will be
 * split to [AB, 123, !@]
 *
 * @return List of Pairs where first element is the reference string group and second is the target
 * string group
 */
fun String.splitOtherCharacterGroups(target: String): List<Pair<String, String>> {
    val referenceGroups = split(Regex("(?<=\\w)(?=\\W)|(?<=\\d)(?=\\D)|(?=\\d)(?<=\\D)"))
    val targetGroups = ArrayList<String>(referenceGroups.size)
    referenceGroups.fold(0) { i, str ->
        (i + str.length).also { targetGroups.add(target.substring(i, it)) }
    }
    return referenceGroups.zip(targetGroups)
}

/**
 * Levenshtein distance to another string with optional weight map
 *
 * @param other String to calculate the distance to
 * @param weights Optional map where the key contains string of similar characters and value
 * contains the weight (0.0 - 1.0)
 */
fun String.distanceTo(other: String, weights: Map<String, Double> = emptyMap()): Double {
    when {
        this == other -> return 0.0
        this.isBlank() -> return other.length.toDouble()
        other.isBlank() -> return length.toDouble()
    }

    val pDistance = DoubleArray(other.length + 1) { it.toDouble() }
    val cDistance = DoubleArray(other.length + 1)

    forEachIndexed { i, char ->
        cDistance[0] = i + 1.0
        other.forEachIndexed { j, otherChar ->
            val deleteCost = pDistance[j + 1] + (1)
            val insertCost = cDistance[j] + (1)
            val subCost = pDistance[j] + if (char == otherChar) {
                0.0
            } else {
                weights.keys.find { it.contains(char) && it.contains(otherChar) }
                    ?.let { weights[it] } ?: 1.0
            }
            cDistance[j + 1] = minOf(deleteCost, insertCost, subCost)
        }
        for (k in 0..other.length) pDistance[k] = cDistance[k]
    }
    return cDistance[other.length]
}
