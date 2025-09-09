package com.edts.desklabv3.features.event.ui

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
import androidx.core.text.HtmlCompat
import androidx.core.text.parseAsHtml
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.R
import com.edts.components.footer.Footer
import com.edts.components.status.badge.StatusBadge
import com.edts.components.toast.Toast
import com.edts.components.tray.BottomTray
import com.edts.desklabv3.core.util.ListTagHandler
import com.edts.desklabv3.databinding.FragmentEventDetailBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EventDetailViewAttendanceOffline : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var timeLocationAdapter: EventTimeLocationAdapter
    private var bottomTray: BottomTray? = null

    companion object {
        const val EVENT_DESCRIPTION_HTML = """
            <p>Spark Talks is our new sharing session series to <b>ignite ideas</b> and spread knowledge across EDTS. This session is designed to create a space where trainees share fresh insights with Edtizens sparking curiosity, collaboration, and growth!</p>
            <p>This session features:</p>
            <ul>
              <li>Interactive discussions with industry experts</li>
              <li>Hands-on workshops and activities</li>
              <li>Networking opportunities with peers</li>
              <li>Q&A sessions with speakers</li>
            </ul>
            <p>Join us for an exciting learning experience! 🚀</p>
            <p>For more information, visit our <a href="https://example.com">website</a>.</p>
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
        binding.eventDetailFooter.delegate = object : com.edts.components.footer.FooterDelegate {
            override fun onPrimaryButtonClicked(footerType: Footer.FooterType) {
                showBottomTray()
            }

            override fun onSecondaryButtonClicked(footerType: Footer.FooterType) {
                showEventInfo()
            }

            override fun onRegisterClicked() {
            }

            override fun onContinueClicked() {
            }

            override fun onCancelClicked() {
            }
        }

        configureFooterForEventState()
    }

    private fun configureFooterForEventState() {
        val isEventFull = false
        val isUserRegistered = false
        val isEventPast = false

        binding.eventDetailFooter.apply {
            setShadowVisibility(true)
            when {
                isEventPast -> {
                    setFooterType(Footer.FooterType.NO_ACTION)
                    setTitleAndDescription(
                        "Event Completed",
                        "This event has already ended"
                    )
                    setStatusBadge("Completed", StatusBadge.ChipType.APPROVED)
                }
                isUserRegistered -> {
                    setFooterType(Footer.FooterType.DUAL_BUTTON)
                    setPrimaryButtonText("View Details")
                    setSecondaryButtonText("Cancel Registration")
                    setPrimaryButtonEnabled(true)
                    setSecondaryButtonEnabled(true)
                }
                isEventFull -> {
                    setFooterType(Footer.FooterType.CALL_TO_ACTION_DETAIL)
                    setTitleAndDescription(
                        "Event Full",
                        "This event has reached maximum capacity"
                    )
                    setPrimaryButtonText("Join Waitlist")
                    setPrimaryButtonEnabled(true)
                }
                else -> {
                    setFooterType(Footer.FooterType.CALL_TO_ACTION)
                    setPrimaryButtonText("Catat Kehadiran")
                    setPrimaryButtonEnabled(true)
                }
            }
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
        val scanQRFragment = ScanQRView()
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
            Triple(R.drawable.placeholder, "Tanggal", formatDateRange(startDateTime, endDateTime)),
            Triple(R.drawable.placeholder, "Waktu", formatTimeRange(startDateTime, endDateTime)),
            Triple(R.drawable.placeholder, "Lokasi Offline", "Grand Ballroom, Hotel Majestic"),
            Triple(R.drawable.placeholder, "Link Meeting", "123 Main Street, City Center")
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

    override fun onDestroyView() {
        super.onDestroyView()
        bottomTray?.dismiss()
        bottomTray = null
        _binding = null
    }
}