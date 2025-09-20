package com.edts.desklabv3

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.edts.components.bottom.navigation.BottomNavigation
import com.edts.components.bottom.navigation.BottomNavigationDelegate
import com.edts.components.bottom.navigation.BottomNavigationItem
import com.edts.components.header.HeaderDelegate
import com.edts.components.toast.Toast
import com.edts.desklabv3.core.EntryPointsView
import com.edts.desklabv3.core.withPushAnimation
import com.edts.desklabv3.databinding.ActivityMainBinding
import com.edts.desklabv3.features.event.ui.EventMenuFragment
import com.edts.desklabv3.features.event.ui.attendanceoffline.AssetQRCodeFragment
import com.edts.desklabv3.features.event.ui.eventdetail.EventDetailRSVPView
import com.edts.desklabv3.features.event.ui.eventdetail.EventDetailViewAttendance
import com.edts.desklabv3.features.event.ui.eventdetail.EventDetailViewNoRSVP
import com.edts.desklabv3.features.event.ui.eventdetail.EventDetailViewTolakUndangan
import com.edts.desklabv3.features.event.ui.eventlist.EventListAttendanceView
import com.edts.desklabv3.features.event.ui.eventlist.EventListDaftarRSVPView
import com.edts.desklabv3.features.event.ui.eventlist.EventListInvitationNoRSVPView
import com.edts.desklabv3.features.event.ui.eventlist.EventListInvitationTolakEndView
import com.edts.desklabv3.features.event.ui.eventlist.EventListInvitationTolakStartView
import com.edts.desklabv3.features.event.ui.invitation.EventInvitationFragmentNoRSVP
import com.edts.desklabv3.features.event.ui.invitation.EventInvitationFragmentTolakUndangan
import com.edts.desklabv3.features.event.ui.myevent.MyEventsFragmentAttendance
import com.edts.desklabv3.features.event.ui.myevent.MyEventsFragmentNoRSVP
import com.edts.desklabv3.features.event.ui.myevent.MyEventsFragmentRSVP
import com.edts.desklabv3.features.event.ui.rsvp.RSVPFormView
import com.edts.desklabv3.features.event.ui.success.SuccessAttendanceOfflineView
import com.edts.desklabv3.features.event.ui.success.SuccessAttendanceOnlineView
import com.edts.desklabv3.features.event.ui.success.SuccessDenyInvitationView
import com.edts.desklabv3.features.event.ui.success.SuccessRegistrationView
import com.edts.desklabv3.features.home.ui.HomeAttendanceView
import com.edts.desklabv3.features.home.ui.HomeDaftarRSVPView
import com.edts.desklabv3.features.home.ui.HomeInvitationNoRSVPView
import com.edts.desklabv3.features.home.ui.HomeInvitationTolakView
import com.edts.desklabv3.features.home.ui.HomeManagerView
import com.edts.desklabv3.features.home.ui.HomeMenuFragment
import com.edts.desklabv3.features.leave.ui.EmployeeLeaveDetailView
import com.edts.desklabv3.features.leave.ui.EmployeeLeaveHistoryView
import com.edts.desklabv3.features.leave.ui.laporantim.TeamReportActivityView
import com.edts.desklabv3.features.leave.ui.laporantim.TeamReportLeaveView
import com.edts.desklabv3.features.leave.ui.laporantim.TeamReportMenuFragment

