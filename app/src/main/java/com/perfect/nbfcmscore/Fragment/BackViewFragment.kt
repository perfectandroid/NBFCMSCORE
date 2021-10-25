package com.perfect.nbfcmscore.Fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*


class BackViewFragment : Fragment() {

    private var progressDialog: ProgressDialog? = null
    val TAG: String = "BackViewFragment"
    var cusid: String? = null
    var token: String? = null
    var vritualcardCombination: String? = null
    var bitmap: Bitmap? = null
    var txtv_addrs: TextView? = null
    var txtv_phone: TextView? = null
    val QRcodeWidth = 500
    var imgv_barcode: ImageView? = null
    var imgv_qrcode: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(
            R.layout.fragment_back_view, container,
            false
        )

        val FK_CustomerSP = activity!!.getSharedPreferences(Config.SHARED_PREF1, 0)
        cusid = FK_CustomerSP.getString("FK_Customer", null)

        val TokenSP = activity!!.getSharedPreferences(Config.SHARED_PREF8, 0)
        val token = TokenSP.getString("Token", null)

        txtv_addrs = v.findViewById<View>(R.id.txtv_addrs) as TextView?
        txtv_phone = v.findViewById<View>(R.id.txtv_phone) as TextView?


        imgv_barcode = v.findViewById<View>(R.id.imgv_barcode) as ImageView?
        imgv_qrcode = v.findViewById<View>(R.id.imgv_qrcode) as ImageView?

        val CusMobileSP = activity!!.getSharedPreferences(Config.SHARED_PREF2,0)
        val AddressSP = activity!!.getSharedPreferences(Config.SHARED_PREF4,0)

        txtv_addrs!!.text = AddressSP.getString("Address",null)
        txtv_phone!!.text = CusMobileSP.getString("CusMobile",null)

        getVritualcardCombination()

