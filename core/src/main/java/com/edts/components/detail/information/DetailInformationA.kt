package com.edts.components.detail.information

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import com.edts.components.R
import com.edts.components.databinding.DetailInformationABinding

class DetailInformationA @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = DetailInformationABinding.inflate(LayoutInflater.from(context), this, true)

    // Delegate property
    var delegate: DetailInformationADelegate? = null

    var icon: Drawable?
        get() = binding.ivInfoIcon.drawable
        set(value) {
            binding.ivInfoIcon.setImageDrawable(value)
            binding.ivInfoIcon.isVisible = value != null
        }

    var title: CharSequence?
        get() = binding.tvInfoTitle.text
        set(value) {
            binding.tvInfoTitle.text = value
            binding.tvInfoTitle.isVisible = !value.isNullOrEmpty()
        }

    var description: CharSequence?
        get() = binding.tvDetailDescription.text
        set(value) {
            binding.tvDetailDescription.text = value
            binding.tvDetailDescription.isVisible = !value.isNullOrEmpty()
        }

    var hasAction: Boolean = false
        set(value) {
            field = value
            binding.btnDetailAction1.isVisible = value
            binding.btnDetailAction2.isVisible = value
            updateDescriptionConstraints()
        }

    // Expose the buttons for external configuration
    val actionButton1: com.edts.components.button.Button
        get() = binding.btnDetailAction1

    val actionButton2: com.edts.components.button.Button
        get() = binding.btnDetailAction2

    init {
        initAttrs(context, attrs)
        setupClickListeners()
        // Set default hasAction to false (buttons hidden by default)
        hasAction = false
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        attrs ?: return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DetailInformationA, 0, 0)

        try {
            icon = typedArray.getDrawable(R.styleable.DetailInformationA_infoIcon)
            title = typedArray.getString(R.styleable.DetailInformationA_infoTitle)
            description = typedArray.getString(R.styleable.DetailInformationA_infoDescription)
            hasAction = typedArray.getBoolean(R.styleable.DetailInformationA_hasAction, false)
        } finally {
            typedArray.recycle()
        }
    }

    private fun setupClickListeners() {
        binding.btnDetailAction1.setOnClickListener {
            delegate?.onAction1Clicked(this)
        }

        binding.btnDetailAction2.setOnClickListener {
            delegate?.onAction2Clicked(this)
        }
    }

    private fun updateDescriptionConstraints() {
        // Use LayoutParams to update constraints instead of ConstraintSet
        val layoutParams = binding.tvDetailDescription.layoutParams as LayoutParams

        if (hasAction) {
            // When buttons are visible, description should be constrained to buttons
            layoutParams.bottomToTop = R.id.btnDetailAction2
            layoutParams.bottomToBottom = LayoutParams.UNSET
            layoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.margin_4dp)
        } else {
            // When buttons are hidden, description should be constrained to parent bottom
            layoutParams.bottomToTop = LayoutParams.UNSET
            layoutParams.bottomToBottom = LayoutParams.PARENT_ID
            layoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.margin_12dp)
        }

        binding.tvDetailDescription.layoutParams = layoutParams
    }

    // Convenience methods for setting button texts
    fun setAction1Text(text: CharSequence?) {
        binding.btnDetailAction1.text = text
    }

    fun setAction2Text(text: CharSequence?) {
        binding.btnDetailAction2.text = text
    }

    // Convenience methods for setting button enabled state
    fun setAction1Enabled(enabled: Boolean) {
        binding.btnDetailAction1.isEnabled = enabled
    }

    fun setAction2Enabled(enabled: Boolean) {
        binding.btnDetailAction2.isEnabled = enabled
    }
}



