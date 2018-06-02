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

package com.waicool20.waicoolutils.javafx.json

import com.fasterxml.jackson.annotation.JsonFilter
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.PropertyWriter
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.waicool20.waicoolutils.RequiresJacksonJson
import javafx.beans.property.*

/**
 * Provides an object mapper which ignores JavaFX Property types
 */
@RequiresJacksonJson
fun fxJacksonObjectMapper() = jacksonObjectMapper().ignoreJavaFXPropertyTypes().registerFXModule()

/**
 * Sets the object mapper to ignore JavaFX Property types
 */
@RequiresJacksonJson
fun ObjectMapper.ignoreJavaFXPropertyTypes() = apply {
    addMixIn(Any::class.java, FXPropertyFilter.MixIn::class.java)
    setFilterProvider(SimpleFilterProvider().addFilter("FXPropertyFilter", FXPropertyFilter()))
}

/**
 * Registers FX type deserializers
 */
@RequiresJacksonJson
fun ObjectMapper.registerFXModule(): ObjectMapper = registerModule(SimpleModule().apply {
    addDeserializer(ListProperty::class.java, ListPropertyDeserializer<Any>())
})

private class FXPropertyFilter : SimpleBeanPropertyFilter() {
    @JsonFilter("FXPropertyFilter")
    class MixIn

    private val filteredTypes = listOf(
            BooleanProperty::class.java,
            FloatProperty::class.java,
            DoubleProperty::class.java,
            IntegerProperty::class.java,
            LongProperty::class.java,
            StringProperty::class.java,
            ListProperty::class.java,
            SetProperty::class.java,
            MapProperty::class.java,
            ObjectProperty::class.java
    )

    override fun serializeAsField(pojo: Any, jgen: JsonGenerator, provider: SerializerProvider, writer: PropertyWriter) {
        if (include(writer)) {
            if (filteredTypes.none { writer.type.isTypeOrSubTypeOf(it) }) {
                writer.serializeAsField(pojo, jgen, provider)
            }
        } else if (!jgen.canOmitFields()) { // since 2.3
            writer.serializeAsOmittedField(pojo, jgen, provider)
        }
    }
}
