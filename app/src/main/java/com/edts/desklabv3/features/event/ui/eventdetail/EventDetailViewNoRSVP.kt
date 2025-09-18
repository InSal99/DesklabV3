package com.edts.desklabv3.features.event.ui.eventdetail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.edts.components.footer.Footer
import com.edts.components.footer.FooterDelegate
import com.edts.components.modal.ModalityConfirmationPopUp
import com.edts.components.modal.ModalityLoadingPopUp
import com.edts.components.tray.BottomTray
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.FragmentEventDetailBinding
import formatDateRange
import formatTimeRange
import setupHtmlDescription

class EventDetailViewNoRSVP : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var timeLocationAdapter: EventTimeLocationAdapter
    private var bottomTray: BottomTray? = null
    private var loadingDialog: AlertDialog? = null

    private var startDateTime: String = ""
    private var endDateTime: String = ""
    private var eventDescription: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDetailEventSpeakerTitle.layoutParams = binding.tvDetailEventSpeakerTitle.layoutParams.apply {
            if (this is ViewGroup.MarginLayoutParams) {
                setMargins(0, 0, 0, 0)
            }
        }
        binding.tvDetailEventSpeakerTitle.scaleX = 0f
        binding.tvDetailEventSpeakerTitle.scaleY = 0f

        binding.rvDetailEventSpeakersInfo.layoutParams = binding.rvDetailEventSpeakersInfo.layoutParams.apply {
            height = 0
            if (this is ViewGroup.MarginLayoutParams) {
                setMargins(0, 0, 0, 0)
            }
        }

        binding.dDetail1.layoutParams = binding.dDetail1.layoutParams.apply {
            if (this is ViewGroup.MarginLayoutParams) {
                setMargins(0, 16, 0, 0)
            }
        }

        setupBackButton()
        setEventDetails()
        setupTimeLocationRecyclerView()
        binding.tvEventDetailDescription.setupHtmlDescription(eventDescription)
        setupFooterButton()
    }

    private fun setEventDetails() {
        binding.ivDetailEventPoster.setImageResource(com.edts.components.R.drawable.poster3)
        binding.tvDetailEventType.text = "Hybrid Event"
        binding.tvDetailEventCategory.text = "General Event"
        binding.tvDetailEventTitle.text = "EDTS Town-Hall 2025: Power of Change"

        startDateTime = EVENT_START_DATETIME
        endDateTime = EVENT_END_DATETIME
        eventDescription = EVENT_DESCRIPTION_HTML
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomTray?.dismiss()
        bottomTray = null
        _binding = null
    }

    private fun setupBackButton() {
        binding.ivDetailBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupFooterButton() {
        binding.eventDetailFooter.footerDelegate = object : FooterDelegate {
            override fun onPrimaryButtonClicked(footerType: Footer.FooterType) {
                showConfirmationModal()
            }
            override fun onSecondaryButtonClicked(footerType: Footer.FooterType) {}
            override fun onRegisterClicked() {}
            override fun onContinueClicked() {}
            override fun onCancelClicked() {}
        }
        configureFooter()
    }

    private fun configureFooter() {
        binding.eventDetailFooter.apply {
            setFooterType(Footer.FooterType.DUAL_BUTTON)
            setPrimaryButtonText("Terima Undangan")
            setSecondaryButtonText("Tolak Undangan")
            setPrimaryButtonEnabled(true)
            setSecondaryButtonEnabled(true)
            setDescriptionVisibility(true)
            setDualButtonDescription("Peserta Terdaftar", "25", "200")
            showInfoBox(false)
        }
    }

    private fun setupTimeLocationRecyclerView() {
        timeLocationAdapter = EventTimeLocationAdapter()
        binding.rvDetailEventTimeLocation.apply {
            adapter = timeLocationAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val timeLocationList = listOf(
            Triple(R.drawable.ic_calendar, "Tanggal", startDateTime.formatDateRange(endDateTime)),
            Triple(R.drawable.ic_clock, "Waktu", startDateTime.formatTimeRange(endDateTime)),
            Triple(R.drawable.ic_video, "Link Meeting", "Tersedia Setelah Kehadiran Tercatat")
        )

        timeLocationAdapter.submitList(timeLocationList)
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
            onClose = { }
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

    companion object {
        const val EVENT_START_DATETIME = "2025-07-23 15:00:00"
        const val EVENT_END_DATETIME = "2025-07-23 17:00:00"

        const val EVENT_DESCRIPTION_HTML = """
            <p>Dear Edtizens,</p>
            <p>We’re excited to invite you to our upcoming <strong>Midyear Town Hall</strong>, a key moment for us to come together as one EDTS family to reflect, realign, and recharge for the second half of the year.</p>
            <p>This session will serve as an important checkpoint where we’ll share critical business updates, unveil our strategic vision for 2025, and open the floor for an engaging Q&A session. It’s not just about looking back; it’s about moving forward, together, with purpose and clarity.</p>
            <p>🔷 What’s on the Agenda?</p>
            <p>Business Updates:</strong> A recap of our progress, achievements, and challenges so far.</p>
            <p>2025 Vision & Strategy:</strong> A look into where we’re headed and how we plan to get there.</p>
            <p>Q&A Session:</strong> Your voice matters—share your thoughts, ask questions, and be part of the conversation.</p>
            <p>👕 <strong>Dress Code:</strong> Wear your best EDTS shirt and show your team spirit!</p>
            <p>Let’s embrace the <strong>Power of Change</strong> and continue <strong>Driving Excellence in 2025</strong> together, stronger than ever. We look forward to your enthusiastic participation!</p>
            <p>Thank you and see you there! 🙏😊</p>
        """
    }
}