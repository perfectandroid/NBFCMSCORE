package com.perfect.nbfcmscore.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.*
import com.perfect.nbfcmscore.Helper.Config.getSSLSocketFactory
import com.perfect.nbfcmscore.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


class SplashActivity : AppCompatActivity() {
    val TAG: String = "SplashActivity"

    private var progressDialog: ProgressDialog? = null
    var tv_error_message: TextView? = null
    var btn_proceed: Button? = null
    var imglogo: ImageView? = null
    var CommonApp: String = ""

    companion object {
        ///////Development
        public val BASE_URL = "https://202.164.150.65:15006/NbfcAndroidAPI/api/"
        public val IMAGE_URL = "https://202.164.150.65:15006/NbfcAndroidAPI/"
        public val DOWNLOAD_URL = "https://202.164.150.65:15006/"
        public val BankKey = "-500"
        public val BankHeader = "PERFECT NBFC BANK HEAD OFFICE"
        public val CERT_NAME = "staticvm.pem"
        public val CERT_NAME_TEST = "nbfctest.pem"

        ///////QA
//        public val BASE_URL = "https://112.133.227.123:14010/NBFCAndroidAPI/api/"  //QA  08.07.2022
//        public val IMAGE_URL = "https://112.133.227.123:14010/NBFCAndroidAPI/"
//        public val DOWNLOAD_URL = "https://112.133.227.123:14010/"
//        public val BankKey = "-500"
//        public val BankHeader = "PERFECT NBFC BANK HEAD OFFICE"
//        public val CERT_NAME = "staticvm1.pem"  //QA
//        public val CERT_NAME_TEST = "nbfctest.pem"  //QA


///////NBFC COMMON
//        public val BASE_URL  = "https://112.133.227.123:14422/NBFCANDROIDAPI/api/"
//        public val IMAGE_URL = "https://112.133.227.123:14422/NBFCANDROIDAPI/"
//        public val DOWNLOAD_URL = "https://112.133.227.123:14422/"
//        public val BankKey = "500"
//        public val BankHeader = "PERFECT NBFC BANK HEAD OFFICE"
//        public val CERT_NAME = "nbfccommon.pem"
//        public val CERT_NAME_TEST = "nbfctest.pem"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
        //  doSplash()

        Log.e("BASE_URL", "BASE_URL  49    " + BASE_URL)

        tv_error_message = findViewById<TextView>(R.id.tv_error_message)
        btn_proceed = findViewById<Button>(R.id.btn_proceed)
//        btn_proceed!!.setOnClickListener {
//
//            startUserregistrationActivity()
//        //    Handler().postDelayed({ this@SplashActivity.startUserregistrationActivity() }, 1)
//        }
        btn_proceed!!.setOnClickListener(View.OnClickListener {

            Log.e(TAG, "btn_proceed   " + 97)
            //  btn_proceed!!.isEnabled = false
            startUserregistrationActivity()
        })


        val ID_CommonApp = applicationContext.getSharedPreferences(Config.SHARED_PREF345, 0)
        var chkstatus = ID_CommonApp.getString("nidhicheck", "")


        if (chkstatus.equals("")) {
            getnidhicheck()

        } else if (chkstatus.equals("true") || chkstatus.equals("false")) {
            getResellerDetails()
        }


        //   getResellerDetails()
        val imgSplash: ImageView = findViewById(R.id.imsplashlogo)
        val imglogo: ImageView = findViewById(R.id.imglogo)
        Glide.with(this).load(R.drawable.splashgif).into(imgSplash)
        try {
            val AppIconImageCodeSP =
                applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
            val imagepath = IMAGE_URL + AppIconImageCodeSP!!.getString("AppIconImageCode", null)
            PicassoTrustAll.getInstance(this@SplashActivity)!!.load(imagepath)
                .error(android.R.color.transparent).into(imglogo!!)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun getnidhicheck() {
        val certificateSP = applicationContext.getSharedPreferences(Config.SHARED_PREF164, 0)
        val certificateSPEditer = certificateSP.edit()
        certificateSPEditer.putString("sslcertificate", CERT_NAME)
        certificateSPEditer.commit()

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurlSPEditer = baseurlSP.edit()
        baseurlSPEditer.putString("baseurl", BASE_URL)
        baseurlSPEditer.commit()

        val baseUrlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseUrlSP.getString("baseurl", null)

        when (ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@SplashActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val trustManagerFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm()
                    )
                    trustManagerFactory.init(null as KeyStore?)
                    val trustManagers = trustManagerFactory.trustManagers
                    check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                        ("Unexpected default trust managers:"
                                + Arrays.toString(trustManagers))
                    }
                    val trustManager = trustManagers[0] as X509TrustManager
                    val client: OkHttpClient = okhttp3.OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .sslSocketFactory(getSSLSocketFactory(this), trustManager)
                        .hostnameVerifier(Config.getHostnameVerifier())
                        .build()
                    val gson = GsonBuilder()
                        .setLenient()
                        .create()
                    val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(client)
                        .build()
                    val apiService = retrofit.create(ApiInterface::class.java!!)
                    val requestObject1 = JSONObject()
                    try {

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(
                            Config.SHARED_PREF1,
                            0
                        )
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = this!!.applicationContext.getSharedPreferences(
                            Config.SHARED_PREF8,
                            0
                        )
                        val Token = TokenSP.getString("Token", null)
                        val BankKeySP =
                            applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP =
                            applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        //  requestObject1.put("Reqmode", MscoreApplication.encryptStart("40"))
                        // requestObject1.put("Token", MscoreApplication.encryptStart(Token))


                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKey))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeader))


                        Log.e("TAG", "requestObject1  commonappchk   " + requestObject1)
                        Log.v("fdsfsdfddd", "requestObject1 " + requestObject1)
                    } catch (e: Exception) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        AlertMessage().alertMessage(
                            this@SplashActivity,
                            this@SplashActivity,
                            "Alert",
                            "Some technical issues.",
                            1
                        );
                    }
                    val body = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getCommonAppchkng(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            Log.v("fdsfsdfddd", "response " + response.body().toString())
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-commonappchk", response.body().toString())
                                if (jObject.getString("StatusCode") == "0") {

                                    val status = jObject.getString("CommonApp")
                                    CommonApp = jObject.getString("CommonApp") as String

                                    if (status!!.equals("true")) {
                                        nidhicodepopup()
                                    }


//                                        val ID_status= this@SplashActivity.getSharedPreferences(Config.SHARED_PREF345, 0)
//                                        val ID_statusEditer = ID_status.edit()
//                                        ID_statusEditer.putString("nidhicheck", jObject.getString("CommonApp") as String)
//                                        ID_statusEditer.commit()


                                    /*   val jsonObj1: JSONObject =
                                           jObject.getJSONObject("Addnewsender")
                                       val jsonobj2 = JSONObject(jsonObj1.toString())

                                       var message = jsonobj2.getString("message")
                                       var status = jsonobj2.getString("Status")
                                       var senderid = jsonobj2.getString("ID_Sender")
                                       var receiverid = jsonobj2.getString("ID_Receiver")
                                       var otpRefNo = jsonobj2.getString("otpRefNo")
                                       var statuscode = jsonobj2.getString("StatusCode")

                                       arrayList2 = ArrayList<SenderReceiver>()
                                       arrayList2!!.add(
                                           SenderReceiver(
                                           message, status, senderid, receiverid,otpRefNo,statuscode
                                       )
                                       )

                                       alertMessage1(status, message)
   */

                                } else {
                                    AlertMessage().alertMessage(
                                        this@SplashActivity,
                                        this@SplashActivity,
                                        "Alert",
                                        jObject.getString("EXMessage"),
                                        1
                                    );
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                AlertMessage().alertMessage(
                                    this@SplashActivity,
                                    this@SplashActivity,
                                    "Alert",
                                    "Some technical issues.",
                                    1
                                );
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()

                            AlertMessage().alertMessage(
                                this@SplashActivity,
                                this@SplashActivity,
                                "Alert",
                                "Some technical issues.",
                                1
                            );
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    AlertMessage().alertMessage(
                        this@SplashActivity,
                        this@SplashActivity,
                        "Alert",
                        "Some technical issues.",
                        1
                    );
                    e.printStackTrace()
                }
            }
            false -> {
                AlertMessage().alertMessage(
                    this@SplashActivity,
                    this@SplashActivity,
                    "Alert",
                    "No Internet Connection.",
                    3
                );
            }
        }
    }

