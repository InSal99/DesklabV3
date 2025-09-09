package com.edts.desklabv3.features.leave.ui.laporantim

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.input.search.InputSearchDelegate
import com.edts.desklabv3.core.util.Utils
import com.edts.desklabv3.databinding.FragmentTeamReportLeaveViewBinding

class TeamReportLeaveView : Fragment(), InputSearchDelegate {

    private var _binding: FragmentTeamReportLeaveViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var leaveCardAdapter: LeaveCardAdapter
    private lateinit var originalEmployees: List<Employee>
    private var filteredEmployees: List<Employee> = emptyList()

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
                com.example.desklabv3.features.SpaceItemDecoration(
                    requireContext(),
                    com.edts.desklabv3.R.dimen.leave_card_item_spacing,
                    com.example.desklabv3.features.SpaceItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun setupSearchFunctionality() {
        binding.cvSearchKaryawan.delegate = this
    }

    private fun filterEmployees(query: String) {
        filteredEmployees = if (query.isEmpty()) {
            originalEmployees
        } else {
            originalEmployees.filter { employee ->
                employee.employeeName.contains(query, ignoreCase = true) ||
                        employee.employeeRole.contains(query, ignoreCase = true)
            }
        }
        leaveCardAdapter.updateEmployees(filteredEmployees)
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

    // InputSearchDelegate implementations
    override fun onSearchTextChange(inputSearch: com.edts.components.input.search.InputSearch, text: String, changeCount: Int) {
        filterEmployees(text)
    }

    override fun onSearchSubmit(inputSearch: com.edts.components.input.search.InputSearch, query: String, submitCount: Int) {
        filterEmployees(query)
    }

    override fun onCloseIconClick(inputSearch: com.edts.components.input.search.InputSearch, clickCount: Int) {
        filterEmployees("")
    }

    override fun onSearchFieldClick(inputSearch: com.edts.components.input.search.InputSearch, clickCount: Int) {
        // Handle search field click if needed
    }

    override fun onStateChange(inputSearch: com.edts.components.input.search.InputSearch, newState: com.edts.components.input.search.InputSearch.State, oldState: com.edts.components.input.search.InputSearch.State) {
        // Handle state changes if needed
    }

    override fun onFocusChange(inputSearch: com.edts.components.input.search.InputSearch, hasFocus: Boolean, newState: com.edts.components.input.search.InputSearch.State, oldState: com.edts.components.input.search.InputSearch.State) {
        // Handle focus changes if needed
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}