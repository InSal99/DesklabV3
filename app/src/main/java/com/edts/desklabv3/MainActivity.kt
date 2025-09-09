package com.edts.desklabv3

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.edts.components.checkbox.CheckBox
import com.edts.components.radiobutton.RadioGroup
import com.edts.desklabv3.features.event.ui.EventDetailView
import com.edts.desklabv3.databinding.ActivityMainBinding
import com.edts.desklabv3.features.event.ui.attendanceoffline.ScanQRAttendanceView
import com.edts.desklabv3.features.event.ui.eventlist.EventListDaftarRSVPView
import com.edts.desklabv3.features.event.ui.success.SuccessAttendanceOfflineView
import com.edts.desklabv3.features.event.ui.success.SuccessAttendanceOnlineView
import com.edts.desklabv3.features.event.ui.success.SuccessDenyInvitationView
import com.edts.desklabv3.features.event.ui.success.SuccessRegistrationView
import com.edts.desklabv3.features.home.ui.HomeDaftarRSVPView
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
//                .replace(R.id.fragment_container, HomeDaftarRSVPView())
//                .replace(R.id.fragment_container, ScanQRAttendanceView())
//                .replace(R.id.fragment_container, SuccessAttendanceOfflineView())
//                .replace(R.id.fragment_container, SuccessAttendanceOnlineView())
//                .replace(R.id.fragment_container, SuccessRegistrationView())
//                .replace(R.id.fragment_container, SuccessDenyInvitationView())
                .replace(R.id.fragment_container, TeamReportLeaveView())
//                .replace(R.id.fragment_container, TeamReportActivityView())

//                .replace(R.id.fragment_container, EventListDaftarRSVPView())

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

    private fun navigateToEventDetail() {
        val eventDetailView = EventDetailView()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, eventDetailView)
            .addToBackStack("event_detail") // Optional: Add to back stack
            .commit()
    }

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
