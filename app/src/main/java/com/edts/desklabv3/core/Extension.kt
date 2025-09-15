package com.edts.desklabv3.core

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

fun String.toSpanned(): Spanned {
    val processedHtml = this.processNumberedLists()
    return Html.fromHtml(processedHtml, Html.FROM_HTML_MODE_COMPACT)
}

private fun String.processNumberedLists(): String {
    return this.replace(Regex("<ol[^>]*>([\\s\\S]*?)</ol>")) { matchResult ->
        val listContent = matchResult.groupValues[1]
        val listItems = listContent.split(Regex("<li[^>]*>"))
            .drop(1) // Remove empty first element
            .mapIndexed { index, item ->
                val cleanItem = item.replace("</li>", "").trim()
                "${index + 1}. $cleanItem"
            }
        listItems.joinToString("<br>")
    }
}

fun TextView.setHtml(html: String) {
    text = html.toSpanned()
    movementMethod = LinkMovementMethod.getInstance()
}

fun Context?.font(@FontRes fontRes: Int) =
    this?.let { ResourcesCompat.getFont(it, fontRes) }

fun Context?.colorAttr(attributedId: Int): Int {
    val typedValue = TypedValue()
    this?.theme?.resolveAttribute(attributedId, typedValue, true)
    return if (typedValue.type == TypedValue.TYPE_REFERENCE)
        color(typedValue.resourceId)
    else
        typedValue.data
}

fun Context?.color(@ColorRes colorRes: Int) =
    this?.let { ContextCompat.getColor(it, colorRes) } ?: Color.TRANSPARENT

val Int.px: Float get() = (this / Resources.getSystem().displayMetrics.density)
val Int.dp: Float get() = (this * Resources.getSystem().displayMetrics.density)
val Int.sp: Float get() = (this / Resources.getSystem().displayMetrics.scaledDensity)

val Float.px: Float get() = (this / Resources.getSystem().displayMetrics.density)
val Float.dp: Float get() = (this * Resources.getSystem().displayMetrics.density)
val Float.sp: Float get() = (this / Resources.getSystem().displayMetrics.scaledDensity)