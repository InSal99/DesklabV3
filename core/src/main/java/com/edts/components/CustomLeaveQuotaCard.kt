package com.edts.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.material.textview.MaterialTextView

/**
 * A custom view that displays leave quota information in a card format.
 *
 * This component encapsulates the layout and logic for displaying key leave-related data, including
 * a title, the remaining leave quota, an expiration date, and the amount of leave already used.
 * The data can be set via XML attributes or programmatically using the provided setter methods.
 *
 * ### XML Usage Example:
 * ```xml
 * <com.example.components.CustomLeaveQuotaCard
 * android:layout_width="wrap_content"
 * android:layout_height="wrap_content"
 * app:leaveQuotaTitle="Cuti Tahunan"
 * app:leaveQuota="5"
 * app:expiredDate="31 Des 2025"
 * app:leaveUsed="2" />
 * ```
 *
 * @param context The Context the view is running in.
 * @param attrs The attributes of the XML tag that is inflating the view.
 * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource.
 */
class CustomLeaveQuotaCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val tvLeaveQuotaTitle: MaterialTextView
    private val tvLeaveQuota: MaterialTextView
    private val tvExpiredDate: MaterialTextView
    private val tvLeaveUsed: MaterialTextView

    init {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.custom_leave_quota_card, this, true)

        tvLeaveQuotaTitle = view.findViewById(R.id.tvLeaveQuotaTitle)
        tvLeaveQuota = view.findViewById(R.id.tvLeaveQuota)
        tvExpiredDate = view.findViewById(R.id.tvExpiredDate)
        tvLeaveUsed = view.findViewById(R.id.tvLeaveUsed)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.CustomLeaveQuotaCard,
                0,
                0
            )
            try {
                val title = typedArray.getString(R.styleable.CustomLeaveQuotaCard_leaveQuotaTitle)
                val quota = typedArray.getInt(R.styleable.CustomLeaveQuotaCard_leaveQuota, 0)
                val date = typedArray.getString(R.styleable.CustomLeaveQuotaCard_expiredDate)
                val used = typedArray.getInt(R.styleable.CustomLeaveQuotaCard_leaveUsed, 0)

                title?.let { t -> setTitle(t) }
                setLeaveQuota(quota)
                date?.let { d -> setExpiredDate(d) }
                setLeaveUsed(used)

            } finally {
                typedArray.recycle()
            }
        }
    }

    /**
     * Sets the title text for the card.
     * Corresponds to the `app:leaveQuotaTitle` XML attribute.
     *
     * @param title The string to be displayed as the title (e.g., "Cuti Tahunan").
     */
    fun setTitle(title: String) {
        tvLeaveQuotaTitle.text = title
    }

    /**
     * Sets the numeric value for the remaining leave balance. The view will format this
     * into a string like "X hari".
     * Corresponds to the `app:leaveQuota` XML attribute.
     *
     * @param quota The integer representing the number of remaining leave days.
     */
    fun setLeaveQuota(quota: Int) {
        tvLeaveQuota.text = context.getString(R.string.leave_balance_format, quota)
    }

    /**
     * Sets the expiration date string. The view will format this into a string like
     * "Kedaluwarsa: [date]".
     * Corresponds to the `app:expiredDate` XML attribute.
     *
     * @param date The expiration date in a string format (e.g., "31 Des 2025").
     */
    fun setExpiredDate(date: String) {
        tvExpiredDate.text = context.getString(R.string.leave_expiry_format, date)
    }

    /**
     * Sets the numeric value for the used leave. The view will format this into a string
     * like "Cuti Terpakai: X Hari".
     * Corresponds to the `app:leaveUsed` XML attribute.
     *
     * @param used The integer representing the number of used leave days.
     */
    fun setLeaveUsed(used: Int) {
        tvLeaveUsed.text = context.getString(R.string.leave_used_format, used)
    }
}