package com.edts.desklabv3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.edts.components.radiobutton.CustomRadioGroup
import com.edts.components.radiobutton.CustomRadioGroupDelegate
import com.edts.components.toast.CustomToast
import com.edts.desklabv3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dynamicRadioGroup: CustomRadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        dynamicRadioGroup = findViewById(R.id.rbTest)
//        setupRadioButtons()
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
}