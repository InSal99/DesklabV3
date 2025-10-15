package com.edts.desklabv3.core.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region
import android.graphics.Shader
import android.graphics.drawable.Drawable
import com.edts.components.R
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttribute

fun Context.createTopShadowBackgroundCustom(
    fillColor: Int = resolveColorAttribute(android.R.attr.colorBackground, R.color.colorFFF),
    shadowOffsetDp: Int = 4
): Drawable {
    val shadowOffsetPx = shadowOffsetDp.dpToPx.toFloat()
    val cornerRadius = 12.dpToPx.toFloat()

    return object : Drawable() {
        private val contentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = fillColor
        }

        private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = resolveColorAttribute(R.attr.colorStrokeSubtle, R.color.colorNeutral30)
        }

        override fun draw(canvas: Canvas) {
            val bounds = bounds

            val contentRect = RectF(
                bounds.left.toFloat(),
                bounds.top.toFloat() + shadowOffsetPx,
                bounds.right.toFloat(),
                bounds.bottom.toFloat()
            )

            val strokeRect = RectF(
                bounds.left.toFloat(),
                bounds.top.toFloat() + shadowOffsetPx - 1.dpToPx,
                bounds.right.toFloat(),
                bounds.bottom.toFloat()
            )

            val shadowRect = RectF(
                bounds.left.toFloat(),
                bounds.top.toFloat(),
                bounds.right.toFloat(),
                bounds.bottom.toFloat(),
            )

            val shadowGradient = LinearGradient(
                0f, shadowRect.top,
                0f, shadowRect.bottom,
                intArrayOf(
                    Color.TRANSPARENT,
                    resolveColorAttribute(R.attr.colorShadowTintedKey, R.color.colorPrimaryOpacity20),
                    resolveColorAttribute(R.attr.colorShadowTintedKey, R.color.colorPrimaryOpacity20)
                ),
                floatArrayOf(0f, 0.05f, 1f),
                Shader.TileMode.CLAMP
            )
            shadowPaint.shader = shadowGradient

            val contentPath = Path().apply {
                addRoundRect(
                    contentRect,
                    floatArrayOf(
                        cornerRadius, cornerRadius,
                        cornerRadius, cornerRadius,
                        0f, 0f,
                        0f, 0f
                    ),
                    Path.Direction.CW
                )
            }

            val strokePath = Path().apply {
                addRoundRect(
                    strokeRect,
                    floatArrayOf(
                        cornerRadius, cornerRadius,
                        cornerRadius, cornerRadius,
                        0f, 0f,
                        0f, 0f
                    ),
                    Path.Direction.CW
                )
            }

            val save = canvas.save()
            canvas.clipPath(contentPath, Region.Op.DIFFERENCE)
            canvas.drawRect(shadowRect, shadowPaint)
            canvas.restoreToCount(save)

            canvas.drawPath(strokePath, strokePaint)
            canvas.drawPath(contentPath, contentPaint)
        }

        override fun getIntrinsicHeight(): Int = -1
        override fun setAlpha(alpha: Int) {
            contentPaint.alpha = alpha
            shadowPaint.alpha = alpha
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
            contentPaint.colorFilter = colorFilter
            shadowPaint.colorFilter = colorFilter
        }

        override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    }
}

fun Context.createBottomShadowBackgroundCustom(
    fillColor: Int = resolveColorAttribute(android.R.attr.colorBackground, R.color.colorFFF),
    shadowOffsetDp: Int = 8,
): Drawable {
    val shadowOffsetPx = shadowOffsetDp.dpToPx.toFloat()

    return object : Drawable() {
        private val contentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = fillColor
        }

        private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        private val padding = Rect(0, 0, 0, shadowOffsetPx.toInt())

        override fun draw(canvas: Canvas) {
            val bounds = bounds

            val contentRect = RectF(
                bounds.left.toFloat(),
                bounds.top.toFloat(),
                bounds.right.toFloat(),
                bounds.bottom.toFloat() - shadowOffsetPx
            )

            val shadowRect = RectF(
                bounds.left.toFloat(),
                contentRect.bottom,
                bounds.right.toFloat(),
                bounds.bottom.toFloat()
            )

            val shadowGradient = LinearGradient(
                0f, shadowRect.top,
                0f, shadowRect.bottom,
                intArrayOf(
                    resolveColorAttribute(R.attr.colorShadowNeutralKey, R.color.color000Opacity20),
                    resolveColorAttribute(R.attr.colorShadowNeutralAmbient, R.color.color000Opacity10),
                    resolveColorAttribute(R.attr.colorBackgroundPrimaryTinted, R.color.colorPrimary10)
                ),
                floatArrayOf(0f, 0.2f, 1f),
                Shader.TileMode.CLAMP
            )
            shadowPaint.shader = shadowGradient


            canvas.drawRect(contentRect, contentPaint)
            canvas.drawRect(shadowRect, shadowPaint)
        }

        override fun getPadding(padding: Rect): Boolean {
            padding.set(this.padding)
            return true
        }

        override fun getIntrinsicHeight(): Int = -1

        override fun setAlpha(alpha: Int) {
            contentPaint.alpha = alpha
            shadowPaint.alpha = alpha
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
            contentPaint.colorFilter = colorFilter
            shadowPaint.colorFilter = colorFilter
        }

        override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    }
}