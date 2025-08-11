package com.example.desklabv3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.components.CustomRadioGroup
import com.example.desklabv3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dynamicRadioGroup: CustomRadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dynamicRadioGroup = findViewById(R.id.rbTest)
        setupRadioButtons()
    }

    private fun setupRadioButtons() {
        // Example with String data
        val stringOptions = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
        dynamicRadioGroup.setData(stringOptions) { it }

        // Example with custom data objects
        val customOptions = listOf(
            UserOption(1, "John Doe"),
            UserOption(2, "Jane Smith"),
            UserOption(3, "Bob Johnson"),
            UserOption(4, "Bob Johnson")
        )
        // dynamicRadioGroup.setData(customOptions) { "${it.name} (ID: ${it.id})" }

        // Set selection listener
        dynamicRadioGroup.setOnItemSelectedListener { position, data ->
            Toast.makeText(this, "Selected: $data at position $position", Toast.LENGTH_SHORT).show()
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