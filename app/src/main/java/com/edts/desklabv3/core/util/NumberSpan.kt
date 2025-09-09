package com.edts.desklabv3.core.util

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.text.Layout
import android.text.ParcelableSpan
import android.text.Spanned
import android.text.style.LeadingMarginSpan
import androidx.annotation.ColorInt
import androidx.annotation.Px

class NumberSpan(
    private val number: Int,
    @Px private val gapWidth: Int = 8,
    @ColorInt private val color: Int = 0,
    private val wantColor: Boolean = color != 0,
    private val maxNumberInList: Int = 99
) : LeadingMarginSpan, ParcelableSpan {

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<NumberSpan> = object : Parcelable.Creator<NumberSpan> {
            override fun createFromParcel(parcel: Parcel) = NumberSpan(parcel)
            override fun newArray(size: Int): Array<NumberSpan?> = arrayOfNulls(size)
        }
    }

    private constructor(src: Parcel) : this(
        src.readInt(), src.readInt(), src.readInt(), src.readInt() != 0, src.readInt()
    )

    override fun getSpanTypeId(): Int = 103
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(number)
        dest.writeInt(gapWidth)
        dest.writeInt(color)
        dest.writeInt(if (wantColor) 1 else 0)
        dest.writeInt(maxNumberInList)
    }

    override fun getLeadingMargin(first: Boolean): Int {
        val paint = Paint().apply {
            textSize = 42f
            typeface = Typeface.DEFAULT
        }

        val maxNumberText = "$maxNumberInList. "
        return paint.measureText(maxNumberText).toInt() + gapWidth
    }

    override fun drawLeadingMargin(
        canvas: Canvas, paint: Paint, x: Int, dir: Int, top: Int, baseline: Int,
        bottom: Int, text: CharSequence, start: Int, end: Int, first: Boolean, layout: Layout?
    ) {
        if (first && text is Spanned && text.getSpanStart(this) == start) {
            val originalColor = paint.color
            if (wantColor) paint.color = this.color

            val numberText = "$number. "
            val numberWidth = paint.measureText(numberText)
            val maxNumberText = "$maxNumberInList. "
            val maxWidth = paint.measureText(maxNumberText)

            val xPosition = if (dir > 0) {
                x + (maxWidth - numberWidth)
            } else {
                x - maxWidth
            }

            canvas.drawText(numberText, xPosition, baseline.toFloat(), paint)
            if (wantColor) paint.color = originalColor
        }
    }
}