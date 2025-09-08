package com.edts.desklabv3.features.event.ui

import android.content.DialogInterface
import android.os.Bundle
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.text.HtmlCompat
import androidx.core.text.parseAsHtml
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.edts.components.R
import com.edts.components.footer.Footer
import com.edts.components.footer.FooterDelegate
import com.edts.components.input.field.InputField
import com.edts.components.input.field.InputFieldConfig
import com.edts.components.input.field.InputFieldDelegate
import com.edts.components.input.field.InputFieldType
import com.edts.components.radiobutton.RadioGroup
import com.edts.components.radiobutton.RadioGroupDelegate
import com.edts.components.status.badge.StatusBadge
import com.edts.components.toast.Toast
import com.edts.components.tray.BottomTray
import com.edts.desklabv3.core.util.ListTagHandler
import com.edts.desklabv3.databinding.FragmentEventDetailBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EventDetailViewTolakUndangan : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var timeLocationAdapter: EventTimeLocationAdapter
    private var bottomTray: BottomTray? = null
    private var inputField: InputField? = null
    private var currentSelectedOption: String? = null
    private var trayContentContainer: LinearLayout? = null

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
            }

            override fun onSecondaryButtonClicked(footerType: Footer.FooterType) {
                showBottomTray()
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
        val isUserRegistered = true
        val isEventPast = false

        binding.eventDetailFooter.apply {
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
                    setPrimaryButtonText("Terima Undangan")
                    setSecondaryButtonText("Tolak Undangan")
                    setPrimaryButtonEnabled(true)
                    setSecondaryButtonEnabled(true)

                    setDualButtonDescription(
                        title = "Peserta Terdaftar",
                        supportText1 = "1",
                        supportText2 = "10"
                    )
                    setDescriptionVisibility(true)
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
            title = "Alasan Penolakan",
            showDragHandle = true,
            showFooter = true,
            hasShadow = true,
            hasStroke = true
        )

        val mainContainer = LinearLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            setPadding(
                resources.getDimensionPixelSize(R.dimen.margin_16dp),
                resources.getDimensionPixelSize(R.dimen.margin_8dp),
                resources.getDimensionPixelSize(R.dimen.margin_16dp),
                resources.getDimensionPixelSize(R.dimen.margin_16dp)
            )
        }
        trayContentContainer = mainContainer

        val radioGroup = createRadioGroup()
        mainContainer.addView(radioGroup)
        bottomTray?.setContentView(mainContainer)


        bottomTray?.delegate = object : com.edts.components.tray.BottomTrayDelegate {
            override fun onShow(dialog: DialogInterface) {
                bottomTray?.configureFooter { footer: Footer ->
                    footer.setFooterType(Footer.FooterType.DUAL_BUTTON)
                    footer.setPrimaryButtonText("Kirim")
                    footer.setSecondaryButtonText("Batalkan")
                    footer.setPrimaryButtonEnabled(true)
                    footer.setSecondaryButtonEnabled(true)

                    footer.delegate = object : FooterDelegate {
                        override fun onPrimaryButtonClicked(footerType: Footer.FooterType) {
                            handleSubmit()
                        }

                        override fun onSecondaryButtonClicked(footerType: Footer.FooterType) {
                            bottomTray?.dismiss()
                        }

                        override fun onRegisterClicked() {}
                        override fun onContinueClicked() {}
                        override fun onCancelClicked() {}
                    }
                }
            }

            override fun onDismiss(dialog: DialogInterface) {
                bottomTray = null
                inputField = null
                currentSelectedOption = null
                trayContentContainer = null
            }
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        }

        bottomTray?.show(childFragmentManager, "rejection_reason_bottom_tray")
    }

    private fun createRadioGroup(): RadioGroup {
        return RadioGroup(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            buttonSpacing = 8.dpToPx()

            val options = listOf(
                "Saya berhalangan dengan jadwal lain",
                "Topik event kurang sesuai minat saya",
                "Lainnya"
            )

            setData(options) { item ->
                item
            }

            setOnItemSelectedListener(object : RadioGroupDelegate {
                override fun onItemSelected(position: Int, data: Any?) {
                    val selectedOption = data.toString()
                    Log.d("RadioGroup", "Selected: $selectedOption at position $position")

                    if (selectedOption == "Lainnya") {
                        showInputField()
                    } else {
                        hideInputField()
                    }

                    currentSelectedOption = selectedOption
                }
            })
        }
    }

    private fun showInputField() {
        hideInputField()

        inputField = InputField(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = resources.getDimensionPixelSize(R.dimen.margin_4dp)
            }

            val config = InputFieldConfig(
                type = InputFieldType.TextArea,
                title = "",
                hint = "Masukkan alasan penolakan",
                isRequired = false,
                minLines = 3,
                maxLines = 5,
                minLength = 10,
                maxLength = 500
            ).apply {
                setSupportingText("Minimum 10 karakter")
            }

            configure(config, "other_reason")

            delegate = object : InputFieldDelegate {
                override fun onValueChange(fieldId: String, value: Any?) {
                    Log.d("InputField", "Value changed: $value")
                }

                override fun onValidationChange(fieldId: String, isValid: Boolean) {
                    Log.d("InputField", "Validation changed: $isValid")
                }
            }
        }

        trayContentContainer?.addView(inputField)
    }

    private fun hideInputField() {
        inputField?.let {
            trayContentContainer?.removeView(it)
            inputField = null
        }
    }

    private fun handleOptionSelected(option: String) {
        when (option) {
            "Saya berhalangan dengan jadwal lain" -> {
                handleOptionOne()
            }
            "Topik event kurang sesuai minat saya" -> {
                handleOptionTwo()
            }
            "Lainnya" -> {
            }
        }
    }

    private fun handleSubmit() {
        val selectedOption = currentSelectedOption
        val otherReason = inputField?.getValue()?.toString()?.trim()

        when {
            selectedOption == "Lainnya" && otherReason.isNullOrEmpty() -> {
                inputField?.setError("Harap jelaskan alasan Anda")
                Toast.error(requireContext(), "Harap isi alasan lainnya")
            }
            selectedOption == "Lainnya" && otherReason != null -> {
                handleOptionThreeWithReason(otherReason)
                bottomTray?.dismiss()
            }
            selectedOption != null -> {
                handleOptionSelected(selectedOption)
                bottomTray?.dismiss()
            }
            else -> {
                Toast.error(requireContext(), "Harap pilih alasan")
            }
        }
    }

    private fun handleOptionThreeWithReason(reason: String) {
        Toast.info(requireContext(), "Alasan: $reason")
        Log.d("Rejection", "Custom reason: $reason")
    }

    private fun handleOptionOne() {
        Toast.info(requireContext(), "Option One selected")
        Log.d("Option", "Option One selected")
    }

    private fun handleOptionTwo() {
        Toast.info(requireContext(), "Option Two selected")
        Log.d("Option", "Option Two selected")
    }

    private fun handleOptionThree() {
        Toast.info(requireContext(), "Option Three selected")
        Log.d("Option", "Option Three selected")
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

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}