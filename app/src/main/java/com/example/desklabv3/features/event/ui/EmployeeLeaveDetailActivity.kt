package com.example.desklabv3.features.event.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.components.R
import com.example.desklabv3.databinding.ActivityEmployeeLeaveDetailBinding
import com.example.desklabv3.features.event.ui.LeaveQuotaAdapter
import com.example.desklabv3.features.event.model.LeaveQuota

class EmployeeLeaveDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeLeaveDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeLeaveDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLeaveQuotaRecycler()
    }

    /**
     * Sets up the horizontal RecyclerView for leave quota cards.
     */
    private fun setupLeaveQuotaRecycler() {
        // Create sample data. In a real app, this would come from an API.
        val leaveQuotaData = listOf(
            LeaveQuota("Cuti Tahun Ini", 5, "15/10/2025", 1),
            LeaveQuota("Cuti Tahun Sebelumnya", 8, "15/10/2025", 2),
            LeaveQuota("Cuti Tahun Progresif", 3, "15/10/2025", 0)
        )

        // Create the adapter
        val adapter = LeaveQuotaAdapter(leaveQuotaData)

        // Setup RecyclerView
        binding.rvLeaveInfoCards.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvLeaveInfoCards.adapter = adapter
    }
}
