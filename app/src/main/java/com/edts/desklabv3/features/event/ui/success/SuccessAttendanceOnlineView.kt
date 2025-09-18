package com.edts.desklabv3.features.event.ui.success

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.edts.components.toast.Toast
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.FragmentSuccessAttendanceOfflineViewBinding

class SuccessAttendanceOnlineView : Fragment() {
    private var _binding: FragmentSuccessAttendanceOfflineViewBinding? = null
    private val binding get() = _binding!!

    private val meetingLink = "https://teams.microsoft.com/l/meetup-join/your-meeting-id"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuccessAttendanceOfflineViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitleAttendanceOffline.text = "Kehadiran Tercatat!"
        binding.tvDescAttendanceOffline.text = "Terima kasih telah konfirmasi kehadiranmu"
        binding.cvPrimaryBtnAttendanceOffline.text = "Akses Meeting"
        binding.cvSecondaryBtnAttendanceOffline.text = "Lihat Detail Event"

        val lottieView = view.findViewById<LottieAnimationView>(R.id.ivIllustAttendanceOffline)
        lottieView.setAnimation(R.raw.il_success)
        lottieView.repeatCount = LottieDrawable.INFINITE
        lottieView.playAnimation()

        setupButtonClickListeners()
    }

    private fun setupButtonClickListeners() {
        binding.cvPrimaryBtnAttendanceOffline.setOnClickListener {
            showShareTray()
        }

        binding.cvSecondaryBtnAttendanceOffline.setOnClickListener {
            navigateToEventDetail()
        }
    }

    private fun showShareTray() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, meetingLink)
            type = "text/plain"
        }

        val shareChooser = Intent.createChooser(shareIntent, "Bagikan link meeting")

        if (shareChooser.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(shareChooser)
        } else {
            // Use the correct ClipboardManager class
            val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("Link Meeting", meetingLink)
            clipboardManager.setPrimaryClip(clip)
            Toast.success(requireContext(), "Link meeting berhasil disalin")
        }
    }

    private fun navigateToEventDetail() {
        val result = bundleOf(
            "fragment_class" to "EventDetailViewAttendance",
            "from_success" to true, // ← Add this identifier
            "attendance_type" to "online",
            "meeting_link" to meetingLink // ← Pass the meeting link
        )
        parentFragmentManager.setFragmentResult("navigate_fragment", result)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}