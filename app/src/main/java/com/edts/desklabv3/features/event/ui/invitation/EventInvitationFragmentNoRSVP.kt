package com.edts.desklabv3.features.event.ui.invitation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.edts.components.R
import com.edts.components.modal.ModalityConfirmationPopUp
import com.edts.components.modal.ModalityLoadingPopUp
import com.edts.components.notification.EventNotificationCard
import com.edts.desklabv3.databinding.FragmentEventInvitationListBinding
import com.edts.desklabv3.features.event.model.EventInvitation
import com.edts.desklabv3.features.SpaceItemDecoration

class EventInvitationFragmentNoRSVP : Fragment() {

    private var _binding: FragmentEventInvitationListBinding? = null
    private val binding get() = _binding!!
    private var loadingDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventInvitationListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val notificationList = createSampleData()

        val notificationAdapter = EventInvitationAdapter(
            notifications = notificationList,
            onCardClick = { notification ->
//                Toast.makeText(requireContext(), "Card clicked: ${notification.title}", Toast.LENGTH_SHORT).show()
                navigateToEventDetail()
            },
            onButtonClick = {
                showConfirmationModal()
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

    private fun navigateToEventDetail() {
        val result = bundleOf("fragment_class" to "EventDetailViewNoRSVP")
        parentFragmentManager.setFragmentResult("navigate_fragment", result)
    }

    private fun createSampleData(): List<EventInvitation> {
        return listOf(
            EventInvitation(
                title = "EDTS Town-Hall 2025: The Power of Change",
                description = "Anda diundang pada Rabu, 23 Juli 2025, pukul 15:00 – 17:00 WIB. Segera konfirmasi kehadiran Anda.",
                eventType = EventNotificationCard.EventType.GENERAL_EVENT
            )
        )
    }

    private fun showConfirmationModal() {
        ModalityConfirmationPopUp.show(
            context = requireContext(),
            title = "Konfirmasi Undangan",
            description = "Apakah kamu yakin terima undangan dan akan menghadiri event ini nanti?",
            confirmButtonLabel = "Ya, Lanjutkan",
            closeButtonLabel = "Tutup",
            onConfirm = {
                startFakeBackgroundTask()
            },
            onClose = {
                Toast.makeText(requireContext(), "Modal Closed.", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun startFakeBackgroundTask() {
        loadingDialog = ModalityLoadingPopUp.show(
            context = requireContext(),
            title = "Tunggu sebentar ...",
            isCancelable = false
        )

        Handler(Looper.getMainLooper()).postDelayed({
            loadingDialog?.dismiss()
            navigateToSuccessScreen()
        }, 3000)
    }

    private fun navigateToSuccessScreen() {
        val result = bundleOf(
            "fragment_class" to "SuccessRegistrationView",
            "flow_type" to "InvitationNoRSVP"
        )
        parentFragmentManager.setFragmentResult("navigate_fragment", result)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loadingDialog?.dismiss()
        _binding = null
    }
}