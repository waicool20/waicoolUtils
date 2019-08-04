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

import kotlinx.coroutines.*
import java.util.*

@RequiresKotlinCoroutines
suspend inline fun <T> Iterable<T>.filterAsync(crossinline predicate: (T) -> Boolean): List<T> {
    return filterToAsync(Collections.synchronizedList<T>(ArrayList()), predicate)
}

@RequiresKotlinCoroutines
suspend inline fun <T, C : MutableCollection<in T>> Iterable<T>.filterToAsync(destination: C, crossinline predicate: (T) -> Boolean): C {
    coroutineScope {
        for (element in this@filterToAsync) {
            launch { if (predicate(element)) destination.add(element) }
        }
    }
    return destination
}

@RequiresKotlinCoroutines
suspend inline fun <T, R> Iterable<T>.mapAsync(crossinline transform: (T) -> R): List<R> {
    return mapToAsync(Collections.synchronizedList<R>(ArrayList()), transform)
}

@RequiresKotlinCoroutines
suspend inline fun <T, R, C : MutableCollection<in R>> Iterable<T>.mapToAsync(destination: C, crossinline transform: (T) -> R): C {
    coroutineScope {
        for (item in this@mapToAsync) {
            launch { destination.add(transform(item)) }
        }
    }
    return destination
}