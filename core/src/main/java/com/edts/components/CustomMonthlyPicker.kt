package com.edts.components

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView

/**
 * A custom UI component that displays a single month as a clickable picker.
 *
 * This component is stateful and visually represents different states based on user interaction
 * and its persistent type. It separates its logic into a persistent `PickerType` (e.g., selected, unselected)
 * and a temporary `InteractionState` (e.g., pressed, focused) for robust state management.
 *
 * A click on the picker will toggle its type between `UNSELECTED` and `SELECTED`.
 *
 * ### XML Usage Example:
 * ```xml
 * <com.example.components.CustomMonthlyPicker
 * android:id="@+id/january_picker"
 * android:layout_width="wrap_content"
 * android:layout_height="wrap_content"
 * app:monthLabel="Jan"
 * app:pickerType="unselected" />
 * ```
 *
 * @param context The Context the view is running in.
 * @param attrs The attributes of the XML tag that is inflating the view.
 * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource.
 * @see CustomMonthlyPickerDelegate for handling click events.
 */
class CustomMonthlyPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyleAttr) {

    /**
     * Defines the persistent, logical state of the picker.
     * This state is changed by user clicks and can be set programmatically or via XML.
     */
    enum class PickerType {
        /** The default state, indicating the month is not selected. */
        UNSELECTED,
        /** The state indicating the month has been chosen by the user. */
        SELECTED,
        /** The state where the picker is inactive and cannot be interacted with. */
        DISABLED
    }

    /**
     * Defines the temporary, visual state based on direct user interaction.
     * This state is managed internally and should not be set from outside the class.
     */
    enum class InteractionState {
        /** The default state when there is no active interaction. */
        REST,
        /** The state when the user is actively pressing down on the view. */
        ON_PRESS,
        /** The state when the view is highlighted by a focus event (e.g., keyboard navigation). */
        ON_FOCUS
    }

    private val monthLabelView: MaterialTextView
    private var monthLabel: String = ""
    private val strokeWidth: Int
    private val focusStrokeWidth: Int
    private val defaultCornerRadius: Float

    private val colorCache = mutableMapOf<Int, Int>()
    private val drawableCache = mutableMapOf<String, Drawable>()

    /**
     * The delegate responsible for handling click events on this picker.
     * Assign an object that implements [CustomMonthlyPickerDelegate] to receive callbacks.
     */
    var delegate: CustomMonthlyPickerDelegate? = null

    /**
     * The persistent type of the picker, which determines its primary appearance and state.
     * Can be set via the `app:pickerType` XML attribute or programmatically.
     * Changing this value will trigger a visual update.
     */
    var type: PickerType = PickerType.UNSELECTED
        set(value) {
            if (field != value) {
                field = value
                applyStyling()
            }
        }

    /** The internal interaction state of the picker. */
    private var interactionState: InteractionState = InteractionState.REST
        set(value) {
            if (field != value) {
                field = value
                applyStyling()
            }
        }

    init {
        strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp)
        focusStrokeWidth = strokeWidth * 2
        defaultCornerRadius = resources.getDimension(R.dimen.radius_12dp)

        monthLabelView = createMonthLabelView()
        addView(monthLabelView)

