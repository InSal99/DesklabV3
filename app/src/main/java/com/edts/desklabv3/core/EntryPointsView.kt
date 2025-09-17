package com.edts.desklabv3.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.edts.desklabv3.R
import com.edts.desklabv3.core.component.BadgeComponentView
import com.edts.desklabv3.core.component.BottomNavigationComponentView
import com.edts.desklabv3.core.component.BottomNavigationItemComponentView
import com.edts.desklabv3.core.component.CardDetailInformationBComponentView
import com.edts.desklabv3.core.component.CardLeftSlotComponentView
import com.edts.desklabv3.core.component.CardMultiDetailCardComponentView
import com.edts.desklabv3.core.component.EventCardBadgeComponentView
import com.edts.desklabv3.core.component.EventCardComponentView
import com.edts.desklabv3.core.component.EventCardStatusComponentView
import com.edts.desklabv3.core.component.HeaderComponentView
import com.edts.desklabv3.core.component.InputSearchComponentView
import com.edts.desklabv3.core.component.MonthlyPickerComponentView
import com.edts.desklabv3.core.component.SelectionChipComponentView
import com.edts.desklabv3.core.component.SelectionDropdownFilterComponentView
import com.edts.desklabv3.core.component.SortButtonComponentView
import com.edts.desklabv3.core.component.TabComponentView
import com.edts.desklabv3.core.component.TabItemComponentView
import com.edts.desklabv3.databinding.FragmentEntryPointsViewBinding
import com.edts.desklabv3.features.event.ui.eventdetail.EventDetailRSVPView
import com.edts.desklabv3.features.event.ui.eventlist.EventListDaftarRSVPView
import com.edts.desklabv3.features.home.ui.HomeAttendanceView
import com.edts.desklabv3.features.home.ui.HomeDaftarRSVPView
import com.edts.desklabv3.features.home.ui.HomeInvitationNoRSVPView
import com.edts.desklabv3.features.home.ui.HomeInvitationTolakView
import com.edts.desklabv3.features.home.ui.HomeManagerView

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
        setupNavigationListener()
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

        binding.btnLibSelectionDropdownFilter.setOnClickListener {
            navigateToFragment(SelectionDropdownFilterComponentView())
        }

        binding.btnLibBottomTray.setOnClickListener {
            navigateToFragment(BottomTrayComponentView())
        }

        binding.btnLibSelectionDropdownFilter.setOnClickListener {
            navigateToFragment(SelectionDropdownFilterComponentView())
        }

        binding.btnLibSelectionChip.setOnClickListener {
            navigateToFragment(SelectionChipComponentView())
        }

        binding.btnLibCardMultiDetailCard.setOnClickListener {
            navigateToFragment(CardMultiDetailCardComponentView())
        }

        binding.btnLibCardDetailInformationB.setOnClickListener {
            navigateToFragment(CardDetailInformationBComponentView())
        }

        binding.btnLibCardLeftSlot.setOnClickListener {
            navigateToFragment(CardLeftSlotComponentView())
        }

        binding.btnLibBottomNavigation.setOnClickListener {
            navigateToFragment(BottomNavigationComponentView())
        }

        binding.btnLibBottomNavigationItem.setOnClickListener {
            navigateToFragment(BottomNavigationItemComponentView())
        }

        binding.btnLibTabItem.setOnClickListener {
            navigateToFragment(TabItemComponentView())
        }

        binding.btnLibInputSearch.setOnClickListener {
            navigateToFragment(InputSearchComponentView())
        }

        binding.btnLibSortButton.setOnClickListener {
            navigateToFragment(SortButtonComponentView())
        }

        binding.btnLibEventCard.setOnClickListener {
            navigateToFragment(EventCardComponentView())
        }

        binding.btnLibEventCardBadge.setOnClickListener {
            navigateToFragment(EventCardBadgeComponentView())
        }

        binding.btnLibEventCardStatus.setOnClickListener {
            navigateToFragment(EventCardStatusComponentView())
        }

        binding.btnLibBadge.setOnClickListener {
            navigateToFragment(BadgeComponentView())
        }

        binding.btnLibTab.setOnClickListener {
            navigateToFragment(TabComponentView())
        }

        binding.btnLibHeader.setOnClickListener {
            navigateToFragment(HeaderComponentView())
        }

        binding.btnLibEventInvitationCard.setOnClickListener {
            navigateToFragment(EventInvitationComponentFragment())
        }
        binding.btnLibEventModalityConfirmation.setOnClickListener {
            navigateToFragment(EventModalityConfirmationComponentFragment())
        }
        binding.btnLibEventModalityLoading.setOnClickListener {
            navigateToFragment(EventModalityLoadingComponentFragment())
        }
        binding.btnLibMyEventCard.setOnClickListener {
            navigateToFragment(MyEventsComponentFragment())
        }

        binding.btnLibMonthlyPicker.setOnClickListener {
            navigateToFragment(MonthlyPickerComponentView())
        }

        binding.btnFlow1.setOnClickListener {
            val result = bundleOf("fragment_class" to "HomeDaftarRSVPView")
            parentFragmentManager.setFragmentResult("entrypoint_navigation", result)
        }

        binding.btnFlow2.setOnClickListener {
            val result = bundleOf("fragment_class" to "HomeInvitationNoRSVPView")
            parentFragmentManager.setFragmentResult("entrypoint_navigation", result)
        }

        binding.btnFlow3.setOnClickListener {
            val result = bundleOf("fragment_class" to "HomeAttendanceView")
            parentFragmentManager.setFragmentResult("entrypoint_navigation", result)
        }

        binding.btnFlow4.setOnClickListener {
            val result = bundleOf("fragment_class" to "HomeInvitationTolakView")
            parentFragmentManager.setFragmentResult("entrypoint_navigation", result)
        }

        binding.btnFlow5.setOnClickListener {
            val result = bundleOf("fragment_class" to "HomeManagerView")
            parentFragmentManager.setFragmentResult("entrypoint_navigation", result)
        }
    }

    private fun setupNavigationListener() {
        parentFragmentManager.setFragmentResultListener("entrypoint_navigation", this) { _, bundle ->
            when (bundle.getString("fragment_class")) {
                "HomeDaftarRSVPView" -> navigateWithAnimation(HomeDaftarRSVPView.newInstance())
                "HomeInvitationNoRSVPView" -> navigateWithAnimation(HomeInvitationNoRSVPView.newInstance())
                "HomeAttendanceView" -> navigateWithAnimation(HomeAttendanceView.newInstance())
                "HomeInvitationTolakView" -> navigateWithAnimation(HomeInvitationTolakView.newInstance())
                "HomeManagerView" -> navigateWithAnimation(HomeManagerView.newInstance())
                "EventDetailRSVPView" -> navigateWithAnimation(EventDetailRSVPView())
                "EventListDaftarRSVPView" -> navigateWithAnimation(EventListDaftarRSVPView())
            }
        }
    }

    private fun navigateWithAnimation(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,  // enter
                R.anim.slide_out_left,  // exit
                R.anim.slide_in_left,   // popEnter
                R.anim.slide_out_right  // popExit
            )
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}