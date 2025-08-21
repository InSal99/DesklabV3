package com.example.desklabv3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.components.databinding.ActivityCustomComponentBinding

class CustomComponentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomComponentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using View Binding
        binding = ActivityCustomComponentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set a title for the activity
        supportActionBar?.title = "Custom Components"
    }
}