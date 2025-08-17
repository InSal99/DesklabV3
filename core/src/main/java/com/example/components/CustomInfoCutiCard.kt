package com.example.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.material.textview.MaterialTextView

/**
 * A custom view that displays leave information in a card format using a FrameLayout.
 *
 * This component encapsulates the layout and logic for displaying:
 * 1. Remaining leave balance (`saldoCuti`).
 * 2. Leave expiration date (`expiredDate`).
 * 3. Used leave count (`usedCuti`).
 *
 * The card appearance (e.g., corner radius, shadow) should be defined
 * within the inflated XML layout file (e.g., using a MaterialCardView as the root).
 */
class CustomInfoCutiCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val tvInfoSaldoCuti: MaterialTextView
    private val tvInfoExpired: MaterialTextView
    private val tvInfoUsed: MaterialTextView
    private val tvInfoCutiTitle: MaterialTextView

    init {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.custom_info_cuti_card, this, true)

        tvInfoSaldoCuti = view.findViewById(R.id.tvInfoSaldoCuti)
        tvInfoExpired = view.findViewById(R.id.tvInfoExpired)
        tvInfoUsed = view.findViewById(R.id.tvInfoUsed)
        tvInfoCutiTitle = view.findViewById(R.id.tvInfoCutiTitle)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.CustomInfoCutiCard,
                0,
                0
            )
            try {
                val title = typedArray.getString(R.styleable.CustomInfoCutiCard_infoCutiTitle)
                val saldoCuti = typedArray.getInt(R.styleable.CustomInfoCutiCard_saldoCuti, 0)
                val expiredDate = typedArray.getString(R.styleable.CustomInfoCutiCard_expiredDate)
                val usedCuti = typedArray.getInt(R.styleable.CustomInfoCutiCard_usedCuti, 0)

                title?.let { t -> setTitle(t) }
                setSaldoCuti(saldoCuti)
                expiredDate?.let { date -> setExpiredDate(date) }
                setUsedCuti(usedCuti)

            } finally {
                typedArray.recycle()
            }
        }
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