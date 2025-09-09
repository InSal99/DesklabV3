package com.edts.components.toast

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.google.android.material.card.MaterialCardView

class Toast @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private var toastType: Type = Type.GENERAL
    private var toastMessage: String = ""
    private val iconView: AppCompatImageView by lazy { findViewById(R.id.iv_icon) }
    private val messageView: AppCompatTextView by lazy { findViewById(R.id.tv_message) }

    enum class Type(@DrawableRes val iconRes: Int, @AttrRes val colorAttr: Int) {
        SUCCESS(R.drawable.placeholder, R.attr.colorBackgroundSuccessIntense),
        ERROR(R.drawable.placeholder, R.attr.colorBackgroundAttentionIntense),
        INFO(R.drawable.placeholder, R.attr.colorBackgroundInfoIntense),
        GENERAL(R.drawable.placeholder, R.attr.colorBackgroundPrimaryInverse)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_toast_view, this, true)
        setupToast()

        if (attrs != null) {
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CustomToast,
                0, 0
            ).apply {
                try {
                    val typeIndex = getInt(R.styleable.CustomToast_toastType, 3)
                    toastType = Type.values()[typeIndex]
                    toastMessage = getString(R.styleable.CustomToast_toastMessage) ?: ""
                } catch (e: Exception) {
                    e.printStackTrace()
                    toastType = Type.GENERAL
                    toastMessage = ""
                } finally {
                    recycle()
                }
            }
        }

        applyToastStyle()
    }

    private fun setupToast() {
        radius = resources.getDimensionPixelSize(R.dimen.radius_12dp).toFloat()
        cardElevation = 2f * context.resources.displayMetrics.density
        strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp)
        strokeColor = resolveColorAttribute(R.attr.colorStrokeInteractive, R.color.colorOpacityWhite20)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            outlineAmbientShadowColor = resolveColorAttribute(
                R.attr.colorShadowNeutralAmbient,
                R.color.colorGreen50
            )
            outlineSpotShadowColor = resolveColorAttribute(
                R.attr.colorShadowNeutralKey,
                R.color.colorGreen50
            )
        }
    }

    private fun applyToastStyle() {
        setCardBackgroundColor(resolveColorAttribute(toastType.colorAttr, R.color.colorFFF))
        iconView.setImageResource(toastType.iconRes)
        messageView.text = toastMessage
    }

    private fun resolveColorAttribute(@AttrRes attrRes: Int, @ColorRes fallbackColor: Int): Int {
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(attrRes, typedValue, true)) {
            if (typedValue.type == TypedValue.TYPE_REFERENCE) {
                ContextCompat.getColor(context, typedValue.resourceId)
            } else {
                typedValue.data
            }
        } else {
            ContextCompat.getColor(context, fallbackColor)
        }
    }

    fun setToast(type: Type, message: String) {
        toastType = type
        toastMessage = message
        applyToastStyle()
    }

    fun showIn(parent: ViewGroup) {
        (parent.findViewWithTag<View>("custom_snackbar") as? Toast)?.let {
            parent.removeView(it)
        }

        tag = "custom_snackbar"

        val bottomMarginDp = (100 * context.resources.displayMetrics.density).toInt()

        parent.addView(
            this,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.BOTTOM
                bottomMargin = bottomMarginDp
                leftMargin = resources.getDimensionPixelSize(R.dimen.margin_16dp)
                rightMargin = resources.getDimensionPixelSize(R.dimen.margin_16dp)
            }
        )

        measure(
            View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.AT_MOST),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        val viewHeight = measuredHeight
        translationY = viewHeight.toFloat() + bottomMarginDp.toFloat()

        animate()
            .translationY(0f)
            .setDuration(300)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {
                postDelayed({
                    animate()
                        .translationY(viewHeight.toFloat() + bottomMarginDp.toFloat())
                        .setDuration(300)
                        .setInterpolator(AccelerateInterpolator())
                        .withEndAction { parent.removeView(this) }
                }, 2500)
            }
            .start()
    }

    companion object {
        fun success(context: Context, message: String) {
            show(context, Type.SUCCESS, message)
        }

        fun error(context: Context, message: String) {
            show(context, Type.ERROR, message)
        }

        fun info(context: Context, message: String) {
            show(context, Type.INFO, message)
        }

        fun general(context: Context, message: String) {
            show(context, Type.GENERAL, message)
        }

        private fun show(context: Context, type: Type, message: String) {
            val toast = Toast(context)
            toast.setToast(type, message)

            val parent = (context as? Activity)
                ?.findViewById<ViewGroup>(android.R.id.content)
                ?: return

            toast.showIn(parent)
        }
    }
}