//    private fun nidhicodepopup() {
//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setContentView(R.layout.popup_nidhi)
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//
//
//        val etxt_nidhicode: EditText = dialog.findViewById<EditText>(R.id.etxt_nidhicode)
//        val btn_submit: Button = dialog.findViewById<Button>(R.id.btn_submit)
//        val btn_resend: Button = dialog.findViewById<Button>(R.id.btn_resend)
//        val idImgV1: ImageView = dialog.findViewById<ImageView>(R.id.idImgV1)
//        Glide.with(this).load(R.drawable.otpgif).into(idImgV1)
//        btn_submit.setOnClickListener {
//            if(!etxt_nidhicode!!.text.toString().equals("")){
//                var nidhicode = etxt_nidhicode!!.text.toString()
//
//                dialog.dismiss()
//                getnidhicode(nidhicode)
//
//            }else {
//                Toast.makeText(applicationContext,"Enter Valid Code", Toast.LENGTH_LONG).show()
//            }
//        }
//
//        dialog.show()
//
//    }


    private fun nidhicodepopup() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.popup_nidhi)
        val etxt_nidhicode: EditText? =
            bottomSheetDialog.findViewById<EditText>(R.id.etxt_nidhicode)
        val btn_submit: TextView? = bottomSheetDialog.findViewById<TextView>(R.id.btn_submit)
        val btn_resend: Button? = bottomSheetDialog.findViewById<Button>(R.id.btn_resend)
        val idImgV1: ImageView? = bottomSheetDialog.findViewById<ImageView>(R.id.idImgV1)
        // Glide.with(this).load(R.drawable.otpgif).into(idImgV1)
        btn_submit?.setOnClickListener {
            if (!etxt_nidhicode!!.text.toString().equals("")) {
                var nidhicode = etxt_nidhicode!!.text.toString()
                bottomSheetDialog.dismiss()
                getnidhicode(nidhicode)
            } else {
                Toast.makeText(applicationContext, "Enter Valid Code", Toast.LENGTH_LONG).show()
            }
        }

        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.show()
    }

    private fun getnidhicode(nidhicode: String) {


        /*    val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
            val baseurl = baseurlSP.getString("baseurl", null)*/
        when (ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@SplashActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val trustManagerFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm()
                    )
                    trustManagerFactory.init(null as KeyStore?)
                    val trustManagers = trustManagerFactory.trustManagers
                    check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                        ("Unexpected default trust managers:"
                                + Arrays.toString(trustManagers))
                    }
                    val trustManager = trustManagers[0] as X509TrustManager
                    val client: OkHttpClient = okhttp3.OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .sslSocketFactory(getSSLSocketFactory(this), trustManager)
                        .hostnameVerifier(Config.getHostnameVerifier())
                        .build()
                    val gson = GsonBuilder()
                        .setLenient()
                        .create()
                    val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(client)
                        .build()
                    val apiService = retrofit.create(ApiInterface::class.java!!)
                    val requestObject1 = JSONObject()
                    try {

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(
                            Config.SHARED_PREF1,
                            0
                        )
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = this!!.applicationContext.getSharedPreferences(
                            Config.SHARED_PREF8,
                            0
                        )
                        val Token = TokenSP.getString("Token", null)
                        val BankKeySP =
                            applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP =
                            applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        //  requestObject1.put("Reqmode", MscoreApplication.encryptStart("40"))
                        // requestObject1.put("Token", MscoreApplication.encryptStart(Token))

                        requestObject1.put("nCode", MscoreApplication.encryptStart(nidhicode))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKey))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeader))


                        Log.e("TAG", "requestObject1  NIDHICODE   " + requestObject1)
                    } catch (e: Exception) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        AlertMessage().alertMessage(
                            this@SplashActivity,
                            this@SplashActivity,
                            "Alert",
                            "Some technical issues.",
                            1
                        );
                    }
                    val body = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getNidhicode(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-NIDHICODE", response.body().toString())
                                if (jObject.getString("StatusCode") == "0") {

                                    var nidhicode = jObject.getString("NidhiCode")

                                    val ID_status = this@SplashActivity.getSharedPreferences(
                                        Config.SHARED_PREF346,
                                        0
                                    )
                                    val ID_statusEditer = ID_status.edit()
                                    ID_statusEditer.putString(
                                        "nidhicode",
                                        jObject.get("NidhiCode") as String
                                    )
                                    ID_statusEditer.commit()


                                    val ID_CommonApp = this@SplashActivity.getSharedPreferences(
                                        Config.SHARED_PREF345,
                                        0
                                    )
                                    val ID_CommonAppEditer = ID_CommonApp.edit()
                                    ID_CommonAppEditer.putString("nidhicheck", CommonApp)
                                    ID_CommonAppEditer.commit()


                                    getResellerDetails()


                                    /*   val jsonObj1: JSONObject =
                                           jObject.getJSONObject("Addnewsender")
                                       val jsonobj2 = JSONObject(jsonObj1.toString())

                                       var message = jsonobj2.getString("message")
                                       var status = jsonobj2.getString("Status")
                                       var senderid = jsonobj2.getString("ID_Sender")
                                       var receiverid = jsonobj2.getString("ID_Receiver")
                                       var otpRefNo = jsonobj2.getString("otpRefNo")
                                       var statuscode = jsonobj2.getString("StatusCode")

                                       arrayList2 = ArrayList<SenderReceiver>()
                                       arrayList2!!.add(
                                           SenderReceiver(
                                           message, status, senderid, receiverid,otpRefNo,statuscode
                                       )
                                       )

                                       alertMessage1(status, message)
   */

                                } else {
                                    alertMessage("Alert", jObject.getString("EXMessage"), 1);

                                }

                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                AlertMessage().alertMessage(
                                    this@SplashActivity,
                                    this@SplashActivity,
                                    "Alert",
                                    "Some technical issues.",
                                    1
                                );
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()

                            AlertMessage().alertMessage(
                                this@SplashActivity,
                                this@SplashActivity,
                                "Alert",
                                "Some technical issues.",
                                1
                            );
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    AlertMessage().alertMessage(
                        this@SplashActivity,
                        this@SplashActivity,
                        "Alert",
                        "Some technical issues.",
                        1
                    );
                    e.printStackTrace()
                }
            }
            false -> {
                AlertMessage().alertMessage(
                    this@SplashActivity,
                    this@SplashActivity,
                    "Alert",
                    "No Internet Connection.",
                    3
                );
            }
        }
    }

    private fun doSplash() {
        val background = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep((4 * 1000).toLong())
                    // btn_proceed!!.isEnabled = true
                    val Loginpref = applicationContext.getSharedPreferences(Config.SHARED_PREF, 0)
                    if (Loginpref.getString("loginsession", null) == null) {
                        val i = Intent(this@SplashActivity, WelcomeSliderActivity::class.java)
                        startActivity(i)
                        finish()
                    } else if (Loginpref.getString(
                            "loginsession",
                            null
                        ) != null && !Loginpref.getString(
                            "loginsession",
                            null
                        )!!.isEmpty() && Loginpref.getString("loginsession", null) == "Yes"
                    ) {
                        val i = Intent(this@SplashActivity, MpinActivity::class.java)
                        startActivity(i)
                        finish()
                    } else if (Loginpref.getString(
                            "loginsession",
                            null
                        ) != null && !Loginpref.getString(
                            "loginsession",
                            null
                        )!!.isEmpty() && Loginpref.getString("loginsession", null) == "No"
                    ) {

                        val i = Intent(this@SplashActivity, WelcomeActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        val i = Intent(this@SplashActivity, WelcomeSliderActivity::class.java)
                        startActivity(i)
                        finish()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }

    private fun getResellerDetails() {
        /*   val certificateSP = applicationContext.getSharedPreferences(Config.SHARED_PREF164, 0)
           val certificateSPEditer = certificateSP.edit()
           certificateSPEditer.putString("sslcertificate", CERT_NAME)
           certificateSPEditer.commit()
           val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
           val baseurlSPEditer = baseurlSP.edit()
           baseurlSPEditer.putString("baseurl", BASE_URL)
           baseurlSPEditer.commit()*/
        val baseUrlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseUrlSP.getString("baseurl", null)
        when (ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@SplashActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val trustManagerFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm()
                    )
                    trustManagerFactory.init(null as KeyStore?)
                    val trustManagers = trustManagerFactory.trustManagers
                    check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                        ("Unexpected default trust managers:"
                                + Arrays.toString(trustManagers))
                    }
                    val trustManager = trustManagers[0] as X509TrustManager
                    val client: OkHttpClient = okhttp3.OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .sslSocketFactory(getSSLSocketFactory(this), trustManager)
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
                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("5"))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKey))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeader))

