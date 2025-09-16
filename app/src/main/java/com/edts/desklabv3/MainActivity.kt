package com.edts.desklabv3

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.edts.components.bottom.navigation.BottomNavigation
import com.edts.components.bottom.navigation.BottomNavigationDelegate
import com.edts.components.bottom.navigation.BottomNavigationItem
import com.edts.components.checkbox.CheckBox
import com.edts.components.radiobutton.RadioGroup
import com.edts.components.tab.Tab
import com.edts.components.tab.TabData
import com.edts.components.tab.TabItem
import com.edts.components.toast.Toast
import com.edts.desklabv3.core.EntryPointsView
import com.edts.desklabv3.core.util.setupWithViewPager2
import com.edts.desklabv3.databinding.ActivityMainBinding
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
import com.edts.desklabv3.features.event.ui.myevent.MyEventsFragmentNoRSVP
import com.edts.desklabv3.features.event.ui.myevent.MyEventsFragmentRSVP
import com.edts.desklabv3.features.event.ui.success.SuccessAttendanceOfflineView
import com.edts.desklabv3.features.event.ui.success.SuccessAttendanceOnlineView
import com.edts.desklabv3.features.event.ui.success.SuccessDenyInvitationView
import com.edts.desklabv3.features.event.ui.success.SuccessRegistrationView
import com.edts.desklabv3.features.home.ui.HomeAttendanceView
import com.edts.desklabv3.features.home.ui.HomeDaftarRSVPView
import com.edts.desklabv3.features.home.ui.HomeInvitationNoRSVPView
import com.edts.desklabv3.features.home.ui.HomeInvitationTolakView
import com.edts.desklabv3.features.home.ui.HomeManagerView
import com.edts.desklabv3.features.leave.ui.laporantim.TeamReportActivityView
import com.edts.desklabv3.features.leave.ui.laporantim.TeamReportLeaveView

