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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

@RequiresKotlinCoroutines
suspend inline fun <T> Iterable<T>.filterAsync(coroutineScope: CoroutineScope = GlobalScope, crossinline predicate: (T) -> Boolean): List<T> {
    return filterToAsync(Collections.synchronizedList<T>(ArrayList()), coroutineScope, predicate)
}

@RequiresKotlinCoroutines
suspend inline fun <T, C : MutableCollection<in T>> Iterable<T>.filterToAsync(destination: C, coroutineScope: CoroutineScope = GlobalScope, crossinline predicate: (T) -> Boolean): C {
    val jobs = mutableListOf<Job>()
    for (element in this) {
        jobs += coroutineScope.launch {
            if (predicate(element)) destination.add(element)
        }
    }
    jobs.forEach { it.join() }
    return destination
}

@RequiresKotlinCoroutines
suspend inline fun <T, R> Iterable<T>.mapAsync(coroutineScope: CoroutineScope = GlobalScope, crossinline transform: (T) -> R): List<R> {
    return mapToAsync(Collections.synchronizedList<R>(ArrayList()), coroutineScope, transform)
}

@RequiresKotlinCoroutines
suspend inline fun <T, R, C : MutableCollection<in R>> Iterable<T>.mapToAsync(destination: C, coroutineScope: CoroutineScope = GlobalScope, crossinline transform: (T) -> R): C {
    val jobs = mutableListOf<Job>()
    for (item in this) {
        jobs += coroutineScope.launch {
            destination.add(transform(item))
        }
    }
    jobs.forEach { it.join() }
    return destination
}