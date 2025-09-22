package com.edts.desklabv3.features.event.ui.success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.FragmentSuccessAttendanceOfflineViewBinding

class SuccessAttendanceOfflineView : Fragment() {
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

        val lottieView = view.findViewById<LottieAnimationView>(R.id.ivIllustAttendanceOffline)
        lottieView.setAnimation(R.raw.il_success)
        lottieView.repeatCount = 0
        lottieView.playAnimation()

        binding.tvTitleAttendanceOffline.text = "Kehadiran Tercatat!"
        binding.tvDescAttendanceOffline.text = "Terima kasih telah konfirmasi kehadiranmu"
        binding.cvPrimaryBtnAttendanceOffline.text = "Lihat Detail Event"
        binding.cvSecondaryBtnAttendanceOffline.text = "Lihat Event Lain"

        setupButtonClickListeners()
    }

    private fun setupButtonClickListeners() {
        binding.cvPrimaryBtnAttendanceOffline.setOnClickListener {
            navigateToEventDetail()
        }

        binding.cvSecondaryBtnAttendanceOffline.setOnClickListener {
            navigateToEventList()
        }
    }

    private fun navigateToEventDetail() {
        val result = bundleOf(
            "fragment_class" to "EventDetailViewAttendance",
            "from_success" to true,
            "attendance_type" to "offline",
            "meeting_link" to meetingLink
        )
        parentFragmentManager.setFragmentResult("navigate_fragment", result)
    }

    private fun navigateToEventList() {
        val result = bundleOf(
            "fragment_class" to "EventMenuFragment",
            "flow_type" to "AttendanceEnd",
            "selected_tab" to 0
        )
        parentFragmentManager.setFragmentResult("navigate_fragment", result)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}