package com.edts.desklabv3.features.event.ui.myevent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.edts.components.R
import com.edts.components.event.card.EventCardBadge
import com.edts.components.input.search.InputSearch
import com.edts.components.input.search.InputSearchDelegate
import com.edts.desklabv3.databinding.FragmentMyEventsBinding
import com.edts.desklabv3.features.SpaceItemDecoration
import com.edts.desklabv3.features.event.model.MyEvent
import com.edts.desklabv3.features.event.model.MyEventStatus
import com.edts.desklabv3.features.event.viewmodel.MyEventsViewModel
import com.edts.desklabv3.features.event.viewmodel.MyEventsViewModelFactory

class MyEventsFragmentNoRSVP : Fragment() {

    private var _binding: FragmentMyEventsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MyEventsViewModel
    private lateinit var eventAdapter: MyEventAdapter
    private lateinit var filterChipAdapter: FilterChipAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelFactory = MyEventsViewModelFactory("Terdaftar")

        viewModel = ViewModelProvider(this, viewModelFactory)[MyEventsViewModel::class.java]
        viewModel.loadEvents(createSampleMyEventData())

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        // Setup Event List RecyclerView
        eventAdapter = MyEventAdapter { event ->
            Toast.makeText(requireContext(), "Clicked on: ${event.title}", Toast.LENGTH_SHORT).show()
        }
        binding.rvMyEvent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
            addItemDecoration(SpaceItemDecoration(requireContext(), R.dimen.margin_8dp, SpaceItemDecoration.VERTICAL))
            setItemViewCacheSize(10)
        }

        // Setup Filter Chips RecyclerView
        filterChipAdapter = FilterChipAdapter { chip -> viewModel.selectFilter(chip.text) }
        binding.rvFilterChips.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = filterChipAdapter
            addItemDecoration(SpaceItemDecoration(requireContext(), R.dimen.margin_8dp, SpaceItemDecoration.HORIZONTAL))
            setHasFixedSize(true)
            setItemViewCacheSize(5)
        }

        // Setup Search Listener
        binding.inputSearchEvent.delegate = object : InputSearchDelegate {
            override fun onSearchTextChange(inputSearch: InputSearch, text: String, changeCount: Int) {
                viewModel.setSearchQuery(text)
            }
            override fun onCloseIconClick(inputSearch: InputSearch, clickCount: Int) {}
            override fun onFocusChange(inputSearch: InputSearch, hasFocus: Boolean, newState: InputSearch.State, previousState: InputSearch.State) {}
            override fun onSearchFieldClick(inputSearch: InputSearch, clickCount: Int) {}
            override fun onSearchSubmit(inputSearch: InputSearch, query: String, searchCount: Int) {
                TODO("Not yet implemented")
            }

            override fun onStateChange(inputSearch: InputSearch, newState: InputSearch.State, oldState: InputSearch.State) {}
        }
    }

    private fun observeViewModel() {
        viewModel.filteredEvents.observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events)
        }

        viewModel.showEmptyState.observe(viewLifecycleOwner) { isEmpty ->
            binding.rvMyEvent.isVisible = !isEmpty
            binding.layoutEmptyState.isVisible = isEmpty
        }

        viewModel.filterChips.observe(viewLifecycleOwner) { chips ->
            filterChipAdapter.submitList(chips)
        }
    }

    /**
     * Creates a list of sample MyEvent objects for demonstration purposes.
     */
    private fun createSampleMyEventData(): List<MyEvent> {
        fun createMyEvent(status: MyEventStatus, date: String, day: String, month: String, time: String, title: String, eventType: String): MyEvent {
            val (badgeText, badgeType) = when(status) {
                MyEventStatus.BERLANGSUNG -> "Berlangsung" to EventCardBadge.BadgeType.LIVE
                MyEventStatus.TERDAFTAR -> "Terdaftar" to EventCardBadge.BadgeType.REGISTERED
                MyEventStatus.HADIR -> "Hadir" to EventCardBadge.BadgeType.REGISTERED
                MyEventStatus.TIDAK_HADIR -> "Tidak Hadir" to EventCardBadge.BadgeType.LIVE
            }
            return MyEvent(
                status = status,
                date = date,
                day = day,
                month = month,
                time = time,
                title = title,
                eventType = eventType,
                badgeText = badgeText,
                badgeType = badgeType
            )
        }

        return listOf(
            createMyEvent(
                status = MyEventStatus.TERDAFTAR,
                date = "23",
                day = "Rab",
                month = "JUL",
                time = "18:00 - 20:00 WIB",
                title = "Game Night with EDTS: Mobile Legend Online Tournament 2025",
                eventType = "Online Event"
            ),
            createMyEvent(
                status = MyEventStatus.TERDAFTAR,
                date = "23",
                day = "Rab",
                month = "JUL",
                time = "15:00 - 17:00 WIB",
                title = "EDTS Town-Hall 2025: The Power of Change",
                eventType = "Hybrid Event"
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}