        preloadColors()
        parseAttributes(attrs)
        setupComponent()
    }

    /**
     * Parses custom XML attributes and initializes the component's state.
     */
    private fun parseAttributes(attrs: AttributeSet?) {
        attrs?.let {
            context.withStyledAttributes(it, R.styleable.CustomMonthlyPicker, 0, 0) {
                monthLabel = getString(R.styleable.CustomMonthlyPicker_monthLabel) ?: ""
                val typeInt = getInt(R.styleable.CustomMonthlyPicker_pickerType, 0)
                type = when (typeInt) {
                    1 -> PickerType.SELECTED
                    2 -> PickerType.DISABLED
                    else -> PickerType.UNSELECTED
                }
            }
        }
    }

    /**
     * Applies all visual styles to the component based on its current `type` and `interactionState`.
     * This includes text color, background, and stroke. It also manages the `isEnabled` property.
     */
    private fun applyStyling() {
        isEnabled = (type != PickerType.DISABLED)

        val textColor = when (type) {
            PickerType.UNSELECTED -> getCachedColor(R.attr.colorForegroundPrimary)
            PickerType.SELECTED -> getCachedColor(R.attr.colorForegroundPrimaryInverse)
            PickerType.DISABLED -> getCachedColor(R.attr.colorForegroundDisabled)
        }
        monthLabelView.setTextColor(textColor)

        val styleKey = "${type}_${interactionState}_${radius}"
        val backgroundDrawable = drawableCache[styleKey] ?: createBackgroundDrawable().also {
            drawableCache[styleKey] = it
        }
        background = backgroundDrawable
    }

    /**
     * Creates the appropriate [Drawable] for the component's background by combining base styles
     * from the `PickerType` with temporary modifiers from the `InteractionState`.
     * @return A drawable representing the current combined state.
     */
    private fun createBackgroundDrawable(): Drawable {
        val (backgroundColor, strokeColor) = when (type) {
            PickerType.UNSELECTED -> Pair(Color.TRANSPARENT, getCachedColor(R.attr.colorStrokeSubtle))
            PickerType.SELECTED -> Pair(getCachedColor(R.attr.colorBackgroundAccentPrimaryIntense), getCachedColor(R.attr.colorStrokeInteractive))
            PickerType.DISABLED -> Pair(Color.TRANSPARENT, getCachedColor(R.attr.colorStrokeDisabled))
        }

        val currentStrokeWidth = if (interactionState == InteractionState.ON_FOCUS) focusStrokeWidth else strokeWidth

        val baseDrawable = GradientDrawable().apply {
            cornerRadius = this@CustomMonthlyPicker.radius
            setColor(backgroundColor)
            setStroke(currentStrokeWidth, strokeColor)
        }

        if (interactionState == InteractionState.ON_PRESS && type != PickerType.DISABLED) {
            val modifierDrawable = GradientDrawable().apply {
                cornerRadius = this@CustomMonthlyPicker.radius
                setColor(getCachedColor(R.attr.colorBackgroundModifierOnPress))
            }
            return LayerDrawable(arrayOf(baseDrawable, modifierDrawable))
        }

        return baseDrawable
    }

    /**
     * Handles focus changes to update the `interactionState`.
     */
    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
        interactionState = if (gainFocus) InteractionState.ON_FOCUS else InteractionState.REST
    }

    /**
     * Handles touch events to manage the `interactionState` and toggle the `type` on click.
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return super.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                interactionState = InteractionState.ON_PRESS
                return true
            }
            MotionEvent.ACTION_UP -> {
                type = if (type == PickerType.SELECTED) PickerType.UNSELECTED else PickerType.SELECTED
                interactionState = if (isFocused) InteractionState.ON_FOCUS else InteractionState.REST
                performClick()
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                interactionState = if (isFocused) InteractionState.ON_FOCUS else InteractionState.REST
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * Performs the click action, logging the event and notifying the delegate.
     */
    override fun performClick(): Boolean {
        Log.d("CustomMonthlyPicker", "Monthly Picker Clicked ✅")
        if (!isEnabled) return false
        delegate?.onMonthClicked(this)
        return super.performClick()
    }

    /**
     * Sets the text to be displayed on the picker.
     * @param label The string to display (e.g., "Jan", "Feb").
     */
    fun setMonthLabel(label: String) {
        this.monthLabel = label
        monthLabelView.text = label
    }

    /**
     * Finalizes component setup.
     */
    private fun setupComponent() {
        cardElevation = 0f
        radius = defaultCornerRadius
        isClickable = true
        isFocusable = true
        setMonthLabel(monthLabel)
        applyStyling()
    }

    /**
     * Creates and configures the internal TextView for displaying the month label.
     */
    private fun createMonthLabelView(): MaterialTextView {
        val style = R.style.TextMedium_Label2
        return MaterialTextView(context, null, 0).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER
            }
            setTextAppearance(style)
            textAlignment = TEXT_ALIGNMENT_CENTER
            val verticalPadding = resources.getDimensionPixelSize(R.dimen.margin_12dp)
            setPadding(0, verticalPadding, 0, verticalPadding)
        }
    }

    /**
     * Preloads theme color attributes into a cache for faster access.
     */
    private fun preloadColors() {
        val colorAttrs = mapOf(
            R.attr.colorForegroundPrimary to R.color.color000,
            R.attr.colorForegroundPrimaryInverse to android.R.color.white,
            R.attr.colorForegroundDisabled to R.color.color000Opacity20,
            R.attr.colorBackgroundAccentPrimaryIntense to R.color.colorPrimary30,
            R.attr.colorStrokeSubtle to R.color.colorNeutral30,
            R.attr.colorStrokeInteractive to R.color.colorOpacityWhite20,
            R.attr.colorStrokeDisabled to R.color.color000Opacity12,
            R.attr.colorBackgroundModifierOnPress to R.color.color000Opacity5
        )

        colorAttrs.forEach { (attr, fallback) ->
            colorCache[attr] = resolveColorAttribute(attr, fallback)
        }
    }

    /**
     * Retrieves a color from the cache.
     */
    private fun getCachedColor(@AttrRes attrRes: Int): Int {
        return colorCache[attrRes] ?: resolveColorAttribute(attrRes, android.R.color.transparent)
    }

    /**
     * Resolves a color attribute from the current theme, providing a fallback if not found.
     */
    private fun resolveColorAttribute(@AttrRes attrRes: Int, @ColorRes fallbackColorRes: Int): Int {
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(attrRes, typedValue, true)) {
            if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                typedValue.data
            } else {
                ContextCompat.getColor(context, typedValue.resourceId)
            }
        } else {
            Log.w("CustomMonthlyPicker", "Attribute '$attrRes' not found, using fallback color.")
            ContextCompat.getColor(context, fallbackColorRes)
        }
    }
}