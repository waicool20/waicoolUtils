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

import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.4.20"
    id("org.openjfx.javafxplugin") version "0.0.8"
}

group = "com.waicool20"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

javafx {
    version = "15"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.swing")
}

dependencies {
    val versions = object {
        val Kotlin by lazy { plugins.getPlugin(KotlinPluginWrapper::class).kotlinPluginVersion }
        val KotlinCoroutines = "1.4.2"
        val Jackson = "2.10.1"
        val TornadoFx = "2.0.0-SNAPSHOT"
        val ControlsFx = "11.0.2"
        val Logback = "1.2.3"
        val JNA = "5.4.0"
    }

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin", "kotlin-reflect", versions.Kotlin)

    /* Coroutines */
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", versions.KotlinCoroutines)
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-javafx", versions.KotlinCoroutines)

    /* Jackson Json */
    implementation("com.fasterxml.jackson.core", "jackson-core", versions.Jackson)
    implementation("com.fasterxml.jackson.core", "jackson-databind", versions.Jackson)
    implementation("com.fasterxml.jackson.core", "jackson-annotations", versions.Jackson)
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", versions.Jackson)
    implementation("com.fasterxml.jackson.datatype", "jackson-datatype-jsr310", versions.Jackson)

    /* Logging */
    implementation("ch.qos.logback", "logback-classic", versions.Logback)

    /* TornadoFX */
    implementation("no.tornado", "tornadofx", versions.TornadoFx)

    /* ControlsFX */
    implementation("org.controlsfx", "controlsfx", versions.ControlsFx)

    /* JNA */
    implementation("net.java.dev.jna", "jna", versions.JNA)
    
    /* --- */
    testImplementation("junit", "junit", "4.12")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}