class MainActivity : AppCompatActivity(), HeaderConfigurator {
    private lateinit var binding: ActivityMainBinding
    private var currentFlow: String? = null
    private var currentTeamReportTab: Int = 0
    private var isNavigatingBackFromDetail: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .withPushAnimation()
                .replace(R.id.fragment_container, EntryPointsView())
                .addToBackStack("EntryPoints")
                .commit()
        }

        supportFragmentManager.setFragmentResultListener("navigate_fragment", this) { requestKey, bundle ->
            val fragmentClassName = bundle.getString("fragment_class")
            val flowType = bundle.getString("flow_type", "")

            val fragment = when (fragmentClassName) {
                "HomeDaftarRSVPView" -> HomeDaftarRSVPView()
                "EventListDaftarRSVPView" -> EventListDaftarRSVPView()
                "EventDetailDaftarRSVPView" -> EventDetailRSVPView()
                "MyEventDaftarRSVPView" -> MyEventsFragmentRSVP()
                "HomeInvitationNoRSVPView" -> HomeInvitationNoRSVPView()
                "EventListInvitationNoRSVPView" -> EventListInvitationNoRSVPView()
                "EventDetailViewNoRSVP" -> EventDetailViewNoRSVP()
                "EventInvitationFragmentNoRSVP" -> EventInvitationFragmentNoRSVP()
                "MyEventsFragmentNoRSVP" -> MyEventsFragmentNoRSVP()
                "HomeAttendanceView" -> HomeAttendanceView()
                "SuccessAttendanceOfflineView" -> SuccessAttendanceOfflineView()
                "SuccessAttendanceOnlineView" -> SuccessAttendanceOnlineView()
                "EventListAttendanceView" -> EventListAttendanceView()
                "HomeInvitationTolakView" -> HomeInvitationTolakView()
                "EventListInvitationTolakStartView" -> EventListInvitationTolakStartView()
                "EventInvitationFragmentTolakUndangan" -> EventInvitationFragmentTolakUndangan()
                "EventDetailViewTolakUndangan" -> EventDetailViewTolakUndangan()
                "SuccessDenyInvitationView" -> SuccessDenyInvitationView()
                "EventListInvitationTolakEndView" -> EventListInvitationTolakEndView()
                "AssetQRCodeFragment" -> AssetQRCodeFragment()
                "RSVPFormView" -> RSVPFormView()
                "EmployeeLeaveDetailView" -> EmployeeLeaveDetailView()
                "EmployeeLeaveHistoryView" -> EmployeeLeaveHistoryView()
                "SuccessRegistrationView" -> SuccessRegistrationView().apply {
                    arguments = Bundle().apply {
                        putString("flow_type", flowType)
                    }
                }
                "EventDetailViewAttendance" -> EventDetailViewAttendance().apply {
                    arguments = Bundle().apply {
                        putBoolean("from_success", bundle.getBoolean("from_success", false))
                        putString("attendance_type", bundle.getString("attendance_type", ""))
                        putString("meeting_link", bundle.getString("meeting_link", ""))
                    }
                }
                "EventMenuFragment" -> EventMenuFragment().apply {
                    currentFlow = flowType
                    arguments = Bundle().apply {
                        putString("flow_type", flowType)
                        putInt("selected_tab", bundle.getInt("selected_tab", 0))
                    }
                }
                "HomeMenuFragment" -> HomeMenuFragment().apply {
                    currentFlow = flowType
                    arguments = Bundle().apply {
                        putString("flow_type", flowType)
                    }
                }
                "HomeManagerView" -> HomeManagerView()
                "TeamReportActivityView" -> TeamReportActivityView()
                "TeamReportMenuFragment" -> TeamReportMenuFragment()
                else -> EntryPointsView()
            }

            if (fragment is SuccessAttendanceOfflineView ||
                fragment is SuccessAttendanceOnlineView ||
                fragment is SuccessDenyInvitationView ||
                fragment is SuccessRegistrationView ||
                fragment is EntryPointsView) {
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            } else {
                loadFragmentWithUI(fragment, addToBackStack = true)
            }
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

            if (currentFragment is TeamReportActivityView || currentFragment is TeamReportLeaveView) {
                val backStackEntryCount = supportFragmentManager.backStackEntryCount
                if (backStackEntryCount > 0) {
                    val topEntry = supportFragmentManager.getBackStackEntryAt(backStackEntryCount - 1)
                    val previousFragmentName = if (backStackEntryCount > 1) {
                        supportFragmentManager.getBackStackEntryAt(backStackEntryCount - 2).name
                    } else null

                    // Check if we came from a detail view
                    if (topEntry.name == "EmployeeLeaveDetailView" || topEntry.name == "EmployeeLeaveHistoryView" ||
                        previousFragmentName == "EmployeeLeaveDetailView" || previousFragmentName == "EmployeeLeaveHistoryView") {
                        isNavigatingBackFromDetail = true
                    }
                }
            }

            currentFragment?.let {
                binding.root.post {
                    currentFragment?.let { configureUIForFragment(it) }
                }
            }
        }

        setupBottomNavigation()
    }

    override fun configureHeader(
        title: String?,
        subtitle: String?,
        showLeftButton: Boolean,
        showRightButton: Boolean,
        rightButtonIconRes: Int?,
        onLeftClick: (() -> Unit)?,
        onRightClick: (() -> Unit)?,
        isVisible: Boolean
    ) {
        val header = binding.cvHeaderTeamReportActivity
        val delay = if (isVisible) 0L else 100L

        header.postDelayed({
            val transitionSet = TransitionSet().apply {
            if (isVisible) {
                addTransition(Slide(Gravity.TOP).apply { duration = 125 })
            } else {
                addTransition(Fade(Fade.OUT).apply { duration = 150 })
            }
        }
            TransitionManager.beginDelayedTransition(header, transitionSet)
            header.visibility = if (isVisible) View.VISIBLE else View.GONE

            if (isVisible) {
                title?.let { header.sectionTitleText = it }
                subtitle?.let { header.sectionSubtitleText = it }

                header.showLeftButton = showLeftButton
                header.showRightButton = showRightButton
                rightButtonIconRes?.let { header.rightButtonSrc = it }

                header.delegate = object : HeaderDelegate {
                    override fun onLeftButtonClicked() {
                        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                        if (currentFragment is TeamReportActivityView || currentFragment is TeamReportMenuFragment) {
                            supportFragmentManager.popBackStack()
                        } else {
                            onLeftClick?.invoke()
                        }
                    }
                    override fun onRightButtonClicked() { onRightClick?.invoke() }
                }
            }
        }, delay)
    }

    private fun setupBottomNavigation() {
        binding.cvBottomNavigation.delegate = object : BottomNavigationDelegate {
            override fun onBottomNavigationItemClicked(
                item: BottomNavigationItem,
                position: Int,
                clickCount: Int
            ) {
                handleBottomNavigationClick(position, item, clickCount)
                binding.cvBottomNavigation.setActiveItem(position)
            }

            override fun onBottomNavigationItemStateChanged(
                item: BottomNavigationItem,
                newState: BottomNavigationItem.NavState,
                oldState: BottomNavigationItem.NavState
            ) {
                Log.d("BottomNav", "State changed for ${item.navText}: $oldState -> $newState")
            }
        }

        binding.cvBottomNavigation.setActiveItem(0)
    }

    fun BottomNavigation.setActiveItem(position: Int) {
        this.setActiveItem(position)
    }

    private fun handleBottomNavigationClick(position: Int, item: BottomNavigationItem, clickCount: Int) {
        when (position) {
            0 -> navigateToHome()
            1 -> navigateToEmployees()
            2 -> navigateToEventList()
            3 -> navigateToProfile()
        }
    }

    private fun navigateToEventList() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        // Try to detect flow from the current fragment first
        val flowType = when (currentFragment) {
            is EventListInvitationNoRSVPView,
            is HomeInvitationNoRSVPView,
            is MyEventsFragmentNoRSVP,
            is EventInvitationFragmentNoRSVP -> "InvitationNoRSVP"

            is EventListInvitationTolakStartView,
            is EventListInvitationTolakEndView,
            is HomeInvitationTolakView,
            is EventInvitationFragmentTolakUndangan -> "TolakUndangan"

            is EventListAttendanceView,
            is HomeAttendanceView,
            is MyEventsFragmentAttendance -> "Attendance"

            is EventListDaftarRSVPView,
            is HomeDaftarRSVPView,
            is MyEventsFragmentRSVP -> "RegisRSVP"

            else -> currentFlow ?: "RegisRSVP"
        }

        // Save the flow so we always know what the user is in
        currentFlow = flowType

        val eventMenuFragment = EventMenuFragment().apply {
            arguments = Bundle().apply {
                putString("flow_type", flowType)
                putInt("selected_tab", 0)
            }
        }
        loadFragmentWithUI(eventMenuFragment, addToBackStack = true)
    }

    private fun navigateToHome() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        val flowType = "RegisRSVP"

        currentFlow = flowType

        val isAlreadyHome = currentFragment is HomeMenuFragment &&
                currentFragment.arguments?.getString("flow_type") == flowType

        if (!isAlreadyHome) {
            val homeMenuFragment = HomeMenuFragment().apply {
                arguments = Bundle().apply {
                    putString("flow_type", flowType)
                }
            }
            loadFragmentWithUI(homeMenuFragment, addToBackStack = true)
        }
    }

    private fun loadFragmentWithUI(fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
            .withPushAnimation()
            .replace(R.id.fragment_container, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(fragment::class.java.simpleName)
            transaction.commit()
            supportFragmentManager.executePendingTransactions()
        } else {
            transaction.commitNow()
        }

        if (fragment is HomeMenuFragment) {
            val flowType = fragment.arguments?.getString("flow_type")
            if (!flowType.isNullOrEmpty()) {
                currentFlow = flowType
                Log.d("FLOW_TRACK", "currentFlow set to $currentFlow from HomeMenuFragment")
            }
        }

        binding.root.post {
            configureUIForFragment(fragment)
        }
    }

    private fun configureUIForFragment(fragment: Fragment) {
        if (!fragment.isAdded || fragment.isDetached || fragment.isRemoving) {
            Log.w("UI_CONFIG", "Fragment not ready for UI configuration: ${fragment::class.java.simpleName}")
            return
        }

        Log.d("UI_CONFIG", "Configuring UI for: ${fragment::class.java.simpleName}")

        when (fragment) {
            is TeamReportActivityView, is TeamReportLeaveView -> {
                configureBottomNavigation(showBadge = false, showBottomNavigation = false)
                configureHeader(
                    title = "Laporan Tim",
                    isVisible = true
                )
            }

            is HomeMenuFragment -> {
                val showBadge = currentFlow == "TolakUndangan"
                configureBottomNavigation(
                    showBadge = showBadge,
                    showBottomNavigation = true
                )
                configureHeader(isVisible = false)
                binding.cvBottomNavigation.setActiveItem(0)
            }

            is EventMenuFragment -> {
                val showBadge = currentFlow == "TolakUndangan"
                configureBottomNavigation(
                    showBadge = showBadge,
                    showBottomNavigation = true
                )
                configureHeader(
                    title = "Event",
                    showLeftButton = false,
                    isVisible = true
                )
            }

            is TeamReportMenuFragment -> {
                configureBottomNavigation(showBadge = false, showBottomNavigation = true)
                configureHeader(
                    title = "Laporan Tim",
                    showLeftButton = true,
                    isVisible = true
                )
            }

            else -> {
                configureBottomNavigation(showBadge = false, showBottomNavigation = false)
                configureHeader(isVisible = false)
            }
        }

    }

    private fun createEmptyFragment(): Fragment {
        return Fragment()
    }

    private fun navigateToEmployees() {
        Toast.info(this, "Navigating to Karyawan")
    }

    private fun navigateToProfile() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, EntryPointsView())
            .addToBackStack("EntryPoints")
            .commit()
    }

    private fun configureBottomNavigation(showBadge: Boolean, showBottomNavigation: Boolean) {
        val bottomNav = binding.cvBottomNavigation
        bottomNav.setItemBadge(2, showBadge)

        val transition = TransitionSet().apply {
            addTransition(Fade())
            duration = 100
        }

        val parent = bottomNav.parent as ViewGroup
        TransitionManager.beginDelayedTransition(parent, transition)

        bottomNav.visibility = if (showBottomNavigation) View.VISIBLE else View.GONE
    }

    fun switchToFragment(fragment: Fragment) {
        loadFragmentWithUI(fragment)
    }
}