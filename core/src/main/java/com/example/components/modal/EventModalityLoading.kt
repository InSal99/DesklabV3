package com.example.components.modal

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.components.R
import com.google.android.material.textview.MaterialTextView

/**
 * A custom view that displays a loading indicator and a text message.
 *
 * This component is designed to be used as a modality overlay to indicate a background
 * process is running. The text can be customized via the `app:modalTitle` attribute in XML
 * or programmatically.
 *
 * ### XML Usage Example:
 * ```xml
 * <com.example.components.modal.CustomEventModalityLoading
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * app:modalTitle="Please wait..." />
 * ```
 *
 * @param context The Context the view is running in.
 * @param attrs The attributes of the XML tag that is inflating the view.
 * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource.
 */
class EventModalityLoading @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val tvModalTitle: MaterialTextView

    init {
        val inflater = LayoutInflater.from(context)
        // Inflate the layout and attach it to this FrameLayout's root.
        val view = inflater.inflate(R.layout.event_modality_loading, this, true)

        // Assign the TextView from the inflated layout.
        tvModalTitle = view.findViewById(R.id.tvModalTitle)

        // --- Parse Custom XML Attributes ---
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.EventModalityLoading,
                0,
                0
            )
            try {
                val title = typedArray.getString(R.styleable.EventModalityLoading_modalLoadingTitle)
                // Set the title from the attribute, or use the default text from the layout if not provided.
                title?.let { t -> setTitle(t) }
            } finally {
                typedArray.recycle()
            }
        }
    }

    /**
     * Sets the title text for the loading modal.
     *
     * @param title The string to be displayed as the loading message
     * (e.g., "Loading data...").
     */
    fun setTitle(title: String) {
        tvModalTitle.text = title
    }
}
