package com.edts.desklabv3.features.event.ui.eventdetail

import android.content.ClipData
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.footer.Footer
import com.edts.components.footer.FooterDelegate
import com.edts.components.modal.ModalityLoadingPopUp
import com.edts.components.status.badge.StatusBadge
import com.edts.components.toast.Toast
import com.edts.components.tray.BottomTray
import com.edts.components.tray.BottomTrayDelegate
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.FragmentEventDetailBinding
import com.edts.desklabv3.features.SpaceItemDecoration
import formatDateRange
import formatTimeRange
import setupHtmlDescription

class EventDetailViewAttendance : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var timeLocationAdapter: EventTimeLocationAdapter
    private var bottomTray: BottomTray? = null
    private var fromSuccess: Boolean = false
    private var attendanceType: String = ""
    private var meetingLink: String = ""

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

        setupBackButton()
        setEventDetails()
        setupSpeakerRecyclerView()
        setupTimeLocationRecyclerView()
        binding.tvEventDetailDescription.setupHtmlDescription(eventDescription)
        setupFooterButton()

        if (fromSuccess) {
            applySuccessViewChanges(attendanceType)
        }
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

    private fun setEventDetails() {
        binding.ivDetailEventPoster.setImageResource(com.edts.components.R.drawable.poster1)
        binding.tvDetailEventType.text = "Hybrid Event"
        binding.tvDetailEventCategory.text = "People Development"
        binding.tvDetailEventTitle.text = "Simplifying UX Complexity: Bridging the Gap Between Design and Development"

        startDateTime = EVENT_START_DATETIME
        endDateTime = EVENT_END_DATETIME
        eventDescription = EVENT_DESCRIPTION_HTML
    }

    private fun setupFooterButton() {
        binding.eventDetailFooter.footerDelegate = object : FooterDelegate {
            override fun onPrimaryButtonClicked(footerType: Footer.FooterType) {
                showBottomTray()
            }
            override fun onSecondaryButtonClicked(footerType: Footer.FooterType) {}
            override fun onRegisterClicked() {}
            override fun onContinueClicked() {}
            override fun onCancelClicked() {}
        }

        configureFooter()
    }

    private fun configureFooter() {
        binding.eventDetailFooter.setFooterType(Footer.FooterType.CALL_TO_ACTION)
        binding.eventDetailFooter.setPrimaryButtonText("Catat Kehadiran")
        binding.eventDetailFooter.setPrimaryButtonEnabled(true)
        binding.eventDetailFooter.showInfoBox(false)
    }

    private fun showBottomTray() {
        if (bottomTray?.isVisible == true) return

        bottomTray = BottomTray.newInstance(
            title = "Pilih Tipe Kehadiran",
            showDragHandle = true,
            showFooter = false,
            hasShadow = true,
            hasStroke = true
        )

        val contentView = createBottomTrayContent()
        bottomTray?.setContentView(contentView)

        bottomTray?.delegate = object : BottomTrayDelegate {
            override fun onShow(dialog: DialogInterface) {}
            override fun onDismiss(dialog: DialogInterface) {
                bottomTray = null
            }
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        }

        bottomTray?.show(childFragmentManager, "event_actions_bottom_tray")
    }

    private fun createBottomTrayContent(): View {
        val contentView = layoutInflater.inflate(com.edts.desklabv3.R.layout.bottom_tray_event_options, null)

        val recyclerView = contentView.findViewById<RecyclerView>(com.edts.desklabv3.R.id.rvEventOptions)
        val optionAdapter = EventOptionAdapter { position ->
            Log.d("Present", "User present option $position")
            bottomTray?.dismiss()

            Handler(Looper.getMainLooper()).postDelayed({
                handleOptionSelected(position)
            }, 300)
        }

        recyclerView.apply {
            adapter = optionAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val options = listOf(
            "online" to R.drawable.ic_chevron_right,
            "offline" to R.drawable.ic_chevron_right
        )

        optionAdapter.submitList(options)

        return contentView
    }

    private fun applySuccessViewChanges(attendanceType: String) {
        configureFooterForSuccess(attendanceType)
        updateTimeLocationListWithAction()
        setupPosterClick()
    }

    private fun setupPosterClick() {
        binding.ivDetailEventPoster.setOnClickListener {
            parentFragmentManager.popBackStack(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }
    }

    private fun configureFooterForSuccess(attendanceType: String) {
        binding.eventDetailFooter.setFooterType(Footer.FooterType.NO_ACTION)
        binding.eventDetailFooter.setPrimaryButtonEnabled(false)
        binding.eventDetailFooter.showInfoBox(false)
        binding.eventDetailFooter.setStatusBadge("Kehadiran Tercatat", StatusBadge.ChipType.APPROVED)

        val typeText = if (attendanceType == "offline") "Kehadiran Offline" else "Kehadiran Online"
        binding.eventDetailFooter.setTitleAndDescription("Tipe Kehadiran", typeText)
    }

    private fun setupSpeakerRecyclerView() {
        val speakerAdapter = EventSpeakerAdapter()
        binding.rvDetailEventSpeakersInfo.apply {
            adapter = speakerAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val spaceDecoration = SpaceItemDecoration(
                requireContext(),
                com.edts.components.R.dimen.margin_8dp,
                SpaceItemDecoration.VERTICAL
            )
            addItemDecoration(spaceDecoration)
        }

        val speakerList = listOf(
            com.edts.desklabv3.R.drawable.image_speaker_1 to "Yovita Handayiani",
            com.edts.desklabv3.R.drawable.image_speaker_2 to "Fauzan Sukmapratama",
            com.edts.desklabv3.R.drawable.image_speaker_3 to "Intan Saliya Utomo"
        )
        speakerAdapter.submitList(speakerList)
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
            Triple(R.drawable.ic_location, "Lokasi Offline", "Amphitheater"),
            Triple(R.drawable.ic_video, "Link Meeting", "Tersedia Setelah Kehadiran Tercatat")
        )

        timeLocationAdapter.submitList(timeLocationList)
    }

    private fun updateTimeLocationListWithAction() {
        val timeLocationList = listOf(
            Triple(R.drawable.ic_calendar, "Tanggal", startDateTime.formatDateRange(endDateTime)),
            Triple(R.drawable.ic_clock, "Waktu", startDateTime.formatTimeRange(endDateTime)),
            Triple(R.drawable.ic_location, "Lokasi Offline", "Amphitheater"),
            Triple(R.drawable.ic_video, "Link Meeting", meetingLink)
        )

        timeLocationAdapter.submitList(timeLocationList)

        timeLocationAdapter.setLinkMeetingAction(true, meetingLink) { actionType ->
            when (actionType) {
                "copy" -> copyMeetingLinkToClipboard()
                "open" -> openMeetingLink()
            }
        }
    }

    private fun copyMeetingLinkToClipboard() {
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = ClipData.newPlainText("Meeting Link", meetingLink)
        clipboardManager.setPrimaryClip(clip)
        Toast.success(requireContext(), "Link meeting disalin")
    }

    private fun openMeetingLink() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(meetingLink))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.error(requireContext(), "Tidak dapat membuka link")
        }
    }

    private fun handleOptionSelected(position: Int) {
        when (position) {
            0 -> handleJoinOnline()
            1 -> handleJoinOffline()
        }
    }

    private fun handleJoinOnline() {
        val loadingDialog = ModalityLoadingPopUp.show(
            context = requireContext(),
            title = "Tunggu sebentar ...",
            isCancelable = false
        )

        binding.root.postDelayed({
            loadingDialog?.dismiss()
            navigateToSuccessAttendanceOnline()
        }, 3000)
    }

    private fun navigateToSuccessAttendanceOnline() {
        val result = bundleOf("fragment_class" to "SuccessAttendanceOnlineView")
        parentFragmentManager.setFragmentResult("navigate_fragment", result)
    }

    private fun handleJoinOffline() {
        val result = bundleOf("fragment_class" to "AssetQRCodeFragment")
        parentFragmentManager.setFragmentResult("navigate_fragment", result)
    }

    companion object {
        const val EVENT_START_DATETIME = "2025-07-24 15:00:00"
        const val EVENT_END_DATETIME = "2025-07-24 17:00:00"

        const val EVENT_DESCRIPTION_HTML = """
            <p>Welcome to Spark Talks!</p>
            <p>Spark Talks is our new sharing session series to ignite ideas and spread knowledge across EDTS. This session is designed to create a space where trainees share fresh insights with Edtizens sparking curiosity, collaboration, and growth!</p>
            <p>For our very first Spark Talks of the year, join Via, Fauzan, and Intan as they dive into the fascinating world of Simplifying UX Complexity: Bridging the Gap Between Design and Development.</p>
            <p>We’ll explore how design and development often speak different “languages,” and how we can build better digital products by bringing them closer together.  Expect fun stories, practical tips, and eye-opening perspectives from both design and tech sides! Special treat!</p>
            <p>The first 30 onsite attendees will get free coffee don’t miss out!</p>
            <p>Let’s spark something new together!🔥</p>
        """
    }
}