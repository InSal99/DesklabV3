package com.edts.desklabv3.features.leave.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.R
import com.edts.desklabv3.databinding.FragmentEmployeeLeaveDetailBinding
import com.edts.desklabv3.features.SpaceItemDecoration
import com.edts.desklabv3.features.event.model.LeaveQuota
import com.google.android.material.transition.MaterialSharedAxis

class EmployeeLeaveDetailFragment : Fragment() {
    private var _binding: FragmentEmployeeLeaveDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeeLeaveDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLeaveQuotaRecyclerView()
    }

    /**
     * Sets up the horizontal RecyclerView for leave quota cards.
     */
    private fun setupLeaveQuotaRecyclerView() {
        // Create sample data. In a real app, this would come from an API.
        val leaveQuotaData = listOf(
            LeaveQuota("Cuti Tahun Ini", 5, "15/10/2025", 1),
            LeaveQuota("Cuti Tahun Sebelumnya", 8, "15/10/2025", 2),
            LeaveQuota("Cuti Tahun Progresif", 3, "15/10/2025", 0)
        )

        val adapter = EmployeeLeaveQuotaAdapter(leaveQuotaData)
        val itemDecoration = SpaceItemDecoration(
            requireContext(),
            R.dimen.margin_8dp,
            SpaceItemDecoration.HORIZONTAL
        )

        binding.rvLeaveInfoCards.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvLeaveInfoCards.addItemDecoration(itemDecoration)
        binding.rvLeaveInfoCards.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}