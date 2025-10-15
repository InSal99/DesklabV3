package com.edts.desklabv3.features.event.ui.success

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.airbnb.lottie.LottieAnimationView
import com.edts.components.toast.Toast
import com.edts.components.tray.BottomTray
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.ContentCustomShareTrayBinding
import com.edts.desklabv3.databinding.FragmentSuccessAttendanceOfflineViewBinding
import com.edts.desklabv3.databinding.ItemShareActionBinding

class SuccessAttendanceOnlineView : Fragment() {
    private var _binding: FragmentSuccessAttendanceOfflineViewBinding? = null
    private val binding get() = _binding!!
    private val meetingLink = "https://teams.microsoft.com/l/meetup-join/your-meeting-id"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuccessAttendanceOfflineViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitleAttendanceOffline.text = "Kehadiran Tercatat!"
        binding.tvDescAttendanceOffline.text = "Terima kasih telah konfirmasi kehadiranmu"
        binding.cvPrimaryBtnAttendanceOffline.text = "Akses Meeting"
        binding.cvSecondaryBtnAttendanceOffline.text = "Lihat Detail Event"

        val lottieView = view.findViewById<LottieAnimationView>(R.id.ivIllustAttendanceOffline)
        lottieView.setAnimation(R.raw.il_success)
        lottieView.repeatCount = 0
        lottieView.playAnimation()

        setupButtonClickListeners()
        setupBackButton()
    }

    private fun setupButtonClickListeners() {
        binding.cvPrimaryBtnAttendanceOffline.setOnClickListener {
            showShareTray()
        }

        binding.cvSecondaryBtnAttendanceOffline.setOnClickListener {
            navigateToEventDetail()
        }
    }

    private fun setupBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                }
            }
        )
    }

    private fun showShareTray() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            showSystemShareTray()
        } else {
            showCustomShareTray()
        }
    }

    private fun showSystemShareTray() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, meetingLink)
            type = "text/plain"
        }

        val shareChooser = Intent.createChooser(shareIntent, "Bagikan link meeting")

        if (shareChooser.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(shareChooser)
        } else {
            copyLinkToClipboard()
        }
    }

    private fun showCustomShareTray() {
        val tray = BottomTray.newInstance(
            title = "Bagikan link meeting",
            showDragHandle = true,
            showFooter = false,
            hasShadow = true,
            hasStroke = true
        )

        val shareBinding = ContentCustomShareTrayBinding.inflate(layoutInflater)
        setupCustomShareView(shareBinding, tray)
        tray.setTrayContentView(shareBinding.root)
        tray.show(parentFragmentManager, "CustomShareTray")
    }

    private fun setupCustomShareView(shareBinding: ContentCustomShareTrayBinding, tray: BottomTray) {
        shareBinding.tvMeetingLink.text = meetingLink
        shareBinding.btnCopy.setOnClickListener {
            copyLinkToClipboard()
            tray.dismiss()
        }
        setupShareAppsInActionRow(shareBinding.actionRow, tray)
    }

    private fun setupShareAppsInActionRow(actionRow: LinearLayout, tray: BottomTray) {
        val shareApps = getAvailableShareApps()
        actionRow.removeAllViews()

        val browserDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_chrome)!!
        addShareAppItem(actionRow, browserDrawable, "Browser") {
            openInBrowser()
            tray.dismiss()
        }

        shareApps.take(6).forEach { resolveInfo ->
            val packageManager = requireActivity().packageManager
            val icon = resolveInfo.loadIcon(packageManager)
            val label = resolveInfo.loadLabel(packageManager).toString()

            addShareAppItem(actionRow, icon, label) {
                shareWithApp(resolveInfo)
                tray.dismiss()
            }
        }
    }

    private fun addShareAppItem(container: LinearLayout, drawable: Drawable, label: String, onClick: () -> Unit) {
        val itemBinding = ItemShareActionBinding.inflate(LayoutInflater.from(container.context), container, false)
        itemBinding.ivActionIcon.setImageDrawable(drawable)
        itemBinding.ivActionIcon.scaleType = ImageView.ScaleType.CENTER_INSIDE
        itemBinding.ivActionIcon.adjustViewBounds = true
        itemBinding.tvActionLabel.text = label
        itemBinding.root.setOnClickListener { onClick() }
        container.addView(itemBinding.root)
    }

    private fun getAvailableShareApps(): List<ResolveInfo> {
        val context = requireContext()
        val packageManager = context.packageManager

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, meetingLink)
        }

        return try {
            packageManager.queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY)
                .take(8)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun shareWithApp(resolveInfo: ResolveInfo) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, meetingLink)
            type = "text/plain"
            setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name)
        }

        try {
            startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.error(requireContext(), "Aplikasi tidak tersedia")
        }
    }

    private fun copyLinkToClipboard() {
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Link Meeting", meetingLink)
        clipboardManager.setPrimaryClip(clip)

        Toast.success(requireContext(), "Link meeting disalin")
    }

    private fun openInBrowser() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(meetingLink))
        try {
            startActivity(browserIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.error(requireContext(), "Tidak ada aplikasi browser ditemukan")
        }
    }

    private fun navigateToEventDetail() {
        val result = bundleOf(
            "fragment_class" to "EventDetailViewAttendance",
            "from_success" to true,
            "attendance_type" to "online",
            "meeting_link" to meetingLink
        )
        parentFragmentManager.setFragmentResult("navigate_fragment", result)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}