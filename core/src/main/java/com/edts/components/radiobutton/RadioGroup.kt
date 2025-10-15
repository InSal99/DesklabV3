package com.edts.components.radiobutton

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.core.content.withStyledAttributes
import com.edts.components.R

class RadioGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RadioGroup(context, attrs) {
    private var radioGroupDelegate: RadioGroupDelegate? = null
    private val radioButtonDataMap = mutableMapOf<Int, Any?>()
    private var normalTextAppearance = R.style.RadioTextAppearance_Normal
    private var selectedTextAppearance = R.style.RadioTextAppearance_Selected
    private var disabledTextAppearance = R.style.RadioTextAppearance_Disabled
    private var disabledSelectedTextAppearance = R.style.RadioTextAppearance_DisabledSelected
    private var errorTextAppearance = R.style.RadioTextAppearance_Normal
    private var compoundDrawablePadding = 8

    var buttonSpacing: Int = 0
        set(value) {
            field = value
            updateButtonMargins()
        }

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
                    R.styleable.DynamicRadioGroup_radioDisabledTextAppearance,
                    R.style.RadioTextAppearance_Disabled
                )
                disabledSelectedTextAppearance = getResourceId(
                    R.styleable.DynamicRadioGroup_radioDisabledSelectedTextAppearance,
                    R.style.RadioTextAppearance_DisabledSelected
                )
                errorTextAppearance = getResourceId(
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

    private fun updateAllRadioButtonsTextAppearance() {
        for (i in 0 until childCount) {
            val radioButton = getChildAt(i) as? RadioButton
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

    private fun updateButtonMargins() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.layoutParams is LayoutParams) {
                val params = child.layoutParams as LayoutParams
                params.setMargins(0, 0, 0, buttonSpacing)
                child.layoutParams = params
            }
        }
    }

    private fun getPositionById(id: Int): Int {
        for (i in 0 until childCount) {
            if (getChildAt(i).id == id) return i
        }
        return -1
    }

    fun <T> setData(dataList: List<T>, itemDisplayProvider: (T) -> String) {
        removeAllViews()
        radioButtonDataMap.clear()

        dataList.forEachIndexed { index, item ->
            val radioButton = RadioButton(context).apply {
                id = View.generateViewId()
                text = itemDisplayProvider(item)

                setTextAppearances(
                    normal = normalTextAppearance,
                    selected = selectedTextAppearance,
                    disabled = disabledTextAppearance,
                    disabledSelected = disabledSelectedTextAppearance,
                    error = errorTextAppearance
                )

                layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
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

            radioGroupDelegate?.onItemSelected(position, data)
            updateAllRadioButtonsTextAppearance()
        }
    }

    fun setErrorStateOnAll(error: Boolean) {
        for (i in 0 until childCount) {
            val radioButton = getChildAt(i) as? RadioButton
            radioButton?.setErrorState(error)
        }
    }

    fun setErrorStateOnPosition(position: Int, error: Boolean) {
        if (position >= 0 && position < childCount) {
            val radioButton = getChildAt(position) as? RadioButton
            radioButton?.setErrorState(error)
        }
    }

    fun setErrorStateOnData(data: Any, error: Boolean) {
        val entry = radioButtonDataMap.entries.find { it.value == data }
        entry?.let { mapEntry ->
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (child.id == mapEntry.key && child is RadioButton) {
                    child.setErrorState(error)
                    break
                }
            }
        }
    }

    fun clearAllErrorStates() {
        setErrorStateOnAll(false)
    }

    fun setOnItemSelectedListener(listener: RadioGroupDelegate) {
        this.radioGroupDelegate = listener
    }

    fun getSelectedData(): Any? {
        return radioButtonDataMap[checkedRadioButtonId]
    }

    fun getSelectedPosition(): Int {
        return getPositionById(checkedRadioButtonId)
    }

    fun selectItem(position: Int) {
        if (position >= 0 && position < childCount) {
            val radioButton = getChildAt(position) as? RadioButton
            radioButton?.isChecked = true
        }
    }

    fun selectItemByData(data: Any) {
        val entry = radioButtonDataMap.entries.find { it.value == data }
        entry?.let { check(it.key) }
    }
}