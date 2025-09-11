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
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.bottom.navigation.BottomNavigation
import com.edts.components.bottom.navigation.BottomNavigationDelegate
import com.edts.components.bottom.navigation.BottomNavigationItem
import com.edts.components.checkbox.CheckBox
import com.edts.components.radiobutton.RadioGroup
import com.edts.components.tab.Tab
import com.edts.components.toast.Toast
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.edts.components.checkbox.CheckBox
import com.edts.components.radiobutton.RadioGroup
import com.edts.desklabv3.core.EntryPointsView
import com.edts.desklabv3.databinding.ActivityMainBinding
import com.edts.desklabv3.features.event.ui.EventDetailRSVPView
import com.edts.desklabv3.features.event.ui.EventDetailViewAttendance
import com.edts.desklabv3.features.event.ui.EventDetailViewNoRSVP
import com.edts.desklabv3.features.event.ui.EventDetailViewTolakUndangan
import com.edts.desklabv3.features.event.ui.attendanceoffline.ScanQRAttendanceView
import com.edts.desklabv3.features.event.ui.eventlist.EventListAttendanceView
import com.edts.desklabv3.features.event.ui.eventlist.EventListDaftarRSVPView
import com.edts.desklabv3.features.event.ui.eventlist.EventListInvitationNoRSVPView
import com.edts.desklabv3.features.event.ui.eventlist.EventListInvitationTolakEndView
import com.edts.desklabv3.features.event.ui.eventlist.EventListInvitationTolakStartView
import com.edts.desklabv3.features.event.ui.eventlist.TabEventListDaftarRSVPAdapter
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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dynamicRadioGroup: RadioGroup
    private lateinit var checkboxContainer: LinearLayout
    private val checkboxes = mutableListOf<CheckBox>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {

//             loadFragmentWithUI(EntryPointsView(), addToBackStack = false)

//             // Example usage - uncomment the fragment you want to test:
// //             loadFragmentWithUI(ScanQRAttendanceView())
//             // loadFragmentWithUI(HomeInvitationNoRSVPView())
//             // loadFragmentWithUI(EventListDaftarRSVPView())
//             // loadFragmentWithUI(EventListInvitationNoRSVPView())
//         }

//         supportFragmentManager.setFragmentResultListener("navigate_fragment", this) { requestKey, bundle ->
//             val fragmentClassName = bundle.getString("fragment_class")
//             val flowType = bundle.getString("flow_type", "")
//             val fragment = when (fragmentClassName) {
//                 "HomeDaftarRSVPView" -> HomeDaftarRSVPView()
//                 "EventListDaftarRSVPView" -> EventListDaftarRSVPView()
//                 "EventDetailDaftarRSVPView" -> EventDetailRSVPView()
//                 "MyEventDaftarRSVPView" -> MyEventsFragmentRSVP()
//                 "HomeInvitationNoRSVPView" -> HomeInvitationNoRSVPView()
//                 "EventListInvitationNoRSVPView" -> EventListInvitationNoRSVPView()
//                 "EventDetailViewNoRSVP" -> EventDetailViewNoRSVP()
//                 "EventInvitationFragmentNoRSVP" -> EventInvitationFragmentNoRSVP()
//                 "MyEventsFragmentNoRSVP" -> MyEventsFragmentNoRSVP()
//                 "HomeAttendanceView" -> HomeAttendanceView()
//                 "ScanQRAttendanceView" -> ScanQRAttendanceView()
//                 "SuccessAttendanceOfflineView" -> SuccessAttendanceOfflineView()
//                 "SuccessAttendanceOnlineView" -> SuccessAttendanceOnlineView()
//                 "EventListAttendanceView" -> EventListAttendanceView()
//                 "HomeInvitationTolakView" -> HomeInvitationTolakView()
//                 "EventListInvitationTolakStartView" -> EventListInvitationTolakStartView()
//                 "EventInvitationFragmentTolakUndangan" -> EventInvitationFragmentTolakUndangan()
//                 "EventDetailViewTolakUndangan" -> EventDetailViewTolakUndangan()
//                 "SuccessDenyInvitationView" -> SuccessDenyInvitationView()
//                 "EventListInvitationTolakEndView" -> EventListInvitationTolakEndView()
//                 "SuccessRegistrationView" -> SuccessRegistrationView().apply {
//                     arguments = Bundle().apply {
//                         putString("flow_type", flowType)
//                     }
//                 }
//                 "EventDetailViewAttendance" -> EventDetailViewAttendance().apply {
//                     arguments = Bundle().apply {
//                         putBoolean("from_success", bundle.getBoolean("from_success", false))
//                         putString("attendance_type", bundle.getString("attendance_type", ""))
//                         putString("meeting_link", bundle.getString("meeting_link", ""))
//                     }
//                 }
//                 else -> EntryPointsView()
//             }
//             loadFragmentWithUI(fragment, addToBackStack = true)
//         }

//         supportFragmentManager.addOnBackStackChangedListener {
//             val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
//             currentFragment?.let {
//                 configureUIForFragment(it)
//             }
//         }

//         setupBottomNavigation()
//     }

//     private fun setupBottomNavigation() {
//         binding.cvBottomNavigation.delegate = object : BottomNavigationDelegate {
//             override fun onBottomNavigationItemClicked(
//                 item: BottomNavigationItem,
//                 position: Int,
//                 clickCount: Int
//             ) {
//                 handleBottomNavigationClick(position, item, clickCount)
//                 binding.cvBottomNavigation.setActiveItem(position)
//             }

//             override fun onBottomNavigationItemStateChanged(
//                 item: BottomNavigationItem,
//                 newState: BottomNavigationItem.NavState,
//                 oldState: BottomNavigationItem.NavState
//             ) {
//                 Log.d("BottomNav", "State changed for ${item.navText}: $oldState -> $newState")
//             }
//         }

//         // Set initial active item (Home tab)
//         binding.cvBottomNavigation.setActiveItem(0)
//     }

//     fun BottomNavigation.setActiveItem(position: Int) {
//         this.setActiveItem(position)
//     }

//     private fun handleBottomNavigationClick(position: Int, item: BottomNavigationItem, clickCount: Int) {
//         when (position) {
//             0 -> navigateToHome() // Home tab (position 0)
//             1 -> navigateToEmployees() // Karyawan tab (position 1)
//             2 -> navigateToEventList() // Event tab (position 2) - This is what you want!
//             3 -> navigateToProfile() // Profil tab (position 3)
//         }
//     }

//     private fun navigateToEventList() {
//         val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

//         val targetFragment = when (currentFragment) {
//             is EventListInvitationNoRSVPView -> EventListInvitationNoRSVPView()

//             is HomeInvitationTolakView,
//             is EventListInvitationTolakStartView -> EventListInvitationTolakStartView()

//             is HomeAttendanceView -> EventListAttendanceView()

//             is HomeInvitationNoRSVPView -> EventListInvitationTolakStartView()

//             else -> EventListDaftarRSVPView()
//         }

//         if (currentFragment?.javaClass != targetFragment.javaClass) {
//             loadFragmentWithUI(targetFragment, addToBackStack = true)
//         }
//     }

//     private fun navigateToHome() {
//         val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

//         val targetHomeFragment = when (currentFragment) {
//             is EventListInvitationNoRSVPView -> HomeInvitationNoRSVPView()
//             is MyEventsFragmentNoRSVP, is EventInvitationFragmentNoRSVP -> HomeInvitationNoRSVPView()
//             is EventListInvitationTolakStartView -> HomeInvitationTolakView()
//             is HomeInvitationTolakView -> EventListInvitationTolakStartView()
//             else -> HomeDaftarRSVPView()
//         }

//         if (currentFragment?.javaClass != targetHomeFragment.javaClass) {
//             loadFragmentWithUI(targetHomeFragment, addToBackStack = true)
//         }
//     }

//     private fun loadFragmentWithUI(fragment: Fragment, addToBackStack: Boolean = true) {
//         val transaction = supportFragmentManager.beginTransaction()
//             .replace(R.id.fragment_container, fragment)

//         if (addToBackStack) {
//             transaction.addToBackStack(fragment::class.java.simpleName)
//         }

//         transaction.commit()
//         configureUIForFragment(fragment)
//     }

//     private fun configureUIForFragment(fragment: Fragment) {
//         Log.d("UI_CONFIG", "Configuring UI for: ${fragment::class.java.simpleName}")
//         when (fragment) {
//             // Home fragments with no badge on Event tab
//             is HomeDaftarRSVPView, is HomeAttendanceView, is HomeManagerView -> {
//                 configureBottomNavigation(showBadge = false, showBottomNavigation = true)
//                 configureHeaders(showTeamReport = false, showEventList = false)
//                 binding.cvBottomNavigation.setActiveItem(0)
//             }

//             // Home fragments with badge on Event tab
//             is HomeInvitationNoRSVPView, is HomeInvitationTolakView -> {
//                 configureBottomNavigation(showBadge = true, showBottomNavigation = true)
//                 configureHeaders(showTeamReport = false, showEventList = false)
//                 binding.cvBottomNavigation.setActiveItem(0)
//             }

//             // Event list fragments with no badge on Undangan tab
//             is EventListInvitationTolakEndView -> {
//                 configureBottomNavigation(showBadge = true, showBottomNavigation = true)
//                 configureHeaders(showTeamReport = false, showEventList = true)
//                 configureEventListTabs(showUndanganBadge = false)
//                 binding.cvBottomNavigation.setActiveItem(2)
//             }

//             is EventListDaftarRSVPView, is EventListAttendanceView -> {
//                 configureBottomNavigation(showBadge = false, showBottomNavigation = true)
//                 configureHeaders(showTeamReport = false, showEventList = true)
//                 configureEventListTabs(showUndanganBadge = false, selectedPosition = 0) // Daftar Event
//                 binding.cvBottomNavigation.setActiveItem(2)
//             }

//             is MyEventsFragmentRSVP, is MyEventsFragmentNoRSVP -> {
//                 configureBottomNavigation(showBadge = false, showBottomNavigation = true)
//                 configureHeaders(showTeamReport = false, showEventList = true)
//                 configureEventListTabs(showUndanganBadge = false, selectedPosition = 1) // ✅ Event Saya
//                 binding.cvBottomNavigation.setActiveItem(2)
//             }

//             is EventInvitationFragmentNoRSVP, is EventInvitationFragmentTolakUndangan -> {
//                 configureBottomNavigation(showBadge = true, showBottomNavigation = true)
//                 configureHeaders(showTeamReport = false, showEventList = true)
//                 configureEventListTabs(showUndanganBadge = true, selectedPosition = 2)
//                 binding.cvBottomNavigation.setActiveItem(2)
//             }


//             // Event list fragments with badge on Undangan tab
//             is EventListInvitationNoRSVPView, is EventListInvitationTolakStartView -> {
//                 Log.d("UI_CONFIG", "Handling EventListInvitationNoRSVPView with badges")
//                 configureBottomNavigation(showBadge = true, showBottomNavigation = true)
//                 configureHeaders(showTeamReport = false, showEventList = true)
//                 configureEventListTabs(showUndanganBadge = true)
//                 binding.cvBottomNavigation.setActiveItem(2)
//             }

//             // Team report fragments
//             is TeamReportLeaveView, is TeamReportActivityView -> {
//                 configureBottomNavigation(showBadge = false, showBottomNavigation = false)
//                 configureHeaders(showTeamReport = true, showEventList = false)
//             }
//
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, EntryPointsView())

