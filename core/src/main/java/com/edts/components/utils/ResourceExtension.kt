package com.edts.components.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.edts.components.R

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

fun Context?.resolveColorAttr(@AttrRes attrRes: Int, @ColorRes fallbackColor: Int): Int {
    val typedValue = TypedValue()
    return if (this?.theme?.resolveAttribute(attrRes, typedValue, true) == true) {
        if (typedValue.type == TypedValue.TYPE_REFERENCE) {
            ContextCompat.getColor(this, typedValue.resourceId)
        } else {
            typedValue.data
        }
    } else {
        this?.let { ContextCompat.getColor(it, fallbackColor) } ?: Color.TRANSPARENT
    }
}

fun Context?.resolveStyleAttr(
    @AttrRes attributeId: Int,
    @StyleRes fallbackStyle: Int
): Int {
    val typedValue = TypedValue()
    return if (this?.theme?.resolveAttribute(attributeId, typedValue, true) == true) {
        typedValue.resourceId
    } else {
        fallbackStyle
    }
}


fun ImageView.loadImageDynamic(
    imageUrl: String? = null,
    imageRes: Int? = null,
    placeholderRes: Int = R.drawable.avatar_placeholder
) {
    val request = when {
        !imageUrl.isNullOrBlank() -> {
            Glide.with(this.context)
                .load(imageUrl)
        }
        imageRes != null -> {
            Glide.with(this.context)
                .load(imageRes)
        }
        else -> {
            Glide.with(this.context)
                .load(placeholderRes)
        }
    }

    request
        .placeholder(placeholderRes)
        .centerCrop()
        .into(this)
}