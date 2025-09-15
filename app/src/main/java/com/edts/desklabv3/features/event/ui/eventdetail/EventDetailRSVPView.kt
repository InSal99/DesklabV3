package com.edts.desklabv3.features.event.ui.eventdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.edts.components.footer.Footer
import com.edts.components.footer.FooterDelegate
import com.edts.components.infobox.InfoBox
import com.edts.components.tray.BottomTray
import com.edts.desklabv3.R
import com.edts.desklabv3.core.util.formatDateRange
import com.edts.desklabv3.core.util.formatTimeRange
import com.edts.desklabv3.core.util.setupHtmlDescription
import com.edts.desklabv3.databinding.FragmentEventDetailBinding
import com.edts.desklabv3.features.event.ui.rsvp.RSVPFormView

class EventDetailRSVPView : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var timeLocationAdapter: EventTimeLocationAdapter
    private var bottomTray: BottomTray? = null

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
                navigateToRSVPForm()
            }

            override fun onSecondaryButtonClicked(footerType: Footer.FooterType) {}
            override fun onRegisterClicked() {}
            override fun onContinueClicked() {}
            override fun onCancelClicked() {}
        }

        configureFooter()
    }

    private fun navigateToRSVPForm() {
        val rsvpFormView = RSVPFormView()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, rsvpFormView)
            .addToBackStack("RSVPFormView")
            .commit()
    }

    private fun configureFooter() {
        binding.eventDetailFooter.apply {
            setFooterType(Footer.FooterType.CALL_TO_ACTION)
            setPrimaryButtonText("Daftar Sekarang")
            setPrimaryButtonEnabled(true)
            setDescriptionVisibility(true)
            setDualButtonDescription("Peserta Terdaftar", "5", "200")

            setInfoBoxText("5 peserta sudah reservasi. Amankan spotmu!")
            setInfoBoxVariant(InfoBox.InfoBoxVariant.INFORMATION)
            showInfoBox(true)
        }
    }

    private fun setEventDetails() {
        startDateTime = EVENT_START_DATETIME
        endDateTime = EVENT_END_DATETIME
        eventDescription = EVENT_DESCRIPTION_HTML
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

    companion object {
        const val EVENT_START_DATETIME = "2025-07-23 15:00:00"
        const val EVENT_END_DATETIME = "2025-07-23 17:00:00"

        const val EVENT_DESCRIPTION_HTML = """
            <p>HALO EDTIZENS! 🔥</p>
            <p>Siap-siap, medan pertempuran Land of Dawn akan segera memanas! Saatnya menunjukkan siapa yang benar-benar penguasa lantai 40 & 42 dalam dunia Mobile Legends!</p>
            <p>EDTS and Ukirama Mobile Legends Championship 2024 resmi dimulai! 🎮</p>
            <p>Turnamen ini bukan sekadar game ini adalah ajang pembuktian. Unjuk strategi, kerja sama tim, refleks cepat, dan mental juara!</p>
            <p>🛡️⚔️ Apa yang Bisa Kamu Harapkan? </p>
            <ul>
              <li>Pertempuran epik 5v5 yang mendebarkan antara tim-tim terbaik dari EDTS dan Ukirama.</li>
              <li>Kesempatan untuk menunjukkan skill individu & kerja sama tim. Momen clutch, comeback dramatis, dan strategi outplay yang akan jadi bahan obrolan sebulan ke depan!</li>
              <li>Suasana seru, support antar kolega, dan tentu saja... hadiah & pengakuan sebagai Champion!</li>
            </ul>
            <p>🛡️⚔️ Bagaimana cara mendaftarkan tim? </p>
            <ol>
              <li>Daftarkan diri pada acara ini</li>
              <li>Isi data tim pada form reservasi</li>
              <li>Tunggu email konfirmasi pendaftaran</li>
              <li>Jika terkonfirmasi, maka bersiaplah untuk bertempur!</li>
            </ol>
            <p>Jika ada pertanyaan bisa menghubungi <a href="https://telegram.org/">@myudasulaiman</a>.</p>
            <p>Terima kasih 🙏😊</p>
        """
    }
}