//class MainActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var dynamicRadioGroup: RadioGroup
//    private lateinit var checkboxContainer: LinearLayout
//    private val checkboxes = mutableListOf<CheckBox>()
//    private var eventListPagerAdapter: TabFragmentAdapter? = null
//    private var teamReportPagerAdapter: TabFragmentAdapter? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        if (savedInstanceState == null) {
//            loadFragmentWithUI(EntryPointsView(), addToBackStack = false)
//
//            // Example usage - uncomment the fragment you want to test:
////             loadFragmentWithUI(ScanQRAttendanceView())
//            // loadFragmentWithUI(HomeInvitationNoRSVPView())
//            // loadFragmentWithUI(EventListDaftarRSVPView())
//            // loadFragmentWithUI(EventListInvitationNoRSVPView())
//        }
//
//        supportFragmentManager.setFragmentResultListener("navigate_fragment", this) { requestKey, bundle ->
//            val fragmentClassName = bundle.getString("fragment_class")
//            val flowType = bundle.getString("flow_type", "")
//            val fragment = when (fragmentClassName) {
//                "HomeDaftarRSVPView" -> HomeDaftarRSVPView()
//                "EventListDaftarRSVPView" -> EventListDaftarRSVPView()
//                "EventDetailDaftarRSVPView" -> EventDetailRSVPView()
//                "MyEventDaftarRSVPView" -> MyEventsFragmentRSVP()
//                "HomeInvitationNoRSVPView" -> HomeInvitationNoRSVPView()
//                "EventListInvitationNoRSVPView" -> EventListInvitationNoRSVPView()
//                "EventDetailViewNoRSVP" -> EventDetailViewNoRSVP()
//                "EventInvitationFragmentNoRSVP" -> EventInvitationFragmentNoRSVP()
//                "MyEventsFragmentNoRSVP" -> MyEventsFragmentNoRSVP()
//                "HomeAttendanceView" -> HomeAttendanceView()
//                "SuccessAttendanceOfflineView" -> SuccessAttendanceOfflineView()
//                "SuccessAttendanceOnlineView" -> SuccessAttendanceOnlineView()
//                "EventListAttendanceView" -> EventListAttendanceView()
//                "HomeInvitationTolakView" -> HomeInvitationTolakView()
//                "EventListInvitationTolakStartView" -> EventListInvitationTolakStartView()
//                "EventInvitationFragmentTolakUndangan" -> EventInvitationFragmentTolakUndangan()
//                "EventDetailViewTolakUndangan" -> EventDetailViewTolakUndangan()
//                "SuccessDenyInvitationView" -> SuccessDenyInvitationView()
//                "EventListInvitationTolakEndView" -> EventListInvitationTolakEndView()
//                "AssetQRCodeFragment" -> AssetQRCodeFragment()
//                "SuccessRegistrationView" -> SuccessRegistrationView().apply {
//                    arguments = Bundle().apply {
//                        putString("flow_type", flowType)
//                    }
//                }
//                "EventDetailViewAttendance" -> EventDetailViewAttendance().apply {
//                    arguments = Bundle().apply {
//                        putBoolean("from_success", bundle.getBoolean("from_success", false))
//                        putString("attendance_type", bundle.getString("attendance_type", ""))
//                        putString("meeting_link", bundle.getString("meeting_link", ""))
//                    }
//                }
//                "HomeManagerView" -> HomeManagerView()
//                "TeamReportActivityView" -> TeamReportActivityView()
//                else -> EntryPointsView()
//            }
//            loadFragmentWithUI(fragment, addToBackStack = true)
//        }
//
//        supportFragmentManager.addOnBackStackChangedListener {
//            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
//            currentFragment?.let {
//                configureUIForFragment(it)
//            }
//        }
//
//        setupBottomNavigation()
//    }
//
//    private fun setupBottomNavigation() {
//        binding.cvBottomNavigation.delegate = object : BottomNavigationDelegate {
//            override fun onBottomNavigationItemClicked(
//                item: BottomNavigationItem,
//                position: Int,
//                clickCount: Int
//            ) {
//                handleBottomNavigationClick(position, item, clickCount)
//                binding.cvBottomNavigation.setActiveItem(position)
//            }
//
//            override fun onBottomNavigationItemStateChanged(
//                item: BottomNavigationItem,
//                newState: BottomNavigationItem.NavState,
//                oldState: BottomNavigationItem.NavState
//            ) {
//                Log.d("BottomNav", "State changed for ${item.navText}: $oldState -> $newState")
//            }
//        }
//
//        binding.cvBottomNavigation.setActiveItem(0)
//    }
//
//    fun BottomNavigation.setActiveItem(position: Int) {
//        this.setActiveItem(position)
//    }
//
//    private fun handleBottomNavigationClick(position: Int, item: BottomNavigationItem, clickCount: Int) {
//        when (position) {
//            0 -> navigateToHome()
//            1 -> navigateToEmployees()
//            2 -> navigateToEventList()
//            3 -> navigateToProfile()
//        }
//    }
//
//    private fun navigateToEventList() {
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
//
//        val targetFragment = when (currentFragment) {
//            is EventListInvitationNoRSVPView -> EventListInvitationNoRSVPView()
//            is HomeInvitationTolakView,
//            is EventListInvitationTolakStartView -> EventListInvitationTolakStartView()
//            is HomeAttendanceView -> EventListAttendanceView()
//            is HomeInvitationNoRSVPView -> EventListInvitationNoRSVPView()
//            else -> EventListDaftarRSVPView()
//        }
//
//        if (currentFragment?.javaClass != targetFragment.javaClass) {
//            loadFragmentWithUI(targetFragment, addToBackStack = true)
//        }
//    }
//
//    private fun navigateToHome() {
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
//
//        val targetHomeFragment = when (currentFragment) {
//            is EventListInvitationNoRSVPView -> HomeInvitationNoRSVPView()
//            is MyEventsFragmentNoRSVP, is EventInvitationFragmentNoRSVP -> HomeInvitationNoRSVPView()
//            is EventListInvitationTolakStartView -> HomeInvitationTolakView()
//            is HomeInvitationTolakView -> EventListInvitationTolakStartView()
//            else -> HomeDaftarRSVPView()
//        }
//
//        if (currentFragment?.javaClass != targetHomeFragment.javaClass) {
//            loadFragmentWithUI(targetHomeFragment, addToBackStack = true)
//        }
//    }
//
//    private fun loadFragmentWithUI(fragment: Fragment, addToBackStack: Boolean = true) {
//        val transaction = supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, fragment)
//
//        if (addToBackStack) {
//            transaction.addToBackStack(fragment::class.java.simpleName)
//        }
//
//        transaction.commit()
//        configureUIForFragment(fragment)
//    }
//
//    private fun configureUIForFragment(fragment: Fragment) {
//        Log.d("UI_CONFIG", "Configuring UI for: ${fragment::class.java.simpleName}")
//
//        when (fragment) {
//            // Home fragments with no badge on Event tab
//            is HomeDaftarRSVPView, is HomeAttendanceView, is HomeManagerView -> {
//                configureBottomNavigation(showBadge = false, showBottomNavigation = true)
//                configureHeaders(showTeamReport = false, showEventList = false)
//                showFragmentContainer()
//                binding.cvBottomNavigation.setActiveItem(0)
//            }
//
//            // Home fragments with badge on Event tab
//            is HomeInvitationNoRSVPView, is HomeInvitationTolakView -> {
//                configureBottomNavigation(showBadge = true, showBottomNavigation = true)
//                configureHeaders(showTeamReport = false, showEventList = false)
//                showFragmentContainer()
//                binding.cvBottomNavigation.setActiveItem(0)
//            }
//
//            // Event list fragments with no badge on Undangan tab
//            is EventListInvitationTolakEndView -> {
//                configureBottomNavigation(showBadge = false, showBottomNavigation = true)
//                configureHeaders(showTeamReport = false, showEventList = true)
//                setupEventListViewPager(false, 2)
//                binding.cvBottomNavigation.setActiveItem(2)
//            }
//
//            is EventListDaftarRSVPView, is EventListAttendanceView -> {
//                configureBottomNavigation(showBadge = false, showBottomNavigation = true)
//                configureHeaders(showTeamReport = false, showEventList = true)
//                setupEventListViewPager(false, 0)
//                binding.cvBottomNavigation.setActiveItem(2)
//            }
//
//            is MyEventsFragmentRSVP, is MyEventsFragmentNoRSVP -> {
//                configureBottomNavigation(showBadge = false, showBottomNavigation = true)
//                configureHeaders(showTeamReport = false, showEventList = true)
//                setupEventListViewPager(false, 1)
//                binding.cvBottomNavigation.setActiveItem(2)
//            }
//
//            is EventInvitationFragmentNoRSVP, is EventInvitationFragmentTolakUndangan -> {
//                configureBottomNavigation(showBadge = true, showBottomNavigation = true)
//                configureHeaders(showTeamReport = false, showEventList = true)
//                setupEventListViewPager(true, 2)
//                binding.cvBottomNavigation.setActiveItem(2)
//            }
//
//            // Event list fragments with badge on Undangan tab
//            is EventListInvitationNoRSVPView -> {
//                Log.d("UI_CONFIG", "Handling EventListInvitationNoRSVPView with badges")
//                configureBottomNavigation(showBadge = true, showBottomNavigation = true)
//                configureHeaders(showTeamReport = false, showEventList = true)
//                setupEventListViewPager(true, 2)
//                binding.cvBottomNavigation.setActiveItem(2)
//            }
//
//            is EventListInvitationTolakStartView -> {
//                Log.d("UI_CONFIG", "Handling EventListInvitationNoRSVPView with badges")
//                configureBottomNavigation(showBadge = true, showBottomNavigation = true)
//                configureHeaders(showTeamReport = false, showEventList = true)
//                setupEventListViewPager(showUndanganBadge = true, selectedPosition = 2)
//                binding.cvBottomNavigation.setActiveItem(2)
//            }
//
//            // Team report fragments
//            is TeamReportActivityView, is TeamReportLeaveView -> {
//                val selectedPosition = if (fragment is TeamReportLeaveView) 1 else 0
//                configureBottomNavigation(showBadge = false, showBottomNavigation = false)
//                configureHeaders(showTeamReport = true, showEventList = false)
//                setupTeamReportViewPager(selectedPosition)
//            }
//
//            // Other fragments (Success views, ScanQR, etc.)
//            else -> {
//                configureBottomNavigation(showBadge = false, showBottomNavigation = false)
//                configureHeaders(showTeamReport = false, showEventList = false)
//                showFragmentContainer()
//            }
//        }
//    }
//
////    private fun configureUIForFragment(fragment: Fragment) {
////        Log.d("UI_CONFIG", "Configuring UI for: ${fragment::class.java.simpleName}")
////        when (fragment) {
////            // Home fragments with no badge on Event tab
////            is HomeDaftarRSVPView, is HomeAttendanceView, is HomeManagerView -> {
////                configureBottomNavigation(showBadge = false, showBottomNavigation = true)
////                configureHeaders(showTeamReport = false, showEventList = false)
////                showFragmentContainer()
////                binding.cvBottomNavigation.setActiveItem(0)
////            }
////
////            // Home fragments with badge on Event tab
////            is HomeInvitationNoRSVPView, is HomeInvitationTolakView -> {
////                configureBottomNavigation(showBadge = true, showBottomNavigation = true)
////                configureHeaders(showTeamReport = false, showEventList = false)
////                showFragmentContainer()
////                binding.cvBottomNavigation.setActiveItem(0)
////            }
////
//////            // Event list fragments with no badge on Undangan tab
//////            is EventListInvitationTolakEndView -> {
//////                configureBottomNavigation(showBadge = false, showBottomNavigation = true)
//////                configureHeaders(showTeamReport = false, showEventList = true)
//////                configureEventListTabs(showUndanganBadge = false)
//////                binding.cvBottomNavigation.setActiveItem(2)
//////            }
//////
//////            is EventListDaftarRSVPView, is EventListAttendanceView -> {
//////                configureBottomNavigation(showBadge = false, showBottomNavigation = true)
//////                configureHeaders(showTeamReport = false, showEventList = true)
//////                configureEventListTabs(showUndanganBadge = false, selectedPosition = 0) // Daftar Event
//////                binding.cvBottomNavigation.setActiveItem(2)
//////            }
//////
//////            is MyEventsFragmentRSVP, is MyEventsFragmentNoRSVP -> {
//////                configureBottomNavigation(showBadge = false, showBottomNavigation = true)
//////                configureHeaders(showTeamReport = false, showEventList = true)
//////                configureEventListTabs(showUndanganBadge = false, selectedPosition = 1) // ✅ Event Saya
//////                binding.cvBottomNavigation.setActiveItem(2)
//////            }
//////
//////            is EventInvitationFragmentNoRSVP, is EventInvitationFragmentTolakUndangan -> {
//////                configureBottomNavigation(showBadge = true, showBottomNavigation = true)
//////                configureHeaders(showTeamReport = false, showEventList = true)
//////                configureEventListTabs(showUndanganBadge = true, selectedPosition = 2)
//////                binding.cvBottomNavigation.setActiveItem(2)
//////            }
//////
//////
//////            // Event list fragments with badge on Undangan tab
//////            is EventListInvitationNoRSVPView, is EventListInvitationTolakStartView -> {
//////                Log.d("UI_CONFIG", "Handling EventListInvitationNoRSVPView with badges")
//////                configureBottomNavigation(showBadge = true, showBottomNavigation = true)
//////                configureHeaders(showTeamReport = false, showEventList = true)
//////                configureEventListTabs(showUndanganBadge = true)
//////                binding.cvBottomNavigation.setActiveItem(2)
//////            }
//////
//////            // Team report fragments
//////            is TeamReportActivityView -> {
//////                configureBottomNavigation(showBadge = false, showBottomNavigation = false)
//////                configureHeaders(showTeamReport = true, showEventList = false)
//////                configureTeamReportTabs(selectedPosition = 0)
//////            }
//////
//////            is TeamReportLeaveView -> {
//////                configureBottomNavigation(showBadge = false, showBottomNavigation = false)
//////                configureHeaders(showTeamReport = true, showEventList = false)
//////                configureTeamReportTabs(selectedPosition = 1)
//////            }
////
////            // Event list fragments - USE VIEWPAGER2
////            is EventListDaftarRSVPView, is EventListAttendanceView,
////            is EventListInvitationNoRSVPView, is EventListInvitationTolakStartView,
////            is MyEventsFragmentRSVP, is MyEventsFragmentNoRSVP,
////            is EventInvitationFragmentNoRSVP, is EventInvitationFragmentTolakUndangan -> {
////
////                val showUndanganBadge = fragment is EventListInvitationNoRSVPView ||
////                        fragment is EventListInvitationTolakStartView ||
////                        fragment is EventInvitationFragmentNoRSVP ||
////                        fragment is EventInvitationFragmentTolakUndangan
////
////                val selectedPosition = when (fragment) {
////                    is MyEventsFragmentRSVP, is MyEventsFragmentNoRSVP -> 1
////                    is EventInvitationFragmentNoRSVP, is EventInvitationFragmentTolakUndangan -> 2
////                    else -> 0
////                }
////
////                configureBottomNavigation(showBadge = showUndanganBadge, showBottomNavigation = true)
////                configureHeaders(showTeamReport = false, showEventList = true)
////                setupEventListViewPager(showUndanganBadge, selectedPosition) // NEW METHOD
////                binding.cvBottomNavigation.setActiveItem(2)
////            }
////
////            is EventListInvitationTolakEndView -> {
////                configureBottomNavigation(showBadge = false, showBottomNavigation = true)
////                configureHeaders(showTeamReport = false, showEventList = true)
////                setupEventListViewPager(showUndanganBadge = false, selectedPosition = 2)
////                binding.cvBottomNavigation.setActiveItem(2)
////            }
////
////            is TeamReportActivityView, is TeamReportLeaveView -> {
////                val selectedPosition = if (fragment is TeamReportLeaveView) 1 else 0
////                configureBottomNavigation(showBadge = false, showBottomNavigation = false)
////                configureHeaders(showTeamReport = true, showEventList = false)
////                setupTeamReportViewPager(selectedPosition)
////            }
////
////            // Other fragments (Success views, ScanQR, etc.)
////            else -> {
////                configureBottomNavigation(showBadge = false, showBottomNavigation = false)
////                configureHeaders(showTeamReport = false, showEventList = false)
////                showFragmentContainer()
////            }
////        }
////    }
//
//    private fun navigateToEmployees() {
//        Toast.info(this, "Navigating to Karyawan")
//    }
//
//    private fun navigateToProfile() {
//        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, EntryPointsView())
//            .commit()
//
//        binding.cvBottomNavigation.setActiveItem(3)
//    }
//
//    private fun configureHeaders(showTeamReport: Boolean, showEventList: Boolean) {
//        binding.HeaderWrapperTeamReportActivity.visibility = if (showTeamReport) View.VISIBLE else View.GONE
//        binding.headerWrapperEventListDaftarRSVP.visibility = if (showEventList) View.VISIBLE else View.GONE
//    }
//
//    private fun configureBottomNavigation(showBadge: Boolean, showBottomNavigation: Boolean) {
//        Log.d("UI_CONFIG", "Configuring bottom nav: showBadge=$showBadge, showBottomNav=$showBottomNavigation")
//        binding.cvBottomNavigation.visibility = if (showBottomNavigation) View.VISIBLE else View.GONE
//        binding.cvBottomNavigation.apply {
//            setItemBadge(2, showBadge)
//        }
//    }
//
//    private fun showFragmentContainer() {
//        binding.viewPager.visibility = View.GONE
//        binding.fragmentContainer.visibility = View.VISIBLE
//    }
//
//    private fun showViewPager() {
//        binding.fragmentContainer.visibility = View.GONE
//        binding.viewPager.visibility = View.VISIBLE
//    }
//
//    private fun setupEventListViewPager(showUndanganBadge: Boolean, selectedPosition: Int = 0) {
//        showViewPager()
//
//        if (eventListPagerAdapter == null) {
//            val fragments = listOf(
//                EventListDaftarRSVPView(),
//                MyEventsFragmentRSVP(),
//                EventInvitationFragmentNoRSVP()
//            )
//            eventListPagerAdapter = TabFragmentAdapter(this, fragments)
//        }
//
//        // 🔑 Always delay adapter assignment to avoid conflict with FragmentManager transactions
//        binding.viewPager.post {
//            // only set adapter if it’s different
//            if (binding.viewPager.adapter != eventListPagerAdapter) {
//                binding.viewPager.adapter = eventListPagerAdapter
//                binding.cvTabEventListDaftarRSVP.setupWithViewPager2(binding.viewPager)
//            }
//
//            binding.viewPager.setCurrentItem(selectedPosition, false)
//            configureEventListTabs(showUndanganBadge, selectedPosition)
//        }
//    }
//
//
//    private fun setupTeamReportViewPager(selectedPosition: Int = 0) {
//        showViewPager()
//
//        if (teamReportPagerAdapter == null) {
//            val fragments = listOf(
//                TeamReportActivityView(),
//                TeamReportLeaveView()
//            )
//            teamReportPagerAdapter = TabFragmentAdapter(this, fragments)
//        }
//
//        binding.viewPager.post {
//            if (binding.viewPager.adapter != teamReportPagerAdapter) {
//                binding.viewPager.adapter = teamReportPagerAdapter
//                binding.cvTabTeamReportActivity.setupWithViewPager2(binding.viewPager)
//            }
//
//            binding.viewPager.setCurrentItem(selectedPosition, false)
//            configureTeamReportTabs(selectedPosition)
//        }
//    }
//
//
//    private fun configureTeamReportTabs(selectedPosition: Int = 0) {
//        Log.d("UI_CONFIG", "Configuring team report tabs: selectedPosition=$selectedPosition")
//
//        binding.cvHeaderTeamReportActivity.delegate = object : com.edts.components.header.HeaderDelegate {
//            override fun onLeftButtonClicked() {
//                loadFragmentWithUI(HomeManagerView(), addToBackStack = true)
//            }
//
//            override fun onRightButtonClicked() {
//                // TODO: Implement if needed
//            }
//        }
//
//        val tabDataList = mutableListOf<TabData>()
//
//        tabDataList.add(TabData(
//            text = "Aktivitas",
//            badgeText = "",
//            showBadge = false,
//            state = if (selectedPosition == 0) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
//        ))
//
//        tabDataList.add(TabData(
//            text = "Cuti",
//            badgeText = "",
//            showBadge = false,
//            state = if (selectedPosition == 1) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
//        ))
//
//        binding.cvTabTeamReportActivity.setTabs(tabDataList, selectedPosition)
//    }
//
////    private fun configureEventListTabs(showUndanganBadge: Boolean, selectedPosition: Int = 0) {
////        Log.d("UI_CONFIG", "Configuring tabs: showUndanganBadge=$showUndanganBadge, selectedPosition=$selectedPosition")
////
////        val tabDataList = mutableListOf<TabData>()
////
////        tabDataList.add(TabData(
////            text = "Daftar Event",
////            badgeText = null,
////            showBadge = false,
////            state = if (selectedPosition == 0) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
////        ))
////
////        tabDataList.add(TabData(
////            text = "Event Saya",
////            badgeText = null,
////            showBadge = false,
////            state = if (selectedPosition == 1) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
////        ))
////
////        tabDataList.add(TabData(
////            text = "Undangan",
////            badgeText = if (showUndanganBadge) "1" else null,
////            showBadge = showUndanganBadge,
////            state = if (selectedPosition == 2) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
////        ))
////
////        binding.cvTabEventListDaftarRSVP.setupWithViewPager2(binding.viewPager)
////    }
//
//    private fun configureEventListTabs(showUndanganBadge: Boolean, selectedPosition: Int = 0) {
//        Log.d("UI_CONFIG", "Configuring tabs: showUndanganBadge=$showUndanganBadge, selectedPosition=$selectedPosition")
//
//        val tabDataList = mutableListOf<TabData>()
//
//        tabDataList.add(TabData(
//            text = "Daftar Event",
//            badgeText = null,
//            showBadge = false,
//            state = if (selectedPosition == 0) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
//        ))
//
//        tabDataList.add(TabData(
//            text = "Event Saya",
//            badgeText = null,
//            showBadge = false,
//            state = if (selectedPosition == 1) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
//        ))
//
//        tabDataList.add(
//            TabData(
//            text = "Undangan",
//            badgeText = if (showUndanganBadge) "1" else null,
//            showBadge = showUndanganBadge,
//            state = if (selectedPosition == 2) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
//        ))
//
//        binding.cvTabEventListDaftarRSVP.apply {
//            setTabs(tabDataList, selectedPosition)
//            setOnTabClickListener(object : Tab.OnTabClickListener {
//                override fun onTabClick(position: Int, tabText: String) {
//                    Log.d("UI_CONFIG", "Tab clicked: position=$position, text=$tabText")
//                    when (position) {
//                        0 -> {
//                            val result = bundleOf("fragment_class" to "EventListDaftarRSVPView")
//                            supportFragmentManager.setFragmentResult("navigate_fragment", result)
//                        }
//                        1 -> {
//                            val result = bundleOf("fragment_class" to "MyEventDaftarRSVPView")
//                            supportFragmentManager.setFragmentResult("navigate_fragment", result)
//                        }
//                        2 -> {
//                            val result = bundleOf("fragment_class" to "EventInvitationFragmentNoRSVP")
//                            supportFragmentManager.setFragmentResult("navigate_fragment", result)
//                        }
//                    }
//                }
//            })
//        }
//    }
//
//
//    fun switchToFragment(fragment: Fragment) {
//        loadFragmentWithUI(fragment)
//    }
//
//    override fun onDestroy() {
//        eventListPagerAdapter= null
//        teamReportPagerAdapter = null
//        super.onDestroy()
//    }
//}

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dynamicRadioGroup: RadioGroup
    private lateinit var checkboxContainer: LinearLayout
    private val checkboxes = mutableListOf<CheckBox>()

    // Separate adapters for each flow
    private var flow1PagerAdapter: TabFragmentAdapter? = null  // HomeDaftarRSVPView flow
    private var flow2PagerAdapter: TabFragmentAdapter? = null  // HomeAttendanceView flow
    private var flow3PagerAdapter: TabFragmentAdapter? = null  // HomeInvitationNoRSVPView flow
    private var flow4PagerAdapter: TabFragmentAdapter? = null  // HomeInvitationTolakView flow
    private var teamReportPagerAdapter: TabFragmentAdapter? = null
    private var currentFlow: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            loadFragmentWithUI(EntryPointsView(), addToBackStack = false)
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
                "HomeManagerView" -> HomeManagerView()
                "TeamReportActivityView" -> TeamReportActivityView()
                else -> EntryPointsView()
            }
            loadFragmentWithUI(fragment, addToBackStack = true)
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            currentFragment?.let {
                configureUIForFragment(it)
            }
        }

        setupBottomNavigation()
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

        val targetFragment = when (currentFragment) {
            is EventListInvitationNoRSVPView -> EventListInvitationNoRSVPView()
            is HomeInvitationTolakView,
            is EventListInvitationTolakStartView -> EventListInvitationTolakStartView()
            is HomeAttendanceView -> EventListAttendanceView()
            is HomeInvitationNoRSVPView -> EventListInvitationNoRSVPView()
            else -> EventListDaftarRSVPView()
        }

        if (currentFragment?.javaClass != targetFragment.javaClass) {
            loadFragmentWithUI(targetFragment, addToBackStack = true)
        }
    }

    private fun navigateToHome() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        val targetHomeFragment = when (currentFragment) {
            is EventListInvitationNoRSVPView -> HomeInvitationNoRSVPView()
            is MyEventsFragmentNoRSVP, is EventInvitationFragmentNoRSVP -> HomeInvitationNoRSVPView()
            is EventListInvitationTolakStartView -> HomeInvitationTolakView()
            is HomeInvitationTolakView -> EventListInvitationTolakStartView()
            else -> HomeDaftarRSVPView()
        }

        if (currentFragment?.javaClass != targetHomeFragment.javaClass) {
            loadFragmentWithUI(targetHomeFragment, addToBackStack = true)
        }
    }

    private fun loadFragmentWithUI(fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(fragment::class.java.simpleName)
            transaction.commit()
            supportFragmentManager.executePendingTransactions()
        } else {
            transaction.commitNow()
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
            // Flow 1: HomeDaftarRSVPView flow
            is HomeDaftarRSVPView -> {
                configureBottomNavigation(showBadge = false, showBottomNavigation = true)
                configureHeaders(showTeamReport = false, showEventList = false)
                showFragmentContainer()
                binding.cvBottomNavigation.setActiveItem(0)
            }

            is EventListDaftarRSVPView -> {
                configureBottomNavigation(showBadge = false, showBottomNavigation = true)
                configureHeaders(showTeamReport = false, showEventList = true)
                setupFlow1ViewPager(selectedPosition = 0) // Daftar Event tab
                binding.cvBottomNavigation.setActiveItem(2)
            }

            is MyEventsFragmentRSVP -> {
                configureBottomNavigation(showBadge = false, showBottomNavigation = true)
                configureHeaders(showTeamReport = false, showEventList = true)
                setupFlow1ViewPager(selectedPosition = 1) // Event Saya tab
                binding.cvBottomNavigation.setActiveItem(2)
            }

            // Flow 2: HomeAttendanceView flow
            is HomeAttendanceView -> {
                configureBottomNavigation(showBadge = false, showBottomNavigation = true)
                configureHeaders(showTeamReport = false, showEventList = false)
                showFragmentContainer()
                binding.cvBottomNavigation.setActiveItem(0)
            }

            is EventListAttendanceView -> {
                configureBottomNavigation(showBadge = false, showBottomNavigation = true)
                configureHeaders(showTeamReport = false, showEventList = true)
                setupFlow2ViewPager(selectedPosition = 0) // Daftar Event tab
                binding.cvBottomNavigation.setActiveItem(2)
            }

            // Flow 3: HomeInvitationNoRSVPView flow
            is HomeInvitationNoRSVPView -> {
                configureBottomNavigation(showBadge = true, showBottomNavigation = true)
                configureHeaders(showTeamReport = false, showEventList = false)
                showFragmentContainer()
                binding.cvBottomNavigation.setActiveItem(0)
            }

            is EventListInvitationNoRSVPView -> {
                configureBottomNavigation(showBadge = true, showBottomNavigation = true)
                configureHeaders(showTeamReport = false, showEventList = true)
                setupFlow3ViewPager(selectedPosition = 0) // Daftar Event tab (but shows EventListInvitationNoRSVPView)
                binding.cvBottomNavigation.setActiveItem(2)
            }

            is MyEventsFragmentNoRSVP -> {
                configureBottomNavigation(showBadge = true, showBottomNavigation = true)
                configureHeaders(showTeamReport = false, showEventList = true)
                setupFlow3ViewPager(selectedPosition = 1) // Event Saya tab
                binding.cvBottomNavigation.setActiveItem(2)
            }

            is EventInvitationFragmentNoRSVP -> {
                configureBottomNavigation(showBadge = true, showBottomNavigation = true)
                configureHeaders(showTeamReport = false, showEventList = true)
                setupFlow3ViewPager(selectedPosition = 2) // Undangan tab
                binding.cvBottomNavigation.setActiveItem(2)
            }

            // Flow 4: HomeInvitationTolakView flow
            is HomeInvitationTolakView -> {
                configureBottomNavigation(showBadge = true, showBottomNavigation = true)
                configureHeaders(showTeamReport = false, showEventList = false)
                showFragmentContainer()
                binding.cvBottomNavigation.setActiveItem(0)
            }

            is EventListInvitationTolakStartView -> {
                configureBottomNavigation(showBadge = true, showBottomNavigation = true)
                configureHeaders(showTeamReport = false, showEventList = true)
                setupFlow4ViewPager(selectedPosition = 0) // Daftar Event tab
                binding.cvBottomNavigation.setActiveItem(2)
            }

            is EventInvitationFragmentTolakUndangan -> {
                configureBottomNavigation(showBadge = true, showBottomNavigation = true)
                configureHeaders(showTeamReport = false, showEventList = true)
                setupFlow4ViewPager(selectedPosition = 2) // Undangan tab
                binding.cvBottomNavigation.setActiveItem(2)
            }

            // Special case for Flow 4 end
            is EventListInvitationTolakEndView -> {
                configureBottomNavigation(showBadge = false, showBottomNavigation = true)
                configureHeaders(showTeamReport = false, showEventList = true)
                setupFlow4ViewPager(selectedPosition = 2) // Show as Undangan tab but no badge
                binding.cvBottomNavigation.setActiveItem(2)
            }

            // Manager and Team Report
            is HomeManagerView -> {
                configureBottomNavigation(showBadge = false, showBottomNavigation = true)
                configureHeaders(showTeamReport = false, showEventList = false)
                showFragmentContainer()
                binding.cvBottomNavigation.setActiveItem(0)
            }

            is TeamReportActivityView, is TeamReportLeaveView -> {
                val selectedPosition = if (fragment is TeamReportLeaveView) 1 else 0
                configureBottomNavigation(showBadge = false, showBottomNavigation = false)
                configureHeaders(showTeamReport = true, showEventList = false)
                setupTeamReportViewPager(selectedPosition)
            }

            // Other fragments (Success views, ScanQR, etc.)
            else -> {
                configureBottomNavigation(showBadge = false, showBottomNavigation = false)
                configureHeaders(showTeamReport = false, showEventList = false)
                showFragmentContainer()
            }
        }
    }

    // Create an empty fragment for disabled tabs
    private fun createEmptyFragment(): Fragment {
        return Fragment() // This will show an empty view
    }

    // Flow 1: HomeDaftarRSVPView → EventListDaftarRSVPView + MyEventsFragmentRSVP + Empty Undangan
    private fun setupFlow1ViewPager(selectedPosition: Int = 0) {
        showViewPager()

        // 🔑 CONFIGURE TABS FIRST to prevent glitch
        binding.viewPager.visibility = View.GONE
        configureFlow1Tabs(selectedPosition)

        if (flow1PagerAdapter == null) {
            val fragments = listOf(
                EventListDaftarRSVPView(),    // Tab 0: Daftar Event
                MyEventsFragmentRSVP(),       // Tab 1: Event Saya
                createEmptyFragment()         // Tab 2: Undangan (empty)
            )
            flow1PagerAdapter = TabFragmentAdapter(this, fragments)
        }

        binding.viewPager.post {
            if (binding.viewPager.adapter != flow1PagerAdapter) {
                binding.viewPager.adapter = null
                binding.viewPager.adapter = flow1PagerAdapter
                binding.cvTabEventListDaftarRSVP.setupWithViewPager2(binding.viewPager)
            }
            binding.viewPager.setCurrentItem(selectedPosition, false)
            binding.viewPager.visibility = View.VISIBLE
        }
    }

    // Flow 2: HomeAttendanceView → EventListAttendanceView + Empty Event Saya + Empty Undangan
    private fun setupFlow2ViewPager(selectedPosition: Int = 0) {
        showViewPager()

        // 🔑 CONFIGURE TABS FIRST to prevent glitch
        binding.viewPager.visibility = View.GONE
        configureFlow2Tabs(selectedPosition)

        if (flow2PagerAdapter == null) {
            val fragments = listOf(
                EventListAttendanceView(),    // Tab 0: Daftar Event
                createEmptyFragment(),        // Tab 1: Event Saya (empty)
                createEmptyFragment()         // Tab 2: Undangan (empty)
            )
            flow2PagerAdapter = TabFragmentAdapter(this, fragments)
        }

        binding.viewPager.post {
            if (binding.viewPager.adapter != flow2PagerAdapter) {
                binding.viewPager.adapter = null
                binding.viewPager.adapter = flow2PagerAdapter
                binding.cvTabEventListDaftarRSVP.setupWithViewPager2(binding.viewPager)
            }
            binding.viewPager.setCurrentItem(selectedPosition, false)
            binding.viewPager.visibility = View.VISIBLE
        }
    }

    // Flow 3: HomeInvitationNoRSVPView → EventListInvitationNoRSVPView + MyEventsFragmentNoRSVP + EventInvitationFragmentNoRSVP
    private fun setupFlow3ViewPager(selectedPosition: Int = 0) {
        showViewPager()

        // 🔑 CONFIGURE TABS FIRST to prevent glitch
        binding.viewPager.visibility = View.GONE
        configureFlow3Tabs(selectedPosition)

        if (flow3PagerAdapter == null) {
            val fragments = listOf(
                EventListInvitationNoRSVPView(),  // Tab 0: Daftar Event
                MyEventsFragmentNoRSVP(),         // Tab 1: Event Saya
                EventInvitationFragmentNoRSVP()   // Tab 2: Undangan
            )
            flow3PagerAdapter = TabFragmentAdapter(this, fragments)
        }

        binding.viewPager.post {
            if (binding.viewPager.adapter != flow3PagerAdapter) {
                binding.viewPager.adapter = null
                binding.viewPager.adapter = flow3PagerAdapter
                binding.cvTabEventListDaftarRSVP.setupWithViewPager2(binding.viewPager)
            }
            binding.viewPager.setCurrentItem(selectedPosition, false)
            binding.viewPager.visibility = View.VISIBLE
        }
    }

    // Flow 4: HomeInvitationTolakView → EventListInvitationTolakStartView + Empty Event Saya + EventInvitationFragmentTolakUndangan
    private fun setupFlow4ViewPager(selectedPosition: Int = 0) {
        showViewPager()

        // 🔑 CONFIGURE TABS FIRST to prevent glitch
        binding.viewPager.visibility = View.GONE
        configureFlow4Tabs(selectedPosition)

        if (flow4PagerAdapter == null) {
            val fragments = listOf(
                EventListInvitationTolakStartView(),    // Tab 0: Daftar Event
                createEmptyFragment(),                   // Tab 1: Event Saya (empty)
                EventInvitationFragmentTolakUndangan()  // Tab 2: Undangan
            )
            flow4PagerAdapter = TabFragmentAdapter(this, fragments)
        }

        binding.viewPager.post {
            if (binding.viewPager.adapter != flow4PagerAdapter) {
                binding.viewPager.adapter = null
                binding.viewPager.adapter = flow4PagerAdapter
                binding.cvTabEventListDaftarRSVP.setupWithViewPager2(binding.viewPager)
            }
            binding.viewPager.setCurrentItem(selectedPosition, false)
            binding.viewPager.visibility = View.VISIBLE
        }
    }

    private fun setupTeamReportViewPager(selectedPosition: Int = 0) {
        showViewPager()

        // 🔑 CONFIGURE TABS FIRST to prevent glitch
        binding.viewPager.visibility = View.GONE
        configureTeamReportTabs(selectedPosition)

        if (teamReportPagerAdapter == null) {
            val fragments = listOf(
                TeamReportActivityView(),
                TeamReportLeaveView()
            )
            teamReportPagerAdapter = TabFragmentAdapter(this, fragments)
        }

        binding.viewPager.post {
            if (binding.viewPager.adapter != teamReportPagerAdapter) {
                binding.viewPager.adapter = null
                binding.viewPager.adapter = teamReportPagerAdapter
                binding.cvTabTeamReportActivity.setupWithViewPager2(binding.viewPager)
            }
            binding.viewPager.setCurrentItem(selectedPosition, false)
            binding.viewPager.visibility = View.VISIBLE
        }
    }

    // Tab configurations for each flow
    private fun configureFlow1Tabs(selectedPosition: Int = 0) {
        val tabDataList = mutableListOf<TabData>()

        tabDataList.add(TabData(
            text = "Daftar Event",
            badgeText = null,
            showBadge = false,
            state = if (selectedPosition == 0) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
        ))

        tabDataList.add(TabData(
            text = "Event Saya",
            badgeText = null,
            showBadge = false,
            state = if (selectedPosition == 1) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
        ))

        tabDataList.add(TabData(
            text = "Undangan",
            badgeText = null,
            showBadge = false,
            state = if (selectedPosition == 2) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
        ))

        binding.cvTabEventListDaftarRSVP.setTabs(tabDataList, selectedPosition)
    }

    private fun configureFlow2Tabs(selectedPosition: Int = 0) {
        val tabDataList = mutableListOf<TabData>()

        tabDataList.add(TabData(
            text = "Daftar Event",
            badgeText = null,
            showBadge = false,
            state = if (selectedPosition == 0) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
        ))

        tabDataList.add(TabData(
            text = "Event Saya",
            badgeText = null,
            showBadge = false,
            state = if (selectedPosition == 1) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
        ))

        tabDataList.add(TabData(
            text = "Undangan",
            badgeText = null,
            showBadge = false,
            state = if (selectedPosition == 2) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
        ))

        binding.cvTabEventListDaftarRSVP.setTabs(tabDataList, selectedPosition)
    }

    private fun configureFlow3Tabs(selectedPosition: Int = 0) {
        val tabDataList = mutableListOf<TabData>()

        tabDataList.add(TabData(
            text = "Daftar Event",
            badgeText = null,
            showBadge = false,
            state = if (selectedPosition == 0) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
        ))

        tabDataList.add(TabData(
            text = "Event Saya",
            badgeText = null,
            showBadge = false,
            state = if (selectedPosition == 1) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
        ))

        tabDataList.add(TabData(
            text = "Undangan",
            badgeText = "1", // Always show badge for Flow 3
            showBadge = true,
            state = if (selectedPosition == 2) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
        ))

        binding.cvTabEventListDaftarRSVP.setTabs(tabDataList, selectedPosition)
    }

    private fun configureFlow4Tabs(selectedPosition: Int = 0) {
        val tabDataList = mutableListOf<TabData>()

        tabDataList.add(TabData(
            text = "Daftar Event",
            badgeText = null,
            showBadge = false,
            state = if (selectedPosition == 0) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
        ))

        tabDataList.add(TabData(
            text = "Event Saya",
            badgeText = null,
            showBadge = false,
            state = if (selectedPosition == 1) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
        ))

        tabDataList.add(TabData(
            text = "Undangan",
            badgeText = if (supportFragmentManager.findFragmentById(R.id.fragment_container) is EventListInvitationTolakEndView) null else "1",
            showBadge = supportFragmentManager.findFragmentById(R.id.fragment_container) !is EventListInvitationTolakEndView,
            state = if (selectedPosition == 2) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
        ))

        binding.cvTabEventListDaftarRSVP.setTabs(tabDataList, selectedPosition)
    }

    private fun configureTeamReportTabs(selectedPosition: Int = 0) {
        Log.d("UI_CONFIG", "Configuring team report tabs: selectedPosition=$selectedPosition")

        binding.cvHeaderTeamReportActivity.delegate = object : com.edts.components.header.HeaderDelegate {
            override fun onLeftButtonClicked() {
                loadFragmentWithUI(HomeManagerView(), addToBackStack = true)
            }

            override fun onRightButtonClicked() {
                // TODO: Implement if needed
            }
        }

        val tabDataList = mutableListOf<TabData>()

        tabDataList.add(TabData(
            text = "Aktivitas",
            badgeText = "",
            showBadge = false,
            state = if (selectedPosition == 0) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
        ))

        tabDataList.add(TabData(
            text = "Cuti",
            badgeText = "",
            showBadge = false,
            state = if (selectedPosition == 1) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
        ))

        binding.cvTabTeamReportActivity.setTabs(tabDataList, selectedPosition)
    }

    private fun navigateToEmployees() {
        Toast.info(this, "Navigating to Karyawan")
    }

    private fun navigateToProfile() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, EntryPointsView())
            .commit()

        binding.cvBottomNavigation.setActiveItem(3)
    }

    private fun configureHeaders(showTeamReport: Boolean, showEventList: Boolean) {
        binding.HeaderWrapperTeamReportActivity.visibility = if (showTeamReport) View.VISIBLE else View.GONE
        binding.headerWrapperEventListDaftarRSVP.visibility = if (showEventList) View.VISIBLE else View.GONE
    }

    private fun configureBottomNavigation(showBadge: Boolean, showBottomNavigation: Boolean) {
        Log.d("UI_CONFIG", "Configuring bottom nav: showBadge=$showBadge, showBottomNav=$showBottomNavigation")
        binding.cvBottomNavigation.visibility = if (showBottomNavigation) View.VISIBLE else View.GONE
        binding.cvBottomNavigation.apply {
            setItemBadge(2, showBadge)
        }
    }

    private fun showFragmentContainer() {
        binding.viewPager.visibility = View.GONE
        binding.fragmentContainer.visibility = View.VISIBLE
    }

    private fun showViewPager() {
        binding.fragmentContainer.visibility = View.GONE
        binding.viewPager.visibility = View.VISIBLE
    }

    fun switchToFragment(fragment: Fragment) {
        loadFragmentWithUI(fragment)
    }

    override fun onDestroy() {
        flow1PagerAdapter = null
        flow2PagerAdapter = null
        flow3PagerAdapter = null
        flow4PagerAdapter = null
        teamReportPagerAdapter = null
        super.onDestroy()
    }
}