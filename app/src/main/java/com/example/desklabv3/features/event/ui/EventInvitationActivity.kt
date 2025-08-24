package com.example.desklabv3.features.event.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.components.modal.ModalityLoadingPopUp
import com.example.components.modal.ModalityConfirmationPopUp
import com.example.components.notification.CustomNotificationCard
import com.example.desklabv3.databinding.ActivityEventInvitationListBinding
import com.example.desklabv3.features.event.model.EventInvitation

class EventInvitationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventInvitationListBinding
    private var loadingDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventInvitationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val notificationList = createSampleData()

        val notificationAdapter = EventInvitationAdapter(
            notifications = notificationList,
            onCardClick = { notification ->
                Toast.makeText(this, "Card clicked: ${notification.title}", Toast.LENGTH_SHORT).show()
            },
            onButtonClick = {
                showConfirmationModal()
            }
        )

        binding.rvEventInvitation.apply {
            layoutManager = LinearLayoutManager(this@EventInvitationActivity)
            adapter = notificationAdapter
        }
    }

    private fun createSampleData(): List<EventInvitation> {
        return listOf(
            EventInvitation(
                id = 1,
                title = "Simplifying UX Complexity: Bridging the Gap Between Design and Development",
                description = "Anda diundang pada Rabu, 23 Juli 2025, pukul 15:00 – 17:00 WIB. Segera konfirmasi kehadiran Anda.",
                eventType = CustomNotificationCard.EventType.GENERAL_EVENT
            ),
            EventInvitation(
                id = 2,
                title = "Simplifying UX Complexity: Bridging the Gap Between Design and Development",
                description = "Anda diundang pada Rabu, 23 Juli 2025, pukul 15:00 – 17:00 WIB. Segera konfirmasi kehadiran Anda.",
                eventType = CustomNotificationCard.EventType.PEOPLE_DEVELOPMENT
            ),
            EventInvitation(
                id = 3,
                title = "Simplifying UX Complexity: Bridging the Gap Between Design and Development",
                description = "Anda diundang pada Rabu, 23 Juli 2025, pukul 15:00 – 17:00 WIB. Segera konfirmasi kehadiran Anda.",
                eventType = CustomNotificationCard.EventType.PEOPLE_DEVELOPMENT
            ),
            EventInvitation(
                id = 4,
                title = "Simplifying UX Complexity: Bridging the Gap Between Design and Development",
                description = "Anda diundang pada Rabu, 23 Juli 2025, pukul 15:00 – 17:00 WIB. Segera konfirmasi kehadiran Anda.",
                eventType = CustomNotificationCard.EventType.PEOPLE_DEVELOPMENT
            ),
            EventInvitation(
                id = 5,
                title = "Simplifying UX Complexity: Bridging the Gap Between Design and Development",
                description = "Anda diundang pada Rabu, 23 Juli 2025, pukul 15:00 – 17:00 WIB. Segera konfirmasi kehadiran Anda.",
                eventType = CustomNotificationCard.EventType.EMPLOYEE_BENEFIT
            )
        )
    }

    private fun showConfirmationModal() {
        ModalityConfirmationPopUp.show(
            context = this,
            title = "Konfirmasi Undangan",
            description = "Apakah kamu yakin terima undangan dan akan menghadiri event ini nanti?",
            confirmButtonLabel = "Ya, Lanjutkan",
            closeButtonLabel = "Tutup",
            onConfirm = {
                startFakeBackgroundTask()
            },
            onClose = {
                Toast.makeText(this, "Modal Closed.", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun startFakeBackgroundTask() {
        loadingDialog = ModalityLoadingPopUp.show(
            context = this,
            title = "Tunggu sebentar ...",
            isCancelable = false
        )

        Handler(Looper.getMainLooper()).postDelayed({
            loadingDialog?.dismiss()
        }, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog?.dismiss()
    }
}