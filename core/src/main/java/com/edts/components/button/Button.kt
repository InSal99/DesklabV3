package com.edts.components.button

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.google.android.material.button.MaterialButton
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.withStyledAttributes

class Button @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialButtonStyle
) : MaterialButton(context, attrs, defStyleAttr) {

    enum class ButtonSize(val heightDp: Int, val paddingHorizontalDp: Int, val iconSizeDp: Int, val textPaddingDp: Int) {
        XS(28, 12, 16, 4),
        SM(36, 12, 20, 4),
        MD(40, 16, 24, 6),
        LG(48, 16, 24, 8)
    }

    enum class ButtonState { REST, ON_PRESS, ON_FOCUS }
    enum class ButtonType { PRIMARY, SECONDARY }

    private var buttonType: ButtonType = ButtonType.PRIMARY
    private var buttonSize: ButtonSize = ButtonSize.MD
    private var buttonState: ButtonState = ButtonState.REST
    private var isButtonDisabled: Boolean = false
    private var isButtonDestructive: Boolean = false
    private var buttonLabel: String = ""
    private var leftIconDrawable: Drawable? = null
    private var rightIconDrawable: Drawable? = null

    private val strokeWidth: Int
    private val cornerRadius: Float
    private val iconSpacing: Int
    private val colorCache = mutableMapOf<Int, Int>()
    private val drawablePool = mutableMapOf<String, Drawable>()
    private val density: Float = context.resources.displayMetrics.density

    var buttonDelegate: ButtonDelegate? = null

    init {
        strokeWidth = try {
            resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp)
        } catch (e: Exception) {
            1
        }

        cornerRadius = try {
            resources.getDimension(R.dimen.radius_999dp)
        } catch (e: Exception) {
            99999f
        }

        iconSpacing = 4.dpToPx()

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
            colorCache[attr] = resolveColorAttribute(attr, fallback)
        }
    }

    private fun parseAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        attrs?.let { attributeSet ->
            try {
                context.withStyledAttributes(attributeSet, R.styleable.CustomButton, defStyleAttr, 0) {
                    buttonType = ButtonType.values().getOrElse(
                        getInt(R.styleable.CustomButton_customButtonType, ButtonType.PRIMARY.ordinal)
                    ) { ButtonType.PRIMARY }

                    buttonSize = ButtonSize.values().getOrElse(
                        getInt(R.styleable.CustomButton_customButtonSize, ButtonSize.MD.ordinal)
                    ) { ButtonSize.MD }

                    buttonState = ButtonState.values().getOrElse(
                        getInt(R.styleable.CustomButton_customButtonState, ButtonState.REST.ordinal)
                    ) { ButtonState.REST }

                    isButtonDisabled = getBoolean(R.styleable.CustomButton_isButtonDisabled, false)
                    isButtonDestructive = getBoolean(R.styleable.CustomButton_isButtonDestructive, false)
                    buttonLabel = getString(R.styleable.CustomButton_label) ?: ""
                    leftIconDrawable = getDrawable(R.styleable.CustomButton_iconLeft)
                    rightIconDrawable = getDrawable(R.styleable.CustomButton_iconRight)
                }
            } catch (e: Exception) {
                Log.w("CustomButton", "Attribute goes fallback")
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
        text = if (buttonLabel.isNotEmpty()) buttonLabel else "Button"

        val textStyle = getTextStyleForButtonSize(buttonSize)
        if (textStyle != 0) {
            setTextAppearance(textStyle)
            letterSpacing = 0f
        }

        applyButtonStyling()
        updateLayout()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateLayout()
    }

    private fun updateLayout() {
        val heightPx = buttonSize.heightDp.dpToPx()
        val horizontalPadding = buttonSize.paddingHorizontalDp.dpToPx()

        layoutParams?.height = heightPx
        setPadding(horizontalPadding, 0, horizontalPadding, 0)

        if (isAttachedToWindow) {
            requestLayout()
        }
    }

    private fun applyButtonStyling() {
        val styleKey = generateStyleKey()

        val cachedDrawable = drawablePool[styleKey]
        if (cachedDrawable != null) {
            background = cachedDrawable
            setTextColor(getTextColorForCurrentState())
            updateIcons()
            return
        }

        val drawable = createBackgroundDrawable()
        drawablePool[styleKey] = drawable
        background = drawable

        setTextColor(getTextColorForCurrentState())
        updateIcons()
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
            val strokeWidth = if (buttonState == ButtonState.ON_FOCUS) 3 else this@Button.strokeWidth
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
            val strokeWidth = if (buttonState == ButtonState.ON_FOCUS) 4 else this@Button.strokeWidth
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
            val strokeWidth = if (buttonState == ButtonState.ON_FOCUS) 3 else this@Button.strokeWidth
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
            val strokeWidth = if (buttonState == ButtonState.ON_FOCUS) 3 else this@Button.strokeWidth
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

    private fun updateIcons() {
        val iconSize = buttonSize.iconSizeDp.dpToPx()
        val horizontalPadding = buttonSize.paddingHorizontalDp.dpToPx()

        when {
//            leftIconDrawable != null && rightIconDrawable != null -> {
//                icon = null
//                iconPadding = 0
//
//                leftIconDrawable?.let {
//                    it.setBounds(0, 0, iconSize, iconSize)
//                    it.colorFilter = PorterDuffColorFilter(getIconColor(), PorterDuff.Mode.SRC_IN)
//                }
//                rightIconDrawable?.let {
//                    it.setBounds(0, 0, iconSize, iconSize)
//                    it.colorFilter = PorterDuffColorFilter(getIconColor(), PorterDuff.Mode.SRC_IN)
//                }
//
//                setCompoundDrawables(leftIconDrawable, null, rightIconDrawable, null)
//                compoundDrawablePadding = iconSpacing
//            }

            leftIconDrawable != null -> {
                icon = leftIconDrawable
                this.iconSize = iconSize
                iconPadding = iconSpacing
                iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
                iconTint = ColorStateList.valueOf(getIconColor())
            }

            rightIconDrawable != null -> {
                icon = rightIconDrawable
                this.iconSize = iconSize
                iconPadding = iconSpacing
                iconGravity = MaterialButton.ICON_GRAVITY_TEXT_END
                iconTint = ColorStateList.valueOf(getIconColor())
            }

            else -> {
                icon = null
                iconPadding = 0
                compoundDrawablePadding = 0
            }
        }

        gravity = Gravity.CENTER
        setPadding(horizontalPadding, 0, horizontalPadding, 0)
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
        return colorCache[attrRes] ?: resolveColorAttribute(attrRes, android.R.color.transparent)
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
            Log.d("Custom Button", "Fallback Color")
            ContextCompat.getColor(context, fallbackColor)
        }
    }

    private fun getTextStyleForButtonSize(size: ButtonSize): Int {
        return when (size) {
            ButtonSize.XS -> R.style.CustomButton_TextStyle_ExtraSmall
            ButtonSize.SM -> R.style.CustomButton_TextStyle_Small
            ButtonSize.MD -> R.style.CustomButton_TextStyle_Medium
            ButtonSize.LG -> R.style.CustomButton_TextStyle_Large
        }
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

    override fun performClick(): Boolean {
        Log.d("CustomButton", "button clicked")
        buttonDelegate?.onClick(this)
        return super.performClick()
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
        if (buttonLabel != label) {
            buttonLabel = label
            text = label
        }
    }

    fun setCornerRadius(radiusDp: Float) {
        val newRadius = radiusDp.dpToPx().toFloat()
        if (cornerRadius != newRadius) {
            drawablePool.clear()
            applyButtonStyling()
        }
    }

    fun setLeftIcon(drawable: Drawable?) {
        if (leftIconDrawable != drawable) {
            leftIconDrawable = drawable
            updateIcons()
        }
    }

    fun setRightIcon(drawable: Drawable?) {
        if (rightIconDrawable != drawable) {
            rightIconDrawable = drawable
            updateIcons()
        }
    }

    fun setLeftIcon(@DrawableRes drawableRes: Int?) {
        val drawable = drawableRes?.let { ContextCompat.getDrawable(context, it) }
        setLeftIcon(drawable)
    }

    fun setRightIcon(@DrawableRes drawableRes: Int?) {
        val drawable = drawableRes?.let { ContextCompat.getDrawable(context, it) }
        setRightIcon(drawable)
    }

    private fun Int.dpToPx(): Int = (this * density).toInt()
    private fun Float.dpToPx(): Int = (this * density).toInt()
}