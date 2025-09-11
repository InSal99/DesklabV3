package com.edts.desklabv3.features.event.ui.success

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.FragmentSuccessAttendanceOfflineViewBinding

class SuccessDenyInvitationView : Fragment() {
    private var _binding: FragmentSuccessAttendanceOfflineViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuccessAttendanceOfflineViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivIllustAttendanceOffline.setImageResource(R.drawable.il_calendar)
        binding.tvTitleAttendanceOffline.text = "Undangan Ditolak!"
        binding.tvDescAttendanceOffline.text = "Kamu tetap bisa ikut event lainnya"
        binding.cvPrimaryBtnAttendanceOffline.text = "Lihat Event Lainnya"
        binding.cvSecondaryBtnAttendanceOffline.text = "Kembali Ke Beranda"

        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        binding.cvPrimaryBtnAttendanceOffline.setOnClickListener {
            val result = bundleOf("fragment_class" to "EventListInvitationTolakEndView")
            parentFragmentManager.setFragmentResult("navigate_fragment", result)
        }

        binding.cvSecondaryBtnAttendanceOffline.setOnClickListener {
            val result = bundleOf("fragment_class" to "HomeDaftarRSVPView")
            parentFragmentManager.setFragmentResult("navigate_fragment", result)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}