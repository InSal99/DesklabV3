package com.edts.desklabv3.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.edts.components.R
import com.edts.components.notification.EventNotificationCard
import com.edts.desklabv3.databinding.FragmentEventInvitationComponentBinding
import com.edts.desklabv3.features.SpaceItemDecoration
import com.edts.desklabv3.features.event.model.EventInvitation
import com.edts.desklabv3.features.event.ui.invitation.EventInvitationAdapter

class EventInvitationComponentFragment : Fragment() {

    private var _binding: FragmentEventInvitationComponentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventInvitationComponentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        binding.btnLibEventInvitationCardBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        val notificationList = createSampleData()

        val notificationAdapter = EventInvitationAdapter(
            notifications = notificationList,
            onCardClick = { notification ->
                Toast.makeText(requireContext(), "Card clicked: ${notification.title}", Toast.LENGTH_SHORT).show()
            },
            onButtonClick = { notification ->
                Toast.makeText(requireContext(), "Button clicked for: ${notification.title}", Toast.LENGTH_SHORT).show()
            }
        )

        val itemDecoration = SpaceItemDecoration(
            requireContext(),
            R.dimen.margin_8dp,
            SpaceItemDecoration.VERTICAL
        )

        binding.rvEventInvitation.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(itemDecoration)
            adapter = notificationAdapter
        }
    }

    private fun createSampleData(): List<EventInvitation> {
        return listOf(
            EventInvitation(
                title = "Simplifying UX Complexity: Bridging the Gap Between Design and Development",
                description = "Anda diundang pada Rabu, 23 Juli 2025, pukul 15:00 – 17:00 WIB. Segera konfirmasi kehadiran Anda.",
                eventCategory = EventNotificationCard.EventCategory.GENERAL_EVENT
            ),
            EventInvitation(
                title = "EDTS Town-Hall 2025: Power of Change",
                description = "Anda diundang pada Jumat, 25 Juli 2025, pukul 10:00 – 12:00 WIB. Segera konfirmasi kehadiran Anda.",
                eventCategory = EventNotificationCard.EventCategory.PEOPLE_DEVELOPMENT
            ),
            EventInvitation(
                title = "EDTS Town-Hall 2025: Power of Change",
                description = "Anda diundang pada Jumat, 25 Juli 2025, pukul 10:00 – 12:00 WIB. Segera konfirmasi kehadiran Anda.",
                eventCategory = EventNotificationCard.EventCategory.EMPLOYEE_BENEFIT
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}