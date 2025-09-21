package com.edts.desklabv3.features.event.ui.success

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.FragmentSuccessAttendanceOfflineViewBinding

class SuccessRegistrationView : Fragment() {
    private var _binding: FragmentSuccessAttendanceOfflineViewBinding? = null
    private val binding get() = _binding!!

    private var flowType: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuccessAttendanceOfflineViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flowType = arguments?.getString("flow_type", "") ?: ""

        setupUI()
        setupButtonClicks()
    }

    private fun setupUI() {
        val lottieView = view?.findViewById<LottieAnimationView>(R.id.ivIllustAttendanceOffline)
        lottieView?.setAnimation(R.raw.il_success)
        lottieView?.repeatCount = 0
        lottieView?.playAnimation()

        binding.tvTitleAttendanceOffline.text = "Registrasi Berhasil!"
        binding.tvDescAttendanceOffline.text = "Lihat event saya untuk detail pendaftaran"
        binding.cvPrimaryBtnAttendanceOffline.text = "Lihat Event Saya"
        binding.cvSecondaryBtnAttendanceOffline.text = "Lihat Event Lain"
    }

//    private fun setupButtonClicks() {
//        binding.cvPrimaryBtnAttendanceOffline.setOnClickListener {
//            when (flowType) {
//                "RegisRSVP" -> {
//                    parentFragmentManager.setFragmentResult(
//                        "navigate_fragment",
//                        bundleOf("fragment_class" to "MyEventDaftarRSVPView")
//                    )
//                }
//                "InvitationNoRSVP" -> {
//                    parentFragmentManager.setFragmentResult(
//                        "navigate_fragment",
//                        bundleOf("fragment_class" to "MyEventsFragmentNoRSVP")
//                    )
//                }
//                else -> {
//                    parentFragmentManager.setFragmentResult(
//                        "navigate_fragment",
//                        bundleOf("fragment_class" to "MyEventDaftarRSVPView")
//                    )
//                }
//            }
//        }
//
//        binding.cvSecondaryBtnAttendanceOffline.setOnClickListener {
//            when (flowType) {
//                "RegisRSVP" -> {
//                    parentFragmentManager.setFragmentResult(
//                        "navigate_fragment",
//                        bundleOf("fragment_class" to "EventListDaftarRSVPView")
//                    )
//                }
//                "InvitationNoRSVP" -> {
//                    parentFragmentManager.setFragmentResult(
//                        "navigate_fragment",
//                        bundleOf("fragment_class" to "EventListInvitationNoRSVPView")
//                    )
//                }
//                else -> {
//                    parentFragmentManager.setFragmentResult(
//                        "navigate_fragment",
//                        bundleOf("fragment_class" to "EventListDaftarRSVPView")
//                    )
//                }
//            }
//        }
//    }

    private fun setupButtonClicks() {
        binding.cvPrimaryBtnAttendanceOffline.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                "navigate_fragment",
                bundleOf(
                    "fragment_class" to "EventMenuFragment",
                    "flow_type" to flowType,
                    "selected_tab" to 1 // Event Saya
                )
            )
        }

        binding.cvSecondaryBtnAttendanceOffline.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                "navigate_fragment",
                bundleOf(
                    "fragment_class" to "EventMenuFragment",
                    "flow_type" to flowType,
                    "selected_tab" to 0 // Daftar Event
                )
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}