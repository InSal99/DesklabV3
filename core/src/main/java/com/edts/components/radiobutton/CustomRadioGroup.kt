package com.edts.components.radiobutton

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import androidx.core.content.withStyledAttributes
import com.edts.components.R

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
    private var errorTextAppearance = R.style.RadioTextAppearance_Normal // Add error text appearance
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
                    R.styleable.DynamicRadioGroup_radioDisabledTextAppearance, // Fixed: should be disabled, not selected
                    R.style.RadioTextAppearance_Disabled
                )
                disabledSelectedTextAppearance = getResourceId(
                    R.styleable.DynamicRadioGroup_radioDisabledSelectedTextAppearance,
                    R.style.RadioTextAppearance_DisabledSelected
                )
                errorTextAppearance = getResourceId( // Add error text appearance
                    R.styleable.DynamicRadioGroup_radioErrorTextAppearance,
                    R.style.RadioTextAppearance_Normal
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
                    disabledSelected = disabledSelectedTextAppearance,
                    error = errorTextAppearance // Add error text appearance
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
                it.setTextAppearances(
                    normalTextAppearance,
                    selectedTextAppearance,
                    disabledTextAppearance,
                    disabledSelectedTextAppearance,
                    errorTextAppearance
                )
            }
        }
    }

    // Method to set error state on all radio buttons
    fun setErrorStateOnAll(error: Boolean) {
        for (i in 0 until childCount) {
            val radioButton = getChildAt(i) as? CustomRadioButton
            radioButton?.setErrorState(error)
        }
    }

    // Method to set error state on a specific radio button by position
    fun setErrorStateOnPosition(position: Int, error: Boolean) {
        if (position >= 0 && position < childCount) {
            val radioButton = getChildAt(position) as? CustomRadioButton
            radioButton?.setErrorState(error)
        }
    }

    // Method to set error state on a specific radio button by data
    fun setErrorStateOnData(data: Any, error: Boolean) {
        val entry = radioButtonDataMap.entries.find { it.value == data }
        entry?.let {
            val radioButton = findViewById<CustomRadioButton>(it.key)
            radioButton?.setErrorState(error)
        }
    }

    // Method to clear all error states
    fun clearAllErrorStates() {
        setErrorStateOnAll(false)
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

    // Delegate interface
    interface CustomRadioGroupDelegate {
        fun onItemSelected(position: Int, data: Any?)
    }
}



//class CustomRadioGroup @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null
//) : RadioGroup(context, attrs) {
//
//    private var customRadioGroupDelegate: CustomRadioGroupDelegate? = null
//    private val radioButtonDataMap = mutableMapOf<Int, Any?>()
//    private var normalTextAppearance = R.style.RadioTextAppearance_Normal
//    private var selectedTextAppearance = R.style.RadioTextAppearance_Selected
//    private var disabledTextAppearance = R.style.RadioTextAppearance_Disabled
//    private var disabledSelectedTextAppearance = R.style.RadioTextAppearance_DisabledSelected
//    private var buttonSpacing = 0
//    private var compoundDrawablePadding = 8
//
//    init {
//        attrs?.let {
//            context.withStyledAttributes(it, R.styleable.DynamicRadioGroup) {
//
//                normalTextAppearance = getResourceId(
//                    R.styleable.DynamicRadioGroup_radioNormalTextAppearance,
//                    R.style.RadioTextAppearance_Normal
//                )
//                selectedTextAppearance = getResourceId(
//                    R.styleable.DynamicRadioGroup_radioSelectedTextAppearance,
//                    R.style.RadioTextAppearance_Selected
//                )
//                disabledTextAppearance = getResourceId(
//                    R.styleable.DynamicRadioGroup_radioSelectedTextAppearance,
//                    R.style.RadioTextAppearance_Disabled
//                )
//                disabledSelectedTextAppearance = getResourceId(
//                    R.styleable.DynamicRadioGroup_radioDisabledSelectedTextAppearance,
//                    R.style.RadioTextAppearance_DisabledSelected
//                )
//                buttonSpacing = getDimensionPixelSize(
//                    R.styleable.DynamicRadioGroup_radioButtonSpacing,
//                    resources.getDimensionPixelSize(R.dimen.margin_8dp)
//                )
//                compoundDrawablePadding = getDimensionPixelSize(
//                    R.styleable.DynamicRadioGroup_radioCompoundDrawablePadding,
//                    resources.getDimensionPixelSize(R.dimen.margin_8dp)
//                )
//            }
//        }
//    }
//
//    fun <T> setData(dataList: List<T>, itemDisplayProvider: (T) -> String) {
//        removeAllViews()
//        radioButtonDataMap.clear()
//
//        dataList.forEachIndexed { index, item ->
//            val radioButton = CustomRadioButton(context).apply {
//                id = View.generateViewId()
//                text = itemDisplayProvider(item)
//
//                setTextAppearances(
//                    normal = normalTextAppearance,
//                    selected = selectedTextAppearance,
//                    disabled = disabledTextAppearance,
//                    disabledSelected = disabledSelectedTextAppearance
//                )
//
//                layoutParams = RadioGroup.LayoutParams(
//                    RadioGroup.LayoutParams.WRAP_CONTENT,
//                    resources.getDimensionPixelSize(R.dimen.line_height_24)
//                ).apply {
//                    setMargins(0, 0, 0, buttonSpacing)
//                }
//            }
//
//            radioButtonDataMap[radioButton.id] = item
//            addView(radioButton)
//        }
//
//        setOnCheckedChangeListener { _, checkedId ->
//            val position = getPositionById(checkedId)
//            val data = radioButtonDataMap[checkedId]
//
//            Log.d("CustomRadioGroup", "Item selected at position: $position, with data: $data")
//
//            customRadioGroupDelegate?.onItemSelected(position, data)
//            updateAllRadioButtonsTextAppearance()
//        }
//    }
//
//    private fun updateAllRadioButtonsTextAppearance() {
//        for (i in 0 until childCount) {
//            val radioButton = getChildAt(i) as? CustomRadioButton
//            radioButton?.let {
//                it.setTextAppearances(normalTextAppearance, selectedTextAppearance, disabledTextAppearance)
//            }
//        }
//    }
//
//    fun setOnItemSelectedListener(listener: CustomRadioGroupDelegate) {
//        this.customRadioGroupDelegate = listener
//    }
//
//    fun getSelectedData(): Any? {
//        return radioButtonDataMap[checkedRadioButtonId]
//    }
//
//    fun getSelectedPosition(): Int {
//        return getPositionById(checkedRadioButtonId)
//    }
//
//    fun selectItem(position: Int) {
//        if (position >= 0 && position < childCount) {
//            val radioButton = getChildAt(position) as? CustomRadioButton
//            radioButton?.isChecked = true
//        }
//    }
//
//    fun selectItemByData(data: Any) {
//        val entry = radioButtonDataMap.entries.find { it.value == data }
//        entry?.let { check(it.key) }
//    }
//
//    private fun getPositionById(id: Int): Int {
//        for (i in 0 until childCount) {
//            if (getChildAt(i).id == id) return i
//        }
//        return -1
//    }
//}