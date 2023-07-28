package com.perfect.nbfcmscore.Activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.gms.vision.text.TextRecognizer
import com.perfect.nbfcmscore.Helper.IdleUtil
import com.perfect.nbfcmscore.R
import java.io.IOException

class FundTransferScan : AppCompatActivity() {
    private var context: Context? = null
    private var L1: LinearLayout? = null
    private var cameraSource: CameraSource? = null
    private var barcodeDetector: BarcodeDetector? = null
    private var surfaceView: SurfaceView? = null
    private val REQUEST_CAMERA_PERMISSION = 200
    private val CAMERA_REQUEST = 101
    private lateinit var qrcode: SparseArray<Barcode>
    private var value1: String? = null
    private var phase: kotlin.String? = null
    private val user_id = 0
    private var holder1: SurfaceHolder? = null
    private lateinit var img_barcode: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_main)
        L1 = findViewById(R.id.L1)
        context = this@FundTransferScan
        surfaceView = findViewById(R.id.surfaceview)
        img_barcode = findViewById(R.id.image_barcode)
        phase = intent.getStringExtra("phase")
        Glide.with(this).asGif().load(R.raw.scan).into(img_barcode)
        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS).build()
        val textRecognizer = TextRecognizer.Builder(applicationContext).build()

        cameraSource = CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(640, 480)
            .setAutoFocusEnabled(true)
            .build()
        surfaceView?.getHolder()?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                holder1 = holder
                if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@FundTransferScan,
                        arrayOf<String>(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        REQUEST_CAMERA_PERMISSION
                    )
                } else {
                    try {
                        cameraSource?.start(holder)
                        Log.v("fgfdg", "ca$cameraSource")
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Log.v("fgfdg", "e$e")
                    }
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                Log.v("fgfdg", "ca" + "stop1")
                cameraSource?.stop()
            }
        })

        barcodeDetector?.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}
            override fun receiveDetections(detections: Detections<Barcode>) {
                qrcode = detections.detectedItems
                if (qrcode?.size() != 0) {
                    try {
                        value1 = qrcode?.valueAt(0)!!.displayValue
                        Log.v("dfsdfdsfd", "value $value1")
                        if (value1 == "") {
                        }
                        val handler = Handler(Looper.getMainLooper())
                        handler.post {
                            if (cameraSource != null) {
                                cameraSource!!.release()
                                cameraSource = null
                            }
                            Log.v("dfsdfdddd", "val " + value1)
                            val intent = Intent()
                            intent.putExtra("Value", value1)
                            setResult(200, intent)
                            finish()
                            // insert(value1);
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                try {
                    cameraSource!!.start(holder1)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        IdleUtil.startLogoutTimer(this, this)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        IdleUtil.startLogoutTimer(this, this)
    }
}