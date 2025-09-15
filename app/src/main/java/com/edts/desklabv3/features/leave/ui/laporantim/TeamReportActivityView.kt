package com.edts.desklabv3.features.leave.ui.laporantim

import EmployeeActivityAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.toast.Toast
import com.edts.components.tray.BottomTray
import com.edts.desklabv3.R
import com.edts.desklabv3.core.dp
import com.edts.desklabv3.databinding.FragmentTeamReportActivityViewBinding
import com.edts.desklabv3.features.SpaceItemDecoration

class TeamReportActivityView : Fragment() {
    private var _binding: FragmentTeamReportActivityViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var employeeActivityAdapter: EmployeeActivityAdapter
    private lateinit var chipTeamReportAdapter: ChipTeamReportAdapter
    private var selectedWeekPosition: Int = 0

    private val weekData = listOf(
        "Pekan 1" to "01 - 07 Sept",
        "Pekan 2" to "08 - 14 Sept",
        "Pekan 3" to "15 - 21 Sept",
        "Pekan 4" to "22 - 30 Sept"
    )

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
        updateFilterDisplay()

        binding.cvFilterHorizontal.setOnClickListener {
            Log.d("TeamReportView", "Filter clicked")
            setupBottomTray()
        }
    }

    private fun setupBottomTray() {
        val bottomTray = BottomTray.newInstance(
            title = "Pilih Pekan",
            showDragHandle = true,
            showFooter = false
        )

        val recyclerView = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = WeekFilterAdapter(
                items = weekData,
                onClick = { card, position ->
                    selectedWeekPosition = position
                    updateFilterDisplay()
                    Toast.info(requireContext(), "Selected: ${card.titleText}")
                    bottomTray.dismiss()
                },
                selectedPosition = selectedWeekPosition
            )

            addItemDecoration(
                SpaceItemDecoration(
                    context = requireContext(),
                    spaceResId = com.edts.components.R.dimen.margin_8dp,
                    orientation = SpaceItemDecoration.VERTICAL
                )
            )
        }

        val container = FrameLayout(requireContext()).apply {
            val hMargin = resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_16dp)
            val vMargin = resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_8dp)

            addView(
                recyclerView,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(hMargin, vMargin, hMargin, vMargin)
                    clipChildren = false
                    clipToPadding = false
                }
            )
        }

        bottomTray.setContentView(container)
        bottomTray.show(parentFragmentManager, "BottomTrayPilihPekan")
    }

    private fun updateFilterDisplay() {
        val selectedWeek = weekData[selectedWeekPosition]
        binding.cvFilterHorizontal.apply {
            title = selectedWeek.first
            description = selectedWeek.second
        }
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

