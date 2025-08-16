package com.example.desklabv3

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.components.button.CustomButton
import com.example.components.radiobutton.CustomRadioGroup
import com.example.components.toast.CustomToast
import com.example.desklabv3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dynamicRadioGroup: CustomRadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        dynamicRadioGroup = findViewById(R.id.rbTest)
//        setupRadioButtons()

//        val customButton = CustomButton(this).apply {
//            setLabel("Click Me")
//            setButtonSize(CustomButton.ButtonSize.MD)
//            layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            ).apply {
//                setMargins(16, 16, 16, 16)
//            }
//        }
//
//        val parentLayout = findViewById<LinearLayout>(R.id.main)
//        parentLayout.addView(customButton)
    }

    private fun setupRadioButtons() {
        // Example data
        val options = listOf("Option 11111111", "Option 2", "Option 33333333", "Option 4")

        dynamicRadioGroup.setData(options) { it }

        dynamicRadioGroup.setOnItemSelectedListener { position, data ->
            Log.d("RadioButton", "Selected: $data at position $position")

            CustomToast.success(this, "Selected: $data at position $position")
        }

        // Optional: Pre-select an item
        dynamicRadioGroup.selectItem(0)
    }
}

data class UserOption(val id: Int, val name: String)

// Extension function for easier usage
fun <T> CustomRadioGroup.setSimpleData(dataList: List<T>) {
    setData(dataList) { it.toString() }
}