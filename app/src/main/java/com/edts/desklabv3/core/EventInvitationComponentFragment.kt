package com.edts.desklabv3.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.edts.components.R
import com.edts.components.notification.NotificationCard
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
            onPrimaryButtonClick = { notification ->
                Toast.makeText(requireContext(), "Primary button clicked for: ${notification.title}", Toast.LENGTH_SHORT).show()
            },
            onSecondaryButtonClick = { notification ->
                Toast.makeText(requireContext(), "Secondary button clicked for: ${notification.title}", Toast.LENGTH_SHORT).show()
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
                notificationCategory = NotificationCard.NotificationCategory.GENERAL_EVENT
            ),
            EventInvitation(
                title = "EDTS Town-Hall 2025: Power of Change",
                description = "Anda diundang pada Jumat, 25 Juli 2025, pukul 10:00 – 12:00 WIB. Segera konfirmasi kehadiran Anda.",
                notificationCategory = NotificationCard.NotificationCategory.PEOPLE_DEVELOPMENT
            ),
            EventInvitation(
                title = "Employee Benefits Update 2025",
                description = "Anda diundang pada Senin, 28 Juli 2025, pukul 14:00 – 16:00 WIB. Segera konfirmasi kehadiran Anda.",
                notificationCategory = NotificationCard.NotificationCategory.EMPLOYEE_BENEFIT
            ),
            EventInvitation(
                title = "Persetujuan Pengubahan Aktivitas",
                description = "Angga Kho Meidy telah menyetujui pengubahan aktivitas Anda untuk pekan 1 (1 Jan 2025 - 7 Jan 2025)",
                notificationCategory = NotificationCard.NotificationCategory.ACTIVITY,
                isPrimaryButtonVisible = false
            ),
            EventInvitation(
                title = "Persetujuan Cuti",
                description = "Anga Kho Meidy telah menyetujui permintaan cuti Anda untuk tanggal 31 Agu 2024 - 2 Jan 2025",
                notificationCategory = NotificationCard.NotificationCategory.LEAVE,
                isPrimaryButtonVisible = false
            ),
            EventInvitation(
                title = "Persetujuan Kerja Khusus",
                description = "Anga Kho Meidy telah menyetujui permintaan kerja khusus Anda untuk tanggal 31 Agu 2024 - 2 Jan 2025",
                notificationCategory = NotificationCard.NotificationCategory.SPECIAL_WORK,
                isPrimaryButtonVisible = false
            ),
            EventInvitation(
                title = "Pemberitahuan Delegasi",
                description = "Muhammad Dzaky Waly Andarwa telah memilih Anda sebagai delegasi untuk tanggal 31 Agu 2024",
                notificationCategory = NotificationCard.NotificationCategory.DELEGATION,
                isPrimaryButtonVisible = false
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}