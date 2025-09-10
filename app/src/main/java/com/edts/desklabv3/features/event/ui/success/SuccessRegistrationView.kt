package com.edts.desklabv3.features.event.ui.success

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.FragmentSuccessAttendanceOfflineViewBinding

class SuccessRegistrationView : Fragment() {
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

        binding.ivIllustAttendanceOffline.setImageResource(R.drawable.il_check)
        binding.tvTitleAttendanceOffline.text = "Registrasi Berhasil!"
        binding.tvDescAttendanceOffline.text = "Lihat event saya untuk detail pendaftaran"
        binding.cvPrimaryBtnAttendanceOffline.text = "Lihat Event Saya"
        binding.cvSecondaryBtnAttendanceOffline.text = "Lihat Event Lain"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}