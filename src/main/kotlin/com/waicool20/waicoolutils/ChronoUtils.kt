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

import java.time.Duration
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.math.floor
import kotlin.math.roundToLong

private val durationRegex = run {
    val week = Regex("(?:(\\d+)\\s?(?:w|(?i:weeks?))\\s*)?")
    val day = Regex("(?:(\\d+)\\s?(?:d|(?i:days?))\\s*)?")
    val hour = Regex("(?:(\\d+)\\s?(?:h|(?i:hours?))\\s*)?")
    val minute = Regex("(?:(\\d+)\\s?(?:m|(?i:minutes?))\\s*)?")
    val second = Regex("(?:(\\d+)\\s?(?:s|(?i:seconds?))\\s*)?")
    Regex("(?<=^|\\s)$week$day$hour$minute$second(?=\\s|$)")
}

object DurationUtils {
    /**
     * Tries to parse a string into a list of String to Duration pairs
     * Format being repeated sequence of a number followed by a time unit in descending magnitude
     * Valid time units are week, day, hour, minute, second
     * First letter of the time unit can also be used
     *
     * @param string String to parse
     */
    fun parseString(string: String): List<Pair<String, Duration>> {
        return durationRegex.findAll(string).filter { it.value.isNotBlank() }.map {
            val (weekString, dayString,
                hourString, minuteString, secondString) = it.destructured
            val weeks = weekString.toLongOrNull()?.takeIf { it >= 0 } ?: 0
            val days = dayString.toLongOrNull()?.takeIf { it >= 0 } ?: 0
            val hours = hourString.toLongOrNull()?.takeIf { it >= 0 } ?: 0
            val minutes = minuteString.toLongOrNull()?.takeIf { it >= 0 } ?: 0
            val seconds = secondString.toLongOrNull()?.takeIf { it >= 0 } ?: 0
            val duration = of(seconds, minutes, hours, days, weeks)
            it.value.trim() to duration
        }.toList()
    }

    /**
     * Constructs a [Duration] object from the given time
     */
    fun of(
        seconds: Long = 0,
        minutes: Long = 0,
        hours: Long = 0,
        days: Long = 0,
        weeks: Long = 0
    ): Duration {
        return Duration.ofSeconds(seconds + (minutes * 60) + (hours * 3600) + (days * 86400) + (weeks * 604800))
    }
}

/**
 * Formats a duration into pretty string with format
 * 1 Week 2 Days 3 Hours 4 Minutes 5 Seconds
 * Omitting time units that have 0 magnitude
 */
fun Duration.prettyString(): String {
    var secondsLeft = seconds
    val weeks = floor(secondsLeft / 604800.0).roundToLong()
    secondsLeft %= 604800
    val days = floor(secondsLeft / 86400.0).roundToLong()
    secondsLeft %= 86400
    val hours = floor(secondsLeft / 3600.0).roundToLong()
    secondsLeft %= 3600
    val minutes = floor(secondsLeft / 60.0).roundToLong()
    secondsLeft %= 60

    fun plural(i: Long) = if (i > 1) "s" else ""

    var duration = ""
    if (weeks > 0) duration += "$weeks Week${plural(weeks)} "
    if (days > 0) duration += "$days Day${plural(days)} "
    if (hours > 0) duration += "$hours Hour${plural(hours)} "
    if (minutes > 0) duration += "$minutes Minute${plural(minutes)} "
    duration += "$secondsLeft Second${plural(seconds)}"
    return duration
}

/**
 * Returns same ZonedDateTime instant with UTC offset
 */
val ZonedDateTime.utc
    get() = withZoneSameInstant(ZoneOffset.UTC)
