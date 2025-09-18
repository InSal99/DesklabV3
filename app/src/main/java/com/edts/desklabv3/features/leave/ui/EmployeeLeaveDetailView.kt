package com.edts.desklabv3.features.leave.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.R
import com.edts.desklabv3.core.withPushAnimation
import com.edts.desklabv3.databinding.FragmentEmployeeLeaveDetailBinding
import com.edts.desklabv3.features.SpaceItemDecoration
import com.edts.desklabv3.features.event.model.LeaveQuota

class EmployeeLeaveDetailView : Fragment() {
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

        binding.cvEmployeeLeaveDetailHeader.delegate = object : com.edts.components.header.HeaderDelegate {
            override fun onLeftButtonClicked() {
                parentFragmentManager.popBackStack()
            }

            override fun onRightButtonClicked() {
                // TODO: Implement if needed
            }
        }

        binding.cvMultiDetail2.setOnClickListener{
//            val fragment = EmployeeLeaveHistoryView()
//            parentFragmentManager.beginTransaction()
//                .withPushAnimation()
//                .replace(com.edts.desklabv3.R.id.fragment_container, fragment)
//                .addToBackStack(EmployeeLeaveDetailView::class.java.simpleName)
//                .commit()

            val result = bundleOf("fragment_class" to "EmployeeLeaveHistoryView")
            parentFragmentManager.setFragmentResult("navigate_fragment", result)
        }

        setupLeaveQuotaRecyclerView()
    }

    private fun setupLeaveQuotaRecyclerView() {
        val leaveQuotaData = listOf(
            LeaveQuota("Cuti Tahun Ini", 5, "15/10/2025", 1),
            LeaveQuota("Cuti Tahun Sebelumnya", 8, "15/10/2025", 2),
            LeaveQuota("Cuti Progresif", 3, "15/10/2025", 0)
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