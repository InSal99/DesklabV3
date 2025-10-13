package com.edts.desklabv3.features.event.ui.myevent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.edts.components.R
import com.edts.components.input.search.InputSearch
import com.edts.components.input.search.InputSearchDelegate
import com.edts.desklabv3.databinding.FragmentMyEventsEmptyViewBinding
import com.edts.desklabv3.features.SpaceItemDecoration
import com.edts.desklabv3.features.event.model.FilterChip
import com.edts.desklabv3.features.event.viewmodel.MyEventsViewModel
import com.edts.desklabv3.features.event.viewmodel.MyEventsViewModelFactory

class MyEventsEmptyView : Fragment() {
    private var _binding: FragmentMyEventsEmptyViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MyEventsViewModel
    private lateinit var filterChipAdapter: FilterChipAdapter

    private var currentSearchQuery: String = ""
    private var currentSelectedFilter: String = "Semua"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyEventsEmptyViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = MyEventsViewModelFactory("Semua")
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[MyEventsViewModel::class.java]
        setupUI()
        loadFilterChips()
        updateEmptyStateImage(currentSearchQuery, currentSelectedFilter)
    }

    private fun setupUI() {
        filterChipAdapter = FilterChipAdapter { chip ->
            if (chip.text != currentSelectedFilter) {
                currentSelectedFilter = chip.text
                viewModel.selectFilter(chip.text)
                updateEmptyStateImage(currentSearchQuery, currentSelectedFilter)

                loadFilterChips()
            }
        }
        binding.rvFilterChips.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = filterChipAdapter
            addItemDecoration(SpaceItemDecoration(requireContext(), R.dimen.margin_8dp, SpaceItemDecoration.HORIZONTAL))
            setHasFixedSize(true)
            setItemViewCacheSize(5)
        }

        binding.inputSearchEvent.delegate = object : InputSearchDelegate {
            override fun onSearchTextChange(inputSearch: InputSearch, text: String, changeCount: Int) {
                currentSearchQuery = text
                viewModel.setSearchQuery(text)
                updateEmptyStateImage(currentSearchQuery, currentSelectedFilter)
            }
            override fun onCloseIconClick(inputSearch: InputSearch, clickCount: Int) {}
            override fun onFocusChange(inputSearch: InputSearch, hasFocus: Boolean, newState: InputSearch.State, previousState: InputSearch.State) {}
            override fun onSearchFieldClick(inputSearch: InputSearch, clickCount: Int) {}
            override fun onSearchSubmit(inputSearch: InputSearch, query: String, searchCount: Int) {}
            override fun onStateChange(inputSearch: InputSearch, newState: InputSearch.State, oldState: InputSearch.State) {}
        }
    }

    private fun loadFilterChips() {
        val chipTexts = listOf("Semua", "Berlangsung", "Terdaftar", "Tidak Hadir", "Hadir")
        val chips = chipTexts.map { text ->
            FilterChip(
                text = text,
                isSelected = text == currentSelectedFilter
            )
        }
        filterChipAdapter.submitList(chips)
    }

    private fun updateEmptyStateImage(searchQuery: String?, selectedFilter: String?) {
        val isSearchingOrFiltering = !searchQuery.isNullOrBlank() ||
                (selectedFilter != null && selectedFilter != "Semua")

        if (isSearchingOrFiltering) {
            binding.layoutEmptyState.setImageResource(com.edts.desklabv3.R.drawable.image_my_event_empty)
        } else {
            binding.layoutEmptyState.setImageResource(com.edts.desklabv3.R.drawable.empty_state_my_event)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}