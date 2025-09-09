package com.edts.desklabv3.features.event.ui.eventlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.event.card.EventCardBadge
import com.edts.components.event.card.EventCardStatus
import com.edts.components.input.search.InputSearchDelegate
import com.edts.desklabv3.R
import com.edts.desklabv3.core.util.Utils
import com.edts.desklabv3.databinding.FragmentEventListDaftarRsvpViewBinding
import com.edts.desklabv3.features.event.model.Event
import com.edts.desklabv3.features.event.model.EventCategory
import com.edts.desklabv3.features.event.model.EventSample
import com.edts.desklabv3.features.event.model.EventStatus
import com.edts.desklabv3.features.event.model.EventType
import com.example.desklabv3.features.SpaceItemDecoration
import java.text.SimpleDateFormat
import java.util.*

class EventListDaftarRSVPView : Fragment(), InputSearchDelegate {

    private var _binding: FragmentEventListDaftarRsvpViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var tabAdapter: TabEventListDaftarRSVPAdapter
    private lateinit var eventChipAdapter: EventChipAdapter
    private lateinit var eventListAdapter: EventListAdapter

    private val chipTexts = arrayOf("Semua", "Employee Benefit", "General Event", "People Development")

    private val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

    private val daftarRSVPList = listOf(
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
            eventTitle = "Simplifying UX Complexity: Bridging the Gap Between Design and Development",
            eventImage = "image_event_simplyfying",
            eventCategory = EventCategory.PEOPLE_DEVELOPMENT,
            eventType = EventType.Hybrid(name = "Hybrid Event", meetingUrl = "", platform = "Teams", location = "EDTS Office"),
            eventDate = dateFormatter.parse("24 Juli 2025") ?: Date(),
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

    private val undanganNoRSVPList = listOf(
        EventSample(
            eventTitle = "EDTS Town-Hall 2025: Power of Change",
            eventImage = "image_event_power",
            eventCategory = EventCategory.GENERAL_EVENT,
            eventType = EventType.Hybrid(name = "Hybrid Event", meetingUrl = "", platform = "Teams", location = "EDTS Office"),
            eventDate = dateFormatter.parse("23 Juli 2025") ?: Date(),
            statusText = "Reservasi Sekarang!",
            statusType = EventCardStatus.StatusType.RSVP,
            badgeType = EventCardBadge.BadgeType.INVITED,
            badgeText = "Diundang"
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
            eventTitle = "Simplifying UX Complexity: Bridging the Gap Between Design and Development",
            eventImage = "image_event_simplyfying",
            eventCategory = EventCategory.PEOPLE_DEVELOPMENT,
            eventType = EventType.Hybrid(name = "Hybrid Event", meetingUrl = "", platform = "Teams", location = "EDTS Office"),
            eventDate = dateFormatter.parse("24 Juli 2025") ?: Date(),
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

    private val undanganTolakAfterList = listOf(
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
            eventDate = dateFormatter.parse("24 Juli 2025") ?: Date(),
            statusText = "Reservasi Sekarang!",
            statusType = EventCardStatus.StatusType.RSVP
        ),
        EventSample(
            eventTitle = "Simplifying UX Complexity: Bridging the Gap Between Design and Development",
            eventImage = "image_event_simplyfying",
            eventCategory = EventCategory.PEOPLE_DEVELOPMENT,
            eventType = EventType.Hybrid(name = "Hybrid Event", meetingUrl = "", platform = "Teams", location = "EDTS Office"),
            eventDate = dateFormatter.parse("24 Juli 2025") ?: Date(),
            statusText = "Undangan Ditolak",
            statusType = EventCardStatus.StatusType.UNREGISTERED
        )
    )

    private val attendanceList = listOf(
        EventSample(
            eventTitle = "Simplifying UX Complexity: Bridging the Gap Between Design and Development",
            eventImage = "image_event_simplyfying",
            eventCategory = EventCategory.PEOPLE_DEVELOPMENT,
            eventType = EventType.Hybrid(name = "Hybrid Event", meetingUrl = "", platform = "Teams", location = "EDTS Office"),
            eventDate = dateFormatter.parse("24 Juli 2025") ?: Date(),
            statusText = "Catat Kehadiran",
            statusType = EventCardStatus.StatusType.REGISTERED
        ),
        EventSample(
            eventTitle = "IT Security Awareness: Stay Ahead of Threats, Stay Secure",
            eventImage = "image_event_it",
            eventCategory = EventCategory.GENERAL_EVENT,
            eventType = EventType.Offline(name = "Offline Event", location = "EDTS Office"),
            eventDate = dateFormatter.parse("24 Juli 2025") ?: Date()
        )
    )

    // Manual list switching - uncomment the one you want to use
//    private var currentEventList = daftarRSVPList
    private var currentEventList = undanganNoRSVPList
//    private var currentEventList = undanganTolakBeforeList
//    private var currentEventList = undanganTolakAfterList
//    private var currentEventList = attendanceList

    // Store original events for search filtering
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

        setupTabRecyclerView()
        setupChipRecyclerView()
        setupEventRecyclerView()
        setupSearchFunctionality()
        setupOutsideClickListener()
    }

    private fun setupTabRecyclerView() {
        tabAdapter = TabEventListDaftarRSVPAdapter(
            selectedPosition = 0 // Default to "Daftar Event"
        ) { position, tabText ->
            handleTabSelection(position, tabText)
        }

        binding.rvTabEventListDaftarRSVP.apply {
            adapter = tabAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setupChipRecyclerView() {
        eventChipAdapter = EventChipAdapter(
            chipTexts = chipTexts,
            selectedPosition = 0,
            onChipClick = { position, chipText ->
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
            otherClickableViews = listOf(binding.rvTabEventListDaftarRSVP, binding.rvEventChips)
        )
    }

    private fun handleTabSelection(position: Int, tabText: String) {
        when (position) {
            0 -> {
                originalEvents = currentEventList
            }
            1 -> {
                // "Event Saya"
            }
            2 -> {
                // "Undangan"
            }
        }

        currentSearchQuery = ""
        currentChipFilter = "Semua"
        applyFilters()

        eventChipAdapter.updateSelectedPosition(0)

        println("Selected tab: $tabText at position $position")
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
    }

    private fun handleEventClick(event: EventSample) {
        // Handle event item click
        // Navigate to event detail or perform other actions
    }

    // InputSearchDelegate implementations
    override fun onSearchTextChange(inputSearch: com.edts.components.input.search.InputSearch, text: String, changeCount: Int) {
        currentSearchQuery = text
        applyFilters()
    }

    override fun onSearchSubmit(inputSearch: com.edts.components.input.search.InputSearch, query: String, submitCount: Int) {
        currentSearchQuery = query
        applyFilters()
    }

    override fun onCloseIconClick(inputSearch: com.edts.components.input.search.InputSearch, clickCount: Int) {
        currentSearchQuery = ""
        applyFilters()
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

    // Optional: Public methods for external control
    fun selectTab(position: Int) {
        tabAdapter.updateSelectedPosition(position)
    }

    fun getCurrentTab(): String? {
        return tabAdapter.getSelectedTabText()
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