//                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
//                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))
                        Log.e(TAG, "requestObject1 Reseller 10001   " + requestObject1)

                    } catch (e: Exception) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        AlertMessage().alertMessage(
                            this@SplashActivity,
                            this@SplashActivity,
                            "Alert",
                            "Some technical issues.",
                            1
                        );
                    }
                    val body = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getResellerDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                Log.e("response", "response  1131   " + response.body())
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jobjt = jObject.getJSONObject("ResellerDetails")
                                    val jArray: JSONArray = jobjt.getJSONArray("MenuList")

                                    for (i in 0 until jArray!!.length()) {

                                        val json_data = jArray.getJSONObject(i)
                                        var rchrg = json_data.getString("Recharge")
                                        var imp = json_data.getString("Imps")
                                        var rtg = json_data.getString("Rtgs")
                                        var ksb = json_data.getString("Kseb")
                                        var nft = json_data.getString("Neft")
                                        var qpy = json_data.getString("QuickPay")


                                        val LicenceImpsSP = applicationContext.getSharedPreferences(
                                            Config.SHARED_PREF293,
                                            0
                                        )
                                        val LicenceImpsEditer = LicenceImpsSP.edit()
                                        LicenceImpsEditer.putString("LicenceImps", imp)
                                        LicenceImpsEditer.commit()

                                        val LicenceNeftSP = applicationContext.getSharedPreferences(
                                            Config.SHARED_PREF295,
                                            0
                                        )
                                        val LicenceNeftEditer = LicenceNeftSP.edit()
                                        LicenceNeftEditer.putString("LicenceNeft", nft)
                                        LicenceNeftEditer.commit()

                                        val LicenceRtgsSP = applicationContext.getSharedPreferences(
                                            Config.SHARED_PREF298,
                                            0
                                        )
                                        val LicenceRtgsEditer = LicenceRtgsSP.edit()
                                        LicenceRtgsEditer.putString("LicenceRtgs", rtg)
                                        LicenceRtgsEditer.commit()


                                        val LicenceQuickPaySP =
                                            applicationContext.getSharedPreferences(
                                                Config.SHARED_PREF296,
                                                0
                                            )
                                        val LicenceQuickPayEditer = LicenceQuickPaySP.edit()
                                        LicenceQuickPayEditer.putString("LicenceQuickPay", qpy)
                                        LicenceQuickPayEditer.commit()

                                        val LicenceKsebSP = applicationContext.getSharedPreferences(
                                            Config.SHARED_PREF294,
                                            0
                                        )
                                        val LicenceKsebEditer = LicenceKsebSP.edit()
                                        LicenceKsebEditer.putString("LicenceKseb", ksb)
                                        LicenceKsebEditer.commit()

                                        val LicenceRechargeSP =
                                            applicationContext.getSharedPreferences(
                                                Config.SHARED_PREF297,
                                                0
                                            )
                                        val LicenceRechargeEditer = LicenceRechargeSP.edit()
                                        LicenceRechargeEditer.putString("LicenceRecharge", rchrg)
                                        LicenceRechargeEditer.commit()


                                        Log.i(
                                            "Rchrgs",
                                            rchrg + "\n" + imp + "\n" + rtg + "\n" + ksb + "\n" + nft + "\n" + qpy
                                        )
                                    }


                                    val AppStoreLinkSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF10,
                                        0
                                    )
                                    val AppStoreLinkEditer = AppStoreLinkSP.edit()
                                    AppStoreLinkEditer.putString(
                                        "AppStoreLink",
                                        jobjt.getString("AppStoreLink")
                                    )
                                    AppStoreLinkEditer.commit()

                                    val PlayStoreLinkSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF11,
                                        0
                                    )
                                    val PlayStoreLinkEditer = PlayStoreLinkSP.edit()
                                    PlayStoreLinkEditer.putString(
                                        "PlayStoreLink",
                                        jobjt.getString("PlayStoreLink")
                                    )
                                    PlayStoreLinkEditer.commit()

                                    val ProductNameSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF12,
                                        0
                                    )
                                    val ProductNameEditer = ProductNameSP.edit()
                                    ProductNameEditer.putString(
                                        "ProductName",
                                        jobjt.getString("ProductName")
                                    )
                                    ProductNameEditer.commit()

                                    val CompanyLogoImageCodeSP =
                                        applicationContext.getSharedPreferences(
                                            Config.SHARED_PREF13,
                                            0
                                        )
                                    val CompanyLogoImageCodeEditer = CompanyLogoImageCodeSP.edit()
                                    CompanyLogoImageCodeEditer.putString(
                                        "CompanyLogoImageCode",
                                        jobjt.getString("CompanyLogoImageCode")
                                    )
                                    CompanyLogoImageCodeEditer.commit()

                                    val AppIconImageCodeSP =
                                        applicationContext.getSharedPreferences(
                                            Config.SHARED_PREF14,
                                            0
                                        )
                                    val AppIconImageCodeEditer = AppIconImageCodeSP.edit()
                                    AppIconImageCodeEditer.putString(
                                        "AppIconImageCode",
                                        jobjt.getString("AppIconImageCode")
                                    )
                                    AppIconImageCodeEditer.commit()


                                    setImage()

                                    val ResellerNameSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF15,
                                        0
                                    )
                                    val ResellerNameEditer = ResellerNameSP.edit()
                                    ResellerNameEditer.putString(
                                        "ResellerName",
                                        jobjt.getString("ResellerName")
                                    )
                                    ResellerNameEditer.commit()

                                    val ContactNumberSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF30,
                                        0
                                    )
                                    val ContactNumberEditer = ContactNumberSP.edit()
                                    ContactNumberEditer.putString(
                                        "ContactNumber",
                                        jobjt.getString("ContactNumber")
                                    )
                                    ContactNumberEditer.commit()

                                    val ContactEmailSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF31,
                                        0
                                    )
                                    val ContactEmailEditer = ContactEmailSP.edit()
                                    ContactEmailEditer.putString(
                                        "ContactEmail",
                                        jobjt.getString("ContactEmail")
                                    )
                                    ContactEmailEditer.commit()

                                    val ContactAddressSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF32,
                                        0
                                    )
                                    val ContactAddressEditer = ContactAddressSP.edit()
                                    ContactAddressEditer.putString(
                                        "ContactAddress",
                                        jobjt.getString("ContactAddress")
                                    )
                                    ContactAddressEditer.commit()

                                    val IsNBFCSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF33,
                                        0
                                    )
                                    val IsNBFCEditer = IsNBFCSP.edit()
                                    IsNBFCEditer.putString("IsNBFC", jobjt.getString("IsNBFC"))
                                    IsNBFCEditer.commit()

                                    val baseurlSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF163,
                                        0
                                    )
                                    val baseurlSPEditer = baseurlSP.edit()
                                    baseurlSPEditer.putString("baseurl", BASE_URL)
                                    baseurlSPEditer.commit()
                                    val ImageURLSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF165,
                                        0
                                    )
                                    val ImageURLSPEditer = ImageURLSP.edit()
                                    ImageURLSPEditer.putString("ImageURL", IMAGE_URL)
                                    ImageURLSPEditer.commit()

