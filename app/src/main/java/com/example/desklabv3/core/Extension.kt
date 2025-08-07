package com.example.desklabv3.core

import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.widget.TextView

// Converts HTML String to Spanned (styled text)
fun String.toSpanned(): Spanned {
    val processedHtml = this.processNumberedLists()
    return Html.fromHtml(processedHtml, Html.FROM_HTML_MODE_COMPACT)
}

// Helper function to map numbered lists (unsupported) styled text
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

// Sets HTML-styled text and makes links clickable
fun TextView.setHtml(html: String) {
    text = html.toSpanned()
    movementMethod = LinkMovementMethod.getInstance()
}