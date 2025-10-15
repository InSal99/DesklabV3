package com.edts.desklabv3.features.event.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.edts.desklabv3.features.event.model.FilterChip
import com.edts.desklabv3.features.event.model.MyEvent
import com.edts.desklabv3.features.event.model.MyEventStatus

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
        _filterChips.value = listOf(
            FilterChip("Semua", isSelected = "Semua" == defaultFilter),
            FilterChip("Berlangsung", isSelected = "Berlangsung" == defaultFilter),
            FilterChip("Terdaftar", isSelected = "Terdaftar" == defaultFilter),
            FilterChip("Hadir", isSelected = "Hadir" == defaultFilter),
            FilterChip("Tidak Hadir", isSelected = "Tidak Hadir" == defaultFilter)
        )
    }
    fun loadEvents(events: List<MyEvent>) {
        allEvents = events
        applyFiltersAndSearch()
    }
    fun setSearchQuery(query: String) {
        currentSearchQuery = query
        applyFiltersAndSearch()
    }
    fun selectFilter(chipText: String) {
        val currentChips = _filterChips.value ?: return
        val selectedChip = currentChips.find { it.isSelected }?.text

        if (selectedChip == chipText) return

        val newChips = currentChips.map { it.copy(isSelected = it.text == chipText) }
        _filterChips.value = newChips
        applyFiltersAndSearch()
    }
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