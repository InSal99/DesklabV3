package com.edts.desklabv3.features.event.ui.eventdetail

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.edts.components.footer.Footer
import com.edts.components.footer.FooterDelegate
import com.edts.components.infobox.InfoBox
import com.edts.components.input.field.InputField
import com.edts.components.input.field.InputFieldConfig
import com.edts.components.input.field.InputFieldType
import com.edts.components.modal.ModalityConfirmationPopUp
import com.edts.components.modal.ModalityLoadingPopUp
import com.edts.components.radiobutton.RadioGroup
import com.edts.components.radiobutton.RadioGroupDelegate
import com.edts.components.toast.Toast
import com.edts.components.tray.BottomTray
import com.edts.components.tray.BottomTrayDelegate
import com.edts.components.utils.dpToPx
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.FragmentEventDetailBinding
import com.edts.desklabv3.features.SpaceItemDecoration
import formatDateRange
import formatTimeRange
import setupHtmlDescription

class EventDetailViewInvitationDecline : Fragment() {
    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var timeLocationAdapter: EventTimeLocationAdapter
    private var bottomTray: BottomTray? = null
    private var inputField: InputField? = null
    private var currentSelectedOption: String? = null
    private var trayContentContainer: LinearLayout? = null
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

        setupBackButton()
        setEventDetails()
        setupSpeakerRecyclerView()
        setupTimeLocationRecyclerView()
        binding.tvEventDetailDescription.setupHtmlDescription(eventDescription)
        setupFooterButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomTray?.dismiss()
        bottomTray = null
        _binding = null
    }

    private fun setupBackButton() {
        binding.ivDetailBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setupFooterButton() {
        binding.eventDetailFooter.footerDelegate = object : FooterDelegate {
            override fun onPrimaryButtonClicked(footerType: Footer.FooterType) {}
            override fun onSecondaryButtonClicked(footerType: Footer.FooterType) {
                showBottomTray()
            }
            override fun onRegisterClicked() {}
            override fun onContinueClicked() {}
            override fun onCancelClicked() {}
        }
        configureFooter()
    }

    private fun setEventDetails() {
        binding.ivDetailEventPoster.setImageResource(com.edts.components.R.drawable.kit_im_poster1)
        binding.tvDetailEventType.text = "Hybrid Event"
        binding.tvDetailEventCategory.text = "People Development"
        binding.tvDetailEventTitle.text = "Simplifying UX Complexity: Bridging the Gap Between Design and Development"

        startDateTime = EVENT_START_DATETIME
        endDateTime = EVENT_END_DATETIME
        eventDescription = EVENT_DESCRIPTION_HTML
    }

    private fun configureFooter() {
        binding.eventDetailFooter.apply {
            setFooterType(Footer.FooterType.DUAL_BUTTON)
            setPrimaryButtonText("Terima Undangan")
            setSecondaryButtonText("Tolak Undangan")
            setPrimaryButtonEnabled(true)
            setSecondaryButtonEnabled(true)
            setDescriptionVisibility(true)
            setDualButtonDescription("Peserta Terdaftar", "15", "200")
            showInfoBox(true)
            setInfoBoxText("15 peserta sudah reservasi. Amankan spotmu!")
            setInfoBoxVariant(InfoBox.InfoBoxVariant.INFORMATION)
        }
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
                resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_16dp),
                resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_8dp),
                resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_16dp),
                resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_16dp)
            )
        }
        trayContentContainer = mainContainer

        val radioGroup = createRadioGroup()
        mainContainer.addView(radioGroup)
        bottomTray?.setTrayContentView(mainContainer)

        bottomTray?.delegate = object : BottomTrayDelegate {
            override fun onShow(dialog: DialogInterface) {
                bottomTray?.configureFooter { footer: Footer ->
                    footer.setFooterType(Footer.FooterType.DUAL_BUTTON)
                    footer.setPrimaryButtonText("Kirim")
                    footer.setSecondaryButtonText("Batalkan")
                    footer.setPrimaryButtonEnabled(true)
                    footer.setSecondaryButtonEnabled(true)
                    footer.setStroke(true)

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

            buttonSpacing = 8.dpToPx

            val options = listOf(
                "Saya berhalangan dengan jadwal lain",
                "Topik event kurang sesuai minat saya",
                "Lainnya"
            )

            setData(options) { item -> item }

            setOnItemSelectedListener(object : RadioGroupDelegate {
                override fun onItemSelected(position: Int, data: Any?) {
                    val selectedOption = data.toString()
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
                topMargin = resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_4dp)
            }

            val config = InputFieldConfig(
                type = InputFieldType.TextArea,
                title = "",
                hint = "Masukkan alasan penolakan",
                isRequired = true,
                minLines = 3,
                maxLines = 5,
                minLength = 10,
                maxLength = 500
            ).apply {
                setSupportingText("Minimum 10 karakter")
            }
            configure(config, "other_reason")
        }
        trayContentContainer?.addView(inputField)
    }

    private fun hideInputField() {
        inputField?.let {
            trayContentContainer?.removeView(it)
            inputField = null
        }
    }

    private fun handleSubmit() {
        val selectedOption = currentSelectedOption

        when {
            selectedOption == "Lainnya" -> {
                if (inputField?.isValid() == true) {
                    showConfirmationDialog()
                } else {
                    (bottomTray?.dialog?.findViewById<ViewGroup>(android.R.id.content)
                        ?: requireActivity().findViewById(android.R.id.content)
                            )?.let { parent ->
                            Toast(requireContext()).apply {
                                setToast(Toast.Type.ERROR, "Harap isi alasan minimal 10 karakter")
                                showIn(parent)
                            }
                        }
                }
            }
            selectedOption != null -> {
                showConfirmationDialog()
            }
            else -> {
                (bottomTray?.dialog?.findViewById<ViewGroup>(android.R.id.content)
                    ?: requireActivity().findViewById(android.R.id.content)
                        )?.let { parent ->
                        Toast(requireContext()).apply {
                            setToast(Toast.Type.ERROR, "Harap pilih alasan")
                            showIn(parent)
                        }
                    }
            }
        }
    }

    private fun showConfirmationDialog() {
        ModalityConfirmationPopUp.show(
            context = requireContext(),
            title = "Lanjutkan Penolakan?",
            description = "Dengan melanjutkan, penolakan Anda akan tercatat dan hanya bisa dibatalkan melalui HR. Lanjutkan penolakan?",
            confirmButtonLabel = "Ya, Lanjutkan",
            closeButtonLabel = "Tidak",
            onConfirm = {
                showLoadingAndNavigate()
            },
            onClose = {}
        )
    }

    private fun showLoadingAndNavigate() {
        loadingDialog = ModalityLoadingPopUp.show(
            context = requireContext(),
            title = "Tunggu sebentar ...",
            isCancelable = false
        )
        binding.root.postDelayed({
            bottomTray?.dismiss()
            loadingDialog?.dismiss()

            binding.root.postDelayed({
                navigateToSuccessScreen()
            }, 300)
        }, 3000)
    }

    private fun navigateToSuccessScreen() {
        val result = bundleOf("fragment_class" to "SuccessDenyInvitationView")
        parentFragmentManager.setFragmentResult("navigate_fragment", result)
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
            R.drawable.image_speaker_1 to "Yovita Handayiani",
            R.drawable.image_speaker_2 to "Fauzan Sukmapratama",
            R.drawable.image_speaker_3 to "Intan Saliya Utomo"
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

    companion object {
        const val EVENT_START_DATETIME = "2025-07-24 15:00:00"
        const val EVENT_END_DATETIME = "2025-07-24 17:00:00"
        const val EVENT_DESCRIPTION_HTML = """
            <p>Welcome to Spark Talks!</p>
            <p>Spark Talks is our new sharing session series to ignite ideas and spread knowledge across EDTS. This session is designed to create a space where trainees share fresh insights with Edtizens sparking curiosity, collaboration, and growth!</p>
            <p>For our very first Spark Talks of the year, join Via, Fauzan, and Intan as they dive into the fascinating world of Simplifying UX Complexity: Bridging the Gap Between Design and Development.</p>
            <p>We’ll explore how design and development often speak different “languages,” and how we can build better digital products by bringing them closer together. Expect fun stories, practical tips, and eye-opening perspectives from both design and tech sides! Special treat!</p>
            <p>The first 30 onsite attendees will get free coffee don’t miss out!</p>
            <p>Let’s spark something new together!🔥</p>
        """
    }
}