//class DetailInformationA @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : ConstraintLayout(context, attrs, defStyleAttr) {
//    private val binding = DetailInformationABinding.inflate(LayoutInflater.from(context), this, true)
//
//    // Delegate property
//    var delegate: DetailInformationADelegate? = null
//
//    var icon: Drawable?
//        get() = binding.ivInfoIcon.drawable
//        set(value) {
//            binding.ivInfoIcon.setImageDrawable(value)
//            binding.ivInfoIcon.isVisible = value != null
//        }
//
//    var title: CharSequence?
//        get() = binding.tvInfoTitle.text
//        set(value) {
//            binding.tvInfoTitle.text = value
//            binding.tvInfoTitle.isVisible = !value.isNullOrEmpty()
//        }
//
//    var description: CharSequence?
//        get() = binding.tvDetailDescription.text
//        set(value) {
//            binding.tvDetailDescription.text = value
//            binding.tvDetailDescription.isVisible = !value.isNullOrEmpty()
//        }
//
//    var hasAction: Boolean = false
//        set(value) {
//            field = value
//            binding.btnDetailAction1.isVisible = value
//            binding.btnDetailAction2.isVisible = value
//            updateDescriptionConstraints()
//        }
//
//    // Expose the buttons for external configuration
//    val actionButton1: com.edts.components.button.Button
//        get() = binding.btnDetailAction1
//
//    val actionButton2: com.edts.components.button.Button
//        get() = binding.btnDetailAction2
//
//    init {
//        initAttrs(context, attrs)
//        setupClickListeners()
//        // Set default hasAction to false (buttons hidden by default)
//        hasAction = false
//    }
//
//    private fun initAttrs(context: Context, attrs: AttributeSet?) {
//        attrs ?: return
//
//        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DetailInformationA, 0, 0)
//
//        try {
//            icon = typedArray.getDrawable(R.styleable.DetailInformationA_infoIcon)
//            title = typedArray.getString(R.styleable.DetailInformationA_infoTitle)
//            description = typedArray.getString(R.styleable.DetailInformationA_infoDescription)
//            hasAction = typedArray.getBoolean(R.styleable.DetailInformationA_hasAction, false)
//        } finally {
//            typedArray.recycle()
//        }
//    }
//
//    private fun setupClickListeners() {
//        binding.btnDetailAction1.setOnClickListener {
//            delegate?.onAction1Clicked(this)
//        }
//
//        binding.btnDetailAction2.setOnClickListener {
//            delegate?.onAction2Clicked(this)
//        }
//    }
//
//    private fun updateDescriptionConstraints() {
//        val constraintSet = ConstraintSet()
//        constraintSet.clone(this)
//
//        if (hasAction) {
//            // When buttons are visible, description should be constrained to buttons
//            constraintSet.connect(
//                R.id.tvDetailDescription,
//                ConstraintSet.BOTTOM,
//                R.id.btnDetailAction2,
//                ConstraintSet.TOP
//            )
//        } else {
//            // When buttons are hidden, description should be constrained to parent bottom
//            constraintSet.connect(
//                R.id.tvDetailDescription,
//                ConstraintSet.BOTTOM,
//                ConstraintSet.PARENT_ID,
//                ConstraintSet.BOTTOM
//            )
//        }
//
//        constraintSet.applyTo(this)
//    }
//
//    // Convenience methods for setting button texts
//    fun setAction1Text(text: CharSequence?) {
//        binding.btnDetailAction1.text = text
//    }
//
//    fun setAction2Text(text: CharSequence?) {
//        binding.btnDetailAction2.text = text
//    }
//
//    // Convenience methods for setting button enabled state
//    fun setAction1Enabled(enabled: Boolean) {
//        binding.btnDetailAction1.isEnabled = enabled
//    }
//
//    fun setAction2Enabled(enabled: Boolean) {
//        binding.btnDetailAction2.isEnabled = enabled
//    }
//}



//class DetailInformationA @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : ConstraintLayout(context, attrs, defStyleAttr) {
//    private val binding = DetailInformationABinding.inflate(LayoutInflater.from(context), this, true)
//
//    var icon: Drawable?
//        get() = binding.ivInfoIcon.drawable
//        set(value) {
//            binding.ivInfoIcon.setImageDrawable(value)
//            binding.ivInfoIcon.isVisible = value != null
//        }
//
//    var title: CharSequence?
//        get() = binding.tvInfoTitle.text
//        set(value) {
//            binding.tvInfoTitle.text = value
//            binding.tvInfoTitle.isVisible = !value.isNullOrEmpty()
//        }
//
//    var description: CharSequence?
//        get() = binding.tvDetailDescription.text
//        set(value) {
//            binding.tvDetailDescription.text = value
//            binding.tvDetailDescription.isVisible = !value.isNullOrEmpty()
//        }
//
//    init {
//        initAttrs(context, attrs)
//    }
//
//    private fun initAttrs(context: Context, attrs: AttributeSet?) {
//        attrs ?: return
//
//        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DetailInformationA, 0, 0)
//
//        try {
//            icon = typedArray.getDrawable(R.styleable.DetailInformationA_infoIcon)
//            title = typedArray.getString(R.styleable.DetailInformationA_infoTitle)
//            description = typedArray.getString(R.styleable.DetailInformationA_infoDescription)
//        } finally {
//            typedArray.recycle()
//        }
//    }
//}