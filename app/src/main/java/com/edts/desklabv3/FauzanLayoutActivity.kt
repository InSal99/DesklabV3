package com.edts.desklabv3

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.edts.desklabv3.core.EventInvitationComponentFragment
import com.edts.desklabv3.core.EventModalityConfirmationComponentFragment
import com.edts.desklabv3.core.EventModalityLoadingComponentFragment
import com.edts.desklabv3.core.MyEventsComponentFragment
import com.edts.desklabv3.databinding.ActivityFauzanLayoutBinding
import com.edts.desklabv3.features.leave.ui.EmployeeLeaveDetailView
import com.edts.desklabv3.features.event.ui.invitation.EventInvitationDeclineView
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
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    if (binding.fragmentContainer.isVisible) {
                        binding.fragmentContainer.visibility = View.GONE
                        binding.svMainContent.visibility = View.VISIBLE
                    } else {
                        if (isEnabled) {
                            isEnabled = false
                            onBackPressedDispatcher.onBackPressed()
                        }
                    }
                }
            }
        })

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
            showFragment(EventInvitationDeclineView())
        }

        binding.cvNotificationCard2.setOnClickListener {
            showFragment(EmployeeLeaveDetailView())
        }

        binding.btnMyEventComponent.setOnClickListener {
            showFragment(MyEventsComponentFragment())
        }

        binding.btnEventInvitationComponent.setOnClickListener {
            showFragment(EventInvitationComponentFragment())
        }

        binding.btnEventModalityConfirmation.setOnClickListener {
            showFragment(EventModalityConfirmationComponentFragment())
        }

        binding.btnEventModalityLoading.setOnClickListener {
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