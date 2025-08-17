package com.example.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.components.databinding.CustomMyEventCardBinding
import com.example.components.event.card.EventCardBadge
import com.google.android.material.card.MaterialCardView


class CustomMyEventCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: CustomMyEventCardBinding
    private val ambientShadowPaint = Paint()
    private val keyShadowPaint = Paint()
    private val cornerRadiusPx = 8f * context.resources.displayMetrics.density

    // Path for clipping the canvas to prevent children from drawing over the shadow
    private val clipPath = Path()

    var eventType: String? = null
        set(value) {
            field = value
            binding.tvEventType.text = value
        }

    var eventTitle: String? = null
        set(value) {
            field = value
            binding.tvEventTitle.text = value
        }

    var eventTime: String? = null
        set(value) {
            field = value
            binding.tvEventTime.text = value
        }

    init {
        radius = cornerRadiusPx

        setupShadowPaints()

        binding = CustomMyEventCardBinding.inflate(LayoutInflater.from(context), this, true)

        attrs?.let {
            // Attributes are obtained using the parent's styleable
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomMyEventCard, 0, 0)
            try {
                // Set properties for CustomMyEventCard itself
                eventType = typedArray.getString(R.styleable.CustomMyEventCard_myEventType)
                eventTitle = typedArray.getString(R.styleable.CustomMyEventCard_myEventTitle)
                eventTime = typedArray.getString(R.styleable.CustomMyEventCard_myEventTime)

                // Pass calendar attributes directly to the CustomCalendarCard instance
                binding.customCalendarCard.month = typedArray.getString(R.styleable.CustomMyEventCard_month)
                binding.customCalendarCard.date = typedArray.getString(R.styleable.CustomMyEventCard_date)
                binding.customCalendarCard.day = typedArray.getString(R.styleable.CustomMyEventCard_day)

                // Pass badge attributes directly to the EventCardBadge instance
                val badgeTypeValue = typedArray.getInt(R.styleable.CustomMyEventCard_badgeType, 0)
                binding.eventCardBadge.badgeType = EventCardBadge.BadgeType.fromValue(badgeTypeValue)
                binding.eventCardBadge.badgeText = typedArray.getString(R.styleable.CustomMyEventCard_badgeText)

            } finally {
                typedArray.recycle()
            }
        }
    }

    /**
     * Configures the Paint objects for the two shadow layers.
     */
    private fun setupShadowPaints() {
        // Use software layer to enable shadow drawing
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        ambientShadowPaint.apply {
            setShadowLayer(4f, 0f, 0f, Color.parseColor("#1A2A93D6"))
            color = Color.TRANSPARENT
            isAntiAlias = true
            style = Paint.Style.FILL

        }

        keyShadowPaint.apply {
            setShadowLayer(4f, 0f, 0f, Color.parseColor("#332A93D6"))
            color = Color.TRANSPARENT
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    /**
     * Overrides the default drawing behavior to add custom shadow layers.
     * The shadows are drawn first. Then, the canvas is clipped to the card's rounded
     * rectangle shape before drawing the children, ensuring they do not draw over the shadow.
     */
    override fun onDraw(canvas: Canvas) {
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())

        // 1. Draw your custom shadows first. They are drawn outside the clipping area.
        canvas.drawRoundRect(rect, cornerRadiusPx, cornerRadiusPx, keyShadowPaint)
        canvas.drawRoundRect(rect, cornerRadiusPx, cornerRadiusPx, ambientShadowPaint)

        // 2. Prepare the clipping path
        clipPath.reset()
        clipPath.addRoundRect(rect, cornerRadiusPx, cornerRadiusPx, Path.Direction.CW)

        // 3. Save the canvas state, apply the clip, and then draw the parent and its children
        canvas.save()
        canvas.clipPath(clipPath)
        super.onDraw(canvas) // This draws the card background and its children
        canvas.restore() // Restore the canvas to its original state
    }


    /**
     * Programmatically sets the calendar data.
     */
    fun setCalendarData(month: String, date: String, day: String) {
        binding.customCalendarCard.setCalendarData(month, date, day)
    }

    /**
     * Programmatically sets the badge data.
     */
    fun setBadgeData(badgeType: EventCardBadge.BadgeType, badgeText: String?) {
        binding.eventCardBadge.badgeType = badgeType
        binding.eventCardBadge.badgeText = badgeText
    }
}