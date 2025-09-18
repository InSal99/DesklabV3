package com.edts.desklabv3.features.home.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.utils.resolveColorAttribute
import com.edts.desklabv3.R
import com.edts.desklabv3.core.util.createTopShadowBackgroundCustom
import com.edts.desklabv3.databinding.FragmentHomeDaftarRsvpViewBinding
import com.edts.desklabv3.features.home.model.ActivityItem
import com.edts.desklabv3.features.home.model.ActivityType
import com.edts.desklabv3.features.SpaceItemDecoration

class HomeAttendanceView : Fragment() {
    private var _binding: FragmentHomeDaftarRsvpViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var chipAdapter: ChipHomeCalendarAdapter
    private lateinit var groupedActivitiesAdapter: GroupedActivitiesAdapter

    private var chipDecoration: RecyclerView.ItemDecoration? = null

    private val allActivities = listOf(
        ActivityItem("Simplifying UX Complexity: Bridging the Gap Between Design and Development", "13:00 - 15:00 WIB", true, ActivityType.Event, "2025-07-24"),
        ActivityItem("IT Security Awareness: Stay Ahead of Threats, Stay Secure", "15:00 - 16:00 WIB", true, ActivityType.Event, "2025-07-24"),
        ActivityItem("Tony Varian Pradipta Suharjito", "Cuti Tahunan", false, ActivityType.Cuti, "2025-07-24"),
        ActivityItem("I Putu Gede Devy Soesyawanthara Toestha", "Cuti Progresif 2", false, ActivityType.Cuti, "2025-07-24"),
        ActivityItem("Elrepyan Upadio Jatikalimasada", "Izin Khusus", false, ActivityType.KerjaKhusus, "2025-07-24"),
        ActivityItem("Aldi Kurniawan", "Perdin Dalam Kota", false, ActivityType.KerjaKhusus, "2025-07-25"),
        ActivityItem("Elrepyan Upadio Jatikalimasada", "Izin Khusus", false, ActivityType.KerjaKhusus, "2025-07-25"),
        ActivityItem("Mika Juliani", "Ulang Tahun", false, ActivityType.UlangTahun, "2025-07-25")
    )

    private var filteredActivities = allActivities

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeDaftarRsvpViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clContentLayout.background = requireContext().createTopShadowBackgroundCustom(
            fillColor = requireContext().resolveColorAttribute(android.R.attr.colorBackground, com.edts.components.R.color.colorFFF),
            shadowOffsetDp = 12
        )

        setupChipRecyclerView()
        setupGroupedActivitiesRecyclerView()
        updateEmptyStateVisibility()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupChipRecyclerView() {
        val chipTexts = arrayOf(
            "Semua",
            "Event",
            "Cuti",
            "Kerja Khusus",
            "Libur",
            "Ulang Tahun"
        )

        chipAdapter = ChipHomeCalendarAdapter(
            chipTexts = chipTexts,
            selectedPosition = 0
        ) { position, chipText ->
            handleChipSelection(position, chipText)
        }

        binding.rvChip.apply {
            adapter = chipAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            chipDecoration?.let { removeItemDecoration(it) }

            chipDecoration = SpaceItemDecoration(
                context = requireContext(),
                spaceResId = R.dimen.chip_item_spacing,
                orientation = SpaceItemDecoration.HORIZONTAL
            )
            addItemDecoration(chipDecoration!!)
        }
    }

    private fun setupGroupedActivitiesRecyclerView() {
        val groupedData = GroupedActivitiesAdapter.groupActivitiesByDate(filteredActivities)

        groupedActivitiesAdapter = GroupedActivitiesAdapter(groupedData) { activity ->
            handleActivityClick()
        }

        binding.rvActivitiesGroup.apply {
            adapter = groupedActivitiesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
        }
    }

    private fun handleChipSelection(position: Int, chipText: String) {
        filteredActivities = when (chipText) {
            "Semua" -> allActivities
            "Event" -> allActivities.filter { it.type == ActivityType.Event }
            "Cuti" -> allActivities.filter { it.type == ActivityType.Cuti }
            "Kerja Khusus" -> allActivities.filter { it.type == ActivityType.KerjaKhusus }
            "Libur" -> allActivities.filter { it.type == ActivityType.Libur }
            "Ulang Tahun" -> allActivities.filter { it.type == ActivityType.UlangTahun }
            else -> allActivities
        }

        val groupedData = GroupedActivitiesAdapter.groupActivitiesByDate(filteredActivities)
        groupedActivitiesAdapter.updateDateGroups(groupedData)

        updateEmptyStateVisibility()
    }

    private fun updateEmptyStateVisibility() {
        if (filteredActivities.isEmpty()) {
            binding.ivEmptyState.visibility = View.VISIBLE
            binding.rvActivitiesGroup.visibility = View.INVISIBLE
        } else {
            binding.ivEmptyState.visibility = View.GONE
            binding.rvActivitiesGroup.visibility = View.VISIBLE
        }
    }

    private fun handleActivityClick() {
        navigateToEventDetailAttendance()
    }

    private fun navigateToEventDetailAttendance() {
        val result = bundleOf(
            "fragment_class" to "EventDetailViewAttendance"
        )
        parentFragmentManager.setFragmentResult("navigate_fragment", result)
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeAttendanceView()
    }
}