        return v

    }

    private fun getVritualcardCombination() {

        when(ConnectivityUtils.isConnected(activity!!)) {
            true -> {
                progressDialog = ProgressDialog(activity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(activity!!))
                        .hostnameVerifier(Config.getHostnameVerifier())
                        .build()
                    val gson = GsonBuilder()
                        .setLenient()
                        .create()
                    val retrofit = Retrofit.Builder()
                        .baseUrl(Config.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(client)
                        .build()
                    val apiService = retrofit.create(ApiInterface::class.java!!)
                    val requestObject1 = JSONObject()
                    try {
                        val TokenSP = activity!!.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("15"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(cusid))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))

                        Log.e(TAG,"requestObject1  434   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  2161   "+e.toString())
                        progressDialog!!.dismiss()
                        e.printStackTrace()
//                        val mySnackbar = Snackbar.make(
//                            findViewById(R.id.rl_main),
//                            " Some technical issues.", Snackbar.LENGTH_SHORT
//                        )
//                        mySnackbar.show()
                    }
                    val body = RequestBody.create(
                        okhttp3.MediaType.parse("application/json; charset=utf-8"),
                        requestObject1.toString()
                    )
                    val call = apiService.getBardCodeData(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e(TAG,"response  4342   "+response.body())
                                Log.e(TAG,"response  4343   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {
                                    val jobj = jObject.getJSONObject("BarcodeFormatDet")
                                    vritualcardCombination = jobj.getString("BarcodeFormat")
                                //    tv_vritualcard!!.text = vritualcardCombination

                                    generatebarcode()
                               //     generateqrcode()


                                } else {
//                                    val builder = AlertDialog.Builder(
//                                        activity,
//                                        R.style.MyDialogTheme
//                                    )
//                                    builder.setMessage("" + jObject.getString("EXMessage"))
//                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
//                                    }
//                                    val alertDialog: AlertDialog = builder.create()
//                                    alertDialog.setCancelable(false)
//                                    alertDialog.show()
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()
                                Log.e(TAG,"Some  2162   "+e.toString())
//                                val builder = AlertDialog.Builder(
//                                    this@RechargeActivity,
//                                    R.style.MyDialogTheme
//                                )
//                                builder.setMessage("Some technical issues.")
//                                builder.setPositiveButton("Ok") { dialogInterface, which ->
//                                }
//                                val alertDialog: AlertDialog = builder.create()
//                                alertDialog.setCancelable(false)
//                                alertDialog.show()
//                                e.printStackTrace()
                            }
                        }
                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()
                            Log.e(TAG,"Some  2163   "+t.message)
//                            val builder = AlertDialog.Builder(
//                                this@RechargeActivity,
//                                R.style.MyDialogTheme
//                            )
//                            builder.setMessage("Some technical issues.")
//                            builder.setPositiveButton("Ok") { dialogInterface, which ->
//                            }
//                            val alertDialog: AlertDialog = builder.create()
//                            alertDialog.setCancelable(false)
//                            alertDialog.show()
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    Log.e(TAG,"Some  2165   "+e.toString())
//                    val builder = AlertDialog.Builder(this@RechargeActivity, R.style.MyDialogTheme)
//                    builder.setMessage("Some technical issues.")
//                    builder.setPositiveButton("Ok") { dialogInterface, which ->
//                    }
//                    val alertDialog: AlertDialog = builder.create()
//                    alertDialog.setCancelable(false)
//                    alertDialog.show()
//                    e.printStackTrace()
                }
            }
            false -> {

                val builder = AlertDialog.Builder(activity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    private fun generateqrcode() {

//        val writer = QRCodeWriter()
//        try {
//            val bitMatrix = writer.encode(vritualcardCombination, BarcodeFormat.QR_CODE, 512, 512)
//            val width = bitMatrix.width
//            val height = bitMatrix.height
//            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
//            for (x in 0 until width) {
//                for (y in 0 until height) {
//                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
//                }
//            }
//            imgv_qrcode!!.setImageBitmap(bitmap)
//
////            (findViewById(R.id.img_result_qr) as ImageView).setImageBitmap(bmp)
//        } catch (e: WriterException) {
//            e.printStackTrace()
//        }
    }

    private fun generatebarcode() {
        try {


            bitmap = encodeAsBitmap(vritualcardCombination, BarcodeFormat.CODE_128, 600, 300);
//            iv.setImageBitmap(bitmap);
//            bitmap = TextToImageEncode(vritualcardCombination);

//            val encoder = BarcodeEncoder()
//            val bitmap: Bitmap =
//                encoder.encodeBitmap(vritualcardCombination, BarcodeFormat.CODE_128, 700, 200)
            imgv_barcode!!.setImageBitmap(bitmap)
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
        }
    }




    private val WHITE = -0x1
    private val BLACK = -0x1000000

    @Throws(WriterException::class)
    fun encodeAsBitmap(
        contents: String?,
        format: BarcodeFormat?,
        img_width: Int,
        img_height: Int
    ): Bitmap? {
        val contentsToEncode = contents ?: return null
        var hints: MutableMap<EncodeHintType?, Any?>? = null
        val encoding = guessAppropriateEncoding(contentsToEncode)
        if (encoding != null) {
            hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
            hints!![EncodeHintType.CHARACTER_SET] = encoding
        }
        val writer = MultiFormatWriter()
        val result: BitMatrix
        result = try {
            writer.encode(contentsToEncode, format, img_width, img_height, hints)
        } catch (iae: java.lang.IllegalArgumentException) {
            // Unsupported format
            return null
        }
        val width = result.width
        val height = result.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (result[x, y]) BLACK else WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(
            width, height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

    private fun guessAppropriateEncoding(contents: CharSequence): String? {
        // Very crude at the moment
        for (i in 0 until contents.length) {
            if (contents[i].toInt() > 0xFF) {
                return "UTF-8"
            }
        }
        return null
    }


}