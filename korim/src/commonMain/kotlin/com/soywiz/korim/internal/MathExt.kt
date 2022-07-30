package com.soywiz.korim.internal

import com.soywiz.kmem.clamp01
import kotlin.math.ceil
import kotlin.math.floor

internal fun packIntUnchecked(r: Int, g: Int, b: Int, a: Int): Int =
    (((r and 0xFF) shl 0) or ((g and 0xFF) shl 8) or ((b and 0xFF) shl 16) or ((a and 0xFF) shl 24))

internal fun packIntClamped(r: Int, g: Int, b: Int, a: Int): Int =
    r.clamp0_255() or (g.clamp0_255() shl 8) or (b.clamp0_255() shl 16) or (a.clamp0_255() shl 24)

internal fun d2i(v: Double): Int = (v.clamp01() * 255).toInt()
internal fun f2i(v: Float): Int = (v.clamp01() * 255).toInt()

internal fun floorCeil(v: Double): Double = if (v < 0.0) ceil(v) else floor(v)












