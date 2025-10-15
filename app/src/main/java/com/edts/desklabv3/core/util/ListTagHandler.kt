package com.edts.desklabv3.core.util

import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.style.BulletSpan
import android.text.style.LeadingMarginSpan
import android.util.TypedValue
import android.widget.TextView
import org.xml.sax.XMLReader
import java.util.Stack

class ListTagHandler(
    private val textView: TextView,
    private val bulletIndentDp: Int = 14,
    private val numberIndentDp: Int = 2,
    private val bulletGapDp: Int = 12,
    private val numberGapDp: Int = 4,
) : Html.TagHandler {
    private val listStack = Stack<String>()
    private val olCounters = Stack<Int>()
    private val liStartIndex = Stack<Int>()

    private fun dpToPx(dp: Int): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), textView.resources.displayMetrics).toInt()

    override fun handleTag(opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader) {
        val t = tag.lowercase().trim()
        when (t) {
            "myul" -> {
                if (opening) listStack.push("ul") else if (listStack.isNotEmpty()) listStack.pop()
            }
            "myol" -> {
                if (opening) {
                    listStack.push("ol")
                    olCounters.push(0)
                } else {
                    if (listStack.isNotEmpty()) listStack.pop()
                    if (olCounters.isNotEmpty()) olCounters.pop()
                }
            }
            "myli" -> {
                if (opening) {
                    liStartIndex.push(output.length)
                } else {
                    if (liStartIndex.isEmpty()) return
                    val start = liStartIndex.pop()
                    var end = output.length

                    val baseIndent = dpToPx(bulletIndentDp)
                    val numberIndent = dpToPx(numberIndentDp)
                    val listType = if (listStack.isNotEmpty()) listStack.peek() else "ul"

                    if (end == 0 || output[end - 1] != '\n') {
                        output.insert(end, "\n")
                        end = output.length
                    }

                    if (listType == "ul") {
                        output.setSpan(LeadingMarginSpan.Standard(baseIndent, baseIndent), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        val gap = dpToPx(bulletGapDp)
                        output.setSpan(BulletSpan(gap), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    } else {
                        val count = if (olCounters.isNotEmpty()) {
                            val top = olCounters.pop() + 1
                            olCounters.push(top)
                            top
                        } else 1

                        output.setSpan(LeadingMarginSpan.Standard(numberIndent, numberIndent), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                        val numberGap = dpToPx(numberGapDp)
                        output.setSpan(NumberSpan(count, numberGap), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
            }
        }
    }
}