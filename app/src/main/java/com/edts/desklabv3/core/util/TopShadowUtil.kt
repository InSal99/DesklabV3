package com.edts.desklabv3.core.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LayerDrawable
import androidx.core.content.ContextCompat
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttribute
import com.edts.desklabv3.R

// Solution 1: Extend shadow outside the container bounds
fun Context.createTopShadowBackground(
    fillColor: Int = resolveColorAttribute(android.R.attr.colorBackground, Color.WHITE),
    shadowHeightDp: Int = 12
): Drawable {
    val layers = arrayOfNulls<Drawable>(2)

    // Create a shadow that extends beyond the content bounds
    val shadowDrawable = GradientDrawable(
        GradientDrawable.Orientation.TOP_BOTTOM,
        intArrayOf(
            resolveColorAttribute(com.edts.components.R.attr.colorShadowTintedAmbient, Color.TRANSPARENT),
            resolveColorAttribute(com.edts.components.R.attr.colorShadowTintedKey, Color.TRANSPARENT),
            Color.TRANSPARENT
        )
    ).apply {
        shape = GradientDrawable.RECTANGLE
        gradientType = GradientDrawable.LINEAR_GRADIENT
    }

    // Content background with rounded corners
    val contentDrawable = GradientDrawable().apply {
        cornerRadii = floatArrayOf(
            12.dpToPx.toFloat(), 12.dpToPx.toFloat(), // top left/right
            12.dpToPx.toFloat(), 12.dpToPx.toFloat(), // top left/right
            0f, 0f,                                   // bottom left/right
            0f, 0f
        )
        setColor(fillColor)
    }

    // Use InsetDrawable to position shadow above the content
    val shadowWithInset = InsetDrawable(shadowDrawable, 0, -shadowHeightDp.dpToPx, 0, 0)

    layers[0] = contentDrawable  // Content background
    layers[1] = shadowWithInset  // Shadow positioned above

    return LayerDrawable(layers)
}



//fun Context.createTopShadowBackground(
//    fillColor: Int = resolveColorAttribute(android.R.attr.colorBackground, Color.WHITE),
//    shadowHeightDp: Int = 12
//): Drawable {
//    val layers = arrayOfNulls<Drawable>(2)
//
//    // Top shadow gradient (fades downward within the view)
//    val shadowDrawable = GradientDrawable(
//        GradientDrawable.Orientation.TOP_BOTTOM,
//        intArrayOf(
//            resolveColorAttribute(com.edts.components.R.attr.colorShadowTintedAmbient, Color.TRANSPARENT),
//            resolveColorAttribute(com.edts.components.R.attr.colorShadowTintedKey, Color.TRANSPARENT),
//            Color.TRANSPARENT
//        )
//    ).apply {
//        shape = GradientDrawable.RECTANGLE
//        gradientType = GradientDrawable.LINEAR_GRADIENT
//        setSize(0, shadowHeightDp.dpToPx) // shadow thickness
//    }
//
//    // Content background with rounded corners
//    val contentDrawable = GradientDrawable().apply {
//        cornerRadii = floatArrayOf(
//            12.dpToPx.toFloat(), 12.dpToPx.toFloat(), // top left/right
//            12.dpToPx.toFloat(), 12.dpToPx.toFloat(), // top left/right
//            0f, 0f,                                   // bottom left/right
//            0f, 0f
//        )
//        setColor(fillColor)
//    }
//
//    // Stack: shadow first (on top region), then content
//    layers[0] = shadowDrawable
//    layers[1] = contentDrawable
//
//    return LayerDrawable(layers)
//}




//fun Context.createTopShadowBackground(
//    fillColor: Int = resolveColorAttribute(android.R.attr.colorBackground, Color.WHITE)
//): Drawable {
//    val layers = arrayOfNulls<Drawable>(2)
//
//    val shadowDrawable = GradientDrawable(
//        GradientDrawable.Orientation.TOP_BOTTOM,
//        intArrayOf(
//            resolveColorAttribute(com.edts.components.R.attr.colorShadowTintedAmbient, Color.TRANSPARENT),
//            resolveColorAttribute(com.edts.components.R.attr.colorShadowTintedKey, Color.TRANSPARENT),
//            Color.TRANSPARENT
//        )
//    ).apply {
//        cornerRadii = FloatArray(8) { 0f }
//    }
//
//    val contentDrawable = GradientDrawable().apply {
//        cornerRadii = floatArrayOf(
//            12.dpToPx.toFloat(), 12.dpToPx.toFloat(),
//            12.dpToPx.toFloat(), 12.dpToPx.toFloat(),
//            0f, 0f,
//            0f, 0f
//        )
//        setColor(fillColor)
//    }
//
//    layers[0] = InsetDrawable(shadowDrawable, 0, 0.dpToPx, 0, 0)   // shadow sits "on top"
//    layers[1] = contentDrawable  // content background
//
//    return LayerDrawable(layers)
//}






//fun Context.createTopShadowBackground(): Drawable {
//    val layers = arrayOfNulls<Drawable>(2)
//
//    val shadowDrawable = GradientDrawable(
//        GradientDrawable.Orientation.TOP_BOTTOM,
//        intArrayOf(
//            Color.TRANSPARENT,
//            resolveColorAttribute(com.edts.components.R.attr.colorShadowTintedAmbient, Color.TRANSPARENT),
//            resolveColorAttribute(com.edts.components.R.attr.colorShadowTintedKey, Color.TRANSPARENT),
//            resolveColorAttribute(com.edts.components.R.attr.colorShadowTintedKey, Color.TRANSPARENT)
//        )
//    )
//
//    val contentDrawable = GradientDrawable().apply {
//        cornerRadii = floatArrayOf(
//            12.dpToPx.toFloat(), 12.dpToPx.toFloat(),
//            12.dpToPx.toFloat(), 12.dpToPx.toFloat(),
//            0f, 0f,
//            0f, 0f
//        )
//        setColor(ContextCompat.getColor(this@createTopShadowBackground, android.R.color.transparent))
//    }
//
//    layers[0] = shadowDrawable
//    layers[1] = InsetDrawable(contentDrawable, 0, 8.dpToPx, 0, 0)
//
//    return LayerDrawable(layers)
//}
