package com.example.components

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes

class CustomToast private constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var toastType: Type = Type.GENERAL
    private var toastMessage: String = ""

    enum class Type(@DrawableRes val iconRes: Int, @ColorRes val colorRes: Int) {
        SUCCESS(R.drawable.placeholder, R.color.success_color),
        ERROR(R.drawable.placeholder, R.color.error_color),
        INFO(R.drawable.placeholder, R.color.info_color),
        GENERAL(R.drawable.placeholder, R.color.general_color)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_toast_view, this, true)

        // Read custom attributes if provided
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomToast,
            0, 0
        ).apply {
            try {
                val typeIndex = getInt(R.styleable.CustomToast_toastType, 3) // Default to GENERAL (index 3)
                toastType = Type.values()[typeIndex]
                toastMessage = getString(R.styleable.CustomToast_toastMessage) ?: ""

                applyToastStyle()
            } finally {
                recycle()
            }
        }
    }

    private fun applyToastStyle() {
        // Set single background with custom background color and uniform stroke
        val drawable = ContextCompat.getDrawable(context, R.drawable.bg_toast)?.mutate() as? GradientDrawable
        drawable?.apply {
            setColor(ContextCompat.getColor(context, toastType.colorRes))
            setStroke(2, ContextCompat.getColor(context, R.color.toast_stroke_color)) // Same stroke for all
        }
        background = drawable

        findViewById<AppCompatImageView>(R.id.iv_icon).setImageResource(toastType.iconRes)
        findViewById<AppCompatTextView>(R.id.tv_message).text = toastMessage
    }

    fun setToast(type: Type, message: String) {
        toastType = type
        toastMessage = message
        applyToastStyle()
    }

    fun showToast() {
        android.widget.Toast(context).apply {
            setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 0)
            duration = android.widget.Toast.LENGTH_LONG
            view = this@CustomToast
            show()
        }
    }

    companion object {
        fun success(context: Context, message: String) =
            CustomToast(context).apply {
                setToast(Type.SUCCESS, message)
            }

        fun error(context: Context, message: String) =
            CustomToast(context).apply {
                setToast(Type.ERROR, message)
            }

        fun info(context: Context, message: String) =
            CustomToast(context).apply {
                setToast(Type.INFO, message)
            }

        fun general(context: Context, message: String) =
            CustomToast(context).apply {
                setToast(Type.GENERAL, message)
            }
    }
}