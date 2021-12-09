package com.perfect.nbfcmscore.Fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context.WINDOW_SERVICE
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
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


class BackViewFragment : Fragment() , OnClickListener{

    private var progressDialog: ProgressDialog? = null
    val TAG: String = "BackViewFragment"
    var cusid: String? = null
    var token: String? = null
    var vritualcardCombination: String? = null
    var bitmap: Bitmap? = null
    var bitmapqr: Bitmap? = null
    private var qrgEncoder: QRGEncoder? = null

    var txtv_addrs: TextView? = null
    var txtv_phone: TextView? = null
    val QRcodeWidth = 500
    var imgv_barcode: ImageView? = null
    var imgv_qrcode: ImageView? = null

    var txtv_purpose: TextView? = null
    var txtv_points1: TextView? = null
    var txtv_points2: TextView? = null
    var txtv_points3: TextView? = null
    var txtv_points4: TextView? = null
    var txtv_header: TextView? = null

    var ll_points1: LinearLayout? = null
    var ll_points2: LinearLayout? = null
    var ll_points3: LinearLayout? = null
    var ll_points4: LinearLayout? = null

    var txtv_nontransf: TextView? = null
    var txtv_plskpcard: TextView? = null
    var txtv_add: TextView? = null
    var txtv_ph: TextView? = null

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

        txtv_nontransf = v.findViewById<View>(R.id.txtv_nontransf) as TextView?
        txtv_plskpcard = v.findViewById<View>(R.id.txtv_plskpcard) as TextView?
        txtv_add = v.findViewById<View>(R.id.txtv_add) as TextView?
        txtv_ph = v.findViewById<View>(R.id.txtv_ph) as TextView?

        val NontransfSP = context!!.getSharedPreferences(Config.SHARED_PREF290, 0)
        txtv_nontransf!!.setText(NontransfSP.getString("NotTransferable", null))

        val KpcardSP = context!!.getSharedPreferences(Config.SHARED_PREF291, 0)
        txtv_plskpcard!!.setText(KpcardSP.getString("PleaseKeepYourCardConfidential", null))

        val PhSP = context!!.getSharedPreferences(Config.SHARED_PREF292, 0)
        txtv_ph!!.setText(PhSP.getString("Phone", null))

        val AddrsSP = context!!.getSharedPreferences(Config.SHARED_PREF4, 0)
        txtv_add!!.setText(AddrsSP.getString("Address", null))


        txtv_purpose = v.findViewById<View>(R.id.txtv_purpose) as TextView?
        txtv_points1 = v.findViewById<View>(R.id.txtv_points1) as TextView?
        txtv_points2 = v.findViewById<View>(R.id.txtv_points2) as TextView?
        txtv_points3 = v.findViewById<View>(R.id.txtv_points3) as TextView?
        txtv_points4 = v.findViewById<View>(R.id.txtv_points4) as TextView?
        txtv_header= v.findViewById<View>(R.id.txtv_header) as TextView?

        ll_points1 = v.findViewById<View>(R.id.ll_points1) as LinearLayout?
        ll_points2 = v.findViewById<View>(R.id.ll_points2) as LinearLayout?
        ll_points3 = v.findViewById<View>(R.id.ll_points3) as LinearLayout?
        ll_points4 = v.findViewById<View>(R.id.ll_points4) as LinearLayout?


        //txtv_purpose!!.setText(R.string.purpose)
        txtv_purpose!!.visibility = View.VISIBLE
        txtv_points1!!.visibility = View.VISIBLE
        txtv_points2!!.visibility = View.VISIBLE
        txtv_points3!!.visibility = View.VISIBLE
        txtv_points4!!.visibility = View.VISIBLE

        ll_points1!!.visibility = View.VISIBLE
        ll_points2!!.visibility = View.VISIBLE
        ll_points3!!.visibility = View.VISIBLE
        ll_points4!!.visibility = View.VISIBLE



        imgv_barcode = v.findViewById<View>(R.id.imgv_barcode) as ImageView?
        imgv_qrcode = v.findViewById<View>(R.id.imgv_qrcode) as ImageView?

        imgv_barcode!!.setOnClickListener(this)
        imgv_qrcode!!.setOnClickListener(this)

