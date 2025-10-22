package com.edts.components.header

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.databinding.HeaderBinding

class Header @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: HeaderBinding =
        HeaderBinding.inflate(LayoutInflater.from(context), this, true)

    private val colorCache = mutableMapOf<Int, Int>()

    var delegate: HeaderDelegate? = null

    var showLeftButton: Boolean = true
        set(value) {
            field = value
            binding.ivLeftBtn.visibility = if (value) View.VISIBLE else View.GONE
            updateTitleStyle()
        }

    var sectionTitleText: String = ""
        set(value) {
            field = value
            binding.tvSectionTitle.text = value
            updateTitleStyle()
        }

    var showSectionTitle: Boolean = true
        set(value) {
            field = value
            binding.tvSectionTitle.visibility = if (value) View.VISIBLE else View.GONE
        }

    var sectionSubtitleText: String = ""
        set(value) {
            field = value
            binding.tvSectionSubtitle.text = value
        }

    var showSectionSubtitle: Boolean = true
        set(value) {
            field = value
            binding.tvSectionSubtitle.visibility = if (value) View.VISIBLE else View.GONE
        }

    var showRightButton: Boolean = true
        set(value) {
            field = value
            binding.ivRightBtn.visibility = if (value) View.VISIBLE else View.GONE
        }

    var rightButtonSrc: Int = -1
        set(@DrawableRes value) {
            field = value
            if (value != -1) {
                binding.ivRightBtn.setImageResource(value)
            }
        }

    init {
        setupClickListeners()

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Header,
            0, 0
        ).apply {
            try {
                showLeftButton = getBoolean(R.styleable.Header_showLeftButton, true)
                sectionTitleText = getString(R.styleable.Header_sectionTitleText) ?: ""
                showSectionTitle = getBoolean(R.styleable.Header_showSectionTitle, true)
                sectionSubtitleText = getString(R.styleable.Header_sectionSubtitleText) ?: ""
                showSectionSubtitle = getBoolean(R.styleable.Header_showSectionSubtitle, true)
                showRightButton = getBoolean(R.styleable.Header_showRightButton, true)

                val rightButtonDrawableRes = getResourceId(R.styleable.Header_rightButtonSrc, -1)

                if (rightButtonDrawableRes != -1) {
                    rightButtonSrc = rightButtonDrawableRes
                }

                updateTitleStyle()
            } finally {
                recycle()
            }
        }
    }

    private fun updateTitleStyle() {
        val shouldUseLargerStyle = !showLeftButton && !showSectionSubtitle

        if (shouldUseLargerStyle) {
            val typedValue = TypedValue()
            if (context.theme.resolveAttribute(R.attr.d3SemiBold, typedValue, true)) {
                binding.tvSectionTitle.setTextAppearance(typedValue.resourceId)
            }
        } else {
            val typedValue = TypedValue()
            if (context.theme.resolveAttribute(R.attr.h1SemiBold, typedValue, true)) {
                binding.tvSectionTitle.setTextAppearance(typedValue.resourceId)
            }
        }
    }

    private fun setupClickListeners() {
        binding.ivLeftBtn.setOnClickListener {
            handleLeftButtonClick()
        }
        binding.ivLeftBtn.background = createRippleDrawable()

        binding.ivRightBtn.setOnClickListener {
            handleRightButtonClick()
        }
        binding.ivRightBtn.background = createRippleDrawable()
    }

    private fun createRippleDrawable(): RippleDrawable {
        val rippleColor = ColorStateList.valueOf(getCachedColor(R.attr.colorBackgroundModifierOnPress))
        return RippleDrawable(rippleColor, null, null).apply {
            radius = (16 * Resources.getSystem().displayMetrics.density).toInt()
        }
    }

    private fun getCachedColor(@AttrRes colorAttr: Int): Int {
        return colorCache.getOrPut(colorAttr) {
            resolveColorAttribute(colorAttr)
        }
    }

    private fun resolveColorAttribute(colorRes: Int): Int {
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(colorRes, typedValue, true)) {
            if (typedValue.resourceId != 0) {
                ContextCompat.getColor(context, typedValue.resourceId)
            } else {
                typedValue.data
            }
        } else {
            try {
                ContextCompat.getColor(context, colorRes)
            } catch (e: Exception) {
                colorRes
            }
        }
    }

    private fun handleLeftButtonClick() {
        delegate?.onLeftButtonClicked()
    }

    private fun handleRightButtonClick() {
        delegate?.onRightButtonClicked()
    }
}