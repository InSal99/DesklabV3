package com.edts.desklabv3.features.event.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.edts.desklabv3.features.event.model.FilterChip
import com.edts.desklabv3.features.event.model.MyEvent
import com.edts.desklabv3.features.event.model.MyEventStatus

/**
 * Manages UI state for the MyEvents screen, including event lists and filter chip states.
 * @param defaultFilter The text of the chip that should be selected by default (e.g., "Semua").
 */
class MyEventsViewModel(private val defaultFilter: String) : ViewModel() {

    private var allEvents: List<MyEvent> = emptyList()

    private val _filteredEvents = MutableLiveData<List<MyEvent>>()
    val filteredEvents: LiveData<List<MyEvent>> get() = _filteredEvents

    private val _showEmptyState = MutableLiveData<Boolean>()
    val showEmptyState: LiveData<Boolean> get() = _showEmptyState

    private val _filterChips = MutableLiveData<List<FilterChip>>()
    val filterChips: LiveData<List<FilterChip>> get() = _filterChips

    private var currentSearchQuery: String = ""

    init {
        // Use the constructor parameter to set the default selected chip
        _filterChips.value = listOf(
            FilterChip("Semua", isSelected = "Semua" == defaultFilter),
            FilterChip("Berlangsung", isSelected = "Berlangsung" == defaultFilter),
            FilterChip("Terdaftar", isSelected = "Terdaftar" == defaultFilter),
            FilterChip("Hadir", isSelected = "Hadir" == defaultFilter),
            FilterChip("Tidak Hadir", isSelected = "Tidak Hadir" == defaultFilter)
        )
    }

    /**
     * Loads the master list of events and applies the initial filters.
     */
    fun loadEvents(events: List<MyEvent>) {
        allEvents = events
        applyFiltersAndSearch()
    }

    /**
     * Updates the search query and re-applies filters.
     */
    fun setSearchQuery(query: String) {
        currentSearchQuery = query
        applyFiltersAndSearch()
    }

    /**
     * Updates the selected filter chip and re-applies filters.
     * A new list is created to ensure LiveData observers are triggered.
     */
    fun selectFilter(chipText: String) {
        val currentChips = _filterChips.value ?: return
        val selectedChip = currentChips.find { it.isSelected }?.text

        // Avoid reprocessing if the same chip is clicked again.
        if (selectedChip == chipText) return

        val newChips = currentChips.map { it.copy(isSelected = it.text == chipText) }
        _filterChips.value = newChips
        applyFiltersAndSearch()
    }

    /**
     * Applies the current search query and filter to the master event list
     * and updates all relevant LiveData objects.
     */
    private fun applyFiltersAndSearch() {
        val activeFilter = _filterChips.value?.find { it.isSelected }?.text ?: defaultFilter

        val filteredByStatus = if (activeFilter != "Semua") {
            val status = MyEventStatus.fromString(activeFilter)
            allEvents.filter { it.status == status }
        } else {
            allEvents
        }

        val finalResults = if (currentSearchQuery.isNotBlank()) {
            filteredByStatus.filter {
                it.title.contains(currentSearchQuery, ignoreCase = true)
            }
        } else {
            filteredByStatus
        }

        _filteredEvents.value = finalResults
        _showEmptyState.value = finalResults.isEmpty()
    }
}