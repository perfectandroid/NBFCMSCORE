package com.perfect.nbfcmscore.Activity

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.perfect.nbfcmscore.R

class Upi_share : AppCompatActivity() {
    var imageView: ImageView? = null
    val WIDTH = 400
    val HEIGHT = 400
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upi_share)
        getSupportActionBar()?.hide();
        setId()
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        try {
            val icon: Bitmap = encodeAsBitmap("nihalnihalpavithran@ohhdfcbank")
            imageView?.setImageBitmap(icon)
        } catch (ex: WriterException) {
            ex.printStackTrace()
        }
    }

    fun setId() {
       imageView = findViewById<ImageView>(R.id.imageView)
    }

    @Throws(WriterException::class)
    fun encodeAsBitmap(str: String): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(str, BarcodeFormat.QR_CODE, WIDTH, HEIGHT)
        val w = bitMatrix.width
        val h = bitMatrix.height
        val pixels = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                pixels[y * w + x] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
        return bitmap
    }
}