        val CusMobileSP = activity!!.getSharedPreferences(Config.SHARED_PREF2,0)
        val AddressSP = activity!!.getSharedPreferences(Config.SHARED_PREF4,0)

        txtv_addrs!!.text = AddressSP.getString("Address",null)
        txtv_phone!!.text = CusMobileSP.getString("CusMobile",null)

        val ID_Virtual = activity!!.getSharedPreferences(Config.SHARED_PREF74,0)
        val ID_Purpose = activity!!.getSharedPreferences(Config.SHARED_PREF105,0)
        txtv_header!!.setText(ID_Virtual.getString("VirtualCard",null))
        txtv_purpose!!.setText(ID_Purpose.getString("PurposeofVirtualCard",null))

        val FK_p1SP = activity!!.getSharedPreferences(Config.SHARED_PREF271, 0)
        txtv_points1!!.setText(FK_p1SP.getString("Streamlinetransactions",null))

        val FK_p2SP = activity!!.getSharedPreferences(Config.SHARED_PREF272, 0)
        txtv_points2!!.setText(FK_p2SP.getString("Enableasinglepointofcontactforcreditanddebit",null))

        val FK_p3SP = activity!!.getSharedPreferences(Config.SHARED_PREF273, 0)
        txtv_points3!!.setText(FK_p3SP.getString("Strengthenyourloanportfolio",null))

        val FK_p4SP = activity!!.getSharedPreferences(Config.SHARED_PREF274, 0)
        txtv_points4!!.setText(FK_p4SP.getString("Eliminatethelongqueues",null))



        getVritualcardCombination()

        return v

    }

    private fun getVritualcardCombination() {

        val baseurlSP = context!!.applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
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
                        .baseUrl(baseurl)
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
                                    generateqrcode()


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

        try {

//            val multiFormatWriter = MultiFormatWriter()
//            val bitMatrix = multiFormatWriter.encode(vritualcardCombination, BarcodeFormat.QR_CODE, 200, 200)
//            val barcodeEncoder = BarcodeEncoder()
//            val bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)
//
//            imgv_qrcode!!.setImageBitmap(bitmap)

            val manager = activity!!.getSystemService(WINDOW_SERVICE) as WindowManager?
            val display = manager!!.defaultDisplay
            val point = Point()
            display.getSize(point)
            val width: Int = point.x
            val height: Int = point.y
            var smallerDimension = if (width < height) width else height
            smallerDimension = smallerDimension * 3 / 4

            qrgEncoder = QRGEncoder(
                vritualcardCombination, null,
                QRGContents.Type.TEXT,
                smallerDimension
            )
            qrgEncoder!!.setColorBlack(Color.BLACK)
            qrgEncoder!!.setColorWhite(Color.WHITE)
            try {
                bitmapqr = qrgEncoder!!.getBitmap()
                imgv_qrcode!!.setImageBitmap(bitmapqr)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }



        }catch (e : WriterException) {
            e.printStackTrace();
        }

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

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgv_qrcode -> {
                if (bitmapqr != null){
                    showBarQrAlert(bitmapqr,"QR")
                }
            }
            R.id.imgv_barcode -> {
                if (bitmap != null){
                    showBarQrAlert(bitmap,"BAR")
                }
            }
        }
    }

    private fun showBarQrAlert(bitmapqrbar: Bitmap?, s: String) {

        val dialogBuilder = AlertDialog.Builder(activity)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.alert_barqr_code, null)
        dialogBuilder.setView(dialogView)
        val b = dialogBuilder.create()
        b.setCancelable(false)

        val img_barcode = dialogView.findViewById<ImageView>(R.id.img_barcode)
        val tv_scan_header = dialogView.findViewById<TextView>(R.id.tv_scan_header)
        val img_close = dialogView.findViewById<ImageView>(R.id.img_close)
        if(s.equals("QR")){
            tv_scan_header.text = "Scan QR Code"
        }
        if(s.equals("BAR")){
            tv_scan_header.text = "Scan Bar Code"
        }
        img_barcode.setImageBitmap(bitmapqrbar)


        img_close.setOnClickListener {
            b.dismiss()
        }


        b.show()

    }


}