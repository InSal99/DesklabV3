package com.edts.desklabv3

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.edts.components.checkbox.CustomCheckBox
import com.edts.components.checkbox.CustomCheckboxDelegate
import com.edts.components.radiobutton.CustomRadioGroup
import com.edts.components.radiobutton.CustomRadioGroupDelegate
import com.edts.components.toast.CustomToast
import com.edts.desklabv3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dynamicRadioGroup: CustomRadioGroup
    private lateinit var checkboxContainer: LinearLayout
    private val checkboxes = mutableListOf<CustomCheckBox>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dynamicRadioGroup = findViewById(R.id.rbTest)
        setupRadioButtons()

        checkboxContainer = findViewById(R.id.checkboxContainer)
        setupCheckboxes()
        setupCheckboxErrorTestButtons()
    }

    private fun setupRadioButtons() {
        val options = listOf("Option 1", "Option 2", "Option 3", "Option 4")

        dynamicRadioGroup.setData(options) { item ->
            item
        }

        dynamicRadioGroup.setOnItemSelectedListener(object : CustomRadioGroupDelegate {
            override fun onItemSelected(position: Int, data: Any?) {
                CustomToast.success(this@MainActivity, "Selected: $data at position $position")
            }
        })

    }

    private fun setupCheckboxes() {
        val options = listOf("Option 1", "Option 2", "Option 3", "Option 4")

        // Clear existing checkboxes
        checkboxContainer.removeAllViews()
        checkboxes.clear()

        options.forEachIndexed { index, option ->
            val checkbox = CustomCheckBox(this).apply {
                text = option
                id = View.generateViewId()

                // Set layout params
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    resources.getDimensionPixelSize(com.edts.components.R.dimen.line_height_24)
                ).apply {
                    if (index > 0) {
                        topMargin = resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_8dp)
                    }
                }

                // Set click listener for each checkbox
                setOnClickListener {
                    CustomToast.success(this@MainActivity, "${if (isChecked) "Checked" else "Unchecked"}: $option")
                    // Clear error when user interacts with any checkbox
                    if (isChecked) {
                        setErrorState(false)
                    }
                }

                // Set delegate for checkbox state changes
                setCustomCheckBoxDelegate(object : CustomCheckboxDelegate {
                    override fun onCheckChanged(checkBox: CustomCheckBox, isChecked: Boolean) {
                        Log.d("CustomCheckBox", "Checkbox ${checkBox.text} changed to: $isChecked")
                        // Clear error when user checks any checkbox
                        if (isChecked) {
                            checkBox.setErrorState(false)
                        }
                    }
                })
            }

            checkboxContainer.addView(checkbox)
            checkboxes.add(checkbox)
        }
    }

    private fun setupCheckboxErrorTestButtons() {
        val toggleErrorButton = findViewById<Button>(R.id.toggleCheckboxErrorButton)
        val clearErrorButton = findViewById<Button>(R.id.clearCheckboxErrorsButton)

        toggleErrorButton.setOnClickListener {
            val hasError = checkboxes.firstOrNull()?.isErrorState() ?: false
            if (hasError) {
                setErrorStateOnAllCheckboxes(false)
                Toast.makeText(this, "Errors cleared", Toast.LENGTH_SHORT).show()
            } else {
                setErrorStateOnAllCheckboxes(true)
                Toast.makeText(this, "Errors shown", Toast.LENGTH_SHORT).show()
            }
        }

        clearErrorButton.setOnClickListener {
            setErrorStateOnAllCheckboxes(false)
            Toast.makeText(this, "All errors cleared", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setErrorStateOnAllCheckboxes(error: Boolean) {
        checkboxes.forEach { checkbox ->
            checkbox.setErrorState(error)
            Log.d("MainActivity", "Set error $error on checkbox: ${checkbox.text}")
        }
    }
}