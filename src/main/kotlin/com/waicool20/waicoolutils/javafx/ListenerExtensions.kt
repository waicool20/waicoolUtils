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

package com.waicool20.waicoolutils.javafx

import javafx.animation.PauseTransition
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.util.Duration

private object ListenerTracker {
    val listeners = mutableMapOf<String, ChangeListener<*>>()
    val listListeners = mutableMapOf<String, ListChangeListener<*>>()
}

/**
 * Removes a listener with a given name
 */
@Suppress("UNCHECKED_CAST")
fun <T> ObservableValue<T>.removeListener(name: String) {
    (ListenerTracker.listeners[name] as? ChangeListener<T>)?.let { removeListener(it) }
}

/**
 * Extension function for adding a listener with a given name, guaranteed to have only one listener
 * per name.
 *
 * @param name Unique name of listener
 * @param listener Listener, receives (ObservableValue, Old Value, New Value)
 */
@Suppress("UNCHECKED_CAST")
fun <T> ObservableValue<T>.addListener(
    name: String,
    listener: (ObservableValue<out T>, T, T) -> Unit
) {
    removeListener(name)
    val changeListener = ChangeListener(listener)
    ListenerTracker.listeners[name] = changeListener
    addListener(changeListener)
}


/**
 * Extension function for adding listener with a given name, guaranteed to have only one listener
 * per name.
 *
 * @param name Unique name of listener
 * @param listener Listener, receives (New Value)
 */
fun <T> ObservableValue<T>.addListener(name: String, listener: (T) -> Unit) =
    addListener(name) { _, _, newVal -> listener(newVal) }

/**
 * Extension function for adding listener that receives no parameters.
 *
 * @param listener Listener
 */
fun ObservableValue<*>.listen(listener: () -> Unit) = addListener { _, _, _ -> listener() }

/**
 * Extension function for adding a debounced listener which only executes after
 * a certain period of time that the event has settled.
 *
 * @param ms Amount of time to wait for events to settle
 * @param name Unique name of listener
 * @param listener Listener, receives (ObservableValue, Old Value, New Value)
 */
fun <T> ObservableValue<T>.listenDebounced(
    ms: Long,
    name: String,
    listener: (ObservableValue<out T>, T, T) -> Unit
) {
    val pause = PauseTransition(Duration.millis(ms.toDouble()))
    addListener(name) { obs, oldVal, newVal ->
        pause.setOnFinished { listener(obs, oldVal, newVal) }
        pause.playFromStart()
    }
}

/**
 * Extension function for adding a debounced listener which only executes after
 * a certain period of time that the event has settled.
 *
 * @param ms Amount of time to wait for events to settle
 * @param name Unique name of listener
 * @param listener Listener, receives (New Value)
 */
fun <T> ObservableValue<T>.listenDebounced(ms: Long, name: String, listener: (T) -> Unit) =
    listenDebounced(ms, name) { _, _, newVal -> listener(newVal) }

/**
 * Extension function for adding a debounced listener which only executes after
 * a certain period of time that the event has settled.
 *
 * @param ms Amount of time to wait for events to settle
 * @param listener Listener
 */
fun ObservableValue<*>.listenDebounced(ms: Long, listener: () -> Unit) {
    val pause = PauseTransition(Duration.millis(ms.toDouble()))
    addListener { _, _, _ ->
        pause.setOnFinished { listener() }
        pause.playFromStart()
    }
}

/**
 * Removes a listener with a given name
 */
@Suppress("UNCHECKED_CAST")
fun <T> ObservableList<T>.removeListener(name: String) {
    (ListenerTracker.listListeners[name] as? ListChangeListener<T>)?.let { removeListener(it) }
}

/**
 * Extension function for adding a list listener with a given name, guaranteed to have only one listener
 * per name.
 *
 * @param name Unique name of listener
 * @param listener Listener, receives ([ListChangeListener.Change])
 */
@Suppress("UNCHECKED_CAST")
fun <T> ObservableList<T>.addListener(
    name: String,
    listener: (ListChangeListener.Change<out T>) -> Unit
) {
    removeListener(name)
    val changeListener = ListChangeListener(listener)
    ListenerTracker.listListeners[name] = changeListener
    addListener(changeListener)
}

