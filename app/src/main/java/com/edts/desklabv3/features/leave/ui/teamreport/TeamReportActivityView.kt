package com.edts.desklabv3.features.leave.ui.teamreport

import EmployeeActivityAdapter
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.dropdown.filter.MonthlyPicker
import com.edts.components.dropdown.filter.MonthlyPickerDelegate
import com.edts.components.tray.BottomTray
import com.edts.components.utils.resolveColorAttribute
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.FragmentTeamReportActivityViewBinding
import com.edts.desklabv3.features.SpaceItemDecoration

class TeamReportActivityView : Fragment() {
    private var _binding: FragmentTeamReportActivityViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var employeeActivityAdapter: EmployeeActivityAdapter
    private lateinit var chipTeamReportAdapter: ChipTeamReportAdapter
    private var selectedWeekPosition: Int = 0
    private var selectedMonthPosition: Int = 0
    private var currentFilterType: String = "Mingguan"
    private var selectedYear: Int = 2025

    private val weekData = listOf(
        "Pekan 1" to "01 - 07 Sept",
        "Pekan 2" to "08 - 14 Sept",
        "Pekan 3" to "15 - 21 Sept",
        "Pekan 4" to "22 - 30 Sept"
    )

    private val monthData = listOf(
        "Januari" to "2025",
        "Februari" to "2025",
        "Maret" to "2025",
        "April" to "2025",
        "Mei" to "2025",
        "Juni" to "2025",
        "Juli" to "2025",
        "Agustus" to "2025",
        "September" to "2025",
        "Oktober" to "2025",
        "November" to "2025",
        "Desember" to "2025"
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
        if (currentFilterType == "Mingguan") {
            setupWeeklyBottomTray()
        } else {
            setupMonthlyBottomTray()
        }
    }

    private fun setupWeeklyBottomTray() {
        val bottomTray = BottomTray.newInstance(
            title = "Pilih Pekan",
            showDragHandle = true,
            showFooter = false
        )

        val recyclerView = RecyclerView(requireContext()).apply {
            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean = false
            }

            clipToPadding = false
            clipChildren = false

            adapter = FilterAdapter(
                items = weekData,
                onClick = { card, position ->
                    selectedWeekPosition = position
                    updateFilterDisplay()
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

        val scrollContainer = NestedScrollView(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            clipChildren = false
            clipToPadding = false
            isNestedScrollingEnabled = false
            addView(recyclerView)
        }

        val container = FrameLayout(requireContext()).apply {
            clipChildren = false
            clipToPadding = false
            val hMargin = resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_16dp)
            val vMargin = resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_8dp)

            addView(
                scrollContainer,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(hMargin, vMargin, hMargin, vMargin)
                }
            )
        }

        bottomTray.setTrayContentView(container)
        bottomTray.show(parentFragmentManager, "BottomTrayWeekly")
    }

    private fun setupMonthlyBottomTray() {
        var currentYear = selectedYear

        val bottomTray = BottomTray.newInstance(
            title = "Pilih Bulan",
            showDragHandle = true,
            showFooter = false
        )

        val monthChipsContainer = GridLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            columnCount = 3
            rowCount = 4
            setPadding(
                resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_12dp),
                0,
                resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_12dp),
                resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_8dp)
            )
        }

        fun updateMonthChips(year: Int) {
            monthChipsContainer.removeAllViews()

            val monthNames = listOf(
                "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
                "Jul", "Agu", "Sep", "Okt", "Nov", "Des"
            )

            monthNames.forEachIndexed { index, monthName ->
                val monthlyPicker = MonthlyPicker(requireContext()).apply {
                    setMonthLabel(monthName)
                    type = if (index == selectedMonthPosition)
                        MonthlyPicker.PickerType.SELECTED
                    else
                        MonthlyPicker.PickerType.UNSELECTED

                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 0
                        height = GridLayout.LayoutParams.WRAP_CONTENT
                        columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                        rowSpec = GridLayout.spec(GridLayout.UNDEFINED)
                        setMargins(
                            resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_4dp),
                            resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_4dp),
                            resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_4dp),
                            resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_4dp)
                        )
                    }

                    delegate = object : MonthlyPickerDelegate {
                        override fun onMonthClicked(picker: MonthlyPicker) {
                            selectedMonthPosition = index
                            selectedYear = currentYear
                            updateFilterDisplay()
                            bottomTray.dismiss()
                        }
                    }
                }

                monthChipsContainer.addView(monthlyPicker)
            }
        }

        val yearText = TextView(requireContext()).apply {
            text = currentYear.toString()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setTextAppearance(com.edts.components.R.style.TextMedium_Label1)
            } else {
                @Suppress("DEPRECATION")
                setTextAppearance(context, com.edts.components.R.style.TextMedium_Label1)
            }
            setTextColor(ContextCompat.getColor(requireContext(), com.edts.components.R.color.color000))
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                marginEnd = resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_8dp)
            }
        }

        val yearHeader = LinearLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(
                resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_16dp),
                resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_8dp),
                resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_8dp),
                resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_12dp)
            )

            val prevButton = ImageButton(requireContext()).apply {
                setImageResource(R.drawable.ic_chevron_left)
                val colorResId = requireContext().resolveColorAttribute(
                    com.edts.components.R.attr.colorForegroundTertiary,
                    com.edts.components.R.color.colorNeutral50
                )
                imageTintList = ColorStateList.valueOf(colorResId)
                background = null
                contentDescription = "Tahun sebelumnya"
                layoutParams = LinearLayout.LayoutParams(
                    resources.getDimensionPixelSize(com.edts.components.R.dimen.line_height_24),
                    resources.getDimensionPixelSize(com.edts.components.R.dimen.line_height_24)
                ).apply {
                    marginEnd = resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_8dp)
                }

                setOnClickListener {
                    currentYear--
                    yearText.text = currentYear.toString()
                    updateMonthChips(currentYear)
                }
            }

            val nextButton = ImageButton(requireContext()).apply {
                setImageResource(R.drawable.ic_chevron_right)
                val colorResId = requireContext().resolveColorAttribute(
                    com.edts.components.R.attr.colorForegroundTertiary,
                    com.edts.components.R.color.colorNeutral50
                )
                imageTintList = ColorStateList.valueOf(colorResId)
                background = null
                contentDescription = "Tahun berikutnya"
                layoutParams = LinearLayout.LayoutParams(
                    resources.getDimensionPixelSize(com.edts.components.R.dimen.line_height_24),
                    resources.getDimensionPixelSize(com.edts.components.R.dimen.line_height_24)
                )

                setOnClickListener {
                    currentYear++
                    yearText.text = currentYear.toString()
                    updateMonthChips(currentYear)
                }
            }

            addView(yearText)
            addView(prevButton)
            addView(nextButton)
        }

        val container = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            addView(yearHeader)
            addView(monthChipsContainer)
        }

        updateMonthChips(currentYear)

        bottomTray.setTrayContentView(container)
        bottomTray.show(parentFragmentManager, "BottomTrayMonthly")
    }

    private fun updateFilterDisplay() {
        if (currentFilterType == "Mingguan") {
            val selectedWeek = weekData[selectedWeekPosition]
            binding.cvFilterHorizontal.apply {
                title = selectedWeek.first
                description = selectedWeek.second
            }
        } else {
            val selectedMonth = monthData[selectedMonthPosition]
            binding.cvFilterHorizontal.apply {
                title = selectedMonth.first
                description = "$selectedMonth.second"
                description = selectedYear.toString()
            }
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
        currentFilterType = chipText
        updateActivityList(chipText)
        updateFilterDisplay()
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