//                //HOME
//                .replace(R.id.fragment_container, HomeDaftarRSVPView())
//                .replace(R.id.fragment_container, HomeInvitationNoRSVPView())
//                .replace(R.id.fragment_container, HomeInvitationTolakView())
//                .replace(R.id.fragment_container, HomeAttendanceView())
//                .replace(R.id.fragment_container, HomeManagerView())
//
//                //ATTENDANCE OFFLINE
//                .replace(R.id.fragment_container, ScanQRAttendanceView())
//
//                //LAPORAN TIM
//                .replace(R.id.fragment_container, TeamReportLeaveView())
//                .replace(R.id.fragment_container, TeamReportActivityView())
//
//                //DAFTAR EVENT
//                .replace(R.id.fragment_container, EventListDaftarRSVPView())
//                .replace(R.id.fragment_container, EventListAttendanceView())
//                .replace(R.id.fragment_container, EventListInvitationNoRSVPView())
//                .replace(R.id.fragment_container, EventListInvitationTolakStartView())
//                .replace(R.id.fragment_container, EventListInvitationTolakEndView())
//
//
//
//                //SUCCESS
//                .replace(R.id.fragment_container, SuccessAttendanceOfflineView())
//                .replace(R.id.fragment_container, SuccessAttendanceOnlineView())
//                .replace(R.id.fragment_container, SuccessRegistrationView())
//                .replace(R.id.fragment_container, SuccessDenyInvitationView())

                .commit();
         }