//                                    val m_androidId = Secure.getString(contentResolver, Secure.ANDROID_ID)
//                                    if (m_androidId.equals(jobjt.getString("TestingMachineId"))) {
//                                        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
//                                        val baseurlSPEditer = baseurlSP.edit()
//                                        baseurlSPEditer.putString("baseurl", jobjt.getString("TestingURL"))
//                                        baseurlSPEditer.commit()
//                                        val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
//                                        val ImageURLSPEditer = ImageURLSP.edit()
//                                        ImageURLSPEditer.putString("ImageURL", jobjt.getString("TestingImageURL"))
//                                        ImageURLSPEditer.commit()
//                                    } else {
//                                        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
//                                        val baseurlSPEditer = baseurlSP.edit()
//                                        baseurlSPEditer.putString("baseurl", BASE_URL)
//                                        baseurlSPEditer.commit()
//                                        val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
//                                        val ImageURLSPEditer = ImageURLSP.edit()
//                                        ImageURLSPEditer.putString("ImageURL", IMAGE_URL)
//                                        ImageURLSPEditer.commit()
//                                    }

                                    val certificateSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF164,
                                        0
                                    )
                                    val certificateSPEditer = certificateSP.edit()
                                    certificateSPEditer.putString("sslcertificate", CERT_NAME)
                                    certificateSPEditer.commit()


                                    val TestingMobileNoSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF311,
                                        0
                                    )
                                    val TestingMobileNoEditer = TestingMobileNoSP.edit()
                                    TestingMobileNoEditer.putString(
                                        "TestingMobileNo",
                                        jobjt.getString("TestingMobileNo")
                                    )
                                    TestingMobileNoEditer.commit()

                                    val BankKeySP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF312,
                                        0
                                    )
                                    val BankKeyEditer = BankKeySP.edit()
                                    BankKeyEditer.putString("BankKey", BankKey)
                                    BankKeyEditer.commit()

                                    val BankHeaderSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF313,
                                        0
                                    )
                                    val BankHeaderEditer = BankHeaderSP.edit()
                                    BankHeaderEditer.putString("BankHeader", BankHeader)
                                    BankHeaderEditer.commit()

                                    val CertificateStatusSP =
                                        applicationContext.getSharedPreferences(
                                            Config.SHARED_PREF314,
                                            0
                                        )
                                    val CertificateStatusEditer = CertificateStatusSP.edit()
                                    CertificateStatusEditer.putString(
                                        "CertificateStatus",
                                        jobjt.getString("CertificateStatus")
                                    )
                                    CertificateStatusEditer.commit()

                                    val TestingURLSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF315,
                                        0
                                    )
                                    val TestingURLEditer = TestingURLSP.edit()
                                    TestingURLEditer.putString(
                                        "TestingURL",
                                        jobjt.getString("TestingURL")
                                    )
                                    TestingURLEditer.commit()

                                    val TestingMachineIdSP =
                                        applicationContext.getSharedPreferences(
                                            Config.SHARED_PREF316,
                                            0
                                        )
                                    val TestingMachineIdEditer = TestingMachineIdSP.edit()
                                    TestingMachineIdEditer.putString(
                                        "TestingMachineId",
                                        jobjt.getString("TestingMachineId")
                                    )
                                    TestingMachineIdEditer.commit()

                                    val TestingImageURLSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF317,
                                        0
                                    )
                                    val TestingImageURLEditer = TestingImageURLSP.edit()
                                    TestingImageURLEditer.putString(
                                        "TestingImageURL",
                                        jobjt.getString("TestingImageURL")
                                    )
                                    TestingImageURLEditer.commit()

                                    val sslcertificatetestSP =
                                        applicationContext.getSharedPreferences(
                                            Config.SHARED_PREF318,
                                            0
                                        )
                                    val sslcertificatetestEditer = sslcertificatetestSP.edit()
                                    sslcertificatetestEditer.putString(
                                        "testsslcertificate",
                                        CERT_NAME_TEST
                                    )
                                    sslcertificatetestEditer.commit()

                                    val TestBankKeySP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF319,
                                        0
                                    )
                                    val TestBankKeyEditer = TestBankKeySP.edit()
                                    TestBankKeyEditer.putString(
                                        "testBankKey",
                                        jobjt.getString("BankKey")
                                    )
                                    TestBankKeyEditer.commit()

                                    val TestBankHeaderSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF320,
                                        0
                                    )
                                    val TestBankHeaderEditer = TestBankHeaderSP.edit()
                                    TestBankHeaderEditer.putString(
                                        "testBankHeader",
                                        jobjt.getString("BankHeader")
                                    )
                                    TestBankHeaderEditer.commit()


                                    val pref = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF14,
                                        0
                                    )
                                    val strloginmobile = pref.getString("LoginMobileNo", null)
                                    if (strloginmobile == null || strloginmobile.isEmpty()) {

                                        val baseurlSP = applicationContext.getSharedPreferences(
                                            Config.SHARED_PREF163,
                                            0
                                        )
                                        val baseurlSPEditer = baseurlSP.edit()
                                        baseurlSPEditer.putString("baseurl", BASE_URL)
                                        baseurlSPEditer.commit()

                                        val ImageURLSP = applicationContext.getSharedPreferences(
                                            Config.SHARED_PREF165,
                                            0
                                        )
                                        val ImageURLSPEditer = ImageURLSP.edit()
                                        ImageURLSPEditer.putString("ImageURL", IMAGE_URL)
                                        ImageURLSPEditer.commit()


                                        val BankKeySP = applicationContext.getSharedPreferences(
                                            Config.SHARED_PREF312,
                                            0
                                        )
                                        val BankKeyEditer = BankKeySP.edit()
                                        BankKeyEditer.putString("BankKey", BankKey)
                                        BankKeyEditer.commit()

                                        val BankHeaderSP = applicationContext.getSharedPreferences(
                                            Config.SHARED_PREF313,
                                            0
                                        )
                                        val BankHeaderEditer = BankHeaderSP.edit()
                                        BankHeaderEditer.putString("BankHeader", BankHeader)
                                        BankHeaderEditer.commit()


                                        val certificateSP = applicationContext.getSharedPreferences(
                                            Config.SHARED_PREF164,
                                            0
                                        )
                                        val certificateSPEditer = certificateSP.edit()
                                        certificateSPEditer.putString("sslcertificate", CERT_NAME)
                                        certificateSPEditer.commit()
                                    } else {

                                        if (jobjt.getString("TestingMobileNo")
                                                .equals(strloginmobile)
                                        ) {


                                            if (jobjt.getString("TestingURL")
                                                    .isEmpty() && jobjt.getString("TestingImageURL")
                                                    .isEmpty()
                                                && jobjt.getString("BankKey")
                                                    .isEmpty() && jobjt.getString("BankHeader")
                                                    .isEmpty()
                                            ) {

                                                val baseurlSP =
                                                    applicationContext.getSharedPreferences(
                                                        Config.SHARED_PREF163,
                                                        0
                                                    )
                                                val baseurlSPEditer = baseurlSP.edit()
                                                baseurlSPEditer.putString("baseurl", BASE_URL)
                                                baseurlSPEditer.commit()

                                                val ImageURLSP =
                                                    applicationContext.getSharedPreferences(
                                                        Config.SHARED_PREF165,
                                                        0
                                                    )
                                                val ImageURLSPEditer = ImageURLSP.edit()
                                                ImageURLSPEditer.putString("ImageURL", IMAGE_URL)
                                                ImageURLSPEditer.commit()


                                                val BankKeySP =
                                                    applicationContext.getSharedPreferences(
                                                        Config.SHARED_PREF312,
                                                        0
                                                    )
                                                val BankKeyEditer = BankKeySP.edit()
                                                BankKeyEditer.putString("BankKey", BankKey)
                                                BankKeyEditer.commit()

                                                val BankHeaderSP =
                                                    applicationContext.getSharedPreferences(
                                                        Config.SHARED_PREF313,
                                                        0
                                                    )
                                                val BankHeaderEditer = BankHeaderSP.edit()
                                                BankHeaderEditer.putString("BankHeader", BankHeader)
                                                BankHeaderEditer.commit()


                                                val certificateSP =
                                                    applicationContext.getSharedPreferences(
                                                        Config.SHARED_PREF164,
                                                        0
                                                    )
                                                val certificateSPEditer = certificateSP.edit()
                                                certificateSPEditer.putString(
                                                    "sslcertificate",
                                                    CERT_NAME
                                                )
                                                certificateSPEditer.commit()
                                            } else {

                                                val baseurlSP =
                                                    applicationContext.getSharedPreferences(
                                                        Config.SHARED_PREF163,
                                                        0
                                                    )
                                                val baseurlSPEditer = baseurlSP.edit()
                                                baseurlSPEditer.putString(
                                                    "baseurl",
                                                    jobjt.getString("TestingURL")
                                                )
                                                baseurlSPEditer.commit()

                                                val ImageURLSP =
                                                    applicationContext.getSharedPreferences(
                                                        Config.SHARED_PREF165,
                                                        0
                                                    )
                                                val ImageURLSPEditer = ImageURLSP.edit()
                                                ImageURLSPEditer.putString(
                                                    "ImageURL",
                                                    jobjt.getString("TestingImageURL")
                                                )
                                                ImageURLSPEditer.commit()


                                                val BankKeySP =
                                                    applicationContext.getSharedPreferences(
                                                        Config.SHARED_PREF312,
                                                        0
                                                    )
                                                val BankKeyEditer = BankKeySP.edit()
                                                BankKeyEditer.putString(
                                                    "BankKey",
                                                    jobjt.getString("BankKey")
                                                )
                                                BankKeyEditer.commit()

                                                val BankHeaderSP =
                                                    applicationContext.getSharedPreferences(
                                                        Config.SHARED_PREF313,
                                                        0
                                                    )
                                                val BankHeaderEditer = BankHeaderSP.edit()
                                                BankHeaderEditer.putString(
                                                    "BankHeader",
                                                    jobjt.getString("BankHeader")
                                                )
                                                BankHeaderEditer.commit()


                                                val certificateSP =
                                                    applicationContext.getSharedPreferences(
                                                        Config.SHARED_PREF164,
                                                        0
                                                    )
                                                val certificateSPEditer = certificateSP.edit()
                                                certificateSPEditer.putString(
                                                    "sslcertificate",
                                                    CERT_NAME_TEST
                                                )
                                                certificateSPEditer.commit()
                                            }
                                        } else {
                                            val baseurlSP = applicationContext.getSharedPreferences(
                                                Config.SHARED_PREF163,
                                                0
                                            )
                                            val baseurlSPEditer = baseurlSP.edit()
                                            baseurlSPEditer.putString("baseurl", BASE_URL)
                                            baseurlSPEditer.commit()

                                            val ImageURLSP =
                                                applicationContext.getSharedPreferences(
                                                    Config.SHARED_PREF165,
                                                    0
                                                )
                                            val ImageURLSPEditer = ImageURLSP.edit()
                                            ImageURLSPEditer.putString("ImageURL", IMAGE_URL)
                                            ImageURLSPEditer.commit()


                                            val BankKeySP = applicationContext.getSharedPreferences(
                                                Config.SHARED_PREF312,
                                                0
                                            )
                                            val BankKeyEditer = BankKeySP.edit()
                                            BankKeyEditer.putString("BankKey", BankKey)
                                            BankKeyEditer.commit()

                                            val BankHeaderSP =
                                                applicationContext.getSharedPreferences(
                                                    Config.SHARED_PREF313,
                                                    0
                                                )
                                            val BankHeaderEditer = BankHeaderSP.edit()
                                            BankHeaderEditer.putString("BankHeader", BankHeader)
                                            BankHeaderEditer.commit()


                                            val certificateSP =
                                                applicationContext.getSharedPreferences(
                                                    Config.SHARED_PREF164,
                                                    0
                                                )
                                            val certificateSPEditer = certificateSP.edit()
                                            certificateSPEditer.putString(
                                                "sslcertificate",
                                                CERT_NAME
                                            )
                                            certificateSPEditer.commit()
                                        }

                                    }


