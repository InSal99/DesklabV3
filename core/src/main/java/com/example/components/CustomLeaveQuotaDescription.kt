package com.example.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.google.android.material.textview.MaterialTextView
import java.util.regex.Pattern

/**
 * A custom component that displays a title and a corresponding leave quota value.
 *
 * ## Overview
 * This view is designed to encapsulate the presentation of a leave quota, providing a consistent
 * look and feel across the application. It consists of a title label and a value label for the quota.
 *
 * The component includes logic to automatically format the quota text and change its color to red
 * if the quota value is zero or negative, providing an immediate visual cue to the user.
 *
 * ## XML Attributes
 * You can customize the component directly in your XML layouts using the following attributes:
 * - `app:leaveQuotaTitleDesc`: Sets the text for the title label (e.g., "Annual Leave Balance").
 * - `app:leaveQuotaDesc`: Sets the text for the quota value (e.g., "10", "-5", or "10 Hari").
 *
 * ### Example Usage in XML:
 * ```xml
 * <com.example.components.CustomLeaveQuotaDescription
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * app:leaveQuotaTitleDesc="Saldo Cuti Tahunan"
 * app:leaveQuotaDesc="-5" />
 * ```
 *
 * ## Programmatic Access
 * The title and quota can also be set or updated programmatically through the `title` and `quota` properties.
 *
 * ### Example Usage in Kotlin:
 * ```kotlin
 * val quotaView = findViewById<CustomLeaveQuotaDescription>(R.id.my_quota_view)
 * quotaView.title = "Sisa Cuti"
 * quotaView.quota = "-2 Hari" // The text color will automatically turn red.
 * ```
 */
class CustomLeaveQuotaDescription @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val tvLeaveQuotaTitle: MaterialTextView
    private val tvLeaveQuota: MaterialTextView
    private val defaultTextColor: Int

    /**
     * The title text displayed above the quota value.
     */
    var title: String? = null
        set(value) {
            field = value
            tvLeaveQuotaTitle.text = value
        }

    /**
     * The quota value text.
     * When set, it automatically appends " Hari" if a plain number is provided.
     * The text color will change to red if the parsed number is 0 or less.
     */
    var quota: String? = null
        set(value) {
            field = value
            val displayText = formatQuotaText(value)
            tvLeaveQuota.text = displayText
            updateQuotaTextColor(displayText)
        }

    init {
        // Inflate the layout resource and attach it to this view group.
        LayoutInflater.from(context).inflate(R.layout.custom_leave_quota_description, this, true)

        // Bind the view components from the inflated layout.
        tvLeaveQuotaTitle = findViewById(R.id.tvLeaveQuotaTitle)
        tvLeaveQuota = findViewById(R.id.tvLeaveQuota)

        // Store the default text color to revert to when the quota is positive.
        defaultTextColor = tvLeaveQuota.currentTextColor

        // Process and apply custom attributes from XML.
        attrs?.let { applyAttributes(it) }
    }

    /**
     * Reads the custom styleable attributes from XML and applies them to the view.
     */
    private fun applyAttributes(attrs: AttributeSet) {
        context.withStyledAttributes(attrs, R.styleable.CustomLeaveQuotaDescription, 0, 0) {
            // Setting the properties will trigger their custom setters to update the UI.
            title = getString(R.styleable.CustomLeaveQuotaDescription_leaveQuotaTitleDesc)
            quota = getString(R.styleable.CustomLeaveQuotaDescription_leaveQuotaDesc)
        }
    }

    /**
     * Formats the raw quota string to ensure it's consistently displayed.
     * If the input is a plain number (positive or negative), it appends " Hari".
     * @param quotaText The raw string from the attribute or programmatic setter.
     * @return The formatted string to be displayed.
     */
    private fun formatQuotaText(quotaText: String?): String? {
        // Check if the string can be parsed as an integer.
        return if (quotaText?.toIntOrNull() != null) {
            "$quotaText Hari"
        } else {
            quotaText
        }
    }

    /**
     * Updates the text color of the quota value based on its numerical content.
     * - Red: If the value is 0 or less.
     * - Default: Otherwise.
     * @param displayText The displayed text containing the quota value (e.g., "-5 Hari").
     */
    private fun updateQuotaTextColor(displayText: String?) {
        if (displayText.isNullOrBlank()) {
            tvLeaveQuota.setTextColor(defaultTextColor)
            return
        }

        // CORRECTED: Regex now includes an optional negative sign (-?)
        // to correctly parse negative numbers.
        val matcher = Pattern.compile("-?\\d+").matcher(displayText)
        val quotaValue = if (matcher.find()) {
            matcher.group(0).toIntOrNull()
        } else {
            null // No number found in the string.
        }

        // Set color to red if quota is 0 or less; otherwise, use the default color.
        val color = if (quotaValue != null && quotaValue <= 0) {
            ContextCompat.getColor(context, R.color.colorRed40)
        } else {
            defaultTextColor
        }
        tvLeaveQuota.setTextColor(color)
    }
}