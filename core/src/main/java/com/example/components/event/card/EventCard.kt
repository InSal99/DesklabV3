package com.example.components.event.card

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.example.components.R
import com.example.components.databinding.EventCardBinding
import com.google.android.material.card.MaterialCardView

class EventCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: EventCardBinding = EventCardBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private val shadowPaint1 = Paint()
    private val shadowPaint2 = Paint()
    private val cornerRadiusPx = 8 * context.resources.displayMetrics.density

    var eventCardDelegate: EventCardDelegate? = null

    var eventImageSrc: Int? = null
        set(value) {
            field = value
            updateEventImage()
        }

    var badgeType: EventCardBadge.BadgeType = EventCardBadge.BadgeType.LIVE
        set(value) {
            field = value
            updateBadge()
        }

    var badgeText: String? = null
        set(value) {
            field = value
            updateBadge()
        }

    var eventType: String? = null
        set(value) {
            field = value
            updateBanner()
        }

    var eventCategory: String? = null
        set(value) {
            field = value
            updateBanner()
        }

    var eventTitle: String? = null
        set(value) {
            field = value
            updateDescription()
        }

    var eventDate: String? = null
        set(value) {
            field = value
            updateDescription()
        }

    var statusType: EventCardStatus.StatusType = EventCardStatus.StatusType.UNREGISTERED
        set(value) {
            field = value
            updateStatus()
        }

    var statusText: String? = null
        set(value) {
            field = value
            updateStatus()
        }

    init {
        setupShadowPaints()
        setupClickAnimation()
        radius = cornerRadiusPx

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EventCard,
            0, 0
        ).apply {
            try {
                eventImageSrc = getResourceId(R.styleable.EventCard_eventImageSrc, -1)
                    .takeIf { it != -1 }

                val badgeTypeValue = getInt(R.styleable.EventCard_cardBadgeType, 0)
                badgeType = EventCardBadge.BadgeType.fromValue(badgeTypeValue)
                badgeText = getString(R.styleable.EventCard_cardBadgeText)

                eventType = getString(R.styleable.EventCard_cardEventType)
                eventCategory = getString(R.styleable.EventCard_cardEventCategory)

                eventTitle = getString(R.styleable.EventCard_cardEventTitle)
                eventDate = getString(R.styleable.EventCard_cardEventDate)

                val statusTypeValue = getInt(R.styleable.EventCard_cardStatusType, 0)
                statusType = EventCardStatus.StatusType.fromValue(statusTypeValue)
                statusText = getString(R.styleable.EventCard_cardStatusText)

                updateEventImage()
                updateBadge()
                updateBanner()
                updateDescription()
                updateStatus()

            } finally {
                recycle()
            }
        }
    }

    private fun setupShadowPaints() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        shadowPaint1.apply {
            setShadowLayer(
                2f,
                0f,
                0f,
                R.attr.colorShadowNeutralAmbient
            )
            color = Color.TRANSPARENT
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        shadowPaint2.apply {
            setShadowLayer(
                2f,
                0f,
                0f,
                R.attr.colorShadowNeutralKey
            )
            color = Color.TRANSPARENT
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    private fun setupClickAnimation() {
        isClickable = true
        isFocusable = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                animateScaleDown()
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                animateScaleUp()
                if (event.action == MotionEvent.ACTION_UP) {
                    performClick()
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        return super.performClick()
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

    override fun onDraw(canvas: Canvas) {
        canvas.let { c ->
            val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())

            c.drawRoundRect(rect, cornerRadiusPx, cornerRadiusPx, shadowPaint1)
            c.drawRoundRect(rect, cornerRadiusPx, cornerRadiusPx, shadowPaint2)
        }
        super.onDraw(canvas)
    }

    private fun updateEventImage() {
        eventImageSrc?.let { imageRes ->
            binding.ivEventCard.setImageResource(imageRes)
        }
    }

    private fun updateBadge() {
        binding.cvEventCardBadge?.let { badge ->
            badge.badgeType = badgeType
            badgeText?.let { text ->
                badge.badgeText = text
            }
        }
    }

    private fun updateBanner() {
        binding.cvEventCardBanner?.let { banner ->
            eventType?.let { type ->
                banner.eventType = type
            }
            eventCategory?.let { category ->
                banner.eventCategory = category
            }
        }
    }

    private fun updateDescription() {
        binding.cvEventCardDescription?.let { description ->
            eventTitle?.let { title ->
                description.eventTitle = title
            }
            eventDate?.let { date ->
                description.eventDate = date
            }
        }
    }

    private fun updateStatus() {
        binding.cvEventCardStatus?.let { status ->
            status.statusType = statusType
            statusText?.let { text ->
                status.statusText = text
            }
        }
    }
}