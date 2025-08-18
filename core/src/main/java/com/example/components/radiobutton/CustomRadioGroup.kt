package com.example.components.radiobutton

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import androidx.core.content.withStyledAttributes
import com.example.components.R

class CustomRadioGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RadioGroup(context, attrs) {

    private var customRadioGroupDelegate: CustomRadioGroupDelegate? = null
    private val radioButtonDataMap = mutableMapOf<Int, Any?>()
    private var normalTextAppearance = R.style.RadioTextAppearance_Normal
    private var selectedTextAppearance = R.style.RadioTextAppearance_Selected
    private var disabledTextAppearance = R.style.RadioTextAppearance_Disabled
    private var disabledSelectedTextAppearance = R.style.RadioTextAppearance_DisabledSelected
    private var buttonSpacing = 0
    private var compoundDrawablePadding = 8

    init {
        attrs?.let {
            context.withStyledAttributes(it, R.styleable.DynamicRadioGroup) {

                normalTextAppearance = getResourceId(
                    R.styleable.DynamicRadioGroup_radioNormalTextAppearance,
                    R.style.RadioTextAppearance_Normal
                )
                selectedTextAppearance = getResourceId(
                    R.styleable.DynamicRadioGroup_radioSelectedTextAppearance,
                    R.style.RadioTextAppearance_Selected
                )
                disabledTextAppearance = getResourceId(
                    R.styleable.DynamicRadioGroup_radioSelectedTextAppearance,
                    R.style.RadioTextAppearance_Disabled
                )
                disabledSelectedTextAppearance = getResourceId(
                    R.styleable.DynamicRadioGroup_radioDisabledSelectedTextAppearance,
                    R.style.RadioTextAppearance_DisabledSelected
                )
                buttonSpacing = getDimensionPixelSize(
                    R.styleable.DynamicRadioGroup_radioButtonSpacing,
                    resources.getDimensionPixelSize(R.dimen.margin_8dp)
                )
                compoundDrawablePadding = getDimensionPixelSize(
                    R.styleable.DynamicRadioGroup_radioCompoundDrawablePadding,
                    resources.getDimensionPixelSize(R.dimen.margin_8dp)
                )
            }
        }
    }

    fun <T> setData(dataList: List<T>, itemDisplayProvider: (T) -> String) {
        removeAllViews()
        radioButtonDataMap.clear()

        dataList.forEachIndexed { index, item ->
            val radioButton = CustomRadioButton(context).apply {
                id = View.generateViewId()
                text = itemDisplayProvider(item)

                setTextAppearances(
                    normal = normalTextAppearance,
                    selected = selectedTextAppearance,
                    disabled = disabledTextAppearance,
                    disabledSelected = disabledSelectedTextAppearance
                )

                layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    resources.getDimensionPixelSize(R.dimen.line_height_24)
                ).apply {
                    setMargins(0, 0, 0, buttonSpacing)
                }
            }

            radioButtonDataMap[radioButton.id] = item
            addView(radioButton)
        }

        setOnCheckedChangeListener { _, checkedId ->
            val position = getPositionById(checkedId)
            val data = radioButtonDataMap[checkedId]

            Log.d("CustomRadioGroup", "Item selected at position: $position, with data: $data")

            customRadioGroupDelegate?.onItemSelected(position, data)
            updateAllRadioButtonsTextAppearance()
        }
    }

    private fun updateAllRadioButtonsTextAppearance() {
        for (i in 0 until childCount) {
            val radioButton = getChildAt(i) as? CustomRadioButton
            radioButton?.let {
                it.setTextAppearances(normalTextAppearance, selectedTextAppearance, disabledTextAppearance)
            }
        }
    }

    fun setOnItemSelectedListener(listener: CustomRadioGroupDelegate) {
        this.customRadioGroupDelegate = listener
    }

    fun getSelectedData(): Any? {
        return radioButtonDataMap[checkedRadioButtonId]
    }

    fun getSelectedPosition(): Int {
        return getPositionById(checkedRadioButtonId)
    }

    fun selectItem(position: Int) {
        if (position >= 0 && position < childCount) {
            val radioButton = getChildAt(position) as? CustomRadioButton
            radioButton?.isChecked = true
        }
    }

    fun selectItemByData(data: Any) {
        val entry = radioButtonDataMap.entries.find { it.value == data }
        entry?.let { check(it.key) }
    }

    private fun getPositionById(id: Int): Int {
        for (i in 0 until childCount) {
            if (getChildAt(i).id == id) return i
        }
        return -1
    }
}