package com.edts.desklabv3.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.FragmentEntryPointsViewBinding
import com.edts.desklabv3.features.event.ui.EventDetailViewAttendanceOffline
import com.edts.desklabv3.features.event.ui.EventDetailViewTolakUndangan
import com.edts.desklabv3.features.event.ui.RSVPFormView

class EntryPointsView : Fragment() {

    private var _binding: FragmentEntryPointsViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntryPointsViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonClickListeners()
    }

    private fun setupButtonClickListeners() {
        binding.btnLibButton.setOnClickListener {
            navigateToFragment(ButtonComponentView())
        }

        binding.btnLibInfobox.setOnClickListener {
            navigateToFragment(InfoboxComponentView())
        }

        binding.btnLibFooter.setOnClickListener {
            navigateToFragment(FooterComponentView())
        }

        binding.btnLibToast.setOnClickListener {
            navigateToFragment(ToastComponentView())
        }

        binding.btnLibCheckbox.setOnClickListener {
            navigateToFragment(CheckboxComponentView())
        }

        binding.btnLibRadioButton.setOnClickListener {
            navigateToFragment(RadioButtonComponentView())
        }

        binding.btnLibInputField.setOnClickListener {
            navigateToFragment(InputFieldComponentView())
        }

        binding.btnLibOptionCard.setOnClickListener {
            navigateToFragment(OptionCardComponentView())
        }

        binding.btnLibStatusBadge.setOnClickListener {
            navigateToFragment(StatusBadgeComponentView())
        }

        binding.btnLibDetailInformationA.setOnClickListener {
            navigateToFragment(DetailInformationAComponentView())
        }

        binding.btnLibDetailInformationSpeaker.setOnClickListener {
            navigateToFragment(DetailInformationSpeakerComponentView())
        }

        binding.btnLibBottomTray.setOnClickListener {
            navigateToFragment(BottomTrayComponentView())
        }

        binding.btnFlow1.setOnClickListener {
            navigateToFragment(EventDetailViewAttendanceOffline())
        }

        binding.btnFlow2.setOnClickListener {
            navigateToFragment(EventDetailViewTolakUndangan())
        }

        binding.btnFlow3.setOnClickListener {
            navigateToFragment(RSVPFormView())
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}