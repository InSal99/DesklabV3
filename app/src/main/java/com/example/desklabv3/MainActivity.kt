package com.example.desklabv3

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.components.radiobutton.CustomRadioGroup
import com.example.components.radiobutton.CustomRadioGroupDelegate
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

        binding.cvMyEventCard.setOnClickListener {
            val intent = Intent(this, MyEventsActivity::class.java)
            startActivity(intent)
        }

        binding.cvNotificationCard.setOnClickListener {
            val intent = Intent(this, EventInvitationActivity::class.java)
            startActivity(intent)
        }

        // The following lines can be uncommented if you intend to use the radio buttons.
        // dynamicRadioGroup = findViewById(R.id.rbTest)
        // setupRadioButtons()
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