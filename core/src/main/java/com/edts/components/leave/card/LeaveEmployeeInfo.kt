package com.edts.components.leave.card

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.edts.components.R
import com.edts.components.databinding.LeaveEmployeeInfoBinding

class LeaveEmployeeInfo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = LeaveEmployeeInfoBinding.inflate(LayoutInflater.from(context), this)

    var employeeName: CharSequence?
        get() = binding.tvName.text
        set(value) {
            binding.tvName.text = value
        }

    var employeeRole: CharSequence?
        get() = binding.tvRole.text
        set(value) {
            binding.tvRole.text = value
        }

    var employeeImage: Int?
        get() = binding.ivProfile.slotSrc
        set(value) {
            binding.ivProfile.slotSrc = value
        }

    var actionIcon: Drawable?
        get() = binding.ivInfoAction.drawable
        set(value) {
            binding.ivInfoAction.setImageDrawable(value)
        }

    init {
        binding.ivInfoAction.setImageResource(R.drawable.ic_chevron_right)
        initAttrs(attrs)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs ?: return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LeaveEmployeeInfo, 0, 0)
        try {
            employeeName = typedArray.getString(R.styleable.LeaveEmployeeInfo_employeeName)
            employeeRole = typedArray.getString(R.styleable.LeaveEmployeeInfo_employeeRole)
            employeeImage = typedArray.getResourceId(R.styleable.LeaveCard_employeeImage, R.drawable.placeholder)
        } finally {
            typedArray.recycle()
        }
    }
}