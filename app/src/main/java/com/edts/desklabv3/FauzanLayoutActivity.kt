package com.edts.desklabv3

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.edts.desklabv3.core.EventModalityLoadingComponentFragment
import com.edts.desklabv3.databinding.ActivityFauzanLayoutBinding
import com.edts.desklabv3.features.leave.ui.EmployeeLeaveDetailFragment
import com.edts.desklabv3.features.event.ui.invitation.EventInvitationFragmentTolakUndangan
import com.edts.desklabv3.features.event.ui.myevent.MyEventsFragmentAttendance

class FauzanLayoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFauzanLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFauzanLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // First, check if there are fragments on the back stack
                if (supportFragmentManager.backStackEntryCount > 0) {
                    // If yes, let the FragmentManager handle the pop.
                    // This will automatically trigger the exit animations.
                    supportFragmentManager.popBackStack()
                } else {
                    // If the back stack is empty, and the container is visible,
                    // it means the last fragment was just popped. Hide the container.
                    if (binding.fragmentContainer.isVisible) {
                        binding.fragmentContainer.visibility = View.GONE
                        binding.svMainContent.visibility = View.VISIBLE
                    } else {
                        // If back stack is empty and container is not visible,
                        // perform the default back action (finish activity).
                        if (isEnabled) {
                            isEnabled = false
                            onBackPressedDispatcher.onBackPressed()
                        }
                    }
                }
            }
        })

        // This listener ensures the main view is hidden when a fragment is popped and another is shown
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                binding.svMainContent.visibility = View.VISIBLE
                binding.fragmentContainer.visibility = View.GONE
            }
        }
    }

    private fun setupClickListeners() {
        binding.cvMyEventCard.setOnClickListener {
            showFragment(MyEventsFragmentAttendance())
        }

        binding.cvNotificationCard.setOnClickListener {
            showFragment(EventInvitationFragmentTolakUndangan())
        }

        binding.cvNotificationCard2.setOnClickListener {
            showFragment(EmployeeLeaveDetailFragment())
        }

        binding.cvModalityLoading.setOnClickListener {
            showFragment(EventModalityLoadingComponentFragment())
        }
    }

    private fun showFragment(fragment: Fragment) {
        binding.svMainContent.visibility = View.GONE
        binding.fragmentContainer.visibility = View.VISIBLE

        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )

        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}