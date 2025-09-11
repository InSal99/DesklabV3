package com.edts.desklabv3.features.event.ui.eventlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.edts.components.event.card.EventCardBadge
import com.edts.components.event.card.EventCardStatus
import com.edts.components.input.search.InputSearchDelegate
import com.edts.desklabv3.R
import com.edts.desklabv3.core.util.Utils
import com.edts.desklabv3.databinding.FragmentEventListDaftarRsvpViewBinding
import com.edts.desklabv3.features.SpaceItemDecoration
import com.edts.desklabv3.features.event.model.EventCategory
import com.edts.desklabv3.features.event.model.EventSample
import com.edts.desklabv3.features.event.model.EventType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventListInvitationTolakStartView : Fragment(), InputSearchDelegate {
    private var _binding: FragmentEventListDaftarRsvpViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventChipAdapter: EventChipAdapter
    private lateinit var eventListAdapter: EventListAdapter

    private val chipTexts = arrayOf("Semua", "Employee Benefit", "General Event", "People Development")

    private val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

    private val undanganTolakBeforeList = listOf(
        EventSample(
            eventTitle = "Simplifying UX Complexity: Bridging the Gap Between Design and Development",
            eventImage = "image_event_simplyfying",
            eventCategory = EventCategory.PEOPLE_DEVELOPMENT,
            eventType = EventType.Hybrid(name = "Hybrid Event", meetingUrl = "", platform = "Teams", location = "EDTS Office"),
            eventDate = dateFormatter.parse("24 Juli 2025") ?: Date(),
            statusText = "Reservasi Sekarang!",
            statusType = EventCardStatus.StatusType.RSVP,
            badgeType = EventCardBadge.BadgeType.INVITED,
            badgeText = "Diundang"
        ),
        EventSample(
            eventTitle = "EDTS Town-Hall 2025: Power of Change",
            eventImage = "image_event_power",
            eventCategory = EventCategory.GENERAL_EVENT,
            eventType = EventType.Hybrid(name = "Hybrid Event", meetingUrl = "", platform = "Teams", location = "EDTS Office"),
            eventDate = dateFormatter.parse("23 Juli 2025") ?: Date(),
            statusText = "Reservasi Sekarang!",
            statusType = EventCardStatus.StatusType.RSVP
        ),
        EventSample(
            eventTitle = "Game Night with EDTS: Mobile Legend Online Tournament 2025",
            eventImage = "image_event_game",
            eventCategory = EventCategory.EMPLOYEE_BENEFIT,
            eventType = EventType.Online(name = "Online Event", meetingUrl = "", platform = "Discord"),
            eventDate = dateFormatter.parse("23 Juli 2025") ?: Date(),
            statusText = "Reservasi Sekarang!",
            statusType = EventCardStatus.StatusType.RSVP
        ),
        EventSample(
            eventTitle = "IT Security Awareness: Stay Ahead of Threats, Stay Secure",
            eventImage = "image_event_it",
            eventCategory = EventCategory.GENERAL_EVENT,
            eventType = EventType.Offline(name = "Offline Event", location = "EDTS Office"),
            eventDate = dateFormatter.parse("24 Juli 2025") ?: Date()
        )
    )

    private var currentEventList = undanganTolakBeforeList

    private var originalEvents: List<EventSample> = emptyList()
    private var filteredEvents: List<EventSample> = emptyList()
    private var currentSearchQuery = ""
    private var currentChipFilter = "Semua"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventListDaftarRsvpViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        originalEvents = currentEventList
        filteredEvents = originalEvents

        setupChipRecyclerView()
        setupEventRecyclerView()
        setupSearchFunctionality()
        setupOutsideClickListener()
    }

    private fun setupChipRecyclerView() {
        eventChipAdapter = EventChipAdapter(
            chipTexts = chipTexts,
            selectedPosition = 0,
            onChipClick = { position, chipText ->
                Utils.closeSearchInput(requireContext(), binding.cvSearchEvent, binding.root)
                handleChipClick(position, chipText)
            }
        )

        binding.rvEventChips.apply {
            adapter = eventChipAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)

            addItemDecoration(
                SpaceItemDecoration(
                    requireContext(),
                    R.dimen.chip_item_spacing,
                    SpaceItemDecoration.HORIZONTAL
                )
            )
        }
    }

    private fun setupEventRecyclerView() {
        eventListAdapter = EventListAdapter(
            events = filteredEvents,
            onEventClick = { event ->
                Utils.closeSearchInput(requireContext(), binding.cvSearchEvent, binding.root)
                handleEventClick(event)
            }
        )

        binding.rvEvents.apply {
            adapter = eventListAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)

            addItemDecoration(
                SpaceItemDecoration(
                    requireContext(),
                    R.dimen.leave_card_item_spacing,
                    SpaceItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun setupSearchFunctionality() {
        binding.cvSearchEvent.delegate = this
    }

    private fun setupOutsideClickListener() {
        Utils.setupSearchInputOutsideClickListeners(
            context = requireContext(),
            inputSearch = binding.cvSearchEvent,
            rootView = binding.root,
            recyclerView = binding.rvEvents,
            otherClickableViews = listOf(
                binding.rvEventChips
            )
        )
    }

    private fun handleChipClick(position: Int, chipText: String) {
        currentChipFilter = chipText
        applyFilters()
    }

    private fun applyFilters() {
        var events = originalEvents

        if (currentSearchQuery.isNotEmpty()) {
            events = events.filter { event ->
                event.eventTitle.contains(currentSearchQuery, ignoreCase = true) ||
                        event.eventType.name.contains(currentSearchQuery, ignoreCase = true) ||
                        dateFormatter.format(event.eventDate).contains(currentSearchQuery, ignoreCase = true)
            }
        }

        filteredEvents = when (currentChipFilter) {
            "Semua" -> events
            "Employee Benefit" -> events.filter { it.eventCategory == EventCategory.EMPLOYEE_BENEFIT }
            "General Event" -> events.filter { it.eventCategory == EventCategory.GENERAL_EVENT }
            "People Development" -> events.filter { it.eventCategory == EventCategory.PEOPLE_DEVELOPMENT }
            else -> events
        }

        eventListAdapter.updateEvents(filteredEvents)
        updateEmptyState()
    }

    private fun updateEmptyState() {
        if (filteredEvents.isEmpty()) {
            binding.ivEmptyStateEventList.visibility = View.VISIBLE
            binding.rvEvents.visibility = View.INVISIBLE
        } else {
            binding.ivEmptyStateEventList.visibility = View.GONE
            binding.rvEvents.visibility = View.VISIBLE
        }
    }

    private fun handleEventClick(event: EventSample) {
        val result = bundleOf("fragment_class" to "EventDetailViewTolakUndangan")
        parentFragmentManager.setFragmentResult("navigate_fragment", result)
    }

    override fun onSearchTextChange(inputSearch: com.edts.components.input.search.InputSearch, text: String, changeCount: Int) {
        currentSearchQuery = text
        applyFilters()
    }

    override fun onSearchSubmit(inputSearch: com.edts.components.input.search.InputSearch, query: String, submitCount: Int) {
        currentSearchQuery = query
        applyFilters()
        Utils.closeSearchInput(requireContext(), binding.cvSearchEvent, binding.root)
    }

    override fun onCloseIconClick(inputSearch: com.edts.components.input.search.InputSearch, clickCount: Int) {
        currentSearchQuery = ""
        applyFilters()
    }

    override fun onSearchFieldClick(inputSearch: com.edts.components.input.search.InputSearch, clickCount: Int) {
        // Handle search field click if needed
    }

    override fun onStateChange(inputSearch: com.edts.components.input.search.InputSearch, newState: com.edts.components.input.search.InputSearch.State, oldState: com.edts.components.input.search.InputSearch.State) {
        // Handle state changes
    }

    override fun onFocusChange(inputSearch: com.edts.components.input.search.InputSearch, hasFocus: Boolean, newState: com.edts.components.input.search.InputSearch.State, oldState: com.edts.components.input.search.InputSearch.State) {
        // Handle focus changes
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = EventListDaftarRSVPView()
    }
}