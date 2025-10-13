package com.edts.desklabv3.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.edts.components.R
import com.edts.components.event.card.EventCardBadge
import com.edts.desklabv3.databinding.FragmentMyEventsComponentBinding
import com.edts.desklabv3.features.SpaceItemDecoration
import com.edts.desklabv3.features.event.model.MyEvent
import com.edts.desklabv3.features.event.model.MyEventStatus
import com.edts.desklabv3.features.event.ui.myevent.MyEventAdapter

class MyEventsComponentFragment : Fragment() {
    private var _binding: FragmentMyEventsComponentBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventAdapter: MyEventAdapter
    private lateinit var allEvents: List<MyEvent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyEventsComponentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allEvents = createSampleEventData()
        setupMyEventRecyclerView()

        eventAdapter.submitList(allEvents)

        binding.btnLibMyEventCardBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupMyEventRecyclerView() {
        eventAdapter = MyEventAdapter { event ->
            Toast.makeText(requireContext(), "Clicked on: ${event.title}", Toast.LENGTH_SHORT).show()
        }

        binding.rvMyEvent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
            addItemDecoration(SpaceItemDecoration(requireContext(), R.dimen.margin_8dp, SpaceItemDecoration.VERTICAL))
            setHasFixedSize(true)
            setItemViewCacheSize(10)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createSampleEventData(): List<MyEvent> {
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
                status = MyEventStatus.BERLANGSUNG,
                date = "12",
                day = "Wed",
                month = "SEP",
                time = "18:00 - 20:00 WIB",
                title = "Simplifying UX Complexity: Bridging the Gap Between Design and Development",
                eventType = "Hybrid Event"
            ),
            createMyEvent(
                status = MyEventStatus.TERDAFTAR,
                date = "15",
                day = "Sat",
                month = "SEP",
                time = "10:00 - 12:00 WIB",
                title = "IT Security Awareness: Stay Ahead of Threats, Stay Secure",
                eventType = "Online Event"
            ),
            createMyEvent(
                status = MyEventStatus.HADIR,
                date = "25",
                day = "Thu",
                month = "SEP",
                time = "09:00 - 17:00 WIB",
                title = "Game Night with EDTS: Mobile Legend Online Tournament 2025",
                eventType = "Offline Event"
            ),
            createMyEvent(
                status = MyEventStatus.TIDAK_HADIR,
                date = "01",
                day = "Mon",
                month = "OCT",
                time = "13:00 - 15:00 WIB",
                title = "EDTS Town-Hall 2025: Power of Navigating Changes",
                eventType = "Hybrid Event"
            )
        )
    }
}