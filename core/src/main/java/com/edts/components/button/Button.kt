package com.edts.components.button

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.edts.components.R
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttribute
import com.edts.components.utils.resolveStyleAttribute
import com.google.android.material.button.MaterialButton

class Button @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialButtonStyle
) : MaterialButton(context, attrs, defStyleAttr) {
    enum class ButtonSize(
        val heightDp: Int,
        val paddingHorizontalDp: Int,
        val iconSizeDp: Int,
        val textPaddingDp: Int
    ) {
        XS(28, 12, 16, 4),
        SM(36, 12, 20, 4),
        MD(40, 16, 24, 6),
        LG(48, 24, 24, 8)
    }

    enum class ButtonState { REST, ON_PRESS, ON_FOCUS }
    enum class ButtonType { PRIMARY, SECONDARY }
    enum class IconPlacement { LEFT, RIGHT }

    private var buttonType: ButtonType = ButtonType.PRIMARY
    private var buttonSize: ButtonSize = ButtonSize.MD
    private var buttonState: ButtonState = ButtonState.REST
    private var isButtonDisabled: Boolean = false
    private var isButtonDestructive: Boolean = false
    private var buttonIcon: Drawable? = null
    private var iconPlacement: IconPlacement = IconPlacement.LEFT
    private val strokeWidth: Int
    private val cornerRadius: Float
    private val colorCache = mutableMapOf<Int, Int>()
    private val drawablePool = mutableMapOf<String, Drawable>()
    private val density: Float = context.resources.displayMetrics.density

    var buttonDelegate: ButtonDelegate? = null

    init {
        strokeWidth = try {
            resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp)
        } catch (_: Exception) {
            1
        }

        cornerRadius = try {
            resources.getDimension(R.dimen.radius_999dp)
        } catch (_: Exception) {
            99999f
        }

        preloadColors()
        parseAttributes(attrs, defStyleAttr)
        setupButton()
    }

    private fun preloadColors() {
        val colorAttrs = arrayOf(
            R.attr.colorBackgroundDisabled to R.color.color000Opacity5,
            R.attr.colorStrokeDisabled to R.color.color000Opacity10,
            R.attr.colorForegroundDisabled to R.color.color000Opacity20,
            R.attr.colorBackgroundAttentionIntense to R.color.colorRed30,
            R.attr.colorStrokeInteractive to R.color.colorOpacityWhite20,
            R.attr.colorForegroundWhite to R.color.colorFFF,
            R.attr.colorBackgroundModifierOnPressIntense to R.color.color000Opacity12,
            R.attr.colorBackgroundAttentionSubtle to R.color.colorRed10,
            R.attr.colorStrokeAttentionIntense to R.color.colorRed50,
            R.attr.colorForegroundAttentionIntense to R.color.colorRed40,
            R.attr.colorBackgroundModifierOnPress to R.color.color000Opacity5,
            R.attr.colorBackgroundAccentPrimaryIntense to R.color.colorPrimary30,
            R.attr.colorStrokeFocus to R.color.colorPrimary50,
            R.attr.colorBackgroundPrimary to R.color.colorFFF,
            R.attr.colorStrokeAccent to R.color.colorPrimary30,
            R.attr.colorForegroundAccentPrimaryIntense to R.color.colorPrimary30
        )
        colorAttrs.forEach { (attr, fallback) ->
            colorCache[attr] = context.resolveColorAttribute(attr, fallback)
        }
    }

    private fun parseAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        attrs?.let { attributeSet ->
            try {
                context.withStyledAttributes(attributeSet, R.styleable.Button, defStyleAttr, 0) {
                    buttonType = ButtonType.values().getOrElse(
                        getInt(R.styleable.Button_type, ButtonType.PRIMARY.ordinal)
                    ) { ButtonType.PRIMARY }
                    buttonSize = ButtonSize.values().getOrElse(
                        getInt(R.styleable.Button_size, ButtonSize.MD.ordinal)
                    ) { ButtonSize.MD }
                    buttonState = ButtonState.values().getOrElse(
                        getInt(R.styleable.Button_state, ButtonState.REST.ordinal)
                    ) { ButtonState.REST }
                    isButtonDisabled = getBoolean(R.styleable.Button_isButtonDisabled, false)
                    isButtonDestructive = getBoolean(R.styleable.Button_isButtonDestructive, false)
                    buttonIcon = getDrawable(R.styleable.Button_icon)
                        ?: getDrawable(R.styleable.Button_android_icon)
                    iconPlacement = IconPlacement.values().getOrElse(
                        getInt(R.styleable.Button_iconPlacement, IconPlacement.LEFT.ordinal)
                    ) { IconPlacement.LEFT }
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun setupButton() {
        insetTop = 0
        insetBottom = 0
        minimumHeight = 0
        minimumWidth = 0
        backgroundTintList = null
        backgroundTintMode = null
        elevation = 0f
        stateListAnimator = null
        background = null
        isAllCaps = false
        letterSpacing = 0f
        updateButtonProperties()
    }

    private fun updateButtonProperties() {
        isEnabled = !isButtonDisabled
        val textStyle = getTextStyleForButtonSize(buttonSize)
        if (textStyle != 0) {
            setTextAppearance(textStyle)
            letterSpacing = 0f
        }
        applyButtonStyling()
        updateLayout()
    }

    private fun updateLayout() {
        val heightPx = buttonSize.heightDp.dpToPx
        layoutParams?.height = heightPx
        if (isAttachedToWindow) requestLayout()
    }

    private fun applyButtonStyling() {
        val styleKey = generateStyleKey()
        val cachedDrawable = drawablePool[styleKey]
        if (cachedDrawable != null) {
            background = cachedDrawable
            setTextColor(getTextColorForCurrentState())
            updateIcon()
            return
        }
        val drawable = createBackgroundDrawable()
        drawablePool[styleKey] = drawable
        background = drawable
        setTextColor(getTextColorForCurrentState())
        updateIcon()
    }

    private fun generateStyleKey(): String {
        return "${buttonType}_${buttonState}_${isButtonDisabled}_$isButtonDestructive"
    }

    private fun createBackgroundDrawable(): Drawable {
        return when {
            isButtonDisabled -> createDisabledDrawable()
            isButtonDestructive -> createDestructiveDrawable()
            else -> createNormalDrawable()
        }
    }

    private fun createDisabledDrawable(): GradientDrawable {
        return GradientDrawable().apply {
            cornerRadius = this@Button.cornerRadius
            setColor(getCachedColor(R.attr.colorBackgroundDisabled))
            setStroke(strokeWidth, getCachedColor(R.attr.colorStrokeDisabled))
        }
    }

    private fun createDestructiveDrawable(): Drawable {
        return when (buttonType) {
            ButtonType.PRIMARY -> createDestructivePrimaryDrawable()
            ButtonType.SECONDARY -> createDestructiveSecondaryDrawable()
        }
    }

    private fun createDestructivePrimaryDrawable(): Drawable {
        val baseDrawable = GradientDrawable().apply {
            cornerRadius = this@Button.cornerRadius
            setColor(getCachedColor(R.attr.colorBackgroundAttentionIntense))
            val strokeColor = getCachedColor(R.attr.colorStrokeInteractive)
            val strokeWidth =
                if (buttonState == ButtonState.ON_FOCUS) 3 else this@Button.strokeWidth
            setStroke(strokeWidth, strokeColor)
        }
        return when (buttonState) {
            ButtonState.REST -> baseDrawable
            ButtonState.ON_PRESS -> {
                val modifierDrawable = GradientDrawable().apply {
                    cornerRadius = this@Button.cornerRadius
                    setColor(getCachedColor(R.attr.colorBackgroundModifierOnPressIntense))
                }
                LayerDrawable(arrayOf(baseDrawable, modifierDrawable))
            }

            ButtonState.ON_FOCUS -> {
                val modifierDrawable = GradientDrawable().apply {
                    cornerRadius = this@Button.cornerRadius
                    setColor(getCachedColor(R.attr.colorBackgroundModifierOnPressIntense))
                }
                val focusStrokeDrawable = GradientDrawable().apply {
                    cornerRadius = this@Button.cornerRadius
                    setColor(Color.TRANSPARENT)
                    setStroke(2, getCachedColor(R.attr.colorStrokeAttentionIntense))
                }
                LayerDrawable(arrayOf(baseDrawable, modifierDrawable, focusStrokeDrawable))
            }
        }
    }

    private fun createDestructiveSecondaryDrawable(): Drawable {
        val baseDrawable = GradientDrawable().apply {
            cornerRadius = this@Button.cornerRadius
            setColor(getCachedColor(R.attr.colorBackgroundAttentionSubtle))
            val strokeColor = getCachedColor(R.attr.colorStrokeAttentionIntense)
            val strokeWidth =
                if (buttonState == ButtonState.ON_FOCUS) 4 else this@Button.strokeWidth
            setStroke(strokeWidth, strokeColor)
        }
        return when (buttonState) {
            ButtonState.REST -> baseDrawable
            ButtonState.ON_PRESS -> {
                val modifierDrawable = GradientDrawable().apply {
                    cornerRadius = this@Button.cornerRadius
                    setColor(getCachedColor(R.attr.colorBackgroundModifierOnPress))
                }
                LayerDrawable(arrayOf(baseDrawable, modifierDrawable))
            }

            ButtonState.ON_FOCUS -> {
                val modifierDrawable = GradientDrawable().apply {
                    cornerRadius = this@Button.cornerRadius
                    setColor(getCachedColor(R.attr.colorBackgroundModifierOnPress))
                }
                val focusStrokeDrawable = GradientDrawable().apply {
                    cornerRadius = this@Button.cornerRadius
                    setColor(Color.TRANSPARENT)
                    setStroke(2, getCachedColor(R.attr.colorStrokeAttentionSubtle))
                }
                LayerDrawable(arrayOf(baseDrawable, modifierDrawable, focusStrokeDrawable))
            }
        }
    }

    private fun createNormalDrawable(): Drawable {
        return when (buttonType) {
            ButtonType.PRIMARY -> createNormalPrimaryDrawable()
            ButtonType.SECONDARY -> createNormalSecondaryDrawable()
        }
    }

    private fun createNormalPrimaryDrawable(): Drawable {
        val baseDrawable = GradientDrawable().apply {
            cornerRadius = this@Button.cornerRadius
            setColor(getCachedColor(R.attr.colorBackgroundAccentPrimaryIntense))
            val strokeColor = getCachedColor(R.attr.colorStrokeInteractive)
            val strokeWidth =
                if (buttonState == ButtonState.ON_FOCUS) 3 else this@Button.strokeWidth
            setStroke(strokeWidth, strokeColor)
        }
        return when (buttonState) {
            ButtonState.REST -> baseDrawable
            ButtonState.ON_PRESS -> {
                val modifierDrawable = GradientDrawable().apply {
                    cornerRadius = this@Button.cornerRadius
                    setColor(getCachedColor(R.attr.colorBackgroundModifierOnPressIntense))
                }
                LayerDrawable(arrayOf(baseDrawable, modifierDrawable))
            }

            ButtonState.ON_FOCUS -> {
                val modifierDrawable = GradientDrawable().apply {
                    cornerRadius = this@Button.cornerRadius
                    setColor(getCachedColor(R.attr.colorBackgroundModifierOnPressIntense))
                }
                val focusStrokeDrawable = GradientDrawable().apply {
                    cornerRadius = this@Button.cornerRadius
                    setColor(Color.TRANSPARENT)
                    setStroke(2, getCachedColor(R.attr.colorStrokeFocus))
                }
                LayerDrawable(arrayOf(baseDrawable, modifierDrawable, focusStrokeDrawable))
            }
        }
    }

    private fun createNormalSecondaryDrawable(): Drawable {
        val baseDrawable = GradientDrawable().apply {
            cornerRadius = this@Button.cornerRadius
            setColor(getCachedColor(R.attr.colorBackgroundPrimary))
            val strokeColor = getCachedColor(R.attr.colorStrokeAccent)
            val strokeWidth =
                if (buttonState == ButtonState.ON_FOCUS) 3 else this@Button.strokeWidth
            setStroke(strokeWidth, strokeColor)
        }
        return when (buttonState) {
            ButtonState.REST -> baseDrawable
            ButtonState.ON_PRESS -> {
                val modifierDrawable = GradientDrawable().apply {
                    cornerRadius = this@Button.cornerRadius
                    setColor(getCachedColor(R.attr.colorBackgroundModifierOnPress))
                }
                LayerDrawable(arrayOf(baseDrawable, modifierDrawable))
            }

            ButtonState.ON_FOCUS -> {
                val modifierDrawable = GradientDrawable().apply {
                    cornerRadius = this@Button.cornerRadius
                    setColor(getCachedColor(R.attr.colorBackgroundModifierOnPress))
                }
                val focusStrokeDrawable = GradientDrawable().apply {
                    cornerRadius = this@Button.cornerRadius
                    setColor(Color.TRANSPARENT)
                    setStroke(2, getCachedColor(R.attr.colorStrokeFocus))
                }
                LayerDrawable(arrayOf(baseDrawable, modifierDrawable, focusStrokeDrawable))
            }
        }
    }

    private fun getTextColorForCurrentState(): Int {
        return when {
            isButtonDisabled -> getCachedColor(R.attr.colorForegroundDisabled)
            isButtonDestructive -> when (buttonType) {
                ButtonType.PRIMARY -> getCachedColor(R.attr.colorForegroundWhite)
                ButtonType.SECONDARY -> getCachedColor(R.attr.colorForegroundAttentionIntense)
            }

            else -> when (buttonType) {
                ButtonType.PRIMARY -> getCachedColor(R.attr.colorForegroundWhite)
                ButtonType.SECONDARY -> getCachedColor(R.attr.colorForegroundAccentPrimaryIntense)
            }
        }
    }

    private fun updateIcon() {
        val iconSize = buttonSize.iconSizeDp.dpToPx
        val basePadding = buttonSize.paddingHorizontalDp.dpToPx
        val textPadding = buttonSize.textPaddingDp.dpToPx
        if (buttonIcon != null) {
            when (iconPlacement) {
                IconPlacement.LEFT -> {
                    icon = buttonIcon
                    this.iconSize = iconSize
                    iconPadding = textPadding
                    iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
                    iconTint = ColorStateList.valueOf(getIconColor())
                    setPadding(basePadding + textPadding, 0, basePadding + textPadding, 0)
                }

                IconPlacement.RIGHT -> {
                    icon = buttonIcon
                    this.iconSize = iconSize
                    iconPadding = textPadding
                    iconGravity = MaterialButton.ICON_GRAVITY_TEXT_END
                    iconTint = ColorStateList.valueOf(getIconColor())
                    setPadding(basePadding + textPadding, 0, basePadding + textPadding, 0)
                }
            }
        } else {
            icon = null
            iconPadding = 0
            setPadding(basePadding + textPadding, 0, basePadding + textPadding, 0)
        }
        gravity = Gravity.CENTER
    }

    private fun getIconColor(): Int {
        return when {
            isButtonDisabled -> getCachedColor(R.attr.colorForegroundDisabled)
            isButtonDestructive -> when (buttonType) {
                ButtonType.PRIMARY -> getCachedColor(R.attr.colorForegroundWhite)
                ButtonType.SECONDARY -> getCachedColor(R.attr.colorForegroundAttentionIntense)
            }

            else -> when (buttonType) {
                ButtonType.PRIMARY -> getCachedColor(R.attr.colorForegroundWhite)
                ButtonType.SECONDARY -> getCachedColor(R.attr.colorForegroundAccentPrimaryIntense)
            }
        }
    }

    private fun getCachedColor(@AttrRes attrRes: Int): Int {
        return colorCache[attrRes] ?: context.resolveColorAttribute(attrRes, android.R.color.transparent)
    }

    private fun getTextStyleForButtonSize(size: ButtonSize): Int {
        val attrRes = when (size) {
            ButtonSize.XS -> R.attr.l4Medium
            ButtonSize.SM -> R.attr.l3Medium
            ButtonSize.MD -> R.attr.l2Medium
            ButtonSize.LG -> R.attr.l1Medium
        }
        return context.resolveStyleAttribute(attrRes, R.style.TextMedium_Label2)
    }

    private fun animateScaleDown() {
        val scaleDownX = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 0.95f)
        val scaleDownY = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 0.95f)
        val animatorSet = AnimatorSet().apply {
            playTogether(scaleDownX, scaleDownY)
            duration = 150
            interpolator = AccelerateDecelerateInterpolator()
        }
        animatorSet.start()
    }

    private fun animateScaleUp() {
        val scaleUpX = ObjectAnimator.ofFloat(this, "scaleX", scaleX, 1.0f)
        val scaleUpY = ObjectAnimator.ofFloat(this, "scaleY", scaleY, 1.0f)
        val animatorSet = AnimatorSet().apply {
            playTogether(scaleUpX, scaleUpY)
            duration = 150
            interpolator = AccelerateDecelerateInterpolator()
        }
        animatorSet.start()
    }

    fun setButtonSize(size: ButtonSize) {
        if (buttonSize != size) {
            buttonSize = size
            updateButtonProperties()
        }
    }

    fun setButtonState(state: ButtonState) {
        if (buttonState != state) {
            buttonState = state
            applyButtonStyling()
        }
    }

    fun setButtonDisabled(disabled: Boolean) {
        if (isButtonDisabled != disabled) {
            isButtonDisabled = disabled
            isClickable = !disabled
            isFocusable = !disabled
            updateButtonProperties()
        }
    }

    fun setDestructive(destructive: Boolean) {
        if (isButtonDestructive != destructive) {
            isButtonDestructive = destructive
            applyButtonStyling()
        }
    }

    fun setLabel(label: String) {
        text = label
    }

    fun setCornerRadius(radiusDp: Float) {
        val newRadius = radiusDp.dpToPx.toFloat()
        if (cornerRadius != newRadius) {
            drawablePool.clear()
            applyButtonStyling()
        }
    }

    fun setIcon(drawable: Drawable?, placement: IconPlacement = IconPlacement.LEFT) {
        if (buttonIcon != drawable || iconPlacement != placement) {
            buttonIcon = drawable
            iconPlacement = placement
            updateIcon()
        }
    }

    fun setIcon(@DrawableRes drawableRes: Int?, placement: IconPlacement = IconPlacement.LEFT) {
        val drawable = drawableRes?.let { ContextCompat.getDrawable(context, it) }
        setIcon(drawable, placement)
    }

    fun setIconPlacement(placement: IconPlacement) {
        if (iconPlacement != placement) {
            iconPlacement = placement
            updateIcon()
        }
    }

    fun clearIcon() {
        if (buttonIcon != null) {
            buttonIcon = null
            updateIcon()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateLayout()
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
        if (gainFocus) {
            setButtonState(ButtonState.ON_FOCUS)
        } else {
            setButtonState(ButtonState.REST)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isButtonDisabled || !isEnabled || !isClickable) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                setButtonState(ButtonState.ON_PRESS)
                animateScaleDown()
                return true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (!isFocused) {
                    setButtonState(ButtonState.REST)
                } else {
                    setButtonState(ButtonState.ON_FOCUS)
                }
                animateScaleUp()
                if (event.action == MotionEvent.ACTION_UP) {
                    performClick()
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}