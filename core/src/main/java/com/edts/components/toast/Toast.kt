package com.edts.components.toast

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.appcompat.view.ContextThemeWrapper
import com.edts.components.R
import com.edts.components.databinding.LayoutToastViewBinding
import com.edts.components.utils.resolveColorAttr
import com.google.android.material.card.MaterialCardView

class Toast @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding: LayoutToastViewBinding
    private var toastType: Type = Type.GENERAL
    private var toastMessage: String = ""
    var toastIcon: Int? = null

    var onToastClickListener: (() -> Unit)? = null

    enum class Type(@DrawableRes val iconRes: Int, @AttrRes val colorAttr: Int) {
        SUCCESS(R.drawable.ic_success, R.attr.colorBackgroundSuccessIntense),
        ERROR(R.drawable.ic_attention, R.attr.colorBackgroundAttentionIntense),
        INFO(R.drawable.ic_information, R.attr.colorBackgroundInfoIntense),
        GENERAL(R.drawable.placeholder, R.attr.colorBackgroundPrimaryInverse)
    }

    init {
        val themedContext = ContextThemeWrapper(context, R.style.Theme_Desklab_Kit)
        binding = LayoutToastViewBinding.inflate(LayoutInflater.from(themedContext), this, true)

        setupToast()
        applyAttrs(attrs)
        applyToastStyle()
    }

    private fun applyAttrs(attrs: AttributeSet?) {
        attrs ?: return
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomToast,
            0, 0
        ).apply {
            try {
                val typeIndex = getInt(R.styleable.CustomToast_toastType, 3)
                toastType = Type.values()[typeIndex]
                toastMessage = getString(R.styleable.CustomToast_toastMessage) ?: ""
            } finally {
                recycle()
            }
        }
    }

    private fun setupToast() {
        radius = context.resources.getDimensionPixelSize(R.dimen.radius_12dp).toFloat()
        cardElevation = 2f * context.resources.displayMetrics.density
        strokeWidth = context.resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp)
        strokeColor = context.resolveColorAttr(R.attr.colorStrokeInteractive, R.color.colorOpacityWhite20)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            outlineAmbientShadowColor = context.resolveColorAttr(
                R.attr.colorShadowNeutralAmbient,
                R.color.colorGreen50
            )
            outlineSpotShadowColor = context.resolveColorAttr(
                R.attr.colorShadowNeutralKey,
                R.color.colorGreen50
            )
        }
    }

    private fun applyToastStyle() {
        setCardBackgroundColor(context.resolveColorAttr(toastType.colorAttr, R.color.colorFFF))
        binding.ivIcon.setImageResource(toastIcon ?: toastType.iconRes)
        binding.tvMessage.text = toastMessage
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
                leftMargin = context.resources.getDimensionPixelSize(R.dimen.margin_16dp)
                rightMargin = context.resources.getDimensionPixelSize(R.dimen.margin_16dp)
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