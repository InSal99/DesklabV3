package com.edts.desklabv3.features.leave.ui.laporantim

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.R
import com.edts.components.input.search.InputSearchDelegate
import com.edts.components.tray.BottomTray
import com.edts.desklabv3.core.util.Utils
import com.edts.desklabv3.databinding.FragmentTeamReportLeaveViewBinding
import com.edts.desklabv3.features.event.ui.eventdetail.EventOptionAdapter

class TeamReportLeaveView : Fragment(), InputSearchDelegate {

    private var _binding: FragmentTeamReportLeaveViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var leaveCardAdapter: LeaveCardAdapter
    private lateinit var originalEmployees: List<Employee>
    private var filteredEmployees: List<Employee> = emptyList()
    private var currentSearchQuery = ""
    private var bottomTray: BottomTray? = null

    private var currentSortType: SortType = SortType.NAME_ASCENDING

    enum class SortType {
        NAME_ASCENDING,
        NAME_DESCENDING,
        LEAVE_COUNT_ASCENDING,
        LEAVE_COUNT_DESCENDING
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamReportLeaveViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchFunctionality()
        setupOutsideClickListener()
        setupSortButton()
        updateSortButtonIcon()
    }

    private fun setupRecyclerView() {
        originalEmployees = listOf(
            Employee("Raka Aditya Pratama", "Associate Product Manager", "5 Hari"),
            Employee("Bimo Ardiansyah Wijaya", "Senior Product Manager", "0 Hari"),
            Employee("Indah Permata Wulandari", "Associate Product Manager", "8 Hari"),
            Employee("Ayu Kartika Sari", "Lead Product Manager", "12 Hari"),
            Employee("Gilang Mahardika Saputra", "Product Manager", "-1 Hari")
        )

        filteredEmployees = originalEmployees
        leaveCardAdapter = LeaveCardAdapter(filteredEmployees)

        binding.rvLeaveCards.apply {
            adapter = leaveCardAdapter
            layoutManager = LinearLayoutManager(requireContext())

            addItemDecoration(
                com.edts.desklabv3.features.SpaceItemDecoration(
                    requireContext(),
                    com.edts.desklabv3.R.dimen.leave_card_item_spacing,
                    com.edts.desklabv3.features.SpaceItemDecoration.VERTICAL
                )
            )
        }

        updateEmptyState()
    }

    private fun setupSearchFunctionality() {
        binding.cvSearchKaryawan.delegate = this
    }

    private fun setupSortButton() {
        binding.cvSortBtn.setOnClickListener {
            Utils.closeSearchInput(requireContext(), binding.cvSearchKaryawan, binding.root)
            handleSortClick()
        }
    }

    private fun updateSortButtonIcon() {
        val iconRes = when (currentSortType) {
            SortType.NAME_ASCENDING, SortType.LEAVE_COUNT_ASCENDING ->
                com.edts.desklabv3.R.drawable.ic_sort_ascending
            SortType.NAME_DESCENDING, SortType.LEAVE_COUNT_DESCENDING ->
                com.edts.components.R.drawable.ic_sort
        }

        binding.cvSortBtn.sortIcon = iconRes
    }

    private fun handleSortClick() {
        showBottomTray()
    }

