package com.edts.desklabv3.features.event.ui.eventdetail

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.core.text.parseAsHtml
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.R
import com.edts.components.footer.Footer
import com.edts.components.footer.FooterDelegate
import com.edts.components.modal.ModalityConfirmationPopUp
import com.edts.components.modal.ModalityLoadingPopUp
import com.edts.components.toast.Toast
import com.edts.components.tray.BottomTray
import com.edts.desklabv3.core.util.ListTagHandler
import com.edts.desklabv3.databinding.FragmentEventDetailBinding
import com.edts.desklabv3.features.event.ui.attendanceoffline.ScanQRAttendanceView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EventDetailViewNoRSVP : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var timeLocationAdapter: EventTimeLocationAdapter
    private var bottomTray: BottomTray? = null
    private var loadingDialog: AlertDialog? = null

    companion object {
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
        setupSpeakerRecyclerView()
        setupTimeLocationRecyclerView()
        setupHtmlDescription()
        setupFooterButton()
    }

    private fun setupBackButton() {
        binding.ivDetailBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupFooterButton() {
        binding.eventDetailFooter.footerDelegate = object : FooterDelegate {
            override fun onPrimaryButtonClicked(footerType: Footer.FooterType) {
                startFakeBackgroundTask()
            }

            override fun onSecondaryButtonClicked(footerType: Footer.FooterType) {
//                showEventInfo()
            }

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

    private fun showEventInfo() {
        Toast.info(requireContext(), "Show event info")
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

        bottomTray?.delegate = object : com.edts.components.tray.BottomTrayDelegate {
            override fun onShow(dialog: DialogInterface) {
            }

            override fun onDismiss(dialog: DialogInterface) {
                bottomTray = null
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        }

        bottomTray?.show(childFragmentManager, "event_actions_bottom_tray")
    }

    private fun createBottomTrayContent(): View {
        val contentView = layoutInflater.inflate(com.edts.desklabv3.R.layout.bottom_tray_event_options, null)

        val recyclerView = contentView.findViewById<RecyclerView>(com.edts.desklabv3.R.id.rvEventOptions)
        val optionAdapter = EventOptionAdapter { eventType ->
            Log.d("Present", "User present $eventType")
            bottomTray?.dismiss()

            Handler(Looper.getMainLooper()).postDelayed({
                handleOptionSelected(eventType.toString())
            }, 300)
        }

        recyclerView.apply {
            adapter = optionAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val options = listOf(
            "online" to R.drawable.placeholder,
            "offline" to R.drawable.placeholder
        )

        optionAdapter.submitList(options)

        return contentView
    }

    private fun handleOptionSelected(eventType: String) {
        when (eventType) {
            "0" -> {
                handleJoinOnline()
            }
            "1" -> {
                handleJoinOffline()
            }
        }
    }

    private fun handleJoinOnline() {
        Toast.info(requireContext(), "Joining online event...")
        Log.d("Present Online", "Retrieving Data...")
    }

    private fun handleJoinOffline() {
        val scanQRFragment = ScanQRAttendanceView()
        Log.d("Present Offline", "Go To Scanner")

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, scanQRFragment)
            .addToBackStack("ScanQRView")
            .commit()
            .apply { Log.d("Present Offline", "Go To Scanner") }
    }

    private fun setupSpeakerRecyclerView() {
        val speakerAdapter = EventSpeakerAdapter()
        binding.rvDetailEventSpeakersInfo.apply {
            adapter = speakerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val speakerList = listOf(
            R.drawable.avatar_placeholder to "John Doe",
            R.drawable.avatar_placeholder to "Jane Smith"
        )
        speakerAdapter.submitList(speakerList)
    }

    private fun setupTimeLocationRecyclerView() {
        timeLocationAdapter = EventTimeLocationAdapter()

        binding.rvDetailEventTimeLocation.apply {
            adapter = timeLocationAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val startDateTime = "2023-12-25 19:00:00"
        val endDateTime = "2023-12-27 22:00:00"

        val timeLocationList = listOf(
            Triple(com.edts.desklabv3.R.drawable.ic_calendar, "Tanggal", formatDateRange(startDateTime, endDateTime)),
            Triple(com.edts.desklabv3.R.drawable.ic_clock, "Waktu", formatTimeRange(startDateTime, endDateTime)),
//            Triple(com.edts.desklabv3.R.drawable.ic_location, "Lokasi Offline", "Grand Ballroom, Hotel Majestic"),
            Triple(com.edts.desklabv3.R.drawable.ic_video, "Link Meeting", "123 Main Street, City Center")
        )

        timeLocationAdapter.submitList(timeLocationList)
    }

    private fun formatDateRange(startDateTime: String, endDateTime: String): String {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val startDate = format.parse(startDateTime)!!
            val endDate = format.parse(endDateTime)!!

            val indonesianLocale = Locale("id", "ID")

            if (isSameDay(startDate, endDate)) {
                SimpleDateFormat("EEEE, dd MMMM yyyy", indonesianLocale).format(startDate)
            } else {
                if (isSameYear(startDate, endDate)) {
                    val dateFormat = SimpleDateFormat("EEEE, dd MMMM", indonesianLocale)
                    "${dateFormat.format(startDate)} - ${dateFormat.format(endDate)} ${SimpleDateFormat("yyyy", indonesianLocale).format(endDate)}"
                } else {
                    val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", indonesianLocale)
                    "${dateFormat.format(startDate)} - ${dateFormat.format(endDate)}"
                }
            }
        } catch (e: Exception) {
            "Tanggal tidak valid"
        }
    }

    private fun isSameYear(date1: Date, date2: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date1
        val year1 = calendar.get(Calendar.YEAR)
        calendar.time = date2
        val year2 = calendar.get(Calendar.YEAR)
        return year1 == year2
    }

    private fun formatTimeRange(startDateTime: String, endDateTime: String): String {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val startDate = format.parse(startDateTime)!!
            val endDate = format.parse(endDateTime)!!

            val indonesianLocale = Locale("id", "ID")
            val timeFormat = SimpleDateFormat("HH:mm", indonesianLocale)

            "${timeFormat.format(startDate)} - ${timeFormat.format(endDate)} WIB"
        } catch (e: Exception) {
            "Waktu tidak valid"
        }
    }

    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date1
        val day1 = calendar.get(Calendar.DAY_OF_YEAR)
        calendar.time = date2
        val day2 = calendar.get(Calendar.DAY_OF_YEAR)
        return day1 == day2
    }

    private fun setupHtmlDescription() {
        val preprocessed = EVENT_DESCRIPTION_HTML
            .replace("<ul>", "<myul>")
            .replace("</ul>", "</myul>")
            .replace("<ol>", "<myol>")
            .replace("</ol>", "</myol>")
            .replace("<li>", "<myli>")
            .replace("</li>", "</myli>")

        val handler = ListTagHandler(binding.tvEventDetailDescription)

        val spanned: Spanned = preprocessed.parseAsHtml(HtmlCompat.FROM_HTML_MODE_LEGACY, null, handler)
        binding.tvEventDetailDescription.text = spanned

        binding.tvEventDetailDescription.movementMethod = LinkMovementMethod.getInstance()
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
                android.widget.Toast.makeText(requireContext(), "Modal Closed.", android.widget.Toast.LENGTH_SHORT).show()
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
        bottomTray?.dismiss()
        bottomTray = null
        _binding = null
    }
}