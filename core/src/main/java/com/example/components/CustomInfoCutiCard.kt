package com.example.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView

/**
 * A custom view that displays leave information in a card format.
 *
 * This component encapsulates the layout and logic for displaying:
 * 1. Remaining leave balance (`saldoCuti`).
 * 2. Leave expiration date (`expiredDate`).
 * 3. Used leave count (`usedCuti`).
 *
 * It uses a custom drawing method to render a two-layer shadow
 * that is consistent across all Android versions.
 */
class CustomInfoCutiCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    // Views inside the custom component
    private val tvInfoSaldoCuti: MaterialTextView
    private val tvInfoExpired: MaterialTextView
    private val tvInfoUsed: MaterialTextView
    private val tvInfoCutiTitle: MaterialTextView

    // Paint objects for drawing the custom shadows
    private val ambientShadowPaint = Paint()
    private val keyShadowPaint = Paint()
    // Calculate corner radius in pixels from DP
    private val cornerRadiusPx = 8f * context.resources.displayMetrics.density

    init {
        // Set the corner radius for the card itself.
        radius = cornerRadiusPx

        // Configure the paint objects for the shadows.
        setupShadowPaints()

        // Inflate the XML layout and attach it to this view group.
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.custom_info_cuti_card, this, true)

        // Get references to the TextViews from the inflated layout.
        tvInfoSaldoCuti = view.findViewById(R.id.tvInfoSaldoCuti)
        tvInfoExpired = view.findViewById(R.id.tvInfoExpired)
        tvInfoUsed = view.findViewById(R.id.tvInfoUsed)
        tvInfoCutiTitle = view.findViewById(R.id.tvInfoCutiTitle)


        // Apply attributes set in XML, if any.
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.CustomInfoCutiCard,
                0,
                0
            )
            try {
                // Get the values from the XML attributes.
                val title = typedArray.getString(R.styleable.CustomInfoCutiCard_infoCutiTitle)
                val saldoCuti = typedArray.getInt(R.styleable.CustomInfoCutiCard_saldoCuti, 0)
                val expiredDate = typedArray.getString(R.styleable.CustomInfoCutiCard_expiredDate)
                val usedCuti = typedArray.getInt(R.styleable.CustomInfoCutiCard_usedCuti, 0)

                // Set the initial values from the attributes.
                title?.let { t -> setTitle(t) }
                setSaldoCuti(saldoCuti)
                expiredDate?.let { date -> setExpiredDate(date) }
                setUsedCuti(usedCuti)

            } finally {
                // Always recycle the TypedArray after use.
                typedArray.recycle()
            }
        }
    }

    /**
     * Configures the Paint objects for the two shadow layers.
     */
    private fun setupShadowPaints() {
        // This is crucial for shadow rendering to work.
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        // 1. Ambient Shadow (Top Layer)
        ambientShadowPaint.apply {
            // Blur: 4, X: 0, Y: 0
            setShadowLayer(4f, 0f, 0f, Color.parseColor("#1A2A93D6")) // 10% opacity
            color = Color.TRANSPARENT // The paint itself is transparent, only shadow is visible
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        // 2. Key Shadow (Bottom Layer)
        keyShadowPaint.apply {
            // Blur: 4, X: 0, Y: 0
            setShadowLayer(4f, 0f, 0f, Color.parseColor("#332A93D6")) // 20% opacity
            color = Color.TRANSPARENT
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    /**
     * Overrides the default drawing behavior to add custom shadow layers.
     * The shadows are drawn first, underneath the card's content.
     */
    override fun onDraw(canvas: Canvas) {
        // Create a rectangle representing the bounds of the card.
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())

        // Draw the two shadow layers.
        canvas.drawRoundRect(rect, cornerRadiusPx, cornerRadiusPx, keyShadowPaint)
        canvas.drawRoundRect(rect, cornerRadiusPx, cornerRadiusPx, ambientShadowPaint)

        // Call super.onDraw() to draw the actual card content on top of the shadows.
        super.onDraw(canvas)
    }

    /**
     * Sets the title text for the card.
     * @param title The string to be displayed as the title.
     */
    fun setTitle(title: String) {
        tvInfoCutiTitle.text = title
    }


    /**
     * Sets the numeric value for the remaining leave balance.
     * @param saldo The number of remaining leave days.
     */
    fun setSaldoCuti(saldo: Int) {
        tvInfoSaldoCuti.text = context.getString(R.string.leave_balance_format, saldo)
    }

    /**
     * Sets the expiration date string.
     * @param date The expiration date in a string format (e.g., "01/08/2026").
     */
    fun setExpiredDate(date: String) {
        tvInfoExpired.text = context.getString(R.string.leave_expiry_format, date)
    }

    /**
     * Sets the numeric value for the used leave.
     * @param used The number of used leave days.
     */
    fun setUsedCuti(used: Int) {
        tvInfoUsed.text = context.getString(R.string.leave_used_format, used)
    }
}