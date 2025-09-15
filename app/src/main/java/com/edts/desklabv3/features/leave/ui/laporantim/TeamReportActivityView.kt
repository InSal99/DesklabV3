package com.edts.desklabv3.features.leave.ui.laporantim

import EmployeeActivityAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.FragmentTeamReportActivityViewBinding
import com.edts.desklabv3.features.SpaceItemDecoration

class TeamReportActivityView : Fragment() {
    private var _binding: FragmentTeamReportActivityViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var employeeActivityAdapter: EmployeeActivityAdapter
    private lateinit var chipTeamReportAdapter: ChipTeamReportAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamReportActivityViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChipRecyclerView()
        setupActivityRecyclerView()
    }

    private fun setupChipRecyclerView() {
        chipTeamReportAdapter = ChipTeamReportAdapter { position, chipText ->
            onChipSelected(position, chipText)
        }

        binding.rvChipTeamReport.apply {
            adapter = chipTeamReportAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            addItemDecoration(
                SpaceItemDecoration(
                    requireContext(),
                    R.dimen.chip_item_spacing,
                    SpaceItemDecoration.HORIZONTAL
                )
            )
        }
    }

    private fun setupActivityRecyclerView() {
        updateActivityList("Mingguan")

        binding.rvEmployeeActivities.apply {
            adapter = employeeActivityAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun onChipSelected(position: Int, chipText: String) {
        updateActivityList(chipText)
    }

    private fun updateActivityList(chipType: String) {
        val activityImages = when (chipType) {
            "Mingguan" -> listOf(
                R.drawable.activity_mingguan_1,
                R.drawable.activity_mingguan_2,
                R.drawable.activity_mingguan_3,
                R.drawable.activity_mingguan_4,
                R.drawable.activity_mingguan_5
            )
            "Bulanan" -> listOf(
                R.drawable.activity_bulanan_1,
                R.drawable.activity_bulanan_2,
                R.drawable.activity_bulanan_3,
                R.drawable.activity_bulanan_4,
                R.drawable.activity_bulanan_5
            )
            else -> listOf(
                R.drawable.activity_mingguan_1,
                R.drawable.activity_mingguan_2,
                R.drawable.activity_mingguan_3,
                R.drawable.activity_mingguan_4,
                R.drawable.activity_mingguan_5,
                R.drawable.activity_bulanan_1,
                R.drawable.activity_bulanan_2,
                R.drawable.activity_bulanan_3,
                R.drawable.activity_bulanan_4,
                R.drawable.activity_bulanan_5
            )
        }

        employeeActivityAdapter = EmployeeActivityAdapter(activityImages)
        binding.rvEmployeeActivities.adapter = employeeActivityAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}