package com.edts.desklabv3.features.home.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.edts.desklabv3.HeaderConfigurator
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.FragmentHomeMenuBinding

class HomeMenuFragment : Fragment() {
    private var _binding: FragmentHomeMenuBinding? = null
    private val binding get() = _binding!!

    private var headerConfigurator: HeaderConfigurator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val flowType = arguments?.getString("flow_type", "") ?: ""

        val fragment = when (flowType) {
            "RegisRSVP" -> HomeRegistrationRSVPView.newInstance(isEndFlow = false)
            "RegisEndRSVP" -> HomeRegistrationRSVPView.newInstance(isEndFlow = true)
            "InvitationNoRSVP" -> HomeInvitationNoRSVPView.newInstance(isEndFlow = false)
            "InvitationENDNoRSVP" -> HomeInvitationNoRSVPView.newInstance(isEndFlow = true)
            "Attendance" -> HomeAttendanceView.newInstance(isEndFlow = false)
            "AttendanceEnd" -> HomeAttendanceView.newInstance(isEndFlow = true)
            "TolakUndangan" -> HomeInvitationDeclineView.newInstance(isEndFlow = false)
            "TolakUndanganEnd" -> HomeInvitationDeclineView.newInstance(isEndFlow = true)
            "TeamReport" -> HomeManagerView()
            else -> Fragment()
        }

        loadFragment(fragment)
    }

    private fun loadFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.home_fragment_container, fragment)
            .commit()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HeaderConfigurator) {
            headerConfigurator = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        headerConfigurator = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}