//
//                                    if (jobjt.getString("CertificateStatus").equals("Live")) {
//                                        val certificateSP = applicationContext.getSharedPreferences(Config.SHARED_PREF164, 0)
//                                        val certificateSPEditer = certificateSP.edit()
//                                        certificateSPEditer.putString("sslcertificate", Config.CERT_NAME)
//                                        certificateSPEditer.commit()
//                                    } else {
//                                        val certificateSP = applicationContext.getSharedPreferences(Config.SHARED_PREF164, 0)
//                                        val certificateSPEditer = certificateSP.edit()
//                                        certificateSPEditer.putString("sslcertificate", Config.CERT_NAME)
//                                        certificateSPEditer.commit()
//                                    }

                                    try {
                                        val imagepath = IMAGE_URL + AppIconImageCodeSP!!.getString(
                                            "AppIconImageCode",
                                            null
                                        )
                                        //     PicassoTrustAll.getInstance(this@SplashActivity)!!.load(imagepath).error(android.R.color.transparent).into(imglogo!!)

                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }

//                                    doSplash()
                                    getMaintenanceMessage()
                                    //  getlabels()
                                } else {
                                    AlertMessage().alertMessage(
                                        this@SplashActivity,
                                        this@SplashActivity,
                                        "Alert",
                                        jObject.getString("EXMessage"),
                                        1
                                    );
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                Log.e("TAG", "Exception   289  " + e.toString())
                                AlertMessage().alertMessage(
                                    this@SplashActivity,
                                    this@SplashActivity,
                                    "Alert",
                                    "Some technical issues.",
                                    1
                                );
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()

                            Log.e(TAG, "onFailure    1054    " + t.message)
                            AlertMessage().alertMessage(
                                this@SplashActivity,
                                this@SplashActivity,
                                "Alert",
                                "Some technical issues.",
                                1
                            );
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    AlertMessage().alertMessage(
                        this@SplashActivity,
                        this@SplashActivity,
                        "Alert",
                        "Some technical issues.",
                        1
                    );
                    e.printStackTrace()
                }
            }
            false -> {
                AlertMessage().alertMessage(
                    this@SplashActivity,
                    this@SplashActivity,
                    "Alert",
                    "No Internet Connection.",
                    3
                );
            }
        }

    }

    private fun setImage() {
        val imgSplash: ImageView = findViewById(R.id.imsplashlogo)
        val imglogo: ImageView = findViewById(R.id.imglogo)
        Glide.with(this).load(R.drawable.splashgif).into(imgSplash)
        try {
            val AppIconImageCodeSP =
                applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
            val imagepath = IMAGE_URL + AppIconImageCodeSP!!.getString("AppIconImageCode", null)
            PicassoTrustAll.getInstance(this@SplashActivity)!!.load(imagepath)
                .error(android.R.color.transparent).into(imglogo!!)

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun getMaintenanceMessage() {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when (ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@SplashActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val trustManagerFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm()
                    )
                    trustManagerFactory.init(null as KeyStore?)
                    val trustManagers = trustManagerFactory.trustManagers
                    check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                        ("Unexpected default trust managers:"
                                + Arrays.toString(trustManagers))
                    }
                    val trustManager = trustManagers[0] as X509TrustManager
                    val client: OkHttpClient = okhttp3.OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .sslSocketFactory(getSSLSocketFactory(this), trustManager)
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

                        val FK_CustomerSP =
                            this.applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)
                        val TokenSP =
                            this!!.applicationContext.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)


                        val BankKeySP =
                            applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP =
                            applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)


                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("43"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                            "FK_Customer",
                            MscoreApplication.encryptStart(FK_Customer)
                        )

                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put(
                            "BankHeader",
                            MscoreApplication.encryptStart(BankHeaderPref)
                        )


                        Log.e(TAG, "requestObject1 Maintance 10001   " + requestObject1)
                    } catch (e: Exception) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        AlertMessage().alertMessage(
                            this@SplashActivity,
                            this@SplashActivity,
                            "Alert",
                            "Some technical issues.",
                            1
                        );
                    }
                    val body = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getMaintenanceMsg(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.e("Response-Maintenance", response.body().toString())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jobjt = jObject.getJSONObject("MaintenanceMessage")
                                    val maintenancejobjt =
                                        jobjt.getJSONArray("MaintenanceMessageList")
                                    val MaintenanceMessageList = maintenancejobjt.getJSONObject(0)
                                    val type = MaintenanceMessageList.getString("Type")

                                    var message = ""
                                    if (maintenancejobjt.length() != 0) {
                                        for (i in 0 until maintenancejobjt.length()) {
                                            val MaintenanceMessageL =
                                                maintenancejobjt.getJSONObject(i)
                                            message += if (type == "1") {
                                                "" + MaintenanceMessageL.getString("Description")
                                            } else {
                                                if (i == 0) {
                                                    "" + (i + 1) + " - " + MaintenanceMessageL.getString(
                                                        "Description"
                                                    )
                                                } else {
                                                    """
                                              ${i + 1} - ${MaintenanceMessageL.getString("Description")}"""
                                                }
                                            }
                                        }
                                        if (type == "1") {
                                            tv_error_message!!.setVisibility(View.VISIBLE)
                                            tv_error_message!!.setText(message)
                                        } else if (type == "0") {
                                            tv_error_message!!.setVisibility(View.VISIBLE)
                                            btn_proceed!!.setVisibility(View.VISIBLE)
                                            tv_error_message!!.setText(message)
                                        } else if (type == "-1") {
                                            val splashScreen = getString(R.string.splash_screen)
                                            if (splashScreen == "ON") {
                                                Handler().postDelayed(
                                                    { this@SplashActivity.startUserregistrationActivity() },
                                                    3000
                                                )
                                            } else {
                                                startUserregistrationActivity()
                                            }
                                        }
                                    }

                                } else {
                                    AlertMessage().alertMessage(
                                        this@SplashActivity,
                                        this@SplashActivity,
                                        "Alert",
                                        jObject.getString("EXMessage"),
                                        1
                                    );
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                AlertMessage().alertMessage(
                                    this@SplashActivity,
                                    this@SplashActivity,
                                    "Alert",
                                    "Some technical issues.",
                                    1
                                );
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()

                            AlertMessage().alertMessage(
                                this@SplashActivity,
                                this@SplashActivity,
                                "Alert",
                                "Some technical issues.",
                                1
                            );
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    AlertMessage().alertMessage(
                        this@SplashActivity,
                        this@SplashActivity,
                        "Alert",
                        "Some technical issues.",
                        1
                    );
                    e.printStackTrace()
                }
            }
            false -> {
                AlertMessage().alertMessage(
                    this@SplashActivity,
                    this@SplashActivity,
                    "Alert",
                    "No Internet Connection.",
                    3
                );
            }
        }
    }

    private fun startUserregistrationActivity() {
//        var intent = Intent(this@SplashActivity, MpinActivity::class.java)
//        startActivity(intent)

        doSplash()
    }

    fun alertMessage(header: String, message: String, type: Int) {
        val bottomSheetDialog = BottomSheetDialog(this@SplashActivity)
        bottomSheetDialog.setContentView(R.layout.alert_message)
        val txt_ok = bottomSheetDialog.findViewById<TextView>(R.id.txt_ok)
        val img = bottomSheetDialog.findViewById<ImageView>(R.id.img)
        val txt_cancel = bottomSheetDialog.findViewById<TextView>(R.id.txt_cancel)
        val txtheader = bottomSheetDialog.findViewById<TextView>(R.id.header)
        val txtmessage = bottomSheetDialog.findViewById<TextView>(R.id.message)
        txtmessage!!.setText(message)
        txtheader!!.setText(header)
        txt_cancel!!.setText("OK")
        txt_cancel!!.setOnClickListener {
            bottomSheetDialog.dismiss()
            nidhicodepopup()
        }
        if (type == 1) {
            txt_ok!!.visibility = View.GONE
            txt_cancel!!.visibility = View.VISIBLE
            img!!.setImageResource(R.drawable.new_alert)
        } else if (type == 2) {
            txt_ok!!.visibility = View.GONE
            txt_cancel!!.visibility = View.VISIBLE
            img!!.setImageResource(R.drawable.new_success)
        } else if (type == 3) {
            txt_ok!!.visibility = View.GONE
            txt_cancel!!.visibility = View.VISIBLE
            img!!.setImageResource(R.drawable.new_nonetwork)
        }

        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.show()
    }


}


