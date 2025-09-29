package com.edts.components.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes

val Int.dpToPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Int.pxToDp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.spToPx: Int get() = (this * Resources.getSystem().displayMetrics.scaledDensity).toInt()
val Int.pxToSp: Int get() = (this / Resources.getSystem().displayMetrics.scaledDensity).toInt()

val Float.dpToPx: Float get() = (this * Resources.getSystem().displayMetrics.density)
val Float.pxToDp: Float get() = (this / Resources.getSystem().displayMetrics.density)

val Float.spToPx: Float get() = (this * Resources.getSystem().displayMetrics.scaledDensity)
val Float.pxToSp: Float get() = (this / Resources.getSystem().displayMetrics.scaledDensity)

fun Context?.resolveStyleAttribute(@AttrRes attributeId: Int, @StyleRes fallbackStyle: Int): Int {
    val typedValue = TypedValue()
    return if (this?.theme?.resolveAttribute(attributeId, typedValue, true) == true) {
        typedValue.resourceId
    } else {
        fallbackStyle
    }
}

fun Context?.resolveColorAttribute(@AttrRes attrRes: Int, @ColorRes fallbackColor: Int): Int {
    val typedValue = TypedValue()
    return if (this?.theme?.resolveAttribute(attrRes, typedValue, true) == true) {
        typedValue.data
    } else {
        fallbackColor
    }
}