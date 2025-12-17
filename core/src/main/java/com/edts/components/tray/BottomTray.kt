package com.edts.components.tray

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.edts.components.R
import com.edts.components.databinding.BottomTrayBinding
import com.edts.components.footer.Footer
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttr
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class BottomTray : BottomSheetDialogFragment() {
    private var _binding: BottomTrayBinding? = null
    private val binding get() = _binding!!
    private var _title: String? = null
    private var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>? = null
    private val cachedColors = mutableMapOf<Int, Int>()
    private val cachedDrawables = mutableMapOf<String, Drawable>()
    private var pendingContentView: View? = null

    var delegate: BottomTrayDelegate? = null
    var dragHandleVisibility: Boolean = true
        set(value) {
            field = value
            if (_binding != null) {
                binding.trayDragHandle.isVisible = value
                bottomSheetBehavior?.isDraggable = value
            }
        }
    var showFooter: Boolean = true
        set(value) {
            field = value
            if (_binding != null) {
                binding.trayFooter.isVisible = value
            }
        }
    var hasShadow: Boolean = true
        set(value) {
            field = value
            if (_binding != null) {
                updateBackground()
            }
        }
    var hasStroke: Boolean = true
        set(value) {
            field = value
            if (_binding != null) {
                updateBackground()
            }
        }
    var snapPoints: IntArray = intArrayOf()
        set(value) {
            field = value.copyOf()
            if (_binding != null && value.isNotEmpty()) {
                bottomSheetBehavior?.peekHeight = value.first()
            }
        }
    var isCancelableOnTouchOutside: Boolean = true
    var customAnimationsEnabled: Boolean = true

    @StyleRes
    var titleTextAppearance: Int? = R.style.TextSemiBold_Heading1
        set(value) {
            field = value
            if (_binding != null) {
                applyTitleAppearance()
            }
        }
    @ColorRes
    var titleTextColor: Int? = R.color.kitColorNeutralBlack
        set(value) {
            field = value
            if (_binding != null) {
                applyTitleColor()
            }
        }

    override fun getTheme(): Int = R.style.ThemeOverlay_DesklabV3_UIKit_BottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setCanceledOnTouchOutside(isCancelableOnTouchOutside)
        setupEdgeToEdge(dialog)
        setupDialogShowListener(dialog)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomTrayBinding.inflate(inflater, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                0,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRootView()
        setupViews()
        updateBackground()
        setupDragHandle()

        if (customAnimationsEnabled) {
            setCustomAnimations()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        handleThemeChange()
//        cachedDrawables.clear()
//
//        if (_binding != null) {
//            updateBackground()
//            setupDragHandle()
//            applyTitleColor()
//        }
    }

    private fun handleThemeChange() {
        cachedDrawables.clear()
        cachedColors.clear()

        if (_binding != null) {
            updateBackground()
            setupDragHandle()
            applyTitleColor()

            (dialog as? BottomSheetDialog)?.window?.let { window ->
                window.navigationBarColor = context.resolveColorAttr(
                    R.attr.colorForegroundWhite,
                    R.color.kitColorNeutralWhite
                )
            }
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (manager.findFragmentByTag(tag) != null) {
            return
        }
        super.show(manager, tag)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { delegate?.onShow(it) }
    }

    override fun dismiss() {
        if (isAdded && parentFragmentManager != null) {
            super.dismiss()
        } else {
            dismissAllowingStateLoss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        delegate?.onDismiss(dialog)
        super.onDismiss(dialog)
    }

    override fun dismissAllowingStateLoss() {
        try {
            super.dismissAllowingStateLoss()
        } catch (e: IllegalStateException) { }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cleanup()
    }

    private fun setupEdgeToEdge(dialog: BottomSheetDialog) {
        dialog.window?.let { window ->
//            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = context.resolveColorAttr(R.attr.colorForegroundWhite, R.color.kitColorNeutralWhite)
            WindowInsetsControllerCompat(window, window.decorView).apply {
//                isAppearanceLightNavigationBars = true
//                isAppearanceLightStatusBars = true
            }
        }
    }

    private fun setupDialogShowListener(dialog: BottomSheetDialog) {
        dialog.setOnShowListener { dialogInterface ->
            setupBottomSheetContainer(dialog)
            setupBehavior(dialog)
            delegate?.onShow(dialogInterface)
        }
    }

    private fun setupBottomSheetContainer(dialog: BottomSheetDialog) {
        val bottomSheet = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        val coordinator = dialog.findViewById<CoordinatorLayout>(com.google.android.material.R.id.coordinator)

        coordinator?.apply {
            clipToPadding = false
            clipChildren = false
        }

        bottomSheet?.apply {
            clipToPadding = false
            clipChildren = false
            clipToOutline = false
            setWillNotDraw(false)
            setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun setupBehavior(dialog: BottomSheetDialog) {
        val bottomSheet = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let { sheet ->
            bottomSheetBehavior = BottomSheetBehavior.from(sheet).apply {
                isDraggable = dragHandleVisibility
                if (snapPoints.isNotEmpty()) {
                    peekHeight = snapPoints.first()
                }
                addBottomSheetCallback(bottomSheetCallback)
                state = BottomSheetBehavior.STATE_EXPANDED
                skipCollapsed = true
            }
        }
    }

    private fun setupRootView() {
        binding.root.apply {
            clipToOutline = false
            clipChildren = false
            clipToPadding = false
            setWillNotDraw(false)
        }
    }

    private fun setupViews() {
        binding.trayTitle.text = _title
        binding.trayTitle.isVisible = !_title.isNullOrEmpty()
        binding.trayDragHandle.isVisible = dragHandleVisibility
        binding.trayFooter.isVisible = showFooter
        applyTitleAppearance()
        applyTitleColor()

        pendingContentView?.let { view ->
            binding.trayContent.removeAllViews()
            binding.trayContent.addView(view)
            pendingContentView = null
        }
    }

    private fun setupDragHandle() {
        if (dragHandleVisibility) {
            binding.trayDragHandle.background = createDragHandleDrawable()
        }
    }

    private fun updateBackground() {
        if (_binding == null) return
//        val background = getBackgroundDrawable(hasShadow, hasStroke)
        val background = createBackgroundDrawable(hasShadow, hasStroke)
        binding.root.background = background
        val padding = if (hasShadow) (8.dpToPx) else 0
        binding.root.setPadding(0, padding, 0, 0)
        binding.root.apply {
            clipToOutline = false
            clipChildren = false
            clipToPadding = false
            setWillNotDraw(false)
        }
    }

    private fun getBackgroundDrawable(hasShadow: Boolean, hasStroke: Boolean): Drawable {
        val cacheKey = "bg_shadow_${hasShadow}_stroke_$hasStroke"
        return cachedDrawables.getOrPut(cacheKey) {
            createBackgroundDrawable(hasShadow, hasStroke)
        }
    }

    private fun createBackgroundDrawable(hasShadow: Boolean, hasStroke: Boolean): Drawable {
        val cornerRadius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 16f,
            requireContext().resources.displayMetrics
        )
        val shapeAppearanceModel = ShapeAppearanceModel.builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, cornerRadius)
            .setTopRightCorner(CornerFamily.ROUNDED, cornerRadius)
            .build()

        val bgColor = requireContext().resolveColorAttr(R.attr.colorBackgroundSurface, R.color.kitColorNeutralWhite)
        val backgroundDrawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
            fillColor = ColorStateList.valueOf(bgColor)
            if (hasShadow) {
                initializeElevationOverlay(requireContext())
                elevation = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 8f, requireContext().resources.displayMetrics
                )
                shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
                val shadowColor = requireContext().resolveColorAttr(R.attr.colorShadowNeutralKey, R.color.kitColorNeutralGrayDarkA10)
                setShadowColor(shadowColor)
            }
        }
        if (!hasStroke) {
            return backgroundDrawable
        }
        val strokeColor = requireContext().resolveColorAttr(R.attr.colorStrokeSubtle, R.color.kitColorNeutralGrayLightA30)
        val strokeWidth = resources.getDimension(R.dimen.stroke_weight_1dp)
        val strokeDrawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
            fillColor = ColorStateList.valueOf(Color.TRANSPARENT)
            setStroke(strokeWidth, strokeColor)
        }
        val insetDrawable = InsetDrawable(
            strokeDrawable,
            0, 0, 0, -strokeWidth.toInt()
        )
        return LayerDrawable(arrayOf(backgroundDrawable, insetDrawable))
    }

    private fun getDragHandleDrawable(): Drawable {
        return cachedDrawables.getOrPut("drag_handle") {
            createDragHandleDrawable()
        }
    }

    private fun createDragHandleDrawable(): MaterialShapeDrawable {
        val dragHandleCornerRadius = 2f * requireContext().resources.displayMetrics.density
        val shapeAppearanceModel = ShapeAppearanceModel.builder()
            .setAllCorners(CornerFamily.ROUNDED, dragHandleCornerRadius)
            .build()
        return MaterialShapeDrawable(shapeAppearanceModel).apply {
            fillColor = ColorStateList.valueOf(
                requireContext().resolveColorAttr(R.attr.colorForegroundTertiary, R.color.kitColorNeutralGrayLightA50)
            )
        }
    }

    private fun applyTitleAppearance() {
        titleTextAppearance?.let { appearance ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.trayTitle.setTextAppearance(appearance)
            } else {
                @Suppress("DEPRECATION")
                binding.trayTitle.setTextAppearance(requireContext(), appearance)
            }
        }
    }

    private fun applyTitleColor() {
        titleTextColor?.let { color ->
            binding.trayTitle.setTextColor(requireContext().resolveColorAttr(R.attr.colorForegroundPrimary, color))
        }
    }

    private fun setCustomAnimations() {
    }
    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            delegate?.onStateChanged(bottomSheet, newState)
        }
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            delegate?.onSlide(bottomSheet, slideOffset)
            handleSnapping(bottomSheet, slideOffset)
        }
    }

    private fun handleSnapping(bottomSheet: View, slideOffset: Float) {
        if (snapPoints.isEmpty() || slideOffset <= 0.1f) return
        val targetSnapPoint = findNearestSnapPoint(bottomSheet.height, slideOffset)
        targetSnapPoint?.let { snapPoint ->
            bottomSheetBehavior?.setPeekHeight(snapPoint, true)
        }
    }

    private fun findNearestSnapPoint(bottomSheetHeight: Int, slideOffset: Float): Int? {
        return snapPoints.find { point ->
            val normalizedSnapPoint = point.toFloat() / bottomSheetHeight
            normalizedSnapPoint >= slideOffset
        }
    }

    private fun cleanup() {
        bottomSheetBehavior?.removeBottomSheetCallback(bottomSheetCallback)
        bottomSheetBehavior = null
        cachedColors.clear()
        cachedDrawables.clear()
        _binding = null
    }

    fun getBottomSheetBehavior(): BottomSheetBehavior<FrameLayout>? = bottomSheetBehavior

    fun setTitle(text: String) {
        _title = text
        if (_binding != null) {
            binding.trayTitle.text = text
            binding.trayTitle.isVisible = text.isNotEmpty()
        }
    }

    fun setTitle(resId: Int) {
        val text = getString(resId)
        setTitle(text)
    }

    fun getTitle(): CharSequence? = _title

    fun setTrayContentView(view: View) {
        if (_binding != null) {
            binding.trayContent.removeAllViews()
            binding.trayContent.addView(view)
        } else {
            pendingContentView = view
        }
    }

    fun getTrayContentView(): FrameLayout? = if (_binding != null) binding.trayContent else null

    fun setDragHandleVisible(visible: Boolean) {
        dragHandleVisibility = visible
    }

    fun isDragHandleVisible(): Boolean = dragHandleVisibility

    fun setFooterVisible(visible: Boolean) {
        showFooter = visible
    }

    fun isFooterVisible(): Boolean = showFooter

    fun configureFooter(configure: (Footer) -> Unit) {
        _binding?.trayFooter?.let { footer ->
            configure(footer)
        }
    }

    fun setShadowEnabled(enabled: Boolean) {
        hasShadow = enabled
    }

    fun setStrokeEnabled(enabled: Boolean) {
        hasStroke = enabled
    }

    fun notifyOnShow(dialogInterface: DialogInterface) {
        delegate?.onShow(dialogInterface)
    }

    fun notifyOnDismiss(dialogInterface: DialogInterface) {
        delegate?.onDismiss(dialogInterface)
    }

    fun notifyOnStateChanged(newState: Int) {
        delegate?.onStateChanged(binding.root, newState)
    }

    fun notifyOnSlide(slideOffset: Float) {
        delegate?.onSlide(binding.root, slideOffset)
    }

    companion object {
        fun newInstance(): BottomTray = BottomTray()
        fun newInstance(
            title: String? = null,
            showDragHandle: Boolean = true,
            showFooter: Boolean = true,
            hasShadow: Boolean = true,
            hasStroke: Boolean = true
        ): BottomTray {
            return BottomTray().apply {
                title?.let { setTitle(it) }
                dragHandleVisibility = showDragHandle
                this.showFooter = showFooter
                this.hasShadow = hasShadow
                this.hasStroke = hasStroke
            }
        }
    }
}