//        binding.cvMyEventCard.setOnClickListener {
//            val intent = Intent(this, MyEventsActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.cvNotificationCard.setOnClickListener {
//            val intent = Intent(this, EventInvitationActivity::class.java)
//            startActivity(intent)
//        }

//        binding.btnGoToEventDetail.setOnClickListener {
//            navigateToEventDetail()
//            Log.d("Go To Event Detail", "Button clicked")
//        }

//        dynamicRadioGroup = findViewById(R.id.rbTest)
//        setupRadioButtons()
//
//        checkboxContainer = findViewById(R.id.checkboxContainer)
//        setupCheckboxes()
//        setupCheckboxErrorTestButtons()
//        setupToastTestButtons()
        }
    
    private fun navigateToEmployees() {
        Toast.info(this, "Navigating to Karyawan")
    }

    private fun navigateToProfile() {
        // Clear all back stack and show EntryPointsView as the root fragment
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        // Replace with EntryPointsView without adding to back stack
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, EntryPointsView())
            .commit()

        binding.cvBottomNavigation.setActiveItem(3)
    }

    private fun configureHeaders(showTeamReport: Boolean, showEventList: Boolean) {
        binding.headerTeamReportActivity.visibility = if (showTeamReport) View.VISIBLE else View.GONE
        binding.headerEventListDaftarRSVP.visibility = if (showEventList) View.VISIBLE else View.GONE
    }

    private fun configureBottomNavigation(showBadge: Boolean, showBottomNavigation: Boolean) {
        Log.d("UI_CONFIG", "Configuring bottom nav: showBadge=$showBadge, showBottomNav=$showBottomNavigation")
        binding.cvBottomNavigation.visibility = if (showBottomNavigation) View.VISIBLE else View.GONE
        binding.cvBottomNavigation.apply {
            setItemBadge(2, showBadge) // item3 is at position 2 (0-indexed)
        }
    }

    private fun configureEventListTabs(showUndanganBadge: Boolean, selectedPosition: Int = 0) {
        Log.d("UI_CONFIG", "Configuring tabs: showUndanganBadge=$showUndanganBadge")
        tabAdapter = TabEventListDaftarRSVPAdapter(
            tabTexts = arrayOf("Daftar Event", "Event Saya", "Undangan"),
            selectedPosition = selectedPosition
        ) { position, tabText ->
            if (position == 2 && tabText == "Undangan") {
                val result = bundleOf("fragment_class" to "EventInvitationFragmentNoRSVP")
                supportFragmentManager.setFragmentResult("navigate_fragment", result)
            }
        }
        binding.rvTabEventListDaftarRSVP.adapter = tabAdapter

        // Configure the Undangan tab badge
        binding.rvTabEventListDaftarRSVP.post {
            val undanganTabPosition = 2
            val viewHolder = binding.rvTabEventListDaftarRSVP.findViewHolderForAdapterPosition(undanganTabPosition)
            viewHolder?.let { holder ->
                val tabView = holder.itemView.findViewById<Tab>(R.id.cvTab)
                tabView?.apply {
                    showBadge = showUndanganBadge
                    if (showUndanganBadge) {
                        badgeText = "1"
                    }
                    Log.d("UI_CONFIG", "Tab badge set: showBadge=$showUndanganBadge")
                }
            }
        }
    }


    // Helper method for programmatically switching fragments with UI configuration
    fun switchToFragment(fragment: Fragment) {
        loadFragmentWithUI(fragment)
    }
}
    }
