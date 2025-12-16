package com.edts.components.header

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.databinding.HeaderBinding
import com.edts.components.tab.Tab
import com.edts.components.utils.resolveColorAttr
import com.google.android.material.card.MaterialCardView

class Header @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding: HeaderBinding =
        HeaderBinding.inflate(LayoutInflater.from(context), this, true)

    private val colorCache = mutableMapOf<Int, Int>()

    var delegate: HeaderDelegate? = null

    var showLeftButton: Boolean = true
        set(value) {
            field = value
            binding.ivLeftBtn.visibility = if (value) View.VISIBLE else View.GONE
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

    var showShadow: Boolean = false
        set(value) {
            field = value
            applyShadowState()
        }

    var showBackgroundColor: Boolean = false
        set(value) {
            field = value
            applyBackgroundState()
        }

    var showTab: Boolean = false
        set(value) {
            field = value
            applyTabState()
        }

    val tabView: Tab
        get() = binding.cvTabEventList

    init {
        setupClickListeners()

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Header,
            0, 0
        ).apply {
            try {
                clipChildren = false
                clipToPadding = false
                clipToOutline = false

                showLeftButton = getBoolean(R.styleable.Header_showLeftButton, true)
                sectionTitleText = getString(R.styleable.Header_sectionTitleText) ?: ""
                showSectionTitle = getBoolean(R.styleable.Header_showSectionTitle, true)
                sectionSubtitleText = getString(R.styleable.Header_sectionSubtitleText) ?: ""
                showSectionSubtitle = getBoolean(R.styleable.Header_showSectionSubtitle, true)
                showRightButton = getBoolean(R.styleable.Header_showRightButton, true)
                showShadow = getBoolean(R.styleable.Header_showShadow, false)
                showBackgroundColor = getBoolean(R.styleable.Header_showBackgroundColor, false)
                showTab = getBoolean(R.styleable.Header_showTab, false)

                val rightButtonDrawableRes = getResourceId(R.styleable.Header_rightButtonSrc, -1)

                if (rightButtonDrawableRes != -1) {
                    rightButtonSrc = rightButtonDrawableRes
                }

                updateTitleStyle()
                applyShadowState()
                applyBackgroundState()
                applyTabState()
            } finally {
                recycle()
            }
        }
    }

    private fun applyTabState() {
        binding.cvTabEventList.visibility =
            if (showTab) View.VISIBLE else View.GONE
    }

    private fun applyShadowState() {
        if (showShadow) {
            binding.Header.cardElevation = (2 * Resources.getSystem().displayMetrics.density) // 2dp
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                outlineAmbientShadowColor = context.resolveColorAttr(
                    R.attr.colorShadowTintedAmbient,
                    R.color.kitColorBrandPrimaryA10
                )
                outlineSpotShadowColor = context.resolveColorAttr(
                    R.attr.colorShadowTintedKey,
                    R.color.kitColorBrandPrimaryA20
                )
            }
        } else {
            binding.Header.cardElevation = 0f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                outlineAmbientShadowColor = Color.TRANSPARENT
                outlineSpotShadowColor = Color.TRANSPARENT
            }
        }
    }

    private fun applyBackgroundState() {
        if (showBackgroundColor) {
            binding.Header.setCardBackgroundColor(getCachedColor(R.attr.colorBackgroundPrimary))
        } else {
            binding.Header.setCardBackgroundColor(android.graphics.Color.TRANSPARENT)
        }
    }

    private fun updateTitleStyle() {
        val typedValue = TypedValue()
        if (context.theme.resolveAttribute(R.attr.h1SemiBold, typedValue, true)) {
            binding.tvSectionTitle.setTextAppearance(typedValue.resourceId)
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