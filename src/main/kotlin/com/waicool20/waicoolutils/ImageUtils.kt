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

import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.color.ColorSpace
import java.awt.image.*
import kotlin.math.roundToInt

/**
 * Scales a given [BufferedImage] to a given scaling factor, rounded to the nearest integer pixel
 * size.
 *
 * @param scaleFactor Scaling factor
 * @return New scaled [BufferedImage]
 */
fun BufferedImage.scale(scaleFactor: Double = 2.0): BufferedImage {
    val w = (width * scaleFactor).roundToInt()
    val h = (height * scaleFactor).roundToInt()
    val image = createCompatibleCopy(w, h)
    (image.graphics as Graphics2D).apply {
        setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR
        )
        drawImage(this@scale, 0, 0, w, h, null)
        dispose()
    }
    return image
}

/**
 * Converts a given [BufferedImage] into black and white based on a threshold on each pixels
 * relative luminance
 *
 * @param threshold Deciding threshold, luminance above this will be white else black
 * @return Original [BufferedImage] but in B&W
 */
fun BufferedImage.binarizeImage(threshold: Double = 0.4) = apply {
    for (x in 0 until width) {
        for (y in 0 until height) {
            val newColor = if (Color(getRGB(x, y), true).relativeLuminance() > threshold) {
                Color.WHITE
            } else {
                Color.BLACK
            }
            setRGB(x, y, newColor.rgb)
        }
    }
}

/**
 * Counts the given color in the image
 *
 * @param color Color to count
 * @return Number of times this color appeared
 */
fun BufferedImage.countColor(color: Color) = run {
    var count = 0
    for (x in 0 until width) {
        for (y in 0 until height) {
            if (getRGB(x, y) == color.rgb) count++
        }
    }
    count
}

/**
 * Gets the relative luminance of a color
 *
 * @return Relative luminance
 */
fun Color.relativeLuminance() = (0.2126 * red + 0.7152 * green + 0.0722 * blue) / 255


/**
 * Pads an image with given pixel amount on both sides
 *
 * @param pixelW Amount of pixels to pad the image on the left and right side
 * @param pixelH Amount of pixels to pad the image on the top and bottom side
 * @param color Color to fill the padded region with, Translucent if null (Default)
 */
fun BufferedImage.pad(pixelW: Int, pixelH: Int, color: Color? = null): BufferedImage {
    val newW = width + pixelW
    val newH = height + pixelH
    val image = createCompatibleCopy(newW, newH)
    (image.graphics as Graphics2D).apply {
        if (color != null) {
            paint = color
            fillRect(0, 0, newW, newH)
        }
        drawImage(this@pad, pixelW / 2, pixelH / 2, width, height, null)
        dispose()
    }
    return image
}

/**
 * Creates a new buffered image with the same attributes but different width and height
 * Use this for custom type buffered images
 *
 * @param width Width of new buffered image, defaults to original width
 * @param height Height of new buffered image, defaults to original height
 */
fun BufferedImage.createCompatibleCopy(
    width: Int = this.width,
    height: Int = this.height
): BufferedImage {
    return BufferedImage(
        colorModel,
        raster.createCompatibleWritableRaster(width, height),
        isAlphaPremultiplied,
        null
    )
}

object ImageUtils {
    fun createByteRGBBufferedImage(
        width: Int,
        height: Int,
        hasAlpha: Boolean = false
    ): BufferedImage {
        val cs = ColorSpace.getInstance(ColorSpace.CS_sRGB)
        val cm: ColorModel
        val raster: WritableRaster
        if (hasAlpha) {
            cm = ComponentColorModel(cs, true, false, ColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE)
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height, 4, null)
        } else {
            cm = ComponentColorModel(cs, false, false, ColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE)
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height, 3, null)
        }
        return BufferedImage(cm, raster, false, null)
    }
}