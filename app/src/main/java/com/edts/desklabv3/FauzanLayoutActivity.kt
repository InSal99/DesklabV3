package com.edts.desklabv3

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.edts.desklabv3.databinding.ActivityFauzanLayoutBinding
import com.edts.desklabv3.features.event.ui.EmployeeLeaveDetailFragment
import com.edts.desklabv3.features.event.ui.EventInvitationFragment
import com.edts.desklabv3.features.event.ui.MyEventsFragment

class FauzanLayoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFauzanLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFauzanLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // This card now navigates to the MyEventsFragment
        binding.cvMyEventCard.setOnClickListener {
            showFragment(MyEventsFragment())
        }

        // This card now navigates to the EventInvitationFragment
        binding.cvNotificationCard.setOnClickListener {
            showFragment(EventInvitationFragment())
        }

        // This card navigates to the EmployeeLeaveDetailFragment
        binding.cvNotificationCard2.setOnClickListener {
            showFragment(EmployeeLeaveDetailFragment())
        }

        // Add the back press callback directly to the activity's dispatcher
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.fragmentContainer.visibility == View.VISIBLE) {
                    binding.fragmentContainer.visibility = View.GONE
                    binding.svMainContent.visibility = View.VISIBLE

                    supportFragmentManager.popBackStack()
                } else {
                    if (isEnabled) {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        })
    }

    /**
     * Helper function to replace the content of the FragmentContainerView with a new fragment.
     */
    private fun showFragment(fragment: Fragment) {
        binding.svMainContent.visibility = View.GONE
        binding.fragmentContainer.visibility = View.VISIBLE

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}