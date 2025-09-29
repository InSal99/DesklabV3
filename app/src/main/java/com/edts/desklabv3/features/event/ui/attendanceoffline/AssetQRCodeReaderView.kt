package com.edts.desklabv3.features.event.ui.attendanceoffline

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.edts.desklabv3.R
import java.lang.RuntimeException

class AssetQRCodeReaderView: QRCodeReaderView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    var delegate: AssetQRCodeReaderViewDelegate? = null
    private var flash: Boolean = false
        set(value) {
            setTorchEnabled(value)
            delegate?.setFlash(value)
            field = value
        }

    fun switchFlash(): Int {
        flash = !flash
        return if (!flash) {
            R.drawable.ic_flash
        } else {
            R.drawable.ic_flash_off
        }
    }

    override fun startCamera() {
        super.startCamera()
        try {
            super.startCamera()
        } catch (e: Exception) {
            Log.d("QRReaderView", "Error starting camera")
        }
    }

    override fun stopCamera() {
        try {
            super.stopCamera()
        } catch (e: Exception) {
            Log.d("QRReaderView", "Error stopping camera")
        }
    }

    override fun setTorchEnabled(enabled: Boolean) {
        try {
            super.setTorchEnabled(enabled)
        } catch (e: RuntimeException) {
            Log.d("QRReaderView", "Error enable Torch")
        }
    }
}