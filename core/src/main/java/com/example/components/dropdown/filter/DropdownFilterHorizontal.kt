package com.example.components.dropdown.filter

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.example.components.R
import com.example.components.databinding.DropdownFilterHorizontalBinding
import com.google.android.material.card.MaterialCardView

/**
 * A custom UI component that serves as a clickable dropdown filter.
 *
 * This view displays a title and an optional description, visually responding to touch events.
 * It is designed to be used as a trigger for a dropdown menu or a filtering dialog. The state
 * and content can be controlled both through XML attributes and programmatically.
 *
 */
class DropdownFilterHorizontal @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    /**
     * Defines the visual state of the component based on user interaction.
     */
    enum class DropdownState {
        /** The default state when there is no active interaction. */
        REST,
        /** The state when the user is actively pressing down on the view. */
        ON_PRESS
    }

    private val binding: DropdownFilterHorizontalBinding
    private val cardView: MaterialCardView
    private var dropdownState: DropdownState = DropdownState.REST
    private var dropdownTitle: String = ""
    private var dropdownDescription: String = ""
    private val strokeWidth: Int
    private val cornerRadius: Float
    private val colorCache = mutableMapOf<Int, Int>()

    /**
     * The delegate responsible for handling click events on this dropdown.
     * Assign an object that implements [DropdownFilterHorizontalDelegate] to receive callbacks.
     */
    var dropdownFilterHorizontalDelegate: DropdownFilterHorizontalDelegate? = null

    init {
        strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp)
        cornerRadius = resources.getDimension(R.dimen.radius_999dp)

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dropdown_filter_horizontal, this, false)

        binding = DropdownFilterHorizontalBinding.bind(view)
        cardView = view as MaterialCardView
        cardView.background = null

        addView(cardView)

        preloadColors()
        parseAttributes(attrs, defStyleAttr)
        setupComponent()
    }

    /** Pre-caches colors from theme attributes for better performance. */
    private fun preloadColors() {
        val colorAttrs = arrayOf(
            R.attr.colorBackgroundPrimary to R.color.colorFFF,
            R.attr.colorBackgroundModifierOnPress to R.color.color000Opacity5,
            R.attr.colorStrokeAccent to R.color.colorNeutral30
        )

        colorAttrs.forEach { (attr, fallback) ->
            colorCache[attr] = resolveColorAttribute(attr, fallback)
        }
    }

    /** Parses custom attributes from XML. */
    private fun parseAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        attrs?.let { attributeSet ->
            context.withStyledAttributes(attributeSet, R.styleable.DropdownFilterHorizontal, defStyleAttr, 0) {
                dropdownTitle = getString(R.styleable.DropdownFilterHorizontal_dropdownTitle) ?: ""
                dropdownDescription = getString(R.styleable.DropdownFilterHorizontal_dropdownDescription) ?: ""
            }
        }
    }

    /** Sets up the initial state and properties of the component. */
    private fun setupComponent() {
        isClickable = true
        isFocusable = true

        setTitle(dropdownTitle)
        setDescription(dropdownDescription)
        applyStyling()
    }

    /** Applies the appropriate drawable based on the component's state. */
    private fun applyStyling() {
        val backgroundDrawable = createBackgroundDrawable()
        cardView.background = backgroundDrawable
    }

    /** Constructs the background drawable using base and modifier layers. */
    private fun createBackgroundDrawable(): Drawable {
        val baseDrawable = GradientDrawable().apply {
            cornerRadius = this@DropdownFilterHorizontal.cornerRadius
            setColor(getCachedColor(R.attr.colorForegroundPrimaryInverse))
            setStroke(strokeWidth, getCachedColor(R.attr.colorStrokeSubtle))
        }

        return when (dropdownState) {
            DropdownState.REST -> baseDrawable
            DropdownState.ON_PRESS -> {
                val modifierDrawable = GradientDrawable().apply {
                    cornerRadius = this@DropdownFilterHorizontal.cornerRadius
                    setColor(getCachedColor(R.attr.colorBackgroundModifierOnPress))
                }
                LayerDrawable(arrayOf(baseDrawable, modifierDrawable))
            }
        }
    }

    /** Handles touch events to update the visual state. */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return super.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                setDropdownState(DropdownState.ON_PRESS)
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                setDropdownState(DropdownState.REST)
                if (event.action == MotionEvent.ACTION_UP) {
                    performClick()
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    /** Performs the click action and notifies the delegate. */
    override fun performClick(): Boolean {
        Log.d("CustomDropdown", "Dropdown Clicked ✅")
        dropdownFilterHorizontalDelegate?.onClick(this)
        return super.performClick()
    }

    /**
     * Sets the visual state of the dropdown, triggering a style update.
     * @param state The [DropdownState] to apply.
     */
    fun setDropdownState(state: DropdownState) {
        if (dropdownState != state) {
            dropdownState = state
            applyStyling()
        }
    }

    /**
     * Sets the main title text of the dropdown.
     * @param title The string to display as the title.
     */
    fun setTitle(title: String) {
        this.dropdownTitle = title
        binding.tvTitleLabel.text = title
    }

    /**
     * Sets the descriptive text of the dropdown.
     * If the description is empty, the dot separator will be hidden.
     * @param description The string to display as the description.
     */
    fun setDescription(description: String) {
        this.dropdownDescription = description
        binding.tvDescriptionLabel.text = description
        binding.tvDotSpacer.visibility = if (description.isEmpty()) GONE else VISIBLE
    }

    /** Retrieves a color from the cache or resolves it from theme attributes. */
    private fun getCachedColor(@AttrRes attrRes: Int): Int {
        return colorCache[attrRes] ?: resolveColorAttribute(attrRes, android.R.color.transparent)
    }

    /** Resolves a color attribute from the current theme, providing a fallback if not found. */
    private fun resolveColorAttribute(@AttrRes attrRes: Int, @ColorRes fallbackColorRes: Int): Int {
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(attrRes, typedValue, true)) {
            if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                typedValue.data
            } else {
                ContextCompat.getColor(context, typedValue.resourceId)
            }
        } else {
            Log.w("CustomDropdown", "Attribute not found, using fallback color.")
            ContextCompat.getColor(context, fallbackColorRes)
        }
    }
}