    private fun showBottomTray() {
        if (bottomTray?.isVisible == true) return

        bottomTray = BottomTray.newInstance(
            title = "Urutkan",
            showDragHandle = true,
            showFooter = false,
            hasShadow = true,
            hasStroke = true
        )

        val contentView = createBottomTrayContent()
        bottomTray?.setContentView(contentView)

        bottomTray?.delegate = object : com.edts.components.tray.BottomTrayDelegate {
            override fun onShow(dialog: DialogInterface) {
            }

            override fun onDismiss(dialog: DialogInterface) {
                bottomTray = null
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        }

        bottomTray?.show(childFragmentManager, "event_actions_bottom_tray")
    }

    private fun createBottomTrayContent(): View {
        val contentView = layoutInflater.inflate(com.edts.desklabv3.R.layout.bottom_tray_event_options, null)

        val recyclerView = contentView.findViewById<RecyclerView>(com.edts.desklabv3.R.id.rvEventOptions)
        val optionAdapter = EventOptionAdapter { position ->
            Log.d("Sorting", "User selected sort option $position")
            bottomTray?.dismiss()

            Handler(Looper.getMainLooper()).postDelayed({
                handleOptionSelected(position)
            }, 300)
        }

        recyclerView.apply {
            adapter = optionAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val options = listOf(
            "Nama A –> Z" to R.drawable.ic_chevron_right,
            "Nama Z –> A" to R.drawable.ic_chevron_right,
            "Sisa Cuti Terbanyak –> Tersedikit" to R.drawable.ic_chevron_right,
            "Sisa Cuti Tersedikit –> Terbanyak" to R.drawable.ic_chevron_right
        )

        optionAdapter.submitList(options)

        return contentView
    }

    private fun handleOptionSelected(position: Int) {
        when (position) {
            0 -> {
                currentSortType = SortType.NAME_ASCENDING
                sortByNameAscending()
            }
            1 -> {
                currentSortType = SortType.NAME_DESCENDING
                sortByNameDescending()
            }
            2 -> {
                currentSortType = SortType.LEAVE_COUNT_DESCENDING
                sortByLeaveCountDescending()
            }
            3 -> {
                currentSortType = SortType.LEAVE_COUNT_ASCENDING
                sortByLeaveCountAscending()
            }
        }
        updateSortButtonIcon()
    }

    private fun sortByNameAscending() {
        val sortedEmployees = filteredEmployees.sortedBy { it.employeeName }
        updateEmployeeList(sortedEmployees)
        Log.d("Sorting", "Applied name ascending sort - Icon should be ic_sort_ascending")
    }

    private fun sortByNameDescending() {
        val sortedEmployees = filteredEmployees.sortedByDescending { it.employeeName }
        updateEmployeeList(sortedEmployees)
        Log.d("Sorting", "Applied name descending sort - Icon should be ic_sort")
    }

    private fun sortByLeaveCountDescending() {
        val sortedEmployees = filteredEmployees.sortedByDescending { employee ->
            employee.counterDays
        }
        updateEmployeeList(sortedEmployees)
        Log.d("Sorting", "Applied leave count descending sort - Icon should be ic_sort")
    }

    private fun sortByLeaveCountAscending() {
        val sortedEmployees = filteredEmployees.sortedBy { employee ->
            employee.counterDays
        }
        updateEmployeeList(sortedEmployees)
        Log.d("Sorting", "Applied leave count ascending sort - Icon should be ic_sort_ascending")
    }

    private fun updateEmployeeList(sortedEmployees: List<Employee>) {
        filteredEmployees = sortedEmployees
        leaveCardAdapter.updateEmployees(sortedEmployees)
        updateEmptyState()
    }

    private fun filterEmployees(query: String) {
        currentSearchQuery = query
        applyFilters()
    }

    private fun applyFilters() {
        var employees = originalEmployees

        if (currentSearchQuery.isNotEmpty()) {
            employees = employees.filter { employee ->
                employee.employeeName.contains(currentSearchQuery, ignoreCase = true) ||
                        employee.employeeRole.contains(currentSearchQuery, ignoreCase = true)
            }
        }

        filteredEmployees = employees

        when (currentSortType) {
            SortType.NAME_ASCENDING -> {
                filteredEmployees = filteredEmployees.sortedBy { it.employeeName }
            }
            SortType.NAME_DESCENDING -> {
                filteredEmployees = filteredEmployees.sortedByDescending { it.employeeName }
            }
            SortType.LEAVE_COUNT_ASCENDING -> {
                filteredEmployees = filteredEmployees.sortedBy { it.counterDays }
            }
            SortType.LEAVE_COUNT_DESCENDING -> {
                filteredEmployees = filteredEmployees.sortedByDescending { it.counterDays }
            }
        }

        leaveCardAdapter.updateEmployees(filteredEmployees)
        updateEmptyState()
    }

    private fun updateEmptyState() {
        if (filteredEmployees.isEmpty()) {
            binding.ivEmptyStateLeaveList.visibility = View.VISIBLE
            binding.rvLeaveCards.visibility = View.INVISIBLE
        } else {
            binding.ivEmptyStateLeaveList.visibility = View.GONE
            binding.rvLeaveCards.visibility = View.VISIBLE
        }
    }

    private fun setupOutsideClickListener() {
        Utils.setupSearchInputOutsideClickListeners(
            context = requireContext(),
            inputSearch = binding.cvSearchKaryawan,
            rootView = binding.root,
            recyclerView = binding.rvLeaveCards,
            otherClickableViews = listOf(binding.cvSortBtn)
        )
    }

    override fun onSearchTextChange(inputSearch: com.edts.components.input.search.InputSearch, text: String, changeCount: Int) {
        filterEmployees(text)
    }

    override fun onSearchSubmit(inputSearch: com.edts.components.input.search.InputSearch, query: String, submitCount: Int) {
        filterEmployees(query)
        Utils.closeSearchInput(requireContext(), binding.cvSearchKaryawan, binding.root)
    }

    override fun onCloseIconClick(inputSearch: com.edts.components.input.search.InputSearch, clickCount: Int) {
        filterEmployees("")
    }

    override fun onSearchFieldClick(inputSearch: com.edts.components.input.search.InputSearch, clickCount: Int) {
        // Handle search field click
    }

    override fun onStateChange(inputSearch: com.edts.components.input.search.InputSearch, newState: com.edts.components.input.search.InputSearch.State, oldState: com.edts.components.input.search.InputSearch.State) {
        // Handle state changes
    }

    override fun onFocusChange(inputSearch: com.edts.components.input.search.InputSearch, hasFocus: Boolean, newState: com.edts.components.input.search.InputSearch.State, oldState: com.edts.components.input.search.InputSearch.State) {
        // Handle focus changes
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomTray?.dismiss()
        bottomTray = null
        _binding = null
    }
}