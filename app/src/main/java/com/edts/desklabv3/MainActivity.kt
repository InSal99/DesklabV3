package com.edts.desklabv3

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.edts.components.checkbox.CheckBox
import com.edts.components.radiobutton.RadioGroup
import com.edts.desklabv3.core.EntryPointsView
import com.edts.desklabv3.databinding.ActivityMainBinding
import com.edts.desklabv3.features.event.ui.attendanceoffline.ScanQRAttendanceView
import com.edts.desklabv3.features.event.ui.eventlist.EventListAttendanceView
import com.edts.desklabv3.features.event.ui.eventlist.EventListDaftarRSVPView
import com.edts.desklabv3.features.event.ui.eventlist.EventListInvitationNoRSVPView
import com.edts.desklabv3.features.event.ui.eventlist.EventListInvitationTolakEndView
import com.edts.desklabv3.features.event.ui.eventlist.EventListInvitationTolakStartView
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

//    private fun navigateToEventDetail() {
//        val eventDetailView = EventDetailView()
//
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, eventDetailView)
//            .addToBackStack("event_detail") // Optional: Add to back stack
//            .commit()
//    }

//    private fun setupRadioButtons() {
//        val options = listOf("Option 1", "Option 2", "Option 3", "Option 4")
//
//        dynamicRadioGroup.setData(options) { item ->
//            item
//        }
//
//        dynamicRadioGroup.setOnItemSelectedListener(object : RadioGroupDelegate {
//            override fun onItemSelected(position: Int, data: Any?) {
//                Toast.success(this@MainActivity, "Selected: $data at position $position")
//            }
//        })
//
//    }
//
//    private fun setupCheckboxes() {
//        val options = listOf("Option 1", "Option 2", "Option 3", "Option 4")
//
//        // Clear existing checkboxes
//        checkboxContainer.removeAllViews()
//        checkboxes.clear()
//
//        options.forEachIndexed { index, option ->
//            val checkbox = CheckBox(this).apply {
//                text = option
//                id = View.generateViewId()
//
//                // Set layout params
//                layoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    resources.getDimensionPixelSize(com.edts.components.R.dimen.line_height_24)
//                ).apply {
//                    if (index > 0) {
//                        topMargin = resources.getDimensionPixelSize(com.edts.components.R.dimen.margin_8dp)
//                    }
//                }
//
//                // Set click listener for each checkbox
//                setOnClickListener {
//                    Toast.success(this@MainActivity, "${if (isChecked) "Checked" else "Unchecked"}: $option")
//                    // Clear error when user interacts with any checkbox
//                    if (isChecked) {
//                        setErrorState(false)
//                    }
//                }
//
//                // Set delegate for checkbox state changes
//                setCustomCheckBoxDelegate(object : CheckboxDelegate {
//                    override fun onCheckChanged(checkBox: CheckBox, isChecked: Boolean) {
//                        Log.d("CustomCheckBox", "Checkbox ${checkBox.text} changed to: $isChecked")
//                        // Clear error when user checks any checkbox
//                        if (isChecked) {
//                            checkBox.setErrorState(false)
//                        }
//                    }
//                })
//            }
//
//            checkboxContainer.addView(checkbox)
//            checkboxes.add(checkbox)
//        }
//    }
//
//    private fun setupCheckboxErrorTestButtons() {
//        val toggleErrorButton = findViewById<Button>(R.id.toggleCheckboxErrorButton)
//        val clearErrorButton = findViewById<Button>(R.id.clearCheckboxErrorsButton)
//
//        toggleErrorButton.setOnClickListener {
//            val hasError = checkboxes.firstOrNull()?.isErrorState() ?: false
//            if (hasError) {
//                setErrorStateOnAllCheckboxes(false)
//                Toast.success(this, "Errors cleared")
//            } else {
//                setErrorStateOnAllCheckboxes(true)
//                Toast.success(this, "Errors shown")
//            }
//        }
//
//        clearErrorButton.setOnClickListener {
//            setErrorStateOnAllCheckboxes(false)
//            Toast.success(this, "All errors cleared")
//        }
//    }
//
//    private fun setErrorStateOnAllCheckboxes(error: Boolean) {
//        checkboxes.forEach { checkbox ->
//            checkbox.setErrorState(error)
//            Log.d("MainActivity", "Set error $error on checkbox: ${checkbox.text}")
//        }
//    }
//
//    private fun setupToastTestButtons() {
//        findViewById<Button>(R.id.btnSuccessToast).setOnClickListener {
//            Toast.success(this, "This is a success message!")
//        }
//
//        findViewById<Button>(R.id.btnErrorToast).setOnClickListener {
//            Toast.error(this, "This is an error message!")
//        }
//
//        findViewById<Button>(R.id.btnInfoToast).setOnClickListener {
//            Toast.info(this, "This is an info message!")
//        }
//    }
    }


//package com.edts.desklabv3
//
//import android.os.Bundle
//import android.view.View
//import android.widget.LinearLayout
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.RecyclerView
//import com.edts.components.checkbox.CheckBox
//import com.edts.components.radiobutton.RadioGroup
//import com.edts.components.tab.Tab
//import com.edts.desklabv3.core.EntryPointsView
//import com.edts.desklabv3.databinding.ActivityMainBinding
//import com.edts.desklabv3.features.event.ui.attendanceoffline.ScanQRAttendanceView
//import com.edts.desklabv3.features.event.ui.eventlist.EventListAttendanceView
//import com.edts.desklabv3.features.event.ui.eventlist.EventListDaftarRSVPView
//import com.edts.desklabv3.features.event.ui.eventlist.EventListInvitationNoRSVPView
//import com.edts.desklabv3.features.event.ui.eventlist.EventListInvitationTolakEndView
//import com.edts.desklabv3.features.event.ui.eventlist.EventListInvitationTolakStartView
//import com.edts.desklabv3.features.event.ui.eventlist.TabEventListDaftarRSVPAdapter
//import com.edts.desklabv3.features.event.ui.success.SuccessAttendanceOfflineView
//import com.edts.desklabv3.features.event.ui.success.SuccessAttendanceOnlineView
//import com.edts.desklabv3.features.event.ui.success.SuccessDenyInvitationView
//import com.edts.desklabv3.features.event.ui.success.SuccessRegistrationView
//import com.edts.desklabv3.features.home.ui.HomeAttendanceView
//import com.edts.desklabv3.features.home.ui.HomeDaftarRSVPView
//import com.edts.desklabv3.features.home.ui.HomeInvitationNoRSVPView
//import com.edts.desklabv3.features.home.ui.HomeInvitationTolakView
//import com.edts.desklabv3.features.home.ui.HomeManagerView
//import com.edts.desklabv3.features.leave.ui.laporantim.TeamReportActivityView
//import com.edts.desklabv3.features.leave.ui.laporantim.TeamReportLeaveView
//
//class MainActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var dynamicRadioGroup: RadioGroup
//    private lateinit var checkboxContainer: LinearLayout
//    private val checkboxes = mutableListOf<CheckBox>()
//    private var tabAdapter: TabEventListDaftarRSVPAdapter? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        if (savedInstanceState == null) {
//            // Initialize with EntryPointsView or your desired default fragment
////            loadFragmentWithUI(EntryPointsView())
//
//            // Example usage - uncomment the fragment you want to test:
//             loadFragmentWithUI(ScanQRAttendanceView())
//            // loadFragmentWithUI(HomeInvitationNoRSVPView())
//            // loadFragmentWithUI(EventListDaftarRSVPView())
//            // loadFragmentWithUI(EventListInvitationNoRSVPView())
//        }
//    }
//
//    private fun loadFragmentWithUI(fragment: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, fragment)
//            .commit()
//
//        configureUIForFragment(fragment)
//    }
//
//    private fun configureUIForFragment(fragment: Fragment) {
//        val fragmentClass = fragment::class.java.simpleName
//
//        when (fragmentClass) {
//            // Home fragments with no badge on Event tab
//            "HomeDaftarRSVPView", "HomeAttendanceView", "HomeManagerView" -> {
//                configureBottomNavigation(showBadge = false, showBottomNavigation = true)
//                configureHeaders(showTeamReport = false, showEventList = false)
//            }
//
//            // Home fragments with badge on Event tab
//            "HomeInvitationNoRSVPView", "HomeInvitationTolakView" -> {
//                configureBottomNavigation(showBadge = true, showBottomNavigation = true)
//                configureHeaders(showTeamReport = false, showEventList = false)
//            }
//
//            // Event list fragments with no badge on Undangan tab
//            "EventListDaftarRSVPView", "EventListAttendanceView", "EventListInvitationTolakEndView" -> {
//                configureBottomNavigation(showBadge = true, showBottomNavigation = true)
//                configureHeaders(showTeamReport = false, showEventList = true)
//                configureEventListTabs(showUndanganBadge = false)
//            }
//
//            // Event list fragments with badge on Undangan tab
//            "EventListInvitationNoRSVPView", "EventListInvitationTolakStartView" -> {
//                configureBottomNavigation(showBadge = true, showBottomNavigation = true)
//                configureHeaders(showTeamReport = false, showEventList = true)
//                configureEventListTabs(showUndanganBadge = true)
//            }
//
//            // Team report fragments
//            "TeamReportLeaveView", "TeamReportActivityView" -> {
//                configureBottomNavigation(showBadge = false, showBottomNavigation = false)
//                configureHeaders(showTeamReport = true, showEventList = false)
//            }
//
//            // Other fragments (Success views, ScanQR, etc.)
//            else -> {
//                configureBottomNavigation(showBadge = false, showBottomNavigation = false)
//                configureHeaders(showTeamReport = false, showEventList = false)
//            }
//        }
//    }
//
//    private fun configureHeaders(showTeamReport: Boolean, showEventList: Boolean) {
//        binding.headerTeamReportActivity.visibility = if (showTeamReport) View.VISIBLE else View.GONE
//        binding.headerEventListDaftarRSVP.visibility = if (showEventList) View.VISIBLE else View.GONE
//    }
//
//    private fun configureBottomNavigation(showBadge: Boolean, showBottomNavigation: Boolean) {
//        binding.cvBottomNavigation.visibility = if (showBottomNavigation) View.VISIBLE else View.GONE
//        binding.cvBottomNavigation.apply {
//            setItemBadge(2, showBadge) // item3 is at position 2 (0-indexed)
//        }
//    }
//
//    private fun configureEventListTabs(showUndanganBadge: Boolean) {
//        // Initialize the tab adapter if not already done
//        if (tabAdapter == null) {
//            tabAdapter = TabEventListDaftarRSVPAdapter(
//                tabTexts = arrayOf("Daftar Event", "Event Saya", "Undangan"),
//                selectedPosition = 0
//            ) { position, tabText ->
//                // Handle tab click if needed
//            }
//            binding.rvTabEventListDaftarRSVP.adapter = tabAdapter
//        }
//
//        // Configure the Undangan tab badge using binding
//        binding.rvTabEventListDaftarRSVP.post {
//            val undanganTabPosition = 2 // "Undangan" is at position 2
//
//            val viewHolder = binding.rvTabEventListDaftarRSVP.findViewHolderForAdapterPosition(undanganTabPosition)
//            viewHolder?.let { holder ->
//                val tabView = holder.itemView.findViewById<Tab>(R.id.cvTab)
//                tabView?.apply {
//                    showBadge = showUndanganBadge
//                    if (showUndanganBadge) {
//                        badgeText = "1"
//                    }
//                }
//            }
//        }
//    }
//
//    // Helper method for programmatically switching fragments with UI configuration
//    fun switchToFragment(fragment: Fragment) {
//        loadFragmentWithUI(fragment)
//    }
//}