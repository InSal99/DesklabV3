package com.edts.desklabv3.features

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.edts.desklabv3.R
import java.lang.RuntimeException

class AssetQRCodeReaderView: QRCodeReaderView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    var delegate: AssetQRCodeReaderViewDelegate? = null
    private var flash: Boolean = false
        set(value) {
            try {
                stopCamera()
                post {
                    try {
                        setTorchEnabled(value)
                        startCamera()
                        Log.d("QRReaderView", "Flash toggled: $value")
                    } catch (e: Exception) {
                        Log.e("QRReaderView", "Error toggling flash: ${e.message}")
                    }
                }

                delegate?.setFlash(value)
                field = value
            } catch (e: Exception) {
                Log.e("QRReaderView", "Error in flash setter: ${e.message}")
            }
        }

//    init {
//        try {
//            setQRDecodingEnabled(true)
//            setAutofocusInterval(5000L)
//            setTorchEnabled(flash)
//            setBackCamera()
//            Log.d("QRReaderView", "QRCodeReaderView initialized successfully")
//        } catch (e: Exception) {
//            Log.e("QRReaderView", "Error initializing QRCodeReaderView: ${e.message}")
//        }
//    }

    init {
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    startCamera()
                    Log.d("AssetQRCodeReaderView", "Camera started automatically in surfaceCreated")
                } catch (e: Exception) {
                    Log.e("AssetQRCodeReaderView", "Error starting camera: ${e.message}")
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) { }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                try {
                    stopCamera()
                    Log.d("AssetQRCodeReaderView", "Camera stopped in surfaceDestroyed")
                } catch (e: Exception) {
                    Log.e("AssetQRCodeReaderView", "Error stopping camera: ${e.message}")
                }
            }
        })
    }

    fun switchFlash(): Int {
        flash = !flash
        return if (!flash) {
            R.drawable.ic_flash
        } else {
            R.drawable.ic_flash_off
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        try {
            super.surfaceChanged(holder, format, width, height)
            Log.d("QRReaderView", "Surface changed successfully: ${width}x${height}")
        } catch (e: RuntimeException) {
            Log.e("QRReaderView", "Surface change error: ${e.message}")
            post {
                try {
                    stopCamera()
                    Thread.sleep(500)
                    startCamera()
                    Log.d("QRReaderView", "Camera restarted after surface change error")
                } catch (e2: Exception) {
                    Log.e("QRReaderView", "Failed to restart camera: ${e2.message}")
                }
            }
        }
    }

    override fun setTorchEnabled(enabled: Boolean) {
        try {
            super.setTorchEnabled(enabled)
            Log.d("QRReaderView", "Torch enabled: $enabled")
        } catch (e: RuntimeException) {
            Log.e("QRReaderView", "Error setting torch: ${e.message}")
        }
    }

    override fun startCamera() {
        try {
            super.startCamera()
            Log.d("QRReaderView", "Camera started")
        } catch (e: Exception) {
            Log.e("QRReaderView", "Error starting camera: ${e.message}")
        }
    }

    override fun stopCamera() {
        try {
            super.stopCamera()
            Log.d("QRReaderView", "Camera stopped")
        } catch (e: Exception) {
            Log.e("QRReaderView", "Error stopping camera: ${e.